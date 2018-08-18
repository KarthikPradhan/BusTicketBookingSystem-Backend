package com.system.booking.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.system.booking.model.CityDAO;
import com.system.booking.repository.CityRepository;

/**
 * Created with Spring Tool Suite.
 * Users: Karthik Pradhan
 */
@CrossOrigin
@RestController
public class CityController {

	@Autowired
	private CityRepository cityControllerRepo;

	@RequestMapping(value = "/api/getCities/{city_name}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody List<CityDAO> getCityName(@PathVariable("city_name") String city_name) { 
		return cityControllerRepo.getCityName(city_name);
	}
}
