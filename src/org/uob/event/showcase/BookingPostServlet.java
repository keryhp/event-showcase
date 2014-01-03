package org.uob.event.showcase;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.uob.event.showcase.model.Booking;
import org.uob.event.showcase.model.BookingManager;
import org.uob.event.showcase.model.ESUser;
import org.uob.event.showcase.model.Event;
import org.uob.event.showcase.model.EventManager;

/**
 * Servlet to handle booking post.
 *
 */
public class BookingPostServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
		AppContext appContext = AppContext.getAppContext();
		ESUser currentUser = appContext.getCurrentUser();
		String id = req.getParameter(ServletUtils.REQUEST_PARAM_NAME_EVENT_ID);
		boolean succeeded = false;
		Long eventId = ServletUtils.validateEventId(id);
		StringBuilder builder = new StringBuilder();
		String bookingId = null;
		if (eventId != null && currentUser != null) {
			EventManager eventManager = appContext.getEventManager();
			Event event = eventManager.getEvent(eventId);
			if (event != null) {
				BookingManager bookingManager = appContext.getBookingManager();
				Booking booking = bookingManager.newBooking(currentUser.getUserId());
				booking.setEventId(eventId);
				booking.setEventOwnerId(currentUser.getName());
				booking.setTimestamp(System.currentTimeMillis());
				booking.setBookingOwnerName(currentUser.getName());
				bookingManager.upsertEntity(booking);
				bookingId = booking.getId().toString();
				succeeded = true;
			} else {
				builder.append("Request cannot be handled.");
			}
		} else {
			builder.append("Bad parameters");
		}
		if (succeeded) {
			res.sendRedirect(appContext.getPhotoServiceManager().getBookedRedirectUrl(appContext.getConfigManager().getViewEventUrl(), currentUser.getUserId(), id, bookingId));
		} else {
			res.sendError(400, builder.toString());
		}
	}
}
