FROM openjdk:11.0.4-jre-slim
RUN mkdir /app
WORKDIR /app
COPY ./target/image-managing-1.0.0-SNAPSHOT.jar /app
EXPOSE 8080
CMD ["java", "-jar", "image-managing-1.0.0-SNAPSHOT.jar"]
