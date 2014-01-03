<%@ page contentType="text/html;charset=UTF-8" language="java"
	isELIgnored="false"%>

<%@ page import="java.util.ArrayList"%>
<%@ page import="org.uob.event.showcase.*"%>
<%@ page import="org.uob.event.showcase.model.*"%>
<%@ page import="com.google.appengine.api.users.*"%>
<%@ page
	import="com.google.appengine.api.datastore.DatastoreNeedIndexException"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<%
	UserService userService = UserServiceFactory.getUserService();
	AppContext appContext = AppContext.getAppContext();
	ConfigManager configManager = appContext.getConfigManager();
	PhotoServiceManager serviceManager = appContext
	.getPhotoServiceManager();
	PhotoManager photoManager = appContext.getPhotoManager();
	EventManager eventManager = appContext.getEventManager();
	ESUser currentUser = appContext.getCurrentUser();
%>
<!DOCTYPE html>

<head>
<title>My Events</title>
<link type="text/css" rel="stylesheet" href="/stylesheets/eventfeed.css" />
<link type="text/css" rel="stylesheet" href="/stylesheets/style.css" />
</head>

<body>
	<%
		if (currentUser != null) {
			pageContext.setAttribute("user", currentUser);
	%>

	<!-- Menus -->
	<div style="margin: 2%;">
		<div class="menu">
			<a href='/showcase.jsp'>Home</a>
		</div>
		<div class="menu">
			<a href='/addevent.jsp'>Add Event</a>
		</div>
		<div class="menu">
			<a href='/booking.jsp'>My Bookings</a>
		</div>
		<div class="menu">
			<a href='/myevents.jsp'>My Events</a>
		</div>

		<div class="menu">
			<a href='/about.jsp'>About</a>
		</div>
		<div class="menu">
			<a href='/contact.jsp'>Contact</a>
		</div>
		<div class="menu_last">
			<p>
				Hello, ${fn:escapeXml(user.name)}! (You can <a
					href="<%=userService.createLogoutURL(request.getRequestURI())%>">sign
					out</a>.)
			</p>
		</div>
	</div>



	<div class="welcome_msg">
		<p>Your event bookings!</p>
	</div>
	<table class="gridtable">
		<tr>
			<th>Event title</th>
			<th>Location</th>
			<th>Date and Time</th>
			<th>Booking details</th>
		</tr>
		<%
			Iterable<Event> eventIter = eventManager.getOwnedEvents(currentUser.getUserId());
			ArrayList<Event> events = new ArrayList<Event>();
			try {
				for (Event event : eventIter) {
					events.add(event);
				}
			} catch (DatastoreNeedIndexException e) {
				pageContext
						.forward(configManager
								.getErrorPageUrl(ConfigManager.ERROR_CODE_DATASTORE_INDEX_NOT_READY));
			}
			int count = 0;
			for (Event event : events) {
			%>

		<tr>
			<td><c:out value="<%=event.getTitle()%>" escapeXml="true" /></td>
			<td><c:out value="<%=event.getLocation()%>" escapeXml="true" /></td>
			<%
				if (event.getEventTime() != 0) {
			%>

			<td><c:out
					value="<%=ServletUtils.formatTimestamp(event
								.getEventTime())%>"
					escapeXml="true" /></td>
			<%
				}
			%>
			<td><a href="/eventbooking.jsp?eventId=<%=event.getId()%>">Click
					to View</a></td>
			<%
			}
			%>
		
	</table>

	<%
		} else {
	%>
	<div>
		<p>Hello! Welcome to EventShowcase.</p>
		<p>
			Please <a
				href="<%=userService.createLoginURL(request.getRequestURI())%>">Sign
				in</a> with your Google account to view events.
		</p>
	</div>
	<%
		}
	%>
</body>
</html>
