FROM openjdk:15-jdk

USER root

RUN mkdir /home/snowball/

WORKDIR /home/snowball/

COPY --chown=root . /home/build/

RUN /home/build/gradlew build --no-daemon

COPY /home/build/build/libs/*.jar /snowball/snowball.jar

RUN rm -r /home/build/

ENTRYPOINT ["java", "-jar", "snowball.jar"]