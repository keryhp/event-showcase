package org.uob.event.showcase;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Logger;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.uob.event.showcase.model.Booking;
import org.uob.event.showcase.model.BookingManager;
import org.uob.event.showcase.model.ESUser;
import org.uob.event.showcase.model.Event;
import org.uob.event.showcase.model.EventManager;

import com.google.appengine.api.datastore.DatastoreNeedIndexException;
/**
 * The main service class providing some helper method for sending Emails.
 *
 */
public class EmailServiceManager {
	private static final Logger logger =
			Logger.getLogger(EmailServiceManager.class.getCanonicalName());

	private ConfigManager configManager;
	private EventManager eventManager;
	private BookingManager bookingManager;

	public EmailServiceManager(ConfigManager configManager, EventManager eventManager, BookingManager bookingManager) {
		this.configManager = configManager;
		this.eventManager = eventManager;
		this.bookingManager = bookingManager;
	}

	public void sendEmail(ESUser user) throws UnsupportedEncodingException{
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);

		//String msgBody = "Dear "+ user.getName() +",\n please find the list of your events and their bookings.";
		StringBuilder sb = new StringBuilder();
		sb.append("<html><body><p>Dear "+ user.getName() +",</p><br</br><p>please find the list of your events and their bookings.</p><br></br>");
		Iterable<Event> eventIter = eventManager.getOwnedEvents(user.getUserId());
		ArrayList<Event> events = new ArrayList<Event>();
		try {
			for (Event event : eventIter) {
				events.add(event);
			}
		} catch (DatastoreNeedIndexException e) {
			System.out.println("Error sending email 1\n");
		}
		sb.append("<table><th style=\"width:20%;\">Event Title</th><th style=\"width:20%;\">Booking Id</th><th style=\"width:20%;\">Client Name</th><th style=\"width:20%;\">Booking Date</th>");

		for (Event event : events) {

			Iterable<Booking> bookingIter = bookingManager.getBookings(event);
			ArrayList<Booking> bookings = new ArrayList<Booking>();
			try {
				for (Booking booking : bookingIter) {
					bookings.add(booking);
				}
			} catch (DatastoreNeedIndexException e) {
			}
			for (Booking booking : bookings) {
				sb.append("<tr><td style=\"width:18%;\">");
				sb.append(event.getTitle());
				sb.append("</td><td style=\"width:18%;\">");
				sb.append(booking.getId());
				sb.append("</td><td style=\"width:18%;\">");
				sb.append(booking.getBookingOwnerName());
				sb.append("</td><td style=\"width:18%;\">");
				sb.append(ServletUtils.formatTimestamp(booking.getTimestamp()));
				sb.append("</td></tr>");
			}
		}
		sb.append("</table></body></html>");
		
		String appid = System.getProperty("com.google.appengine.application.id");
		String senderAddress = "admin@" + appid + ".appspotmail.com";
		try {
			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(senderAddress, "EventShowcase Admin"));
			msg.addRecipient(Message.RecipientType.TO,
					new InternetAddress(user.getEmail(), user.getName()));
			msg.setSubject("[EventShowcase Daily mail] Your list of events posted and their bookings");
			msg.setContent(sb.toString(), "text/html");
			Transport.send(msg);

		} catch (AddressException e) {
			System.out.println("Error sending email 2\n");
			e.printStackTrace();
		} catch (MessagingException e) {
			// ...
			System.out.println("Error sending email 3\n");
			e.printStackTrace();

		}
	}
}
