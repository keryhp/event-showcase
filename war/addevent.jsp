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
	ESUser currentUser = appContext.getCurrentUser();
%>
<!DOCTYPE html>

<head>
<title>Add New Event</title>
<link type="text/css" rel="stylesheet" href="/stylesheets/eventfeed.css" />
<link type="text/css" rel="stylesheet" href="/stylesheets/style.css" />
<link rel="stylesheet" href="/stylesheets/jquery-ui.css">
<script src="/js/jquery-1.9.1.js"></script>
<script src="/js/jquery-ui.js"></script>
<script src="/js/jquery-ui-timepicker-addon.js"></script>
<script type="text/javascript">
	$(function() {
		var dateToday = new Date();
		$('#eventTime').datetimepicker({minDate: dateToday});
	});

	function checkEventData() {
		var eventTitleVal = document.getElementById("eventTitle").value;
		var locationVal = document.getElementById("location").value;
		var descriptionVal = document.getElementById("description").value;
		var eventTimeVal = document.getElementById("eventTime").value;

		if (eventTitleVal == null || eventTitleVal == "") {
			alert("Title must be filled out");
			return false;
		} else if (locationVal == null || locationVal == "") {
			alert("Location must be filled out");
			return false;
		} else if (descriptionVal == null || descriptionVal == "") {
			alert("Description must be filled out");
			return false;
		} else if (eventTimeVal == null || eventTimeVal == "") {
			alert("Event Date must be filled out");
			return false;
		} else {
			window.document.forms[0].submit();
		}
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

	<div id="add-event-form" class="addevent_formdata">
		<form action="<%=configManager.getAddEventUrl()%>" method="post">
			<fieldset>
				<legend>Please Add the Event details:</legend>
				<p>
					<label class="field">Title: </label><input id="eventTitle"
						name="eventTitle" placeholder="Write the event title" type="text"
						maxlength="255">
				</p>
				<p>
					<label class="field">Location: </label><input id="location"
						name="location" placeholder="Where is the event held?" type="text"
						maxlength="255">
				</p>
				<p>
					<label class="field">Description: </label>
					<textarea id="description" name="description"
						placeholder="Write a description" maxlength="255"></textarea>
				</p>
				<p>
					<label class="field">Date: </label><input type="text"
						id="eventTime" name="eventTime" placeholder="select date"
						readonly="readonly">
				</p>
			</fieldset>

			<div id="btn-post" class="active btn" onclick="checkEventData()">Add
				Event</div>
		</form>
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
