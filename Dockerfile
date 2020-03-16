FROM openjdk:13-alpine

ENV SERVER_PORT 8080

EXPOSE ${SERVER_PORT}

ADD [ "target/sphinx-backend.jar", "/sphinx-backend.jar" ]

ENTRYPOINT [ "java", "-jar", "/sphinx-backend.jar" ]