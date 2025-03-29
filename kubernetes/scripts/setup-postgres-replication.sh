#!/bin/bash
# На мастере
kubectl exec postgresql-master-0 -n clickhouse -- psql -U admin -c "
CREATE ROLE repl_user WITH LOGIN REPLICATION PASSWORD 'repl_password';"

# На репликах
for pod in $(kubectl get pods -n clickhouse -l app=postgresql-replica -o name); do
  kubectl exec -it $pod -n clickhouse -- bash -c "
    rm -rf /var/lib/postgresql/data/*
    PGPASSWORD=repl_password pg_basebackup -h postgresql-master -U repl_user -D /var/lib/postgresql/data -P -R
    touch /var/lib/postgresql/data/standby.signal"
done