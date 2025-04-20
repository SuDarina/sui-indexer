CREATE DATABASE IF NOT EXISTS suiaggr;

CREATE TABLE IF NOT EXISTS suiaggr.minute_transactions
(
    `date` Date,
    `hour` UInt8,
    `minute` UInt8,
    `total_tx` UInt64,
    `successful_tx` UInt64,
    `failed_tx` UInt64,
    `gas_used_total` UInt64,
    `gas_used_avg` Float64,
    `unique_senders` UInt64,
    `unique_receivers` UInt64,
    `contract_calls_count` UInt64,
    `nft_transfers_count` UInt64,
    `defi_swaps_count` UInt64,
    `volume_total` Float64
)
    ENGINE = MergeTree()
    PARTITION BY toYYYYMM(date)
    ORDER BY (date, hour, minute);
