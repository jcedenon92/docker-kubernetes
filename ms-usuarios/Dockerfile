FROM openjdk:17-jdk-alpine AS builder

WORKDIR /app/ms-usuarios

COPY ./pom.xml /app
COPY ./ms-usuarios/.mvn ./.mvn
COPY ./ms-usuarios/mvnw .
COPY ./ms-usuarios/pom.xml .

#RUN ./mvnw clean package -Dmaven.test.skip -Dmaven.main.skip -Dspring-boot-repackage.skip && rm -r ./target/
RUN ./mvnw dependency:go-offline

COPY ./ms-usuarios/src ./src

RUN ./mvnw clean package -DskipTests

FROM openjdk:17-jdk-alpine

WORKDIR /app
RUN mkdir ./logs
COPY --from=builder /app/ms-usuarios/target/ms-usuarios-0.0.1-SNAPSHOT.jar .

EXPOSE 8001

CMD ["java", "-jar", "ms-usuarios-0.0.1-SNAPSHOT.jar"]

