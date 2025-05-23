# Развертывание в kubernetes

Для старта кластера с **minikube** с необходимыми параметрами (cpu, memory, disk-size):

```bash
minikube start --cpus=4 --memory=8192 --disk-size=50g 
```

Для остановки **minikube**

```bash
minikube stop
```

Для очистки kubernetes
```bash
kubectl delete all --all -n sui-indexer
kubectl delete configmap --all -n sui-indexer
```

Команда для получения внешнего IP системы для доступа к API:

```bash
minikube service sui-api-service -n sui-indexer --url
```

## Requirements
- Kubernetes 1.19+
- Helm 3.2+
- kubectl

## Скрипт для оазвертывания кластера (Deployment):
```bash
chmod +x scripts/deploy.sh
./scripts/deploy.sh
```
