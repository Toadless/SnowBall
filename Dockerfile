FROM ubuntu:12.04

RUN apt-get update \
 && apt-get install -y sudo

RUN adduser --disabled-password --gecos '' docker
RUN adduser docker sudo
RUN echo '%sudo ALL=(ALL) NOPASSWD:ALL' >> /etc/sudoers

USER docker

RUN sudo apt-get update



FROM openjdk:15-jdk

WORKDIR /home/snowball/

COPY --chown=docker . /home/build/

RUN /home/build/gradlew build --no-daemon

COPY --chown=docker /home/build/build/libs/*.jar /snowball/snowball.jar

RUN rm -r /home/build/

ENTRYPOINT ["java", "-jar", "snowball.jar"]