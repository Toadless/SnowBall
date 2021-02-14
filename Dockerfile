FROM openjdk:15-jre

RUN mkdir /prod

COPY --from=build /home/gradle/src/build/libs/Snowball.jar /prod/Snowball.jar

WORKDIR /prod/

ENTRYPOINT ["java", "-jar","/Snowball.jar"]