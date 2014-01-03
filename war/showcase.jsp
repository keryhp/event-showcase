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
<title>Event Showcase</title>
<link type="text/css" rel="stylesheet" href="/stylesheets/eventfeed.css" />
<link type="text/css" rel="stylesheet" href="/stylesheets/style.css" />
<link rel="stylesheet" href="/stylesheets/jquery-ui.css">
<script src="/js/jquery-1.9.1.js"></script>
<script src="/js/jquery-ui.js"></script>
<script src="/js/jquery-ui-timepicker-addon.js"></script>
<script type="text/javascript">

	$(function() {
		var datToday = new Date();
		$('#eventTimeFrom').datepicker({ dateFormat: "yy-mm-dd", minDate: datToday });
		$('#eventTimeTo').datepicker({ dateFormat: "yy-mm-dd", minDate: datToday });
	});
	
	function showEvent(eventId){
		window.location.href = "/viewevent?eventId=" + eventId;
	}
</script>

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
		<p>Welcome to EventShowcase!</p>
	</div>
	<!-- Search form -->
	<div id="search-event-form" class="searchbar">
		<form style="margin-left: 5%;"
			action="<%=configManager.getSearchEventUrl()%>" method="get">
			<label>Search Events:</label> <input id="eventTitle"
				name="eventTitle" placeholder="Search by event title" type="text"
				maxlength="255"> <input id="location" name="location"
				placeholder="Search by event location" type="text" maxlength="255">
			<input type="text" id="eventTimeFrom" name="eventTimeFrom"
				placeholder="From date" readonly="readonly"> <input
				type="text" id="eventTimeTo" name="eventTimeTo"
				placeholder="To date" readonly="readonly"> <input
				id="btn-post" class="active btn" type="submit" value="Search" />
		</form>
	</div>

	<div>
		<%
			Iterable<Event> eventIter = eventManager.getActiveEvents();
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
		<div class="home_img_div">
			<div style="margin-left: 1%;">
				<h3>
					<label>Title: </label>
					<c:out value="<%=event.getTitle()%>" escapeXml="true" />
				</h3>
				<h3>
					<label>Location: </label>
					<c:out value="<%=event.getLocation()%>" escapeXml="true" />
				</h3>
				<%
					if (event.getEventTime() != 0) {
				%>

				<h3>
					<label>Date: </label>
					<c:out
						value="<%=ServletUtils.formatTimestamp(event
								.getEventTime())%>"
						escapeXml="true" />
				</h3>
			</div>
			<%
				Long eventId = event.getId();
							Iterable<Photo> photoIter = photoManager
									.getPhoto(eventId);
							try {
								for (Photo photo : photoIter) {
			%>
			<div style="margin: 2%;">
				<img class="home_img"
					src="<%=serviceManager
										.getImageDownloadUrl(photo)%>"
					alt="Photo Image" onclick="showEvent(<%=eventId%>)" />
			</div>
			<%
				}
							} catch (DatastoreNeedIndexException e) {
								pageContext
										.forward(configManager
												.getErrorPageUrl(ConfigManager.ERROR_CODE_DATASTORE_INDEX_NOT_READY));
							}

						}
			%>

		</div>
		<%
			}
		%>
	</div>

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
