FROM openjdk:15-jdk

RUN mkdir ./home

WORKDIR ./home

RUN mkdir ./snowball

WORKDIR ./snowball

COPY . /home/build/

RUN /home/build/gradlew build --no-daemon

COPY /home/build/build/libs/*.jar /snowball/snowball.jar

RUN rm -r /home/build/

ENTRYPOINT ["java", "-jar", "snowball.jar"]