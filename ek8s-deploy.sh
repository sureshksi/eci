#!/bin/bash

set -e  # Exit immediately if a command exits with a non-zero status

echo "Step-1 ==== Building Docker images ===="
docker build -t product-service:latest ./productservice
docker build -t order-service:latest ./orderservice
docker build -t inventory-service:latest ./inventoryservice
docker build -t api-gateway:latest ./apigateway

echo "Step-2 ==== Loading images into Minikube ===="
minikube image load product-service:latest
minikube image load order-service:latest
minikube image load inventory-service:latest
minikube image load api-gateway:latest

echo "Step-3 ==== Applying Kubernetes manifests ===="
echo "Step-3.1 ==== ConfigMap Namespace ===="
# Step 3.1: Create namespace first
kubectl create namespace microservices || echo "Namespace already exists"

echo "Step-3.2 ==== ConfigMap for service sql seedata ===="
# Step 3.2: Create ConfigMaps
kubectl create configmap product-db-init \
  --from-file=init.sql=./productservice/mysql/init.sql \
  -n microservices

kubectl create configmap order-db-init \
  --from-file=init.sql=./orderservice/mysql/init.sql \
  -n microservices

kubectl create configmap inventory-db-init \
  --from-file=init.sql=./inventoryservice/mysql/init.sql \
  -n microservices
echo "Step-3.3 ==== Deploy k8smanifest file ===="
# Step 3.3: Deploy all manifests
kubectl apply -f k8s/product-service-manifests.yaml
kubectl apply -f k8s/order-service-manifests.yaml
kubectl apply -f k8s/inventory-service-manifests.yaml
kubectl apply -f k8s/api-gateway-manifests.yaml

echo "==== Deployment complete! ===="

kubectl get all -n microservices
echo "==== Port forward Apigateway from cluster to access ===="
kubectl port-forward service/api-gateway 8080:8080 -n microservices