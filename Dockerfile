FROM openjdk:17-jdk-slim

COPY target/investimentos-0.0.1-SNAPSHOT.jar /app/investimentos.jar

WORKDIR /app

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "investimentos.jar"]
