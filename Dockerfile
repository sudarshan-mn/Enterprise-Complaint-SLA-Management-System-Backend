FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# Copy pom & wrapper first
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline

# Copy source and build
COPY src src
RUN ./mvnw clean package -DskipTests

# Rename jar to a fixed name
RUN mv target/*.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
