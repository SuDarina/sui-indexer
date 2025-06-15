use std::net::SocketAddr;
use std::sync::Arc;
use anyhow::Result;
use tokio::sync::RwLock;
use swim_rs::api::{config::SwimConfig, swim::SwimCluster};
use swim_rs::Event;

pub async fn start_swim(me_addr: SocketAddr, me_name: String) -> Result<Arc<RwLock<Vec<String>>>> {
    let seeds_env = std::env::var("SWIM_SEEDS").unwrap_or_else(|_| "127.0.0.1:7000".to_string());
    println!("seed env:{}", seeds_env);
    let seeds: Vec<String> = seeds_env.split(',').map(|s| s.to_string()).collect();

    let cfg = SwimConfig::builder()
        .with_known_peers(seeds)
        .build();

    let cluster = SwimCluster::try_new(me_addr.to_string(), cfg).await?;
    let live_nodes = Arc::new(RwLock::new(vec![me_name.clone()]));
    let nodes_ref = Arc::clone(&live_nodes);
    let mut ev = cluster.subscribe();

    tokio::spawn(async move {
        cluster.run().await;
    });

    tokio::spawn(async move {
        while let Ok(evt) = ev.recv().await {
            match evt {
                Event::NodeJoined(n) => {
                    println!("Node joined: {}", n.new_member);
                    let mut nodes = nodes_ref.write().await;
                    if !nodes.contains(&n.new_member) {
                        nodes.push(n.new_member.clone());
                    }
                }
                Event::NodeDeceased(n) => {
                    println!("Node deceased: {}", n.deceased);
                    let mut nodes = nodes_ref.write().await;
                    nodes.retain(|node| node != &n.deceased);
                }
                _ => {}
            }
        }
    });

    Ok(live_nodes)
}
