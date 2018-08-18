package com.system.booking.controller;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.system.booking.model.BusDAO;
import com.system.booking.model.CustomerDAO;
import com.system.booking.repository.BusRepository;
import com.system.booking.model.CombinedObject;
import com.system.booking.utility.Utility;

/**
 * Created with Spring Tool Suite.
 * Users: Karthik Pradhan
 */
@CrossOrigin
@RestController
public class BusController {

	@Autowired
	private BusRepository BusControllerRepo;

	@RequestMapping(value = "/api/searchBuses", method = RequestMethod.POST)
	public @ResponseBody List<BusDAO> searchBuses(@RequestBody BusDAO bus) {
		return BusControllerRepo.searchBuses(bus);
	}

	@RequestMapping(value = "/api/generateTicket", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<InputStreamResource> generateTicket(@RequestBody CombinedObject objCombined) {
		
		BusDAO bus = objCombined.getBus();
		CustomerDAO customer = objCombined.getCustomer();

		int ticketId = BusControllerRepo.generateTicket(bus, customer);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "inline; filename=" + customer.getLastName() + "_ticket.pdf");

		ByteArrayInputStream ticketPdf = null;

		if (ticketId > 0) {

			ticketPdf = Utility.generatePdf(bus, customer, ticketId);

			return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF)
					.body(new InputStreamResource(ticketPdf));
		} else {
			return null;
		}
	}

	@RequestMapping(value = "/api/scheduleDeparture", method = RequestMethod.POST)
	public @ResponseBody Map<String, String> scheduleDeparture(@RequestBody BusDAO bus) {
		
		Map<String, String> statusMsg = BusControllerRepo.scheduleDeparture(bus);
		return statusMsg;
	}
	
	@RequestMapping(value = "/api/editScheduleDeparture", method = RequestMethod.POST)
	public @ResponseBody Map<String, String> editScheduleDeparture(@RequestBody Map<String, BusDAO> mapBusObj) {
		
		BusDAO busObj = mapBusObj.get("busObj");
		BusDAO oldBusObj = mapBusObj.get("oldBusObj");
		Map<String, String> statusMsg = BusControllerRepo.editScheduleDeparture(busObj, oldBusObj);
		return statusMsg;
	}
	
	@RequestMapping(value = "/api/deleteScheduleDeparture/{busId}", method = RequestMethod.DELETE)
	public @ResponseBody Map<String, String> deleteScheduleDeparture(@PathVariable int busId) {
		
		Map<String, String> statusMsg = BusControllerRepo.deleteScheduleDeparture(busId);
		return statusMsg;
	}
}
