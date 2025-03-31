# Развертывание в kubernetes

Для старта кластера с **minikube** с необходимыми параметрами (cpu, memory, disk-size):

```bash
minikube start --cpus=4 --memory=8192 --disk-size=50g 
```

Для остановки **minikube**

```bash
minikube stop
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
