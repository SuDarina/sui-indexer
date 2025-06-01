use std::fs;
use std::path::PathBuf;
use chrono::{DateTime, Utc};
use std::error::Error;
use std::io::{BufReader, BufRead};
use std::fs::File;

fn main() -> Result<(), Box<dyn Error>> {
    let log_dir = PathBuf::from("logs");
    let mut instance_count = 0;
    let mut total_checkpoints = 0usize;
    let mut earliest_timestamp: Option<DateTime<Utc>> = None;
    let mut latest_timestamp: Option<DateTime<Utc>> = None;

    let mut per_instance_data = Vec::new(); // (pod_name, count, first_ts, last_ts)

    for entry in fs::read_dir(&log_dir)? {
        let entry = entry?;
        let path = entry.path();

        if path.extension().map_or(false, |ext| ext == "csv") {
            instance_count += 1;

            let (count, first_ts, last_ts) = analyze_file(&path)?;
            total_checkpoints += count;

            if let Some(ts) = first_ts {
                if earliest_timestamp.is_none() || ts < earliest_timestamp.unwrap() {
                    earliest_timestamp = Some(ts);
                }
            }
            if let Some(ts) = last_ts {
                if latest_timestamp.is_none() || ts > latest_timestamp.unwrap() {
                    latest_timestamp = Some(ts);
                }
            }

            let pod_name = path.file_stem().unwrap().to_string_lossy().to_string();
            per_instance_data.push((pod_name, count, first_ts, last_ts));
        }
    }

    println!("Конфигурация (число инстансов R): {}\n", instance_count);
    println!("{:<30} {:<20}", "Pod", "Чекпт./сек");

    // Вывод throughput по каждому инстансу
    for (pod, count, first_ts, last_ts) in &per_instance_data {
        if let (Some(start), Some(end)) = (first_ts, last_ts) {
            let duration_secs = (end.signed_duration_since(*start).num_seconds()).max(1) as f64;
            let throughput = *count as f64 / duration_secs;
            println!("{:<30} {:<20.2}", pod, throughput);
        } else {
            println!("{:<30} {:<20}", pod, "нет данных");
        }
    }

    // Вывод общего throughput
    if let (Some(start), Some(end)) = (earliest_timestamp, latest_timestamp) {
        let total_duration_secs = (end.signed_duration_since(start).num_seconds()).max(1) as f64;
        let total_throughput = total_checkpoints as f64 / total_duration_secs;
        println!("\nОбщий throughput по всей системе: {:.2} чекп./сек", total_throughput);
    } else {
        println!("Недостаточно данных для расчёта общего throughput.");
    }

    Ok(())
}

fn analyze_file(path: &PathBuf) -> Result<(usize, Option<DateTime<Utc>>, Option<DateTime<Utc>>), Box<dyn Error>> {
    let file = File::open(path)?;
    let reader = BufReader::new(file);
    let mut timestamps: Vec<DateTime<Utc>> = Vec::new();

    for (i, line) in reader.lines().enumerate() {
        let line = line?;
        if i == 0 {
            continue; // пропускаем заголовок
        }

        let parts: Vec<&str> = line.trim().split(',').collect();
        if parts.len() != 2 {
            continue;
        }

        let ts = DateTime::parse_from_rfc3339(parts[0])?.with_timezone(&Utc);
        timestamps.push(ts);
    }

    if timestamps.is_empty() {
        return Ok((0, None, None));
    }

    timestamps.sort();
    let first_ts = timestamps.first().cloned();
    let last_ts = timestamps.last().cloned();

    Ok((timestamps.len(), first_ts, last_ts))
}
