FROM gradle:6.8.2-jdk15 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build --no-daemon

FROM openjdk:15-jdk

RUN mkdir /prod

COPY --from=build /home/gradle/src/build/libs/*.jar /prod/snowball.jar

ENTRYPOINT ["java", "-jar","/prod/snowball.jar"]