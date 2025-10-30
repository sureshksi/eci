#!/bin/bash

set -e  # Exit immediately if a command exits with a non-zero status

echo "Step-1 ==== Building Docker images ===="
docker build -t product-service:latest ./productservice
docker build -t order-service:latest ./orderservice
docker build -t inventory-service:latest ./inventoryservice
docker build -t customer-service:latest ./customerservice
docker build -t notification-service:latest ./notificationservice
docker build -t shipment-service:latest ./shippingservice
docker build -t payment-service:latest ./paymentservice
docker build -t api-gateway:latest ./apigateway

echo "Step-2 ==== Loading images into Minikube ===="
minikube image load product-service:latest
minikube image load order-service:latest
minikube image load inventory-service:latest
minikube image load customer-service:latest
minikube image load payment-service:latest
minikube image load shipment-service:latest
minikube image load notification-service:latest
minikube image load api-gateway:latest

echo "Step-3 ==== Applying Kubernetes manifests ===="
#kubectl delete namespace microservices
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

kubectl create configmap customer-db-init \
  --from-file=init.sql=./customerservice/mysql/init.sql \
  -n microservices

kubectl create configmap payment-db-init \
  --from-file=init.sql=./paymentservice/mysql/init.sql \
  -n microservices

kubectl create configmap shipment-db-init \
  --from-file=init.sql=./shippingservice/mysql/init.sql \
  -n microservices
      
  
echo "Step-3.3 ==== Deploy k8smanifest file ===="
# Step 3.3: Deploy all manifests
kubectl apply -f k8s/product-service-manifests.yaml
kubectl apply -f k8s/order-service-manifests.yaml
kubectl apply -f k8s/inventory-service-manifests.yaml
kubectl apply -f k8s/customer-service-manifests.yaml
kubectl apply -f k8s/payment-service-manifests.yaml
kubectl apply -f k8s/shipping-service-manifests.yaml
kubectl apply -f k8s/notification-service-manifests.yaml
kubectl apply -f k8s/api-gateway-manifests.yaml

echo "==== Deployment complete! ===="

kubectl get all -n microservices
echo "==== Port forward Apigateway from cluster to access ===="
kubectl port-forward service/api-gateway 8080:8080 -n microservices