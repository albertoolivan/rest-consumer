package org.adidas.code.challange.rest.consumer.service;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.adidas.code.challange.rest.dto.CityDTO;
import org.adidas.code.challange.rest.dto.IntineraryDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.discovery.DiscoveryClient;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;

@RunWith(MockitoJUnitRunner.class)
public class CityItineraryServiceTest {

	private static Logger logger = LoggerFactory.getLogger(CityItineraryServiceTest.class);

	@Mock
	private DiscoveryClient discoveryClient;

	@Mock
	private EurekaClient eurekaClient;

	@Mock
	RestTemplateService restTemplateService;

	@InjectMocks
	CityItineraryService cityItineraryService;

	@Test
	public void cityListTest() {
		// prepare
		Mockito.when(discoveryClient.getServices()).thenReturn(getServiceList());
		Mockito.when(restTemplateService.getForEntity(Mockito.anyString(), Mockito.eq(List.class), Mockito.any()))
				.thenReturn(getCityDTOList());
		// test
		List<CityDTO> check = cityItineraryService.cityList();
		logger.info("Test - cityList: " + check);
		assertEquals(getCityDTOList(), check);
	}

	@Test
	public void cityInfoTest() {
		// prepare
		Mockito.when(discoveryClient.getServices()).thenReturn(getServiceList());
		Mockito.when(restTemplateService.getForEntity(Mockito.anyString(), Mockito.eq(CityDTO.class), Mockito.any()))
				.thenReturn(getCityDTO());
		// test
		CityDTO check = cityItineraryService.cityInfo("MAD");
		logger.info("Test - cityInfo: " + check);
		assertEquals(getCityDTO(), check);
	}

	@Test
	public void getItineraryTest() {
		// prepare
		IntineraryDTO intineraryDTOExpected = new IntineraryDTO();
		Mockito.when(discoveryClient.getServices()).thenReturn(getServiceList());
		Mockito.when(eurekaClient.getNextServerFromEureka(Mockito.anyString(), Mockito.anyBoolean()))
				.thenReturn(getInstanceInfo());
		Mockito.when(restTemplateService.getForEntity(Mockito.anyString(), Mockito.eq(IntineraryDTO.class),
				Mockito.any(), Mockito.anyMap())).thenReturn(intineraryDTOExpected);
		// test
		IntineraryDTO check = cityItineraryService.getItinerary("MAD", "BCN", LocalDateTime.of(2019, 7, 10, 10, 30),
				"url");
		logger.info("Test - getItinerary: " + check);
		assertEquals(intineraryDTOExpected, check);
	}

	public static List<CityDTO> getCityDTOList() {
		List<CityDTO> cityDTOList = new ArrayList<>();
		cityDTOList.add(getCityDTO());
		return cityDTOList;
	}

	public static CityDTO getCityDTO() {
		return new CityDTO("MAD", "Madrid");
	}

	public static List<String> getServiceList() {
		List<String> serviceList = new ArrayList<>();
		serviceList.add(CityItineraryService.REST_PRODUCER);
		return serviceList;
	}

	public static InstanceInfo getInstanceInfo() {
		InstanceInfo instanceInfo = InstanceInfo.Builder.newBuilder().setAppName(CityItineraryService.REST_PRODUCER)
				.build();
		return instanceInfo;
	}

}
