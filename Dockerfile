FROM openjdk:15-alpine
WORKDIR /home/snowball/
COPY build/libs/Snowball.jar Snowball.jar
ENTRYPOINT java -jar Snowball.jar