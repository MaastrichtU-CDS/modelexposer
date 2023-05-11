FROM maven:3.9.0-eclipse-temurin-17-alpine as builder

RUN mkdir /build
COPY ./pom.xml /build/pom.xml
COPY /src/. /build/src
COPY /resources/. /build/resources
COPY /openmarkov/. /build/openmarkov
COPY ./checkstyle.xml /build/checkstyle.xml

WORKDIR /build
RUN mvn install:install-file -Dfile=./openmarkov/OpenMarkov-0.4.1.jar -DgroupId=org.openmarkov -DartifactId=org.openmarkov.full -Dversion=0.4.1-SNAPSHOT -Dpackaging=jar
RUN mvn clean install

COPY resources /resources

FROM maven:3.9.0-eclipse-temurin-17-alpine
WORKDIR /root

COPY /resources/seswoa_1.csv /build/resources/seswoa_1.csv
COPY /resources/seswoa_2.csv /build/resources/seswoa_2.csv
COPY /resources/application.properties /build/resources/application.properties

COPY --from=0 /build/target/final.jar /build/target/final.jar

CMD ["java","-jar","/build/target/final.jar","--spring.config.location=file:/build/resources/application.properties"]

