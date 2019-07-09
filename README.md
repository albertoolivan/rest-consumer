# rest-consumer

rest-consumer is a common rest wich function is connect to a eureka [server-registry](https://github.com/albertoolivan/server-registry) and call [rest-producer](https://github.com/albertoolivan/rest-producer).

0) Pre requirement - install rest-dto-1.0.0-SNAPSHOT.jar 

https://github.com/albertoolivan/ch.rest.dto.git
$ mvn install


1) Maven install to get rest-consumer-1.0.0-SNAPSHOT.jar

$ mvn install

2) Create a docker image rest-producer

$ docker build -t rest-consumer .

3) Execute docker container

$ docker run -p 8082:8082 rest-consumer

4) Test in browser (need service-registry and rest-producer up) 

http://localhost:8082/city/all
http://localhost:8082/city/info/MAD
http://localhost:8082/find-itinerary-short?cityOriginId=MAD&cityDestinationId=BER&departureTime=2019-07-10T01:30:00.000Z
http://localhost:8082/find-itinerary-less?cityOriginId=MAD&cityDestinationId=BER&departureTime=2019-07-10T01:30:00.000Z

5) Swagger Rest doc

http://localhost:8082/swagger-ui.html

See [eureka-dijkstra](https://github.com/albertoolivan/eureka-dijkstra) for more info