FROM maven:3.6.3-openjdk-11-slim AS build
COPY src /usr/src/app/src
COPY pom.xml /usr/src/app
RUN mvn -f /usr/src/app/pom.xml clean package

FROM openjdk:11 AS app
COPY --from=build /usr/src/app/target/superheroe-0.0.1-SNAPSHOT.jar /usr/app/superheroe.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar","/usr/app/superheroe.jar"]
