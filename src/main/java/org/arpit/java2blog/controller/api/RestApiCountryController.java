package org.arpit.java2blog.controller.api;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import org.arpit.java2blog.model.Country;
import org.arpit.java2blog.service.CountryService;
import org.arpit.java2blog.util.CustomErrorType;


@RestController
@RequestMapping("/api")
public class RestApiCountryController {

	public static final Logger logger = LoggerFactory.getLogger(RestApiCountryController.class);

	@Autowired
	CountryService countryService; //Service which will do all data retrieval/manipulation work

	// -------------------Retrieve All Countries---------------------------------------------

	@RequestMapping(value = "/country", method = RequestMethod.GET)
	public ResponseEntity<List<Country>> getAllCountries() {
		List<Country> countrys = (List<Country>) countryService.getAllCountries();
		if (countrys.isEmpty()) {
			return new ResponseEntity(HttpStatus.NO_CONTENT);
			// You many decide to return HttpStatus.NOT_FOUND
		}
		return new ResponseEntity<List<Country>>(countrys, HttpStatus.OK);
	}

	
	// -------------------Retrieve Single Country------------------------------------------

	@RequestMapping(value = "/country/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getCountry(@PathVariable("id") long id) {
		logger.info("Fetching Country with id {}", id);
		Country country  = countryService.getCountry(new Integer((int) id));
		if (country == null) {
			logger.error("Country with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("Country with id " + id 
					+ " not found"), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Country>(country, HttpStatus.OK);
	}


	// -------------------Create a Country-------------------------------------------

	@RequestMapping(value = "/country/", method = RequestMethod.POST)
	public ResponseEntity<?> createCountry(@RequestBody Country country, UriComponentsBuilder ucBuilder) {
		logger.info("Creating Country : {}", country);

		if (country==null) {
			logger.error("Unable to create. A Country with name {} already exist", country.getCountryName());
			return new ResponseEntity(new CustomErrorType("Unable to create. A Country with name " + 
			country.getCountryName() + " already exist."),HttpStatus.CONFLICT);
		}
		countryService.addCountry(country);

		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/api/country/{id}").buildAndExpand(country.getId()).toUri());
		return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	}

	// ------------------- Update a Country ------------------------------------------------

	@RequestMapping(value = "/country/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateCountry(@PathVariable("id") long id, @RequestBody Country country) {
		logger.info("Updating Country with id {}", id);

		Country currentCountry = countryService.getCountry(new Integer((int) id));

		if (currentCountry == null) {
			logger.error("Unable to update. Country with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("Unable to upate. Country with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}

		currentCountry.setCountryName(country.getCountryName());
		currentCountry.setId(country.getId());


		countryService.addCountry(currentCountry);
		return new ResponseEntity<Country>(currentCountry, HttpStatus.OK);
	}

	// ------------------- Delete a Country-----------------------------------------

	@RequestMapping(value = "/country/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteCountry(@PathVariable("id") long id) {
		logger.info("Fetching & Deleting Country with id {}", id);

		Country country = countryService.getCountry(new Integer((int) id));
		if (country == null) {
			logger.error("Unable to delete. Country with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("Unable to delete. Country with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}
		countryService.deleteCountry(new Integer((int) id));
		return new ResponseEntity<Country>(HttpStatus.NO_CONTENT);
	}


	



}