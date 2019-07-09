package org.adidas.code.challange.rest.consumer.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.adidas.code.challange.rest.consumer.exception.RestProducerNotFoundException;
import org.adidas.code.challange.rest.dto.CityDTO;
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

	public static final String HTTP = "http://";
	public static final String REST_PRODUCER = "rest-producer";
	public static final String CITY_LIST = "/city/all";
	public static final String CITY_INFO = "/city/info/{id}";
	public static final String ITINERARY_SHORT = "/city/itinerary-short";
	public static final String ITINERARY_LESS = "/city/itinerary-less";

	public static final String REST_NOT_FOUND_ERROR = "Rest-producer service not available, please try later...";

	@Autowired
	private DiscoveryClient discoveryClient;

	@Autowired
	private EurekaClient eurekaClient;

	@Autowired
	RestTemplateService restTemplateService;

	/**
	 * Get List<CityDTO> calling rest-producer /city/all
	 * 
	 * @return List<CityDTO>
	 */
	@SuppressWarnings("unchecked")
	public List<CityDTO> cityList() {
		String apiUrl = HTTP + REST_PRODUCER + CITY_LIST;
		List<CityDTO> result = null;
		// check if service is available
		List<String> serviceList = discoveryClient.getServices();
		if (serviceList.contains(REST_PRODUCER)) {
			logger.info("Call service url {}", apiUrl);
			// call rest-producer
			result = restTemplateService.getForEntity(apiUrl, List.class, null);
			logger.info("Rest cityList() Return {}", result);
		} else {
			throw new RestProducerNotFoundException(REST_NOT_FOUND_ERROR);
		}
		return result;
	}

	/**
	 * Get CityDTO calling rest-producer /city/info/{id}
	 * 
	 * @param id
	 * @return CityDTO
	 */
	public CityDTO cityInfo(String id) {
		String apiUrl = HTTP + REST_PRODUCER + CITY_INFO;
		CityDTO result = null;
		// check if service is available
		List<String> serviceList = discoveryClient.getServices();
		if (serviceList.contains(REST_PRODUCER)) {
			Map<String, String> params = new HashMap<>();
			params.put("id", id);
			logger.info("Call service url {} with {}", apiUrl, id);
			// call rest-producer
			result = restTemplateService.getForEntity(apiUrl, CityDTO.class, params);
			logger.info("Rest cityInfo() Return {}", result);
		} else {
			throw new RestProducerNotFoundException(REST_NOT_FOUND_ERROR);
		}
		return result;
	}

	/**
	 * Get IntineraryDTO from short distance method, calling rest-producer /city/itinerary-short
	 * 
	 * @param cityOriginId
	 * @param cityDestinationId
	 * @return IntineraryDTO
	 */
	public IntineraryDTO getItineraryShort(String cityOriginId, String cityDestinationId) {
		String apiUrl = HTTP + REST_PRODUCER + ITINERARY_SHORT;
		return getItinerary(cityOriginId, cityDestinationId, apiUrl);
	}

	/**
	 * Get IntineraryDTO from less steps method, calling rest-producer /city/itinerary-less
	 * 
	 * @param cityOriginId
	 * @param cityDestinationId
	 * @return IntineraryDTO
	 */
	public IntineraryDTO getItineraryLess(String cityOriginId, String cityDestinationId) {
		String apiUrl = HTTP + REST_PRODUCER + ITINERARY_LESS;
		return getItinerary(cityOriginId, cityDestinationId, apiUrl);
	}

	/**
	 * Get IntineraryDTO from url (short or less)
	 * 
	 * @param cityOriginId
	 * @param cityDestinationId
	 * @param apiUrl
	 * @return IntineraryDTO
	 */
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
			throw new RestProducerNotFoundException(REST_NOT_FOUND_ERROR);
		}
		return result;
	}

}
