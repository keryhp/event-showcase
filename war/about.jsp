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
<title>About</title>
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
		<p>About this project:</p>
	</div>
	<p></p>
	<div class="welcome_msg">
		<ul>
			<li><p>This project was carried out as part of University of
					Bristol "Cloud Computing" unit.</p></li>
			<li><p>This is a Google App Engine application built using
					Java technologies.</p></li>
			<li><p>A user can view events, add new events, search
					events.</p></li>
			<li><p>The event details include event title, location,
					description and a photo.</p></li>
			<li><p>The Google Datastore is used to store the event text
					data</p></li>
			<li><p>The Google Blobstore is used to store the event
					photos</p></li>
			<li><p>The Google Search API is used to search the events
					based on documents and indexes.</p></li>
			<li><p>The Google Memcache API is used to cache the
					entities.</p></li>
			<li><p>The Java Mail API is used for sending emails.</p></li>
			<li><p>The Google Cron scheduler API is used to run cron job
					(send email of the bookings for an event to the organizer).</p></li>
			<li><p>The images used are free royalty images downloaded
					from http://www.freemediagoo.com/.</p></li>

			<li><p>Thank you.</p></li>
		</ul>
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
