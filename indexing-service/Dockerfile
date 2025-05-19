# ===== СТАДИЯ 1: Сборка Maven проекта =====
FROM eclipse-temurin:21-jdk-alpine AS build

WORKDIR /build

# Копируем весь проект в build-стадию
COPY . .

# Ставим права на mvnw
RUN chmod +x mvnw

# Собираем только нужный модуль и его зависимости
RUN ./mvnw -pl indexing-service -am clean package -DskipTests

# ===== СТАДИЯ 2: Runtime =====
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Копируем собранный jar
COPY --from=build /build/indexing-service/target/*.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]
#ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=prod"]
