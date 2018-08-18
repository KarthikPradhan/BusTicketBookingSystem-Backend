package com.system.booking.repository;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import com.system.booking.model.BusDAO;
import com.system.booking.model.CustomerDAO;
import com.system.booking.utility.Utility;

/**
 * Created with Spring Tool Suite.
 * Users: Karthik Pradhan
 */
@Repository
public class BusRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<BusDAO> searchBuses(BusDAO bus) {

		String sql = "";
		SqlRowSet rowSet = null;

		try {
			if (Utility.isBusObjectNull(bus) == true) {

				sql = "SELECT b.bus_id, sd.bus_timings, sd.cost_of_travel, fc.city_name as from_city, tc.city_name as to_city FROM bus b "
						+ "INNER JOIN schedule_departure sd ON b.bus_id = sd.bus_id "
						+ "INNER JOIN (select city_name, city_id from city) as fc on sd.from_city_id = fc.city_id "
						+ "INNER JOIN (select city_name, city_id from city) as tc on sd.to_city_id = tc.city_id";
				rowSet = jdbcTemplate.queryForRowSet(sql);

			} else {

				sql = "SELECT b.bus_id, sd.bus_timings, sd.cost_of_travel, fc.city_name as from_city, tc.city_name as to_city FROM bus b "
						+ "INNER JOIN schedule_departure sd ON b.bus_id = sd.bus_id "
						+ "INNER JOIN (select city_name, city_id from city) as fc on sd.from_city_id = fc.city_id "
						+ "INNER JOIN (select city_name, city_id from city) as tc on sd.to_city_id = tc.city_id "
						+ "WHERE sd.from_city_id = (SELECT city_id FROM city WHERE city_name = ?) and sd.to_city_id = (SELECT city_id FROM city WHERE city_name = ?) "
						+ "and sd.bus_timings like concat (?, '%')";

				rowSet = jdbcTemplate.queryForRowSet(sql,
						new Object[] { bus.getFromCity(), bus.getToCity(), Utility.trimTime(bus.getBusTimings()) });
			}

			List<BusDAO> lstBuses = new ArrayList<>();
			BusDAO busObj = null;

			while (rowSet.next()) {
				busObj = new BusDAO();
				busObj.setbusId(rowSet.getInt("bus_id"));
				busObj.setFromCity(rowSet.getString("from_city"));
				busObj.setToCity(rowSet.getString("to_city"));
				busObj.setBusTimings(rowSet.getString("bus_timings"));
				busObj.setCostOfTravel(rowSet.getInt("cost_of_travel"));
				lstBuses.add(busObj);
			}
			return lstBuses;
		} catch (Exception e) {
			return new ArrayList<BusDAO>();
		}
	}

	public int generateTicket(BusDAO bus, CustomerDAO customer) {

		int customerId = 0;
		int scheduleDepartureId = 0;
		int ticketNum = 0;
		String timeStamp = new SimpleDateFormat("yyyy.MM.dd HH.mm.ss").format(new Date());

		if (jdbcTemplate.update(
				"INSERT INTO customer(first_name, last_name, phone_number, email, status, created_on, changed_on) VALUES(?, ?, ?, ?, ?, ?, ?)",
				customer.getFirstName(), customer.getLastName(), customer.getPhoneNumber(), customer.getEmail(), 1,
				timeStamp, timeStamp) > 0) {

			String sql = "SELECT customer_id, schedule_departure_id FROM customer, schedule_departure "
					+ "WHERE first_name = ? and last_name = ? and  schedule_departure_id = (SELECT schedule_departure_id "
					+ "FROM schedule_departure WHERE bus_id = ? and bus_timings = ?)";

			SqlRowSet rowSetOfIds = jdbcTemplate.queryForRowSet(sql, customer.getFirstName(), customer.getLastName(),
					bus.getBusId(), bus.getBusTimings());

			while (rowSetOfIds.next()) {
				customerId = rowSetOfIds.getInt("customer_id");
				scheduleDepartureId = rowSetOfIds.getInt("schedule_departure_id");
			}

			if (jdbcTemplate.update(
					"INSERT INTO customer_schedule_departure_mapping(customer_id, schedule_departure_id, status, created_on, changed_on) VALUES(?, ?, ?, ?, ?)",
					customerId, scheduleDepartureId, 1, timeStamp, timeStamp) > 0) {

				String queryForTicket = "SELECT customer_schedule_departure_mapping_id as ticked_id FROM customer_schedule_departure_mapping WHERE customer_id = ? and schedule_departure_id = ?";

				SqlRowSet rowSetTicketId = jdbcTemplate.queryForRowSet(queryForTicket, customerId, scheduleDepartureId);

				while (rowSetTicketId.next()) {
					ticketNum = rowSetTicketId.getInt("ticked_id");
				}
			}

			return ticketNum;
		} else {
			return 0;
		}
	}

	public Map<String, String> scheduleDeparture(BusDAO bus) {

		Map<String, String> message = new HashMap<>();
		String timeStamp = new SimpleDateFormat("yyyy.MM.dd HH.mm.ss").format(new Date());

		List<Integer> cityIds = getCityIds(bus.getFromCity(), bus.getToCity());

		if (jdbcTemplate.update(
				"INSERT INTO schedule_departure(bus_id, from_city_id, to_city_id, bus_timings, cost_of_travel, status, created_on, changed_on) VALUES(?, ?, ?, ?, ?, ?, ?, ?)",
				bus.getBusId(), cityIds.get(0), cityIds.get(1), bus.getBusTimings(), bus.getCostOfTravel(), 1,
				timeStamp, timeStamp) > 0) {

			message.put("message", "Bus number " + bus.getBusId() + " has been added successfully");
			return message;
		} else {
			message.put("message", "Unable to add bus with bus number " + bus.getBusId());
			return message;
		}
	}

	public Map<String, String> editScheduleDeparture(BusDAO busObj, BusDAO oldBusObj) {

		Map<String, String> message = new HashMap<>();
		String timeStamp = new SimpleDateFormat("yyyy.MM.dd HH.mm.ss").format(new Date());

		List<Integer> cityIds = getCityIds(busObj.getFromCity(), busObj.getToCity());
		try {
			if (jdbcTemplate.update("UPDATE schedule_departure "
					+ "set bus_id = ?, from_city_id = ?, to_city_id = ?, bus_timings = ?, cost_of_travel = ?, changed_on = ?"
					+ "WHERE bus_id = ? and bus_timings = ?", busObj.getBusId(), cityIds.get(0), cityIds.get(1),
					busObj.getBusTimings(), busObj.getCostOfTravel(), timeStamp, oldBusObj.getBusId(),
					oldBusObj.getBusTimings()) > 0) {

				message.put("message",
						"Changes to the bus with the number " + busObj.getBusId() + " has been made successfully");
				return message;
			} else {
				message.put("message", "Unable to make changes to the bus with bus number " + busObj.getBusId());
				return message;
			}
		} catch (Exception e) {
			message.put("message", "Unable to make changes to the bus with bus number " + busObj.getBusId());
			return message;
		}
	}

	private List<Integer> getCityIds(String fromCity, String toCity) {

		List<Integer> cityIds = new ArrayList<Integer>();
		String queryForCityIds = "SELECT from_city_id, to_city_id FROM (SELECT city_id as from_city_id FROM city WHERE city_name = ?) as fc,  (SELECT city_id as to_city_id FROM city WHERe city_name = ?) as tc";

		SqlRowSet cityIdsRowSet = jdbcTemplate.queryForRowSet(queryForCityIds, fromCity, toCity);

		while (cityIdsRowSet.next()) {
			cityIds.add(cityIdsRowSet.getInt("from_city_id"));
			cityIds.add(cityIdsRowSet.getInt("to_city_id"));
		}

		return cityIds;
	}

	public Map<String, String> deleteScheduleDeparture(int busId) {
		Map<String, String> message = new HashMap<>();

		if (jdbcTemplate.update("DELETE FROM schedule_departure " + "WHERE bus_id = ?", busId) > 0) {

			message.put("message", "The bus with the number " + busId + " has been deleted");
			return message;
		} else {
			message.put("message", "Unable to delete the bus with bus number " + busId);
			return message;
		}
	}
}