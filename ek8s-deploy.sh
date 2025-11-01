#!/usr/bin/env bash
set -e

echo "🚀 Starting microservices deployment..."

# -------------------------------
# Step 0: Check and Start Minikube
# -------------------------------
if ! minikube status &>/dev/null; then
  echo "➡️  Starting Minikube..."
  minikube start
else
  echo "✅ Minikube is running."
fi

# Use Minikube's Docker daemon (optional but recommended)
eval $(minikube docker-env)

# -------------------------------
# Step 1: Build Docker Images
# -------------------------------
echo "🧱 Step 1: Building Docker images..."

services=(
  "product-service:productservice"
  "order-service:orderservice"
  "inventory-service:inventoryservice"
  "customer-service:customerservice"
  "notification-service:notificationservice"
  "shipment-service:shippingservice"
  "payment-service:paymentservice"
  "api-gateway:apigateway"
)

for svc in "${services[@]}"; do
  name="${svc%%:*}"
  dir="${svc##*:}"
  echo "➡️  Building $name"
  docker build -t "$name:latest" "./$dir"
done

# -------------------------------
# Step 2: Kubernetes Setup
# -------------------------------
NAMESPACE="microservices"

echo "➡️  Creating namespace if not exists..."
kubectl create namespace "$NAMESPACE" --dry-run=client -o yaml | kubectl apply -f -

# Step 2.1: Create ConfigMaps for SQL seed data
echo "➡️  Creating ConfigMaps for DB init scripts..."

db_inits=(
  "product-db-init:./productservice/mysql/init.sql"
  "order-db-init:./orderservice/mysql/init.sql"
  "inventory-db-init:./inventoryservice/mysql/init.sql"
  "customer-db-init:./customerservice/mysql/init.sql"
  "payment-db-init:./paymentservice/mysql/init.sql"
  "shipment-db-init:./shippingservice/mysql/init.sql"
)

for item in "${db_inits[@]}"; do
  name="${item%%:*}"
  file="${item##*:}"
  if [ -f "$file" ]; then
    echo "   ↳ $name"
    kubectl delete configmap "$name" -n "$NAMESPACE" --ignore-not-found
    kubectl create configmap "$name" --from-file=init.sql="$file" -n "$NAMESPACE"
  else
    echo "⚠️  Skipping $name — file not found: $file"
  fi
done

# -------------------------------
# Step 3: Deploy Each Service with Delay
# -------------------------------
echo "🚀 Deploying microservices with 1-minute intervals..."

deployments=(
  "product-service:k8s/product-service-manifests.yaml"
  "order-service:k8s/order-service-manifests.yaml"
  "inventory-service:k8s/inventory-service-manifests.yaml"
  "customer-service:k8s/customer-service-manifests.yaml"
  "payment-service:k8s/payment-service-manifests.yaml"
  "shipment-service:k8s/shipping-service-manifests.yaml"
  "notification-service:k8s/notification-service-manifests.yaml"
  "api-gateway:k8s/api-gateway-manifests.yaml"
)

for item in "${deployments[@]}"; do
  svc="${item%%:*}"
  file="${item##*:}"
  echo "➡️  Applying manifests for $svc"
  kubectl apply -n "$NAMESPACE" -f "$file"

  echo "⏳ Waiting 1 minute to let $svc stabilize..."
  sleep 60
done

# -------------------------------
# Step 4: Post-Deployment Summary
# -------------------------------
echo "✅ All services deployed."
kubectl get pods -n "$NAMESPACE"
kubectl get svc -n "$NAMESPACE"

# -------------------------------
# Step 5: Port Forward API Gateway
# -------------------------------
echo "🌐 Access API Gateway at http://localhost:8080"
echo "➡️  Use Ctrl+C to stop port-forwarding."
kubectl port-forward service/api-gateway 8080:8080 -n "$NAMESPACE"
