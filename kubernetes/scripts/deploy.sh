#!/bin/bash
set -e  # Прерывать выполнение при ошибках

# 1. Создаем namespace
kubectl apply -f ../manifests/00-namespace-clickhouse.yaml

# 2. Устанавливаем Zookeeper (только 2 реплики для minikube)
kubectl apply -f ../manifests/01-zookeeper.yaml

# 3. Ждем готовности Zookeeper (только 2 пода)
kubectl wait --for=condition=Ready pod/zookeeper-0 -n clickhouse --timeout=300s
kubectl wait --for=condition=Ready pod/zookeeper-1 -n clickhouse --timeout=300s

# 4. Устанавливаем ClickHouse Operator
helm repo add altinity https://altinity.github.io/clickhouse-operator
helm repo update
helm upgrade --install clickhouse-operator altinity/altinity-clickhouse-operator \
  --namespace clickhouse \
  --values ../helm/clickhouse-operator/values.yaml

# 5. Ждем инициализации CRD
until kubectl get crd clickhouseinstallations.clickhouse.altinity.com &>/dev/null; do
  sleep 2
  echo "Ожидание доступности CRD..."
done

# 6. Разворачиваем ClickHouse кластер
kubectl apply -f ../manifests/02-clickhouse.yaml

# Настройка PostgreSql
kubectl apply -f manifests/03-postgresql/
./scripts/setup-postgres-replication.sh

# 7. Применяем Network Policies
kubectl apply -f ../manifests/04-network-policies.yaml

echo "ClickHouse кластер успешно развернут!"