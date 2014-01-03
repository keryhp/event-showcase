package org.uob.event.showcase;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.uob.event.showcase.model.ESUser;

@SuppressWarnings("serial")
public class DailyReportServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		Iterable<ESUser> users = AppContext.getAppContext().getESUserManager().getAllUsers();
		for (ESUser esUser : users) {
			AppContext.getAppContext().getEmailServiceManager().sendEmail(esUser);			
		}
	}
}
