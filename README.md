# Simulador de Investimentos API

API REST para simulação de investimentos em ações, com integração a API da Brapi para consulta de cotações em tempo real.

## Tecnologias utilizadas

- Java 17
- Spring Boot
- OpenFeign
- JUnit / Mockito
- H2 Database (atualmente, para testes)
- Maven
- Git
- Docker

## 🔧 Funcionalidades

- Consultar valor de ações por símbolo
- Criar e deletar usuários
- Comprar e vender ações
- Listar ordens e posições de uma carteira

---

## 🚀 Como rodar

### ✅ Requisitos

- [Docker](https://www.docker.com/) instalado

### ▶️ Executar com Docker

1. Clone o repositório:
   ```bash
   git clone https://github.com/Joaoo-Silva/simulador-investimentos.git
   cd simulador-investimentos
   
2. Compile o projeto para gerar o .jar:
    ./mvnw clean package

3. Suba a aplicação com Docker Compose:
docker-compose up --build

📚 Documentação da API

A API está documentada com Swagger (OpenAPI 3.1).

Acesse via navegador:
http://localhost:8080/swagger-ui.html

Ou baixe o arquivo openapi.json e importe em Postman, Insomnia ou outro de sua preferência.
