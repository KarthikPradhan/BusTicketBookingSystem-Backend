package com.system.booking.model;

public class CombinedObject {
	private BusDAO bus;
	private CustomerDAO customer;

	public CombinedObject() {

	}

	public CombinedObject(BusDAO bus, CustomerDAO customer) {
		super();
		this.bus = bus;
		this.customer = customer;
	}

	public BusDAO getBus() {
		return bus;
	}

	public void setBus(BusDAO bus) {
		this.bus = bus;
	}

	public CustomerDAO getCustomer() {
		return customer;
	}

	public void setCustomer(CustomerDAO customer) {
		this.customer = customer;
	}
}
