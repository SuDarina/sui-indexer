use crate::types::Checkpoint;
use redis::aio::Connection;
use rdkafka::producer::{FutureProducer, FutureRecord};
use redis::AsyncCommands;
use tokio::time::Duration;

pub async fn replicate(
    cp: Checkpoint,
    self_id: &str,
    primary: &str,
    backup: Option<&str>,
    kafka: &FutureProducer,
    redis: &mut Connection,
) {
    if self_id == primary {
        let record = FutureRecord::to("sui-checkpoints")
            .payload(&cp.payload)
            .key(&cp.id);
        let _ = kafka.send(record, Duration::from_secs(0)).await;

        let _: () = redis.set_ex(format!("checkpoint:{}", cp.id), "1", 600).await.unwrap();
        let _: () = redis.del(format!("checkpoint:{}:backup", cp.id)).await.unwrap();
    } else if Some(self_id) == backup {
        // Store if not processed yet
        let exists: bool = redis.exists(format!("checkpoint:{}", cp.id)).await.unwrap_or(true);
        if !exists {
            let _: () = redis.set_ex(format!("checkpoint:{}:backup", cp.id), cp.payload, 900).await.unwrap();
        }
    }
}
