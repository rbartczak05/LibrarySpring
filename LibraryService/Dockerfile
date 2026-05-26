# Etap 1: Budowanie
FROM maven:3.9-eclipse-temurin-25 AS builder
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Etap 2: Uruchamianie
FROM eclipse-temurin:25-jre
WORKDIR /app
# Zmieniamy ścieżkę na tę, w której Maven rzeczywiście umieszcza zbudowany artefakt
COPY --from=builder /app/Applications/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]