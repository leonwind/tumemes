FROM openjdk:11-jre-slim
COPY runServer.sh /runServer.sh
COPY ./build/libs/*.jar /service.jar
COPY config.yml /config.yml
ENTRYPOINT ["/runServer.sh", "postgres"]
EXPOSE 8080
