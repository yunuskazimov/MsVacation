FROM openjdk:17-slim-buster

COPY build/libs/MsVacation-0.0.1-SNAPSHOT.jar .

ENTRYPOINT java -jar MsVacation-0.0.1-SNAPSHOT.jar