package org.uob.event.showcase;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.uob.event.showcase.model.ESUser;
import org.uob.event.showcase.model.Event;
import org.uob.event.showcase.model.EventManager;
import org.uob.event.showcase.model.Photo;
import org.uob.event.showcase.model.PhotoManager;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

@SuppressWarnings("serial")
public class ViewEventServlet extends HttpServlet {

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		resp.setContentType("text/html");        

		UserService userService = UserServiceFactory.getUserService();       
		User user = userService.getCurrentUser();   
		req.setAttribute("user", user);    
		req.setAttribute("bookingId", req.getParameter("bookingId"));    
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

		RequestDispatcher jsp = req.getRequestDispatcher("/viewevent.jsp");   
		jsp.forward(req, resp);
	}
}
