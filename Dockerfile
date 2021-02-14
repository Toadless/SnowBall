FROM openjdk:15-jdk

WORKDIR ./home/snowball

COPY . ./home/build/

RUN ./home/ ls -a

RUN ./home/gradlew build --no-daemon

COPY ./home/build/build/libs/*.jar /snowball/snowball.jar

RUN rm -r ./home/build/

ENTRYPOINT ["java", "-jar", "snowball.jar"]