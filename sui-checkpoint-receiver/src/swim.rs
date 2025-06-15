use swim_rs::api::{config::SwimConfig};
use std::sync::Arc;
use tokio::sync::RwLock;
use std::net::SocketAddr;
use anyhow::Result;
use swim_rs::api::swim::SwimCluster;
use swim_rs::Event;

pub async fn start_swim(me_addr: SocketAddr, bind_addr: SocketAddr, me_name: String) -> Result<Arc<RwLock<Vec<String>>>> {
    let mut cfg = SwimConfig::default();
    cfg.bind_addr = bind_addr.ip();
    cfg.bind_port = bind_addr.port();

    let cluster = SwimCluster::try_new(me_addr.to_string(), cfg).await?;
    let live_nodes = Arc::new(RwLock::new(vec![me_name.clone()]));
    let nodes_ref = live_nodes.clone();
    let mut ev = cluster.subscribe();

    tokio::spawn(async move { cluster.run().await });

    tokio::spawn(async move {
        while let Ok(evt) = ev.recv().await {
            match evt {
                Event::NodeJoined(n) => { nodes_ref.write().await.push(n.id); }
                Event::NodeDeceased(n) => {
                    let mut w = nodes_ref.write().await;
                    w.retain(|x| x != &n.id);
                }
                _ => {}
            }
        }
    });

    Ok(live_nodes)
}
