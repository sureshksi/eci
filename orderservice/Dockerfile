# Use OpenJDK image
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Copy the JAR (replace with your actual jar name if needed)
COPY target/orderservice-latest.jar orderservice.jar

COPY wait-for-it.sh wait-for-it.sh

# Make the script executable
RUN chmod +x wait-for-it.sh

# Run wait-for-it before launching the app
ENTRYPOINT ["./wait-for-it.sh", "mysql:3306", "--", "java", "-jar", "orderservice.jar"]

# Run the JAR
#ENTRYPOINT ["java", "-jar", "app.jar"]
