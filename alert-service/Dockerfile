FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /app

COPY . .

RUN mvn -pl alert-service -am clean package -Dmaven.test.skip=true

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=build /app/alert-service/target/*.jar app.jar

ENTRYPOINT ["java","-jar","app.jar"]