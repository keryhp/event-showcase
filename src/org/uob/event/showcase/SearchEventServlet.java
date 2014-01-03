package org.uob.event.showcase;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.uob.event.showcase.model.ESUser;
import org.uob.event.showcase.model.Event;

import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.search.Index;
import com.google.appengine.api.search.IndexSpec;
import com.google.appengine.api.search.Results;
import com.google.appengine.api.search.ScoredDocument;
import com.google.appengine.api.search.SearchException;
import com.google.appengine.api.search.SearchServiceFactory;
import com.google.appengine.api.search.StatusCode;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

@SuppressWarnings("serial")
public class SearchEventServlet extends HttpServlet {

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		AppContext appContext = AppContext.getAppContext();
		ESUser currentUser = appContext.getCurrentUser();

		String eventTitle = req.getParameter(ServletUtils.REQUEST_PARAM_NAME_EVENT_TITLE);
		String eventId = req.getParameter(ServletUtils.REQUEST_PARAM_NAME_EVENT_ID);
		String location = req.getParameter(ServletUtils.REQUEST_PARAM_NAME_EVENT_LOCATION);
		//String description = req.getParameter(ServletUtils.REQUEST_PARAM_NAME_EVENT_DESCRIPTION);
		String eventTimeFrom = req.getParameter(ServletUtils.REQUEST_PARAM_NAME_EVENT_TIME_FROM);
		String eventTimeTo = req.getParameter(ServletUtils.REQUEST_PARAM_NAME_EVENT_TIME_TO);

		boolean succeeded = false;
		StringBuilder builder = new StringBuilder();
		String id = null;
		String queryString = "";

		if(eventTitle == ""){
			eventTitle =null;
		}
		if(location == ""){
			location = null;
		}
		Calendar c = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		if(eventTimeFrom == ""){
			c.add(Calendar.MONTH, -1);
			Date dt = new Date(c.getTimeInMillis());
			eventTimeFrom =df.format(dt);
		}
		if(eventTimeTo == ""){
			c.add(Calendar.MONTH, +2);
			Date dt = new Date(c.getTimeInMillis());
			eventTimeTo =df.format(dt);
		}
		Query query = new Query("Entity");
		Query.Filter eventFilter1 = new Query.FilterPredicate(ServletUtils.REQUEST_PARAM_NAME_EVENT_TITLE, FilterOperator.EQUAL, eventTitle);
		Query.Filter eventFilter2 = new Query.FilterPredicate(ServletUtils.REQUEST_PARAM_NAME_EVENT_LOCATION, FilterOperator.EQUAL, location);
		Query.Filter eventFilter3 = new Query.FilterPredicate(ServletUtils.REQUEST_PARAM_NAME_EVENT_TIME, FilterOperator.GREATER_THAN_OR_EQUAL, eventTimeFrom);
		Query.Filter eventFilter4 = new Query.FilterPredicate(ServletUtils.REQUEST_PARAM_NAME_EVENT_TIME, FilterOperator.LESS_THAN_OR_EQUAL, eventTimeTo);
		List<Filter> filters = Arrays.asList(eventFilter1, eventFilter2);
		Filter filter = new Query.CompositeFilter(CompositeFilterOperator.OR, filters);
		List<Filter> filters2 = Arrays.asList(eventFilter3, eventFilter4);
		Filter filter2 = new Query.CompositeFilter(CompositeFilterOperator.AND, filters2);
		List<Filter> filters3 = Arrays.asList(filter, filter2);
		Filter filter3 = new Query.CompositeFilter(CompositeFilterOperator.AND, filters3);
		query.setFilter(filter3);
		query.addSort(ServletUtils.REQUEST_PARAM_NAME_EVENT_TIME, SortDirection.DESCENDING);	
		queryString = filter3.toString();

		if (queryString != null && !queryString.isEmpty()) {
			try {
				IndexSpec indexSpec = IndexSpec.newBuilder().setName(ServletUtils.REQUEST_PARAM_NAME_EVENT_DOC).build(); 
				Index index = SearchServiceFactory.getSearchService().getIndex(indexSpec);

				Results<ScoredDocument> results = index.search(queryString);

				/*// Iterate over the documents in the results
				for (ScoredDocument document : results) {
					// handle results
					System.out.println("Search results documents");
				}*/
				succeeded =  true;
				req.setAttribute("results", results);
			} catch (SearchException e) {
				if (StatusCode.TRANSIENT_ERROR.equals(e.getOperationResult().getCode())) {
					// retry
				}
				e.printStackTrace();
			}
		} else {
			builder.append("Bad parameters");
		}
		if (!succeeded) {
			resp.sendError(400, builder.toString());
		}

		RequestDispatcher jsp = req.getRequestDispatcher("/searchevent.jsp");   
		jsp.forward(req, resp);
	}
}
