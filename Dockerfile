FROM gradle:4.7.0-jdk8-alpine

MAINTAINER Toadless

RUN gradle build --no-daemon

FROM openjdk:15-jdk-alpine

ENTRYPOINT ["java", "-jar", "build/libs/Snowball.jar"]