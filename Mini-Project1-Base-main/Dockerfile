# Use OpenJDK 25 as the base image
FROM openjdk:25-ea-4-jdk-oraclelinux9

# Set the working directory
WORKDIR /app

# Copy the entire project into the container
COPY ./ /app

# Set environment variable for JSON data path
ENV DATA_JSON_PATH=/app/data

# Ensure the data directory exists
RUN mkdir -p /app/data

# Expose port 8080 for the application
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "/app/target/mini1.jar"]
