#!/bin/bash
set -e

WORK_DIR="$(pwd)"

# 1. Создаем namespace
kubectl apply -f ../manifests/00-namespace.yaml

# 2. Разворачиваем zookeeper-кластер
kubectl apply -f ../manifests/01-zookeeper.yaml -n sui-indexer

# 2. Разворачиваем clickhouse-кластер
chmod +x ../manifests/08-clickhouse/build.sh
cd ../manifests/08-clickhouse
./build.sh
cd $WORK_DIR

# 3. Ждем инициализации CRD
until kubectl get crd clickhouseinstallations.clickhouse.altinity.com &>/dev/null; do
  sleep 2
  echo "Ожидание доступности CRD..."
done

# 4. Настройка Kafka
helm upgrade --install kafka-operator -f ../helm/kafka/values.yaml strimzi/strimzi-kafka-operator -n sui-indexer
kubectl apply -f ../manifests/02-kafka-cluster.yaml -n sui-indexer

# 6. Настройка PostgreSql
helm upgrade postgresql-ha bitnami/postgresql-ha -n sui-indexer -f ../helm/postgresql-ha/values.yaml
kubectl apply -f ../manifests/06-liquibase/liquibase-job.yaml -n sui-indexer

# 7 Разворачиваем sui-api с балансировщиком нагрузки
kubectl apply -f ../manifests/05-sui-api/deployment.yaml -n sui-indexer
kubectl apply -f ../manifests/05-sui-api/service.yaml -n sui-indexer
kubectl apply -f ../manifests/05-sui-api/hpa.yaml -n sui-indexer


# Применяем Network Policies
#kubectl apply -f ../manifests/07-network-policies.yaml

echo "Sui-indexer кластер успешно развернут!"