FROM openjdk:21-jdk-slim

WORKDIR /app

COPY target/ticketmanagement-0.0.1-SNAPSHOT.jar app.jar

# Expose application port and debug port
EXPOSE 8080 5005

# Enable remote debug with suspend=n (don’t wait on start)
ENTRYPOINT ["java", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005", "-jar", "app.jar"]

