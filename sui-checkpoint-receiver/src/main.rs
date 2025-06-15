mod swim;
mod redis_manager;
mod hashing;
mod replication;
mod worker;
mod types;

use anyhow::Result;
use worker::DistributedWorker;
use types::Checkpoint;
use sui_data_ingestion_core::{setup_single_workflow, Worker};
use rdkafka::producer::{FutureProducer};
use rdkafka::ClientConfig;
use std::env;

#[tokio::main]
async fn main() -> Result<()> {
    // Получаем идентификатор узла из переменной окружения, например POD_NAME=node-0
    let pod_name = env::var("POD_NAME").unwrap_or_else(|_| "node-0".to_string());
    let ordinal = pod_name.rsplit_once('-')
        .and_then(|(_, id)| id.parse::<u64>().ok())
        .unwrap_or(0);

    println!("Starting worker with ordinal: {}", ordinal);

    // Конфигурация Kafka
    let kafka_producer: FutureProducer = ClientConfig::new()
        .set("bootstrap.servers", "localhost:29092")
        .set("message.timeout.ms", "3000")
        .set("compression.type", "gzip")
        .create()
        .expect("Failed to create Kafka producer");

    // Создаем распределенного воркера с поддержкой SWIM
    let worker = DistributedWorker::new(ordinal, kafka_producer.clone()).await;

    // Запускаем задачу фоновой репликации (если мы бэкап)
    worker.run_backup_task();

    // Подключение к источнику WebSocket и начало обработки
    let num_workers = 4;
    let (executor, _term_sender) = setup_single_workflow(
        worker,
        "https://checkpoints.testnet.sui.io".to_string(),
        0,
        num_workers,
        None,
    ).await?;

    executor.await?;
    Ok(())
}
