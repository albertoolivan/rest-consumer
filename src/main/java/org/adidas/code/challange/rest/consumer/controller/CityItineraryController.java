package org.adidas.code.challange.rest.consumer.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.adidas.code.challange.rest.consumer.service.RestTemplateService;
import org.adidas.code.challange.rest.dto.ExceptionResponseDTO;
import org.adidas.code.challange.rest.dto.IntineraryDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;

@RestController
public class CityItineraryController {

	private static Logger logger = LoggerFactory.getLogger(CityItineraryController.class);

	@Autowired
	private DiscoveryClient discoveryClient;

	public static final String REST_PRODUCER = "rest-producer";

	@Autowired
	private EurekaClient eurekaClient;

	@Autowired
	RestTemplateService restTemplateService;

	/**
	 * GET /city/itinerary_short
	 * 
	 * Example:
	 * http://localhost:8082/find_itinerary_short?cityOriginId=MAD&cityDestinationId=BER
	 * 
	 * Get IntineraryDTO info
	 * 
	 * @param cityOriginId
	 * @param cityDestinationId
	 * @return IntineraryDTO
	 */
	@RequestMapping(value = "/find_itinerary_short", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> findItineraryShort(
			@RequestParam(value = "cityOriginId", required = true) String cityOriginId,
			@RequestParam(value = "cityDestinationId", required = true) String cityDestinationId) {
		try {
			logger.info("Rest findItinerary() called with cityOriginId {} cityDestinationId {}", cityOriginId,
					cityDestinationId);
			// check if service is available
			List<String> serviceList = discoveryClient.getServices();
			if (serviceList.contains(REST_PRODUCER)) {
				// display service info
				InstanceInfo instance = eurekaClient.getNextServerFromEureka(REST_PRODUCER, false);
				logger.debug("Instance server status: {}", instance.getStatus());
				// prepare call
				IntineraryDTO result = null;
				String apiUrl = "http://rest-producer/city/itinerary_short";
				Map<String, Object> requestParams = new HashMap<>();
				requestParams.put("cityOriginId", cityOriginId);
				requestParams.put("cityDestinationId", cityDestinationId);
				logger.info("Call service url {} with {}", apiUrl, requestParams);
				// call rest-producer
				result = restTemplateService.getForEntity(apiUrl, IntineraryDTO.class, null, requestParams);
				logger.info("Rest findItinerary() Return {}", result);
				if (result != null) {
					return ResponseEntity.ok().body(result);
				} else {
					return ResponseEntity.notFound().build();
				}
			} else {
				String message = "Rest-producer service not available, please try later...";
				logger.info(message);
				IntineraryDTO result = new IntineraryDTO();
				result.setMessage(message);
				return ResponseEntity.ok().body(result);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(new ExceptionResponseDTO(e), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
