FROM openjdk:15-jdk

WORKDIR /home/snowball

COPY build/libs/Snowball.jar snowball.jar

RUN apk update && apk upgrade && apk add curl

ENTRYPOINT ["java", "-jar", "snowball.jar"]
CMD ["-Xmx1G", "-XX:+UseG1GC"]