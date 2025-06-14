# Stage 1: Build the Spring Boot application using Maven
FROM maven:3.9.6-eclipse-temurin-21 AS builder

# Set the working directory inside the container
WORKDIR /build

# Copy the Maven build file and source code into the container
COPY pom.xml .
COPY src ./src

# Build the application and skip the tests to speed up the process
RUN mvn clean package 


# Stage 2: Create the final lightweight image to run the application
FROM eclipse-temurin:21-alpine-3.21

# Set the working directory for the runtime container
WORKDIR /app
RUN apk update && apk add curl
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
# Copy the compiled JAR file from the builder stage
COPY --from=builder /build/target/rentEstate-0.0.1-SNAPSHOT.jar ./application.jar
RUN chown appuser /app/application.jar

USER appuser
# Expose port 8081 for the application
EXPOSE 8081

# Define the default command to run the application
ENTRYPOINT ["java", "-jar", "application.jar"]
