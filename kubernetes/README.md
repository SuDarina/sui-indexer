# ClickHouse Kubernetes Cluster with Replication

## Requirements
- Kubernetes 1.19+
- Helm 3.2+
- kubectl

## Deployment
```bash
chmod +x scripts/deploy.sh
./scripts/deploy.sh
```

## Test Replication
```bash
./scripts/test-replication.sh
```

## Access ClickHouse
```bash
kubectl port-forward svc/clickhouse-cluster 8123:8123 -n clickhouse
# Then connect via: clickhouse-client --host 127.0.0.1 --port 8123
```

## Components
- **Zookeeper**: 3-node ensemble
- **ClickHouse Operator**: Manages cluster lifecycle
- **ClickHouse Cluster**: 1 shard with 3 replicas