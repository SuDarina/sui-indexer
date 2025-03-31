#!/bin/bash
set -e  # Прерывать выполнение при ошибках

# 1. Создаем namespace
kubectl apply -f ../manifests/00-namespace.yaml


# 2. Устанавливаем ClickHouse Operator
helm repo add altinity https://altinity.github.io/clickhouse-operator
helm repo update
helm upgrade --install clickhouse-operator altinity/altinity-clickhouse-operator \
  --namespace sui-indexer \
  --values ../helm/clickhouse-operator/values.yaml

# 3. Ждем инициализации CRD
until kubectl get crd clickhouseinstallations.clickhouse.altinity.com &>/dev/null; do
  sleep 2
  echo "Ожидание доступности CRD..."
done

# 4. Настройка Kafka
helm upgrade --install kafka-operator -f ../helm/kafka/values.yaml strimzi/strimzi-kafka-operator -n sui-indexer
kubectl apply -f ../manifests/02-kafka-cluster.yaml -n sui-indexer

# 5. Разворачиваем ClickHouse кластер
kubectl apply -f ../manifests/01-clickhouse.yaml

# 6. Настройка PostgreSql
helm upgrade postgresql-ha bitnami/postgresql-ha -n sui-indexer -f ../helm/postgresql-ha/values.yaml

# 7. Применяем Network Policies
#kubectl apply -f ../manifests/03-network-policies.yaml

echo "Sui-indexer кластер успешно развернут!"