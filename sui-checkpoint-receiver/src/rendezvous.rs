use std::hash::{Hash, Hasher};
use twox_hash::XxHash64;

pub struct RendezvousHash {
    pub nodes: Vec<String>,
}

impl RendezvousHash {
    pub fn new(nodes: Vec<String>) -> Self {
        Self { nodes }
    }

    pub fn update_nodes(&mut self, nodes: Vec<String>) {
        self.nodes = nodes;
    }

    pub fn get_node(&self, key: &u64) -> Option<String> {
        let mut max_score = 0;
        let mut selected = None;
        for node in &self.nodes {
            let mut hasher = XxHash64::with_seed(0);
            node.hash(&mut hasher);
            key.hash(&mut hasher);
            let score = hasher.finish();
            if selected.is_none() || score > max_score {
                max_score = score;
                selected = Some(node.clone());
            }
        }
        selected
    }
}
