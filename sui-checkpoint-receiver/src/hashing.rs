use std::io::Cursor;
use murmur3::murmur3_x64_128;

fn hash_score(key: &str, node: &str) -> u128 {
    let combined = format!("{}:{}", key, node);
    let mut cursor = Cursor::new(combined.as_bytes());
    murmur3_x64_128(&mut cursor, 0).expect("hashing failed")
}

pub(crate) fn rendezvous_hashing<'a>(key: &str, nodes: &'a [String]) -> Option<(&'a str, Option<&'a str>)> {

    let mut scores: Vec<(&str, u128)> = nodes.iter()
        .map(|node| (node.as_str(), hash_score(key, node)))
        .collect();

    scores.sort_by(|a, b| b.1.cmp(&a.1));

    match scores.len() {
        0 => None,
        1 => Some((scores[0].0, None)),
        _ => Some((scores[0].0, Some(scores[1].0))),
    }
}
