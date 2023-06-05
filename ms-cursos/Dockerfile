FROM openjdk:17-jdk-alpine AS builder

WORKDIR /app/ms-cursos

COPY ./pom.xml /app
COPY ./ms-cursos/.mvn ./.mvn
COPY ./ms-cursos/mvnw .
COPY ./ms-cursos/pom.xml .

#RUN ./mvnw clean package -Dmaven.test.skip -Dmaven.main.skip -Dspring-boot-repackage.skip && rm -r ./target/
RUN ./mvnw dependency:go-offline

COPY ./ms-cursos/src ./src

RUN ./mvnw clean package -DskipTests

FROM openjdk:17-jdk-alpine

WORKDIR /app
RUN mkdir ./logs
COPY --from=builder /app/ms-cursos/target/ms-cursos-0.0.1-SNAPSHOT.jar .

EXPOSE 8002

CMD ["java", "-jar", "ms-cursos-0.0.1-SNAPSHOT.jar"]

