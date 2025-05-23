#  Configs для каждого инстанса
kubectl create configmap clickhouse-config-0 --from-file=config.xml=configs/config-0.xml -n sui-indexer
kubectl create configmap clickhouse-config-1 --from-file=config.xml=configs/config-1.xml -n sui-indexer
kubectl create configmap clickhouse-config-2 --from-file=config.xml=configs/config-2.xml -n sui-indexer

# Общий remote_servers.xml
kubectl create configmap clickhouse-remote-servers --from-file=remote_servers.xml=configs/remote_servers.xml -n sui-indexer

# Общий users.xml
kubectl create configmap clickhouse-users --from-file=users.xml=configs/users.xml -n sui-indexer

# Macros для каждого инстанса
kubectl create configmap clickhouse-macros-0 --from-file=macros.xml=configs/macros-0.xml -n sui-indexer
kubectl create configmap clickhouse-macros-1 --from-file=macros.xml=configs/macros-1.xml -n sui-indexer
kubectl create configmap clickhouse-macros-2 --from-file=macros.xml=configs/macros-2.xml -n sui-indexer

kubectl apply -f clickhouse-service.yaml -n sui-indexer

kubectl apply -f clickhouse-statefulset-0.yaml -n sui-indexer
kubectl apply -f clickhouse-statefulset-1.yaml -n sui-indexer
kubectl apply -f clickhouse-statefulset-2.yaml -n sui-indexer

