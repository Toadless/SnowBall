FROM openjdk:15-jdk

RUN mkdir /prod

COPY build/libs/Snowball.jar /prod/Snowball.jar

WORKDIR /prod/

ENTRYPOINT ["java", "-jar","/Snowball.jar"]