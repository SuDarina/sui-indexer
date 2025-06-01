use redis::AsyncCommands;
use anyhow::Result;

pub struct RedisBuffer {
    client: redis::Client,
}

impl RedisBuffer {
    pub fn new(url: &str) -> Result<Self> {
        Ok(Self { client: redis::Client::open(url)? })
    }

    pub async fn add(&self, seq: u64, json: &str) -> Result<()> {
        let mut conn = self.client.get_async_connection().await?;
        conn.hset("buffer", seq, json).await?;
        Ok(())
    }

    pub async fn remove(&self, seq: u64) -> Result<()> {
        let mut conn = self.client.get_async_connection().await?;
        conn.hdel("buffer", seq).await?;
        Ok(())
    }

    pub async fn all_seqs(&self) -> Result<Vec<u64>> {
        let mut conn = self.client.get_async_connection().await?;
        let keys: Vec<String> = conn.hkeys("buffer").await?;
        Ok(keys.into_iter().filter_map(|k| k.parse().ok()).collect())
    }

    pub async fn get(&self, seq: u64) -> Result<Option<String>> {
        let mut conn = self.client.get_async_connection().await?;
        Ok(conn.hget("buffer", seq).await?)
    }
}
