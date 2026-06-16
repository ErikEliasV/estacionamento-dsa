# --- Etapa de build ---
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
# Cache de dependências: copia o pom primeiro
COPY pom.xml .
RUN mvn -q dependency:go-offline
COPY src ./src
RUN mvn -q clean package -DskipTests

# --- Etapa de runtime ---
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/estacionamento-0.0.1-SNAPSHOT.jar app.jar
# O Render injeta a variável PORT; o Spring lê via server.port=${PORT:8080}
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
