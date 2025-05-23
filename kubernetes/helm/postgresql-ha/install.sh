helm repo add bitnami https://charts.bitnami.com/bitnami
helm install postgresql-ha bitnami/postgresql-ha -n sui-indexer -f ./values.yaml