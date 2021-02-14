FROM ubuntu:12.04

RUN apt-get update && \
      apt-get -y install sudo

RUN useradd -m docker && echo "docker:docker" | chpasswd && adduser docker sudo

USER docker

FROM openjdk:15-jdk

WORKDIR --chown=docker /home/snowball/

COPY --chown=docker . /home/build/

RUN /home/build/gradlew build --no-daemon

COPY --chown=docker /home/build/build/libs/*.jar /snowball/snowball.jar

RUN rm -r /home/build/

ENTRYPOINT ["java", "-jar", "snowball.jar"]