FROM openjdk:8-jre-alpine

ENV APPLICATION_USER ktor
RUN adduser -D -g '' $APPLICATION_USER

RUN mkdir -p /app/pictures
RUN chown -R $APPLICATION_USER /app
# Mandatory to use Argon2-jvm library
RUN apk add argon2-dev

USER $APPLICATION_USER

COPY ./libs/spotfinder-backend.jar /app/spotfinder-backend.jar
WORKDIR /app

CMD ["java", "-server", "-XX:+UnlockExperimentalVMOptions", "-XX:+UseCGroupMemoryLimitForHeap", "-XX:InitialRAMFraction=2", "-XX:MinRAMFraction=2", "-XX:MaxRAMFraction=2", "-XX:+UseG1GC", "-XX:MaxGCPauseMillis=100", "-XX:+UseStringDeduplication", "-jar", "spotfinder-backend.jar"]