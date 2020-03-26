# SmartHut - backend
SA4 Project - Sphinx Team

This is the backend supporting our smart home application. It listens on port `8080` for requests on 
[these routes](https://docs.google.com/document/d/1zfh9SWjNTgY78O2VtZwKhbo_0RKyw0YzUvsAOCPs7TQ/edit?usp=sharing).

**Note:** the port cannot be changed as of now.


### Email service

To correctly run our application, you need to set up an email service. Our service is set up by default, but you will need
to ask the password file (`sh_envfile`) to a team leader to pass it to the container.

The mailer configuration can be overwritten via the following environment variables:

`SH_MAILHOST` mailserver hostname\
`SH_MAILPORT` server port (default: 587)\
`SH_MAILUSER` username\
`SH_MAILPASS` password


## Using our premade Docker image
A public Docker image is available on the Docker repository under `steeven9/sa4-sphinx-backend`.

To use it, first pull the image with\
`docker pull steeven9/sa4-sphinx-backend`

then run it with\
`docker run -it -p 8080:8080 --env-file sh_envfile steeven9/sa4-sphinx-backend`

## Building the project
If you want to build the backend from scratch, compile it first with\
`mvn clean compile`

then build the .jar file to run the server\
`mvn package`

and finally put everything in a Docker container\
`docker build ./ -t sa4-sphinx-backend:latest`

that you can run with\
`docker run -it -p 8080:8080 --env-file sh_envfile sa4-sphinx-backend`