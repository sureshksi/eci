# Use OpenJDK image
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Copy the JAR (replace with your actual jar name if needed)
COPY target/apigatewayservice-latest.jar apigatewayservice.jar

# Run the JAR
ENTRYPOINT ["java", "-jar", "apigatewayservice.jar"]
