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
    ENGINE = ReplicatedMergeTree('/clickhouse/tables/suiaggr/{shard}/minute_transactions', '{replica}')
--     ENGINE = MergeTree
    PARTITION BY toYYYYMM(date)
    ORDER BY (date, hour, minute);


CREATE TABLE IF NOT EXISTS suiaggr.transaction_analytics (
    -- Время
                                       date DateTime64(3),
                                       datetime DateTime64(3),
                                       hour UInt8,
                                       minute UInt8,

    -- Идентификаторы
                                       transaction_digest String,
                                       sender String,

    -- Эпоха исполнения
                                       executed_epoch UInt64,

    -- Статус
                                       status String, -- success / failure

    -- Gas usage
                                       gas_budget UInt64,
                                       gas_price UInt64,
                                       gas_used UInt64,
                                       computation_cost UInt64,
                                       storage_cost UInt64,
                                       storage_rebate UInt64,

    -- Количественные метрики
                                       created_objects UInt16,
                                       mutated_objects UInt16,
                                       deleted_objects UInt16,
                                       wrapped_objects UInt16,
                                       unwrapped_objects UInt16,
                                       unwrapped_then_deleted_objects UInt16,
                                       shared_objects UInt16,

    -- Типы владельцев
                                       address_owned UInt16,
                                       object_owned UInt16,
                                       shared_owned UInt16,
                                       immutable_owned UInt16,

    -- Общее количество объектов
                                       total_objects UInt16,

    -- Идентификатор источника (например, из какого чекпоинта)
                                       checkpoint_seq UInt64,

    -- Массив зависимостей (по желанию)
                                       dependencies Array(String)
)
    ENGINE = ReplicatedMergeTree('/clickhouse/tables/suiaggr/{shard}/transaction_analytics', '{replica}')
--     ENGINE = MergeTree

PARTITION BY toYYYYMM(date)
ORDER BY (date, hour, sender);
