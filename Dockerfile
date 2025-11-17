FROM gradle:8.1.1-jdk17
COPY ./ ./
RUN gradle build

FROM eclipse-temurin:17-jre

ARG JAR=/build/libs/*.jar
COPY $JAR parley-bot.jar
EXPOSE 8080

CMD java -jar /parley-bot.jar