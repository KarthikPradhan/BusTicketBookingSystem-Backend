package com.system.booking.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.system.booking.model.CityDAO;

/**
 * Created with Spring Tool Suite.
 * Users: Karthik Pradhan
 */
@Repository
public class CityRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<CityDAO> getCityName(String cityName) {
		try {
			String sql = "SELECT city_name FROM city WHERE city_name like concat('%', ?, '%') and status = 1";

			List<CityDAO> lstCities = new ArrayList<>();
			CityDAO cityObj = null;
			for (String city : jdbcTemplate.queryForList(sql, String.class, cityName)) {
				cityObj = new CityDAO();
				cityObj.setCityName(city);
				lstCities.add(cityObj);
			}

			return lstCities;
		} catch (Exception e) {
			return new ArrayList<CityDAO>();
		}
	}
}
