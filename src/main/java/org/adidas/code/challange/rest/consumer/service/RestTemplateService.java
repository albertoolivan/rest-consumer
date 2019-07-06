package org.adidas.code.challange.rest.consumer.service;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import org.adidas.code.challange.rest.consumer.controller.CityItineraryController;
import org.adidas.code.challange.rest.dto.ExceptionResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParseException;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class RestTemplateService {

	private static Logger logger = LoggerFactory.getLogger(CityItineraryController.class);

	@Autowired
	RestTemplate restTemplate;

	@Bean
	// not working with loadBalanced
	// @LoadBalanced
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	/**
	 * Enter a url and Map<String, String> requestParams, build a proper url.
	 * 
	 * Example: /student Map<"id", "123"><"filter", "2"> -> /student?id=123&filter=2
	 * 
	 * @param apiUrl
	 * @param requestParams
	 * @return String
	 */
	public String uriBuilder(String apiUrl, Map<String, Object> requestParams) {

		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(apiUrl);
		for (Entry<String, Object> entry : requestParams.entrySet()) {
			// Add query parameter
			logger.debug("key {} value {} ", entry.getKey(), entry.getValue());
			builder.queryParam(entry.getKey(), entry.getValue());
		}

		logger.debug("url: {}", builder.toUriString());
		return builder.toUriString();
	}

	/**
	 * Use RestTemplate to GET a responseType object from apiUrl
	 * 
	 * Example: StudentDTO student = (StudentDTO)
	 * restTemplateUtil.getObjectFromUrl(".../student", StudentDTO.class);
	 * 
	 * To use /student/{id} -> /student/123 Set Map<String,String> params = <"id",
	 * "123">
	 * 
	 * To use /student/ @RequestPAram id=1, filter=2 -> /student?id=1&filter=2 Set
	 * Map<String, String> requestParams = <"id", "1">, <"filter", "2">
	 * 
	 * @param apiUrl
	 * @param responseType
	 * @param params
	 * @param requestParams
	 * @return
	 * @throws ResourceAccessException
	 */
	public <T> T getForEntity(String apiUrl, Class<T> responseType, Map<String, String> params,
			Map<String, Object> requestParams) throws ResourceAccessException {
		return getForEntity(uriBuilder(apiUrl, requestParams), responseType, params);
	}

	/**
	 * Use RestTemplate to GET a responseType object from apiUrl
	 * 
	 * Example: StudentDTO student = (StudentDTO)
	 * restTemplateUtil.getObjectFromUrl(".../student", StudentDTO.class);
	 * 
	 * To use /student/{id} -> /student/123 Set Map<String,String> params = <"id",
	 * "123">
	 * 
	 * @param apiUrl
	 * @param responseType
	 * @param params
	 * @return responseType object
	 * @throws ResourceAccessException
	 */
	@SuppressWarnings("unchecked")
	public <T> T getForEntity(String apiUrl, Class<T> responseType, Map<String, String> params)
			throws ResourceAccessException {

		// Refactor with getListFromUrl

		// call receiving a String, then depending HttpStatus, make expected DTO
		// response if HttpStatus=ERROR, we receive a ExceptionResponseDTO with
		// exception message from EDI-Rest
		ResponseEntity<String> response;

		try {
			if (params == null)
				response = restTemplate.getForEntity(apiUrl, String.class);
			else
				response = restTemplate.getForEntity(apiUrl, String.class, params);

			String responseBody = response.getBody();
			logger.info("response: {}", response);

			if (isError(response.getStatusCode())) {
				// throw Exception from ExceptionResponseDTO
				ExceptionResponseDTO error = (ExceptionResponseDTO) deserializeSringJsonToObject(responseBody,
						ExceptionResponseDTO.class);
				logger.error(error.toString());
				throw new RestClientException(error.getMessage() + "\n" + error.getStackTrace());
			} else {
				// result is ok, convert into excepted DTO
				T result = (T) deserializeSringJsonToObject(responseBody, responseType);
				return result;
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Return true if HttpStatus has error code (400s, 500s)
	 * 
	 * @param status
	 * @return boolean
	 */
	public static boolean isError(HttpStatus status) {
		HttpStatus.Series series = status.series();
		return (HttpStatus.Series.CLIENT_ERROR.equals(series) || HttpStatus.Series.SERVER_ERROR.equals(series));
	}

	/**
	 * From a String (Json format) and Class, return Object.
	 * 
	 * @param entry
	 * @param clazz
	 * @return Object
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public Object deserializeSringJsonToObject(String entry, Class<?> clazz)
			throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		Object obj = mapper.readValue(entry, clazz);
		return obj;
	}

}
