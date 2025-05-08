# Etapa 1: build da aplicação usando Maven
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa 2: imagem final com o JAR pronto
FROM eclipse-temurin:17-jdk
WORKDIR /app

# Copia o JAR gerado e o application-demo.properties
COPY --from=build /app/target/*.jar app.jar
COPY src/main/resources/application-demo.properties ./application-demo.properties

# Expõe a porta padrão do Spring Boot
EXPOSE 8080

# Executa a aplicação com o profile demo e arquivo de propriedades específico
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=demo", "--spring.config.additional-location=./application-demo.properties"]
