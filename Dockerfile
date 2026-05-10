FROM gradle:8.14.3-jdk21
COPY ./ ./
RUN gradle build

FROM eclipse-temurin:21-jre

ARG JAR=/build/libs/*.jar
COPY $JAR parley-bot.jar
EXPOSE 8080

CMD java -jar /parley-bot.jar