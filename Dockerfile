FROM maven:3.8-eclipse-temurin-17-alpine as builder

RUN mkdir /build
COPY ./pom.xml /build/pom.xml
COPY /src/. /build/src
COPY /resources/. /build/resources
COPY /openmarkov/. /build/openmarkov
COPY ./checkstyle.xml /build/checkstyle.xml

WORKDIR /build
RUN mvn install:install-file -Dfile=./openmarkov/OpenMarkov-0.4.0.jar -DgroupId=org.openmarkov -DartifactId=org.openmarkov.full -Dversion=0.4.0-SNAPSHOT -Dpackaging=jar
RUN mvn clean install

COPY resources /resources

ENTRYPOINT ["java","-jar","/build/target/final.jar","--spring.config.location=file:/build/resources/application.properties"]

