package com.system.booking.model;

public class BusDAO {

	private int busId;
	private String busTimings;
	private String fromCity;
	private String toCity;
	private int costOfTravel;

	public BusDAO() {

	}
	
	public BusDAO(int busId, String busTimings, String fromCity, String toCity,
			int costOfTravel) {
		super();
		this.busId = busId;
		this.busTimings = busTimings;
		this.fromCity = fromCity;
		this.toCity = toCity;
		this.costOfTravel = costOfTravel;
	}

	public int getBusId() {
		return busId;
	}

	public void setbusId(int busId) {
		this.busId = busId;
	}

	public String getBusTimings() {
		return busTimings;
	}

	public void setBusTimings(String busTimings) {
		this.busTimings = busTimings;
	}

	public String getFromCity() {
		return fromCity;
	}

	public void setFromCity(String fromCity) {
		this.fromCity = fromCity;
	}

	public String getToCity() {
		return toCity;
	}

	public void setToCity(String toCity) {
		this.toCity = toCity;
	}

	public int getCostOfTravel() {
		return costOfTravel;
	}

	public void setCostOfTravel(int costOfTravel) {
		this.costOfTravel = costOfTravel;
	}

}
