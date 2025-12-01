# --- Estágio 1: Build (Compilar o código) ---
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
WORKDIR /app

# Copia os arquivos do projeto para dentro do container
COPY . .

# Roda o comando do Maven para gerar o .jar (pula os testes para ser mais rápido)
RUN mvn clean package -DskipTests

# --- Estágio 2: Runtime (Rodar a aplicação) ---
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app

# Copia APENAS o .jar gerado no estágio anterior
COPY --from=build /app/target/*.jar app.jar

# Expõe a porta (O Railway vai sobrescrever isso com a variável PORT, mas é bom deixar)
EXPOSE 8080

# Comando para iniciar
ENTRYPOINT ["java", "-jar", "app.jar"]