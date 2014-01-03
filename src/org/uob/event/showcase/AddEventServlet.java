package org.uob.event.showcase;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.uob.event.showcase.model.ESUser;
import org.uob.event.showcase.model.ESUserManager;
import org.uob.event.showcase.model.Event;
import org.uob.event.showcase.model.EventManager;

import com.google.appengine.api.search.Document;
import com.google.appengine.api.search.Field;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

@SuppressWarnings("serial")
public class AddEventServlet extends HttpServlet {

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
		RequestDispatcher jsp = req.getRequestDispatcher("/addevent.jsp");   
		jsp.forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
		AppContext appContext = AppContext.getAppContext();
		ESUser currentUser = appContext.getCurrentUser();

		String eventTitle = req.getParameter(ServletUtils.REQUEST_PARAM_NAME_EVENT_TITLE);
		String location = req.getParameter(ServletUtils.REQUEST_PARAM_NAME_EVENT_LOCATION);
		String description = req.getParameter(ServletUtils.REQUEST_PARAM_NAME_EVENT_DESCRIPTION);
		Long eventTime = new Timestamp((new Date(req.getParameter(ServletUtils.REQUEST_PARAM_NAME_EVENT_TIME))).getTime()).getTime();
		boolean succeeded = false;
		StringBuilder builder = new StringBuilder();
		String id = null;
		if (currentUser != null && eventTitle != null) {
			eventTitle = eventTitle.trim();
			if (!eventTitle.isEmpty()) {
				EventManager eventManager = appContext.getEventManager();
				Event event = eventManager.newEvent(currentUser.getUserId());
				event.setActive(true);
				event.setTitle(eventTitle);
				event.setDescription(description);
				event.setLocation(location);
				event.setOwnerName(ServletUtils.getProtectedUserName(currentUser.getName()));
				event.setOwnerId(currentUser.getUserId());
				event.setUploadTime(System.currentTimeMillis());
				event.setEventTime(eventTime);
				eventManager.upsertEntity(event);
				id = event.getId().toString();
				if(currentUser.getRole() != null && currentUser.getRole().equals(ServletUtils.REQUEST_PARAM_NAME_USER_NORMAL)){
					ESUserManager userManager = appContext.getESUserManager();
					ESUser user = userManager.getUser(currentUser.getUserId());
					user.setRole(ServletUtils.REQUEST_PARAM_NAME_USER_ORGANIZER);
					userManager.upsertEntity(user);
				}
				succeeded = true;
				
				Document doc = Document.newBuilder()
						.addField(Field.newBuilder().setName(ServletUtils.REQUEST_PARAM_NAME_EVENT_ID).setText(id))
				    .addField(Field.newBuilder().setName(ServletUtils.REQUEST_PARAM_NAME_EVENT_TITLE).setText(event.getTitle()))
				    .addField(Field.newBuilder().setName(ServletUtils.REQUEST_PARAM_NAME_EVENT_LOCATION).setText(event.getLocation()))
				    .addField(Field.newBuilder().setName(ServletUtils.REQUEST_PARAM_NAME_EVENT_DESCRIPTION)
				        .setText(event.getDescription()))
				    .addField(Field.newBuilder().setName(ServletUtils.REQUEST_PARAM_NAME_EVENT_TIME).setDate(new Date(event.getEventTime())))
				    .addField(Field.newBuilder().setName(ServletUtils.REQUEST_PARAM_NAME_EVENT_OWNER_ID).setText(event.getOwnerId()))
				    .addField(Field.newBuilder().setName(ServletUtils.REQUEST_PARAM_NAME_ACTIVE).setText(Boolean.toString(event.isActive())))
				    .build();
				
				IndexDocuments.indexADocument(ServletUtils.REQUEST_PARAM_NAME_EVENT_DOC, doc);
				
			} else {
				builder.append("Event Title could not be empty");
			}
		} else {
			builder.append("Bad parameters");
		}
		if (succeeded) {
			res.sendRedirect(appContext.getPhotoServiceManager().getRedirectUrl(
					appContext.getConfigManager().getAddImageUrl(), currentUser.getUserId(), id));
		} else {
			res.sendError(400, builder.toString());
		}
	}
}
