FROM eclipse-temurin:21-jdk-alpine AS build

WORKDIR /build

COPY . .

RUN chmod +x mvnw

RUN ./mvnw -pl sui-api -am clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=build /build/sui-api/target/*.jar app.jar

EXPOSE 8082

ENTRYPOINT ["java", "-jar", "app.jar"]
#ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=prod"]