# SmartHut - backend
SA4 Project - Sphinx Team\

This is the backend supporting our smart home application. It listens on port 8080 for requests on [these routes](https://docs.google.com/document/d/1zfh9SWjNTgY78O2VtZwKhbo_0RKyw0YzUvsAOCPs7TQ/edit?usp=sharing).

## Using our premade Docker image
A public Docker image is available on the Docker repository under `steeven9/sa4-sphinx-backend`.

To use it, first pull the image with\
`docker pull steeven9/sa4-sphinx-backend`

then run it with\
`docker run -p 8080:8080 steeven9/sa4-sphinx-backend`

## Building the project
If you want to build the backend from scratch, compile it first with\
`mvn clean compile`

then build the .jar file to run the server\
`mvn package`

and finally put everything in a Docker container\
`docker build ./ -t steeven9/sa4-sphinx-backend:latest`

that you can run with\
`docker run -p 8080:8080 steeven9/sa4-sphinx-backend`