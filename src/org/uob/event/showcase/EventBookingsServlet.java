package org.uob.event.showcase;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.uob.event.showcase.model.Event;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

@SuppressWarnings("serial")
public class EventBookingsServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		resp.setContentType("text/plain");

		UserService userService = UserServiceFactory.getUserService();       
		User user = userService.getCurrentUser();   
		req.setAttribute("user", user);    
		String eventId = req.getParameter("eventId");
		req.setAttribute("eventId", eventId);
		Event event = null;
		if(user != null){
		    if (eventId != null) {
		    	event = AppContext.getAppContext().getEventManager().getEvent(ServletUtils.validateEventId(eventId));
		    } else {
		      resp.sendError(400, "One or more parameters are not set");
		    }
		}
		req.setAttribute("eventdata", event);    
		RequestDispatcher jsp = req.getRequestDispatcher("/eventbooking.jsp");   
		jsp.forward(req, resp);
	}
}
