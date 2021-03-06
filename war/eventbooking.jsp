<%@ page contentType="text/html;charset=UTF-8" language="java"
	isELIgnored="false"%>

<%@ page import="java.util.ArrayList"%>
<%@ page import="org.uob.event.showcase.*"%>
<%@ page import="org.uob.event.showcase.model.*"%>
<%@ page import="com.google.appengine.api.users.*"%>
<%@ page
	import="com.google.appengine.api.datastore.DatastoreNeedIndexException"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<%
	UserService userService = UserServiceFactory.getUserService();
	AppContext appContext = AppContext.getAppContext();
	ConfigManager configManager = appContext.getConfigManager();
	PhotoServiceManager serviceManager = appContext
			.getPhotoServiceManager();
	PhotoManager photoManager = appContext.getPhotoManager();
	EventManager eventManager = appContext.getEventManager();
	BookingManager bookingManager = appContext.getBookingManager();
	ESUser currentUser = appContext.getCurrentUser();
%>
<!DOCTYPE html>

<head>
<title>Event bookings</title>
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
	<%
		String eventId = (String) request.getParameter("eventId");

			if (eventId != null && eventId != "") {
				AppContext.getAppContext().getEmailServiceManager().sendEmail(currentUser);
				Event event = (Event) eventManager.getEvent(ServletUtils
						.validateEventId(eventId));
				pageContext.setAttribute("eventdata", event);
	%>
	<div class="welcome_msg">
		<p>All bookings for the event:</p>
		<div style="float: left; display: inline-block; margin-right: 2%;">${eventdata.title}</div>
		<div style="float: left; display: inline-block; margin-right: 2%;">${eventdata.location}</div>
		<div style="float: left; display: inline-block; margin-right: 2%;"><%=ServletUtils.formatTimestamp(event.getEventTime())%></div>
		<br></br>
	</div>
	<table class="gridtable">
		<tr>
			<th>Booking Id</th>
			<th>Client Name</th>
			<th>Booking Date</th>
		</tr>
		<%
			Iterable<Booking> bookingIter = bookingManager
							.getBookings(event);
					ArrayList<Booking> bookings = new ArrayList<Booking>();
					try {
						for (Booking booking : bookingIter) {
							bookings.add(booking);
						}
					} catch (DatastoreNeedIndexException e) {
						pageContext
								.forward(configManager
										.getErrorPageUrl(ConfigManager.ERROR_CODE_DATASTORE_INDEX_NOT_READY));
					}
					int count = 0;
					for (Booking booking : bookings) {
		%>

		<tr>
			<td><c:out value="<%=booking.getId()%>" escapeXml="true" /></td>
			<td><c:out value="<%=booking.getBookingOwnerName()%>"
					escapeXml="true" /></td>
			<td><c:out
					value="<%=ServletUtils.formatTimestamp(booking
								.getTimestamp())%>"
					escapeXml="true" /></td>
		</tr>
		<%
			}
		%>
	</table>
	<%
		}
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
