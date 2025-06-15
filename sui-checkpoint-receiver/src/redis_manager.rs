use std::sync::Arc;
use anyhow::Result;
use redis::AsyncCommands;

#[derive(Clone)]
pub struct RedisManager {
    pub(crate) client: Arc<redis::Client>,
}

impl RedisManager {
    pub async fn new(url: &str) -> anyhow::Result<Self> {
        Ok(Self {
            client: Arc::new(redis::Client::open(url)?),
        })
    }

    pub async fn exists_processed(&self, id: &str) -> Result<bool> {
        let mut conn = self.client.get_async_connection().await?;
        Ok(conn.exists(format!("checkpoint:{}", id)).await?)
    }

    pub async fn mark_processed(&self, id: &str) -> Result<()> {
        let mut conn = self.client.get_async_connection().await?;
        conn.set_ex(format!("checkpoint:{}", id), "1", 600).await?;
        Ok(())
    }

    pub async fn store_backup(&self, id: &str, payload: &str) -> Result<()> {
        let mut conn = self.client.get_async_connection().await?;
        conn.set_ex(format!("checkpoint:{}:backup", id), payload, 900).await?;
        Ok(())
    }

    pub async fn list_backups(&self) -> Result<Vec<String>> {
        let mut conn = self.client.get_async_connection().await?;
        Ok(conn.keys("checkpoint:*:backup").await?)
    }

    pub async fn get_backup(&self, id: &str) -> Result<Option<String>> {
        let mut conn = self.client.get_async_connection().await?;
        Ok(conn.get(format!("checkpoint:{}:backup", id)).await?)
    }

    pub async fn delete_backup(&self, id: &str) -> Result<()> {
        let mut conn = self.client.get_async_connection().await?;
        conn.del(format!("checkpoint:{}:backup", id)).await?;
        Ok(())
    }
}
