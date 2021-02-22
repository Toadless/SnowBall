FROM openjdk:15-jdk
WORKDIR /home/snowball/
COPY build/libs/Snowball.jar Snowball.jar
ENTRYPOINT java -jar Snowball.jar