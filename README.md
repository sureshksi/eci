# eci
E-commerce with Inventory (ECI)
eci/
│
├── productservice/
│   ├── Dockerfile
│   ├── src/
│   └── build.gradle / pom.xml
│   └── application.yml
├── orderservice/
│   ├── Dockerfile
│   ├── src/
│   └── build.gradle / pom.xml
│   └── application.yml
├── inventoryservice/
│   ├── Dockerfile
│   ├── src/
│   └── build.gradle / pom.xml
│   └── application.yml
├── customerservice/
│   ├── Dockerfile
│   ├── src/
│   └── build.gradle / pom.xml
│   └── application.yml
├── notificationservice/
│   ├── Dockerfile
│   ├── src/
│   └── build.gradle / pom.xml
│   └── application.yml
├── paymentservice/
│   ├── Dockerfile
│   ├── src/
│   └── build.gradle / pom.xml
│   └── application.yml
├── api-gateway/
│   ├── Dockerfile
│   ├── src/
│   └── build.gradle / pom.xml
│   └── application.yml
├── k8s/
│   ├── api-gateway-manifests.yaml
│   ├── customer-service-manifests.yaml
├   |── inventory-service-manifests.yaml
├   |── notification-service-manifests.yaml
│   |── order-service-manifests.yaml
├   |── payment-service-manifests.yaml
├   |── product-service-manifests.yaml
│   └── shipping-service-manifests.yaml
└── pom.xml
└── docker-compose.yml



Step1-Run pom.xml in eci project using maven.
Step2-Run docker-compose.yml from eci project from CLI

