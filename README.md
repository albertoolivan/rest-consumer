# ch.rest.consumer

0) Pre requirement - install rest.dto-1.0.0-SNAPSHOT.jar 

https://github.com/albertoolivan/ch.rest.dto.git
$ mvn install


1) Maven install to get rest.consumer-1.0.0-SNAPSHOT.jar

$ mvn install

2) Create a docker image rest-producer

$ docker build -t rest-consumer .

3) Execute docker container

$ docker run -p 8082:8082 rest-consumer

4) Test in browser

http://localhost:8082/find_itinerary_short?cityOriginId=MAD&cityDestinationId=BER