use async_trait::async_trait;
use anyhow::Result;
use crate::redis_manager::RedisManager;
use crate::hashing::rendezvous_hashing;
use crate::replication::replicate;
use crate::types::Checkpoint;
use crate::swim::start_swim;
use sui_data_ingestion_core::Worker;
use sui_types::full_checkpoint_content::CheckpointData;
use rdkafka::producer::{FutureProducer, FutureRecord};
use tokio::time::{sleep, Duration};
use std::sync::Arc;
use redis::AsyncCommands;
use tokio::sync::RwLock;

pub struct DistributedWorker {
    self_id: String,
    live_nodes: Arc<RwLock<Vec<String>>>,
    redis: RedisManager,
    kafka: FutureProducer,
}

impl DistributedWorker {
    pub async fn new(ordinal: u64, kafka: FutureProducer) -> Self {
        let self_id = format!("node-{}", ordinal);
        let live_nodes = start_swim(
            format!("0.0.0.0:{}", 7000 + ordinal).parse().unwrap(),
            format!("0.0.0.0:{}", 7000 + ordinal).parse().unwrap(),
            self_id.clone(),
        ).await.unwrap();

        let redis = RedisManager::new("redis://127.0.0.1/").await.unwrap();
        DistributedWorker { self_id, live_nodes, redis, kafka }
    }

    async fn backup_worker(&self) {
        loop {
            let backups = self.redis.list_backups().await.unwrap_or_default();
            for key in backups {
                if let Some(id) = key.strip_prefix("checkpoint:").and_then(|s| s.strip_suffix(":backup")) {
                    let mut conn = self.redis.client.get_async_connection().await.unwrap();
                    let existed: bool = conn.exists(format!("checkpoint:{}", id)).await.unwrap_or(false);
                    if !existed {
                        if let Ok(payload) = conn.get::<_, String>(format!("checkpoint:{}:backup", id)).await {
                            let record = FutureRecord::to("sui-checkpoints")
                                .payload(payload.as_str())
                                .key(id); // или sequence_number_str

                            match self.kafka.send(record, Duration::from_secs(0)).await {
                                Ok(_) => {
                                    println!("Resent backup checkpoint {}", id);
                                    // Попытаться удалить backup, вывести ошибку если не получилось
                                    if let Err(e) = conn.del::<_, ()>(format!("checkpoint:{}:backup", id)).await {
                                        eprintln!("Failed to delete backup checkpoint {}: {:?}", id, e);
                                    }
                                }
                                Err((e, _)) => {
                                    eprintln!("Error resending backup checkpoint {}: {:?}", id, e);
                                }
                            }
                        }
                    }
                }
            }
            sleep(Duration::from_secs(5)).await;
        }
    }

        pub fn run_backup_task(&self) {
            let kafka = self.kafka.clone();
            let redis = self.redis.clone(); // RedisManager должен быть клонируемым (через Arc внутри)
            let self_id = self.self_id.clone();

            tokio::spawn(async move {
                // Создаем временный worker только для запуска backup_worker
                let temp_worker = DistributedWorker {
                    self_id,
                    live_nodes: Arc::new(RwLock::new(vec![])), // сюда не нужен live_nodes
                    redis,
                    kafka,
                };
                temp_worker.backup_worker().await;
            });
        }
    }


#[async_trait]
impl Worker for DistributedWorker {
    type Result = ();

    async fn process_checkpoint(&self, checkpoint: &CheckpointData) -> Result<()> {
        let cp = Checkpoint {
            id: checkpoint.checkpoint_summary.sequence_number.to_string(),
            payload: serde_json::to_string(checkpoint)?,
        };
        let nodes = self.live_nodes.read().await.clone();
        if let Some((primary, backup)) = rendezvous_hashing(&cp.id, &nodes) {
            let mut conn = self.redis.client.get_async_connection().await?;
            replicate(cp, &self.self_id, primary, backup, &self.kafka, &mut conn).await;
        }
        Ok(())
    }
}
