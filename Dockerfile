FROM openjdk:15-alpine
ARG JAR_FILE=target/final.jar
COPY ${JAR_FILE} app.jar
COPY resources /resources

ENTRYPOINT ["java","-jar","/app.jar","--spring.config.location=file:/resources/application.properties"]

