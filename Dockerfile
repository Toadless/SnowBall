FROM gradle:5.3.0-jdk8-alpine AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build --no-daemon

FROM openjdk:15-jre-slim

RUN mkdir /prod

COPY --from=build /home/gradle/src/build/libs/*.jar /prod/snowball.jar

ENTRYPOINT ["java", "-jar","/prod/snowball.jar"]