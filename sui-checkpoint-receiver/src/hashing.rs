use murmur3::murmur3_x64_128;
use std::io::Cursor;

pub fn rendezvous_hashing<'a>(key: &str, nodes: &'a [String]) -> Option<(&'a str, Option<&'a str>)> {
    let mut scores: Vec<(&str, u128)> = nodes.iter()
        .map(|n| (n.as_str(), murmur3_x64_128(&mut Cursor::new(format!("{}:{}", key, n).as_bytes()), 0).unwrap()))
        .collect();
    scores.sort_by(|a, b| b.1.cmp(&a.1));
    match scores.len() {
        0 => None,
        1 => Some((scores[0].0, None)),
        _ => Some((scores[0].0, Some(scores[1].0))),
    }
}
