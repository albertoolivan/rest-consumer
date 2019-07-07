package org.adidas.code.challange.rest.consumer.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import org.adidas.code.challange.rest.consumer.controller.CityItineraryController;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

}
