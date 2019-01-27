FROM oracle/graalvm-ce:1.0.0-rc11
WORKDIR /reactive-playground
COPY ./build/libs/reactiveplayground-1.0-SNAPSHOT.jar /reactive-playground/app.jar
#RUN ./gradlew bootJar
EXPOSE 8080
#ENV TESTING a
CMD ["java", "-jar", "app.jar"]
