#!/bin/bash

# Создаем тестовую таблицу
kubectl exec -it clickhouse-cluster-replicated-cluster-0-0-0 -n clickhouse -- clickhouse-client --query "
CREATE TABLE IF NOT EXISTS test.replicated_data
(
    id UInt64,
    timestamp DateTime,
    data String
)
ENGINE = ReplicatedMergeTree('/clickhouse/tables/{shard}/replicated_data', '{replica}')
ORDER BY (id, timestamp);"

# Вставляем данные
kubectl exec -it clickhouse-cluster-replicated-cluster-0-0-0 -n clickhouse -- clickhouse-client --query "
INSERT INTO test.replicated_data VALUES (1, now(), 'Test data');"

# Проверяем на реплике
kubectl exec -it clickhouse-cluster-replicated-cluster-0-1-0 -n clickhouse -- clickhouse-client --query "
SELECT * FROM test.replicated_data;"

echo "Репликация работает!"