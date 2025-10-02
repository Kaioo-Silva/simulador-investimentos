# Use uma imagem base com OpenJDK
FROM openjdk:17-jdk-slim

# Adiciona os arquivos compilados da aplicação ao container
COPY target/investimentos-0.0.1-SNAPSHOT.jar /app/investimentos.jar

# Define o diretório de trabalho
WORKDIR /app

# Expõe a porta que a aplicação vai rodar
EXPOSE 8080

# Comando para iniciar a aplicação
ENTRYPOINT ["java", "-jar", "investimentos.jar"]
