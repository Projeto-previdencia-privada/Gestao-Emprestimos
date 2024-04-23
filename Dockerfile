# Compilação do serviço em um JAR
FROM maven:3.9.6-eclipse-temurin-17-alpine as builder
WORKDIR /usr/src/app
COPY src/ src/
COPY pom.xml .
RUN ["mvn", "clean", "install", "-DskipTests"]

# Execução do JAR
FROM eclipse-temurin:17-jdk-alpine as runtime
RUN addgroup -S app && adduser -S app -G app
USER app
COPY --from=builder /usr/src/app/target/*.jar /app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]