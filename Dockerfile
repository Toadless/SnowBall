FROM openjdk:15-jdk-alpine

MAINTAINER Toadless

RUN gradlew build

COPY build/libs/Snowball.jar Snowball.jar

CMD ["java","-jar","Snowball.jar"]