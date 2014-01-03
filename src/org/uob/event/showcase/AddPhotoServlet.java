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
public class AddPhotoServlet extends HttpServlet {

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		resp.setContentType("text/html");        

		UserService userService = UserServiceFactory.getUserService();       
		User user = userService.getCurrentUser();   
		String loginUrl = userService.createLoginURL("/");    
		String logoutUrl = userService.createLogoutURL("/");
		req.setAttribute("user", user);    
		req.setAttribute("loginUrl", loginUrl);      
		req.setAttribute("logoutUrl", logoutUrl);    
		req.setAttribute("eventId", req.getParameter("eventId"));    
		RequestDispatcher jsp = req.getRequestDispatcher("/addphoto.jsp");   
		jsp.forward(req, resp);
	}
}
