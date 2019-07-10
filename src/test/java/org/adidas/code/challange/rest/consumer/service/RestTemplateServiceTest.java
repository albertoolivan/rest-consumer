package org.adidas.code.challange.rest.consumer.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.adidas.code.challange.rest.dto.CityDTO;
import org.adidas.code.challange.rest.dto.IntineraryDTO;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class RestTemplateServiceTest {

	private static Logger logger = LoggerFactory.getLogger(RestTemplateServiceTest.class);

	@Test
	public void uriBuilderTest() {
		// prepare
		RestTemplateService restTemplateService = new RestTemplateService();
		String uriTest1 = "/student";
		Map<String, Object> mapTest1 = new HashMap<>();
		mapTest1.put("param1", "value1");
		mapTest1.put("param2", "value2");
		String stringCheck1 = "/student?param1=value1&param2=value2";
		// test
		String stringTest1 = restTemplateService.uriBuilder(uriTest1, mapTest1);
		logger.info("Test - uriBuilder (1): " + stringTest1 + " = " + stringCheck1);
		assertNotNull(stringTest1);
		assertEquals(stringCheck1, stringTest1);

		// prepare
		String uriTest2 = "/student-list";
		Map<String, Object> mapTest2 = new HashMap<>();
		String stringCheck2 = "/student-list";
		// test
		String stringTest2 = restTemplateService.uriBuilder(uriTest2, mapTest2);
		logger.info("Test - uriBuilder (2): " + stringTest2 + " = " + stringCheck2);
		assertNotNull(stringTest2);
		assertEquals(stringCheck1, stringTest1);
	}

	@Test
	public void uriParamBuilderTest() {
		// prepare
		RestTemplateService restTemplateService = new RestTemplateService();
		String uriTest1 = "/student/{id}/{info}";
		Map<String, String> mapTest1 = new HashMap<>();
		mapTest1.put("id", "abc");
		mapTest1.put("info", "123");
		String stringCheck1 = "/student/abc/123";
		// test
		String stringTest1 = restTemplateService.uriParamBuilder(uriTest1, mapTest1);
		logger.info("Test - uriParamBuilder (1): " + stringTest1 + " = " + stringCheck1);
		assertNotNull(stringTest1);
		assertEquals(stringCheck1, stringTest1);

		// prepare
		String uriTest2 = "/student-list";
		Map<String, String> mapTest2 = new HashMap<>();
		String stringCheck2 = "/student-list";
		// test
		String stringTest2 = restTemplateService.uriParamBuilder(uriTest2, mapTest2);
		logger.info("Test - uriParamBuilder (2): " + stringTest2 + " = " + stringCheck2);
		assertNotNull(stringTest2);
		assertEquals(stringCheck1, stringTest1);
	}

	@Test
	public void deserializeStringJsonToObjectTest() throws JsonParseException, JsonMappingException, IOException {
		// prepare
		RestTemplateService restTemplateService = new RestTemplateService();
		IntineraryDTO intineraryDTO = new IntineraryDTO();
		intineraryDTO.setMessage("Itinerary found successfull.");
		intineraryDTO.setDurationTime("12:30");
		intineraryDTO.setSumPathWeight(1500);
		intineraryDTO.setArrivalTime(LocalDateTime.of(2019, 7, 10, 1, 30));
		intineraryDTO.setDepartureTime(LocalDateTime.of(2019, 7, 10, 14, 0));
		LinkedList<CityDTO> path = new LinkedList<>();
		path.add(new CityDTO("MAD", "Madrid"));
		path.add(new CityDTO("BCN", "Barcelona"));
		intineraryDTO.setPath(path);
		String sTest = "{\"path\":[{\"id\":\"MAD\",\"name\":\"Madrid\",\"distanceList\":[]},{\"id\":\"BCN\",\"name\":\"Barcelona\",\"distanceList\":[]}],\"sumPathWeight\":1500,\"message\":\"Itinerary found successfull.\",\"departureTime\":[2019,7,10,14,0],\"arrivalTime\":[2019,7,10,1,30],\"durationTime\":\"12:30\"}";
		// test
		IntineraryDTO intineraryDTOCheck = (IntineraryDTO) restTemplateService.deserializeStringJsonToObject(sTest,
				IntineraryDTO.class);
		logger.info("Test - deserializeStringJsonToObject: {}", intineraryDTOCheck);
		assertNotNull(intineraryDTOCheck);
		assertEquals(intineraryDTO, intineraryDTOCheck);
	}

}
