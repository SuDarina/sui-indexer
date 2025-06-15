mod swim;
mod hashing;

use anyhow::Result;
use async_trait::async_trait;
use std::{env, sync::Arc, time::Duration};
use sui_data_ingestion_core::{setup_single_workflow, Worker};
use sui_types::full_checkpoint_content::CheckpointData;
use rdkafka::{
    producer::{FutureProducer, FutureRecord},
    ClientConfig,
};
use tokio::sync::RwLock;
use serde_json::to_string;

use hashing::rendezvous_hashing;
use swim::start_swim;

pub struct CustomWorker {
    kafka_producer: FutureProducer,
    self_id: String,
    live_nodes: Arc<RwLock<Vec<String>>>,
}

#[async_trait]
impl Worker for CustomWorker {
    type Result = ();

    async fn process_checkpoint(&self, checkpoint: &CheckpointData) -> Result<()> {
        let id = checkpoint.checkpoint_summary.sequence_number.to_string();
        let checkpoint_json = to_string(&checkpoint)?;

        let nodes = self.live_nodes.read().await.clone();

        if let Some((primary, backup)) = rendezvous_hashing(&id, &nodes) {
            if self.self_id == primary {
                let record = FutureRecord::to("sui-checkpoints")
                    .payload(&checkpoint_json)
                    .key(&id);

                match self.kafka_producer.send(record, Duration::from_secs(0)).await {
                    Ok(_) => println!("Primary sent checkpoint {}", id),
                    Err((e, _)) => eprintln!("Kafka send error for {}: {:?}", id, e),
                }
            } else if Some(self.self_id.as_str()) == backup {
                println!("Backup for checkpoint {}", id);
            }
        }

        Ok(())
    }
}

#[tokio::main]
async fn main() -> Result<()> {
    let pod_name = env::var("POD_NAME").unwrap_or_else(|_| "sui-checkpoint-receiver-0".to_string());

    let ordinal = pod_name
        .rsplit_once('-')
        .and_then(|(_, id)| id.parse::<u64>().ok())
        .unwrap_or(0);

    let self_id = format!("0.0.0.0:{}", 7000 + ordinal);

    let bootstrap_servers = env::var("BOOTSTRAP_SERVERS").unwrap_or_else(|_| "localhost:29092".to_string());

    let kafka_producer: FutureProducer = ClientConfig::new()
        .set("bootstrap.servers", bootstrap_servers)
        .set("message.timeout.ms", "3000")
        .set("message.max.bytes", "10485760")
        .set("compression.type", "gzip")
        .create()
        .expect("Failed to create Kafka producer");

    let swim_host = "0.0.0.0";
    let swim_port = 7000 + ordinal;
    let swim_addr = format!("{}:{}", swim_host, swim_port).parse()?;
    let live_nodes = start_swim(swim_addr, self_id.clone()).await?;

    let worker = CustomWorker {
        kafka_producer,
        self_id,
        live_nodes,
    };

    let num_workers = 1;
    let (executor, _term_sender) = setup_single_workflow(
        worker,
        "https://checkpoints.testnet.sui.io".to_string(),
        0,
        num_workers,
        None,
    )
        .await?;

    executor.await?;
    Ok(())
}
