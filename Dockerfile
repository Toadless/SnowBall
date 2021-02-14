FROM gradle:4.7.0-jdk8-alpine AS build

MAINTAINER Toadless

COPY --chown=gradle:gradle . /home/snowball/src

WORKDIR /home/gradle/src

RUN gradle build --no-daemon

FROM openjdk:15-jdk-alpine

RUN mkdir /prod

COPY --from=build /home/snowball/src/build/libs/*.jar /prod/Snowball.jar

ENTRYPOINT ["java", "-jar", "prod/Snowball.jar"]