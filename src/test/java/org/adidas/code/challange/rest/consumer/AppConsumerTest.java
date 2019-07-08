package org.adidas.code.challange.rest.consumer;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Test AppConsumer to start up a EurekaServer and check if service is available
 * calling rest /service-instances/rest-consumer
 * 
 * This test can display exceptions in console because try to discover/connect
 * many times.
 * 
 * @author alberto.olivan
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AppConsumer.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class AppConsumerTest {

	private static Logger logger = LoggerFactory.getLogger(AppConsumerTest.class);

	static ConfigurableApplicationContext eurekaServer;

	@BeforeClass
	public static void startEureka() {
		eurekaServer = SpringApplication.run(EurekaServer.class, "--server.port=8761",
				"--eureka.instance.leaseRenewalIntervalInSeconds=1");
	}

	@AfterClass
	public static void closeEureka() {
		eurekaServer.close();
	}

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate testRestTemplate;

	@Test
	public void shouldRegisterClientInEurekaServer() throws InterruptedException {
		// registration has to take place...
		Thread.sleep(5000);

		logger.info("Call service-instances/rest-consumer to check if servie is registered in EurekaServer");
		ResponseEntity<String> response = this.testRestTemplate
				.getForEntity("http://localhost:" + this.port + "/service-instances/rest-consumer", String.class);

		logger.info("Response: {}", response.getBody());
		then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		then(response.getBody()).contains("rest-consumer");
	}

	@Test
	public void findItineraryShortTest() throws InterruptedException {
		// registration has to take place...
		Thread.sleep(5000);

		logger.info("Call /find_itinerary-short to check controller return Rest-producer is not available.");
		ResponseEntity<String> response = this.testRestTemplate
				.getForEntity("http://localhost:" + this.port + "/find-itinerary-short?cityOriginId=MAD&cityDestinationId=BER", String.class);

		logger.info("Response: {}", response.getBody());
		then(response.getStatusCode()).isEqualTo(HttpStatus.SERVICE_UNAVAILABLE);
		then(response.getBody()).contains("Rest-producer service not available, please try later...");
	}
	
	@Configuration
	@EnableAutoConfiguration
	@EnableEurekaServer
	static class EurekaServer {
	}
}