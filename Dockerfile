# --- Estágio 1: Build ---
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
WORKDIR /app

COPY . .

# CORREÇÃO AQUI: Adicionei flags para forçar o encoding UTF-8 e evitar erro com acentos
RUN mvn clean package -DskipTests -Dproject.build.sourceEncoding=UTF-8 -Dproject.reporting.outputEncoding=UTF-8

# --- Estágio 2: Runtime ---
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]