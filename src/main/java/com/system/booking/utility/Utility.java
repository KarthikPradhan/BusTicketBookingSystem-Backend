package com.system.booking.utility;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.system.booking.model.BusDAO;
import com.system.booking.model.CustomerDAO;

/**
 * Created with Spring Tool Suite.
 * Users: Karthik Pradhan
 */
public class Utility {

	public static String trimTime(String dateTime) {
		return dateTime.toString().split(" ")[0];
	}

	public static ByteArrayInputStream generatePdf(BusDAO bus, CustomerDAO customer, int ticketId) {
		
		Document document = new Document(new Rectangle(350f, 300f), 36f, 72f, 108f, 108f);
		document.setMargins(2, 2, 2, 2);
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		try {
			Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
			LineSeparator addLine = new LineSeparator();
			
			Paragraph companyName = new Paragraph("Bus Ticket Booking System", headFont);
			companyName.setAlignment(Element.ALIGN_CENTER);
			
			Paragraph sourceDestination = new Paragraph("From " + bus.getFromCity() + " to " + bus.getToCity(), headFont);
			sourceDestination.setAlignment(Element.ALIGN_CENTER);
			
			Paragraph busNumber = new Paragraph("Bus Number: " + bus.getBusId());
			
			Paragraph passengerName = new Paragraph("Passenger Name: " + customer.getFirstName() + " " + customer.getLastName(), headFont);
			
			Paragraph ticketNumber = new Paragraph("Ticket Number: " + ticketId);
			Paragraph departure = new Paragraph("Departure: " + bus.getBusTimings());
			Paragraph cost = new Paragraph("Travel Cost: € " + bus.getCostOfTravel());
			
			
			Paragraph footer = new Paragraph("Happy Journey!", headFont);
			footer.setAlignment(Element.ALIGN_CENTER);

			PdfWriter.getInstance(document, out);
			document.open();
			document.add(companyName);
			document.add(new Chunk(addLine));
			document.add(sourceDestination);
			document.add(Chunk.NEWLINE);
			document.add(Chunk.NEWLINE);
			document.add(ticketNumber);
			document.add(busNumber);
			document.add(passengerName);
			document.add(departure);
			document.add(cost);
			document.add(Chunk.NEWLINE);
			document.add(Chunk.NEWLINE);
			document.add(new Chunk(addLine));
			document.add(footer);
			document.close();

		} catch (Exception e) {

		}

		return new ByteArrayInputStream(out.toByteArray());
	}
	
	public static boolean isBusObjectNull(BusDAO busObj) {
		
		if (busObj.getFromCity() != null && busObj.getToCity() != null && busObj.getBusTimings() != null) {
			return false;
		} else {
			return true;
		}
	}
}
