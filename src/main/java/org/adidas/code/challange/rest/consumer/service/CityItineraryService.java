package org.adidas.code.challange.rest.consumer.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.adidas.code.challange.rest.dto.IntineraryDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;

@Service
public class CityItineraryService {

	private static Logger logger = LoggerFactory.getLogger(CityItineraryService.class);

	public static final String REST_PRODUCER = "rest-producer";
	public static final String ITINERARY_SHORT = "/city/itinerary_short";
	public static final String ITINERARY_LESS = "/city/itinerary_less";

	@Autowired
	private DiscoveryClient discoveryClient;

	@Autowired
	private EurekaClient eurekaClient;

	@Autowired
	RestTemplateService restTemplateService;

	public IntineraryDTO getItineraryShort(String cityOriginId, String cityDestinationId) {
		String apiUrl = "http://" + REST_PRODUCER + ITINERARY_SHORT;
		return getItinerary(cityOriginId, cityDestinationId, apiUrl);
	}

	public IntineraryDTO getItineraryLess(String cityOriginId, String cityDestinationId) {
		String apiUrl = "http://" + REST_PRODUCER + ITINERARY_LESS;
		return getItinerary(cityOriginId, cityDestinationId, apiUrl);
	}

	public IntineraryDTO getItinerary(String cityOriginId, String cityDestinationId, String apiUrl) {
		IntineraryDTO result = null;
		// check if service is available
		List<String> serviceList = discoveryClient.getServices();
		if (serviceList.contains(REST_PRODUCER)) {
			// display service info
			InstanceInfo instance = eurekaClient.getNextServerFromEureka(REST_PRODUCER, false);
			logger.debug("Instance server status: {}", instance.getStatus());
			// prepare call
			Map<String, Object> requestParams = new HashMap<>();
			requestParams.put("cityOriginId", cityOriginId);
			requestParams.put("cityDestinationId", cityDestinationId);
			logger.info("Call service url {} with {}", apiUrl, requestParams);
			// call rest-producer
			result = restTemplateService.getForEntity(apiUrl, IntineraryDTO.class, null, requestParams);
			logger.info("Rest findItinerary() Return {}", result);
		} else {
			String message = "Rest-producer service not available, please try later...";
			logger.info(message);
			result = new IntineraryDTO();
			result.setMessage(message);
		}
		return result;
	}

}