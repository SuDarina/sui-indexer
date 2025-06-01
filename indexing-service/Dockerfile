FROM eclipse-temurin:21-jdk-alpine AS build

WORKDIR /build

COPY . .

RUN chmod +x mvnw

RUN ./mvnw -pl indexing-service -am clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=build /build/indexing-service/target/*.jar app.jar

EXPOSE 8081

# =====simple docker start=====
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=docker"]

# =====kubernetes start=====
#ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=prod"]
