use anyhow::Result;
use async_trait::async_trait;
use sui_data_ingestion_core::{setup_single_workflow, Worker};
use sui_types::full_checkpoint_content::CheckpointData;
use rdkafka::producer::{FutureProducer, FutureRecord};
use rdkafka::ClientConfig;
use std::time::Duration;
use serde_json::to_string;

struct CustomWorker {
    kafka_producer: FutureProducer,
}

#[async_trait]
impl Worker for CustomWorker {
    type Result = ();
    async fn process_checkpoint(&self, checkpoint: &CheckpointData) -> Result<()> {
        
        let checkpoint_json = to_string(&checkpoint)?;

        let sequence_number_str = checkpoint.checkpoint_summary.sequence_number.to_string();

        let record = FutureRecord::to("sui-checkpoints")
            .payload(&checkpoint_json)
            .key(&sequence_number_str);

        match self.kafka_producer.send(record, Duration::from_secs(0)).await {
            Ok(_) => println!(
                "Sent checkpoint {} to Kafka",
                checkpoint.checkpoint_summary.sequence_number
            ),
            Err((e, _)) => eprintln!("Error sending to Kafka: {:?}. Details: {:?}", e, e.to_string()),
        }

        Ok(())
    }
}

#[tokio::main]
async fn main() -> Result<()> {
    let kafka_producer: FutureProducer = ClientConfig::new()
        .set("bootstrap.servers", "localhost:9092")
        .set("message.timeout.ms", "3000")
        .set("message.max.bytes", "10485760")
        .set("compression.type", "gzip")
        .create()
        .expect("Failed to create Kafka producer");

    let worker = CustomWorker { kafka_producer };

    let (executor, _term_sender) = setup_single_workflow(
        worker,
        "https://checkpoints.testnet.sui.io".to_string(),
        0,
        1,
        None,
    )
    .await?;

    executor.await?;

    Ok(())
}