# Use OpenJDK image
FROM openjdk:26-ea-17-jdk-slim

# Install curl (and update package list first)
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

# Set working directory
WORKDIR /app

# Copy the JAR (replace with your actual jar name if needed)
COPY target/apigatewayservice-latest.jar apigatewayservice.jar

# Run the JAR
ENTRYPOINT ["java", "-jar", "apigatewayservice.jar"]