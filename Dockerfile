FROM openjdk:15-jdk
MAINTAINER Toadless
COPY application.yml /production/application.yml
COPY Lavalite.jar /production/Lavalite.jar
WORKDIR /production/
CMD ["java","-jar","Lavalite.jar"]