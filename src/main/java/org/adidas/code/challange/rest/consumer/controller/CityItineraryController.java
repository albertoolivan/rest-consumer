package org.adidas.code.challange.rest.consumer.controller;

import java.util.List;

import org.adidas.code.challange.rest.consumer.exception.RestProducerNotFoundException;
import org.adidas.code.challange.rest.consumer.service.CityItineraryService;
import org.adidas.code.challange.rest.dto.CityDTO;
import org.adidas.code.challange.rest.dto.ExceptionResponseDTO;
import org.adidas.code.challange.rest.dto.IntineraryDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CityItineraryController {

	private static Logger logger = LoggerFactory.getLogger(CityItineraryController.class);

	@Autowired
	private CityItineraryService cityItineraryService;

	/**
	 * GET /city/all
	 * 
	 * Example: http://localhost:8082/city/all
	 * 
	 * Get CityDTO info
	 * 
	 * @return List<CityDTO>
	 */
	@RequestMapping(value = "/city/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> cityList() {
		try {
			logger.info("Rest cityList() called");

			List<CityDTO> result = cityItineraryService.cityList();

			if (result != null) {
				return ResponseEntity.ok().body(result);
			} else {
				return ResponseEntity.notFound().build();
			}

		} catch (RestProducerNotFoundException e1) {
			return new ResponseEntity<>(new ExceptionResponseDTO(e1.getMessage()), HttpStatus.SERVICE_UNAVAILABLE);
		} catch (Exception e) {
			return new ResponseEntity<>(new ExceptionResponseDTO(e), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * GET /city/info/{id}
	 * 
	 * Example: http://localhost:8082/city/info/MAD
	 * 
	 * Get CityDTO info
	 * 
	 * @param id
	 * @return CityDTO
	 */
	@RequestMapping(value = "/city/info/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> cityInfo(@PathVariable String id) {
		try {
			logger.info("Rest findItineraryShort() called with id {}", id);

			CityDTO result = cityItineraryService.cityInfo(id);

			if (result != null) {
				return ResponseEntity.ok().body(result);
			} else {
				return ResponseEntity.notFound().build();
			}

		} catch (RestProducerNotFoundException e1) {
			return new ResponseEntity<>(new ExceptionResponseDTO(e1.getMessage()), HttpStatus.SERVICE_UNAVAILABLE);
		} catch (Exception e) {
			return new ResponseEntity<>(new ExceptionResponseDTO(e), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * GET /city/find-itinerary-short
	 * 
	 * Example:
	 * http://localhost:8082/find-itinerary-short?cityOriginId=MAD&cityDestinationId=BER
	 * 
	 * Get IntineraryDTO info
	 * 
	 * @param cityOriginId
	 * @param cityDestinationId
	 * @return IntineraryDTO
	 */
	@RequestMapping(value = "/find-itinerary-short", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> findItineraryShort(
			@RequestParam(value = "cityOriginId", required = true) String cityOriginId,
			@RequestParam(value = "cityDestinationId", required = true) String cityDestinationId) {
		try {
			logger.info("Rest findItineraryShort() called with cityOriginId {} cityDestinationId {}", cityOriginId,
					cityDestinationId);

			IntineraryDTO result = cityItineraryService.getItineraryShort(cityOriginId, cityDestinationId);

			if (result != null) {
				return ResponseEntity.ok().body(result);
			} else {
				return ResponseEntity.notFound().build();
			}

		} catch (RestProducerNotFoundException e1) {
			return new ResponseEntity<>(new ExceptionResponseDTO(e1.getMessage()), HttpStatus.SERVICE_UNAVAILABLE);
		} catch (Exception e) {
			return new ResponseEntity<>(new ExceptionResponseDTO(e), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * GET /city/find-itinerary-less
	 * 
	 * Example:
	 * http://localhost:8082/find-itinerary-less?cityOriginId=MAD&cityDestinationId=BER
	 * 
	 * Get IntineraryDTO info
	 * 
	 * @param cityOriginId
	 * @param cityDestinationId
	 * @return IntineraryDTO
	 */
	@RequestMapping(value = "/find-itinerary-less", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> findItineraryLess(
			@RequestParam(value = "cityOriginId", required = true) String cityOriginId,
			@RequestParam(value = "cityDestinationId", required = true) String cityDestinationId) {
		try {
			logger.info("Rest findItineraryLess() called with cityOriginId {} cityDestinationId {}", cityOriginId,
					cityDestinationId);

			IntineraryDTO result = cityItineraryService.getItineraryLess(cityOriginId, cityDestinationId);

			if (result != null) {
				return ResponseEntity.ok().body(result);
			} else {
				return ResponseEntity.notFound().build();
			}

		} catch (RestProducerNotFoundException e1) {
			return new ResponseEntity<>(new ExceptionResponseDTO(e1.getMessage()), HttpStatus.SERVICE_UNAVAILABLE);
		} catch (Exception e) {
			return new ResponseEntity<>(new ExceptionResponseDTO(e), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
