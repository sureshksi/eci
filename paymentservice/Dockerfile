# Use OpenJDK image 
FROM openjdk:26-ea-17-jdk-slim

# Install curl (and update package list first)
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

# Set working directory
WORKDIR /app

# Copy the JAR (replace with your actual jar name if needed)
COPY target/paymentservice-latest.jar paymentservice.jar

#COPY wait-for-it.sh wait-for-it.sh

# Make the script executable
#RUN chmod +x wait-for-it.sh

# Run wait-for-it before launching the app
#ENTRYPOINT ["./wait-for-it.sh", "mysql:3306", "--", "java", "-jar", "productservice.jar"]

# Run the JAR
ENTRYPOINT ["java", "-jar", "paymentservice.jar"]
