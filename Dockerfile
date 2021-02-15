FROM openjdk:15-alpine
COPY ./build/libs/Snowball.jar /home/snowball/Snowball.jar
WORKDIR /home/snowball/
ENTRYPOINT java -jar Snowball.jar