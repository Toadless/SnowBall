FROM openjdk:15-jdk
MAINTAINER Toadless
COPY application.yml /production/application.yml
COPY Snowball.jar /production/Snowball.jar
WORKDIR /production/
CMD ["java","-jar","Snowball.jar"]