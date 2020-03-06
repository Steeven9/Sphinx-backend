FROM "ubuntu"

RUN "apt-get update"
RUN "apt-get install -y maven git"
RUN "git clone https://lab.si.usi.ch/sa4-2020/sphinx/backend.git"
RUN "cd backend"
RUN "mvn clean; mvn compile"

EXPOSE 443 80 22