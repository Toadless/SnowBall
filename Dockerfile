FROM openjdk:15-jdk

RUN mkdir /snowball

WORKDIR /snowball/snowball/

COPY . /snowball/build/

RUN /snowball/build/gradlew build --no-daemon

COPY /home/build/build/libs/*.jar /snowball/snowball.jar

RUN rm -r /snowball/build/

ENTRYPOINT ["java", "-jar", "snowball.jar"]