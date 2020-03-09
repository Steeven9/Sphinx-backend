FROM openjdk:13-alpine

ENV ECHO_SERVER_PORT 3000

EXPOSE ${ECHO_SERVER_PORT}

ADD [ "target/sphinx-*", "/sa4-sphinx.jar" ]

ENTRYPOINT [ "java", "-jar", "/sa4-sphinx.jar" ]