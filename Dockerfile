FROM openjdk:13-alpine

ENV ECHO_SERVER_PORT 3000

EXPOSE ${ECHO_SERVER_PORT}

ADD [ "target/sphinx-backend.jar", "/sphinx-backend.jar" ]

ENTRYPOINT [ "java", "-jar", "/sphinx-backend.jar" ]