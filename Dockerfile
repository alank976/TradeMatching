FROM openjdk:8u191-jre-alpine3.9
WORKDIR /trade-matching
COPY ./build/libs/trade-matching-1.0-SNAPSHOT.jar /trade-matching/app.jar
#RUN ./gradlew bootJar
EXPOSE 8080
#ENV TESTING a
CMD ["java", "-Dspring.profiles.active=default,prod", "-jar", "app.jar"]
