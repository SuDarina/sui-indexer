#!/bin/bash
echo "Stopping ClickHouse cluster..."

# Остановка ClickHouse (soft shutdown)
kubectl patch clickhouseinstallation clickhouse-cluster --type merge -p '{"spec":{"stop":"true"}}'

# Остановка Zookeeper (без удаления данных)
kubectl scale statefulset zookeeper --replicas=0

echo "Cluster stopped. Data preserved in PVCs."