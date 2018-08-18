package com.system.booking.model;

public class CityDAO {

	private String cityName;

	public CityDAO() {

	}
	
	public CityDAO(String cityName) {
		super();
		this.cityName = cityName;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

}
