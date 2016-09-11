FROM java:8-jdk
MAINTAINER Erik Khalimov <biomaks@gmail.com>
RUN apt-get update && apt-get install -y curl vim nano maven
RUN git clone https://github.com/biomaks/java-transit-bot.git application
WORKDIR /application
RUN mvn clean install
CMD mvn exec:java -Dexec.mainClass=com.github.Main
