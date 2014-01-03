<%@page import="java.util.Date"%>
<%@page import="com.google.appengine.api.search.ScoredDocument"%>
<%@page import="com.google.appengine.api.search.Results"%>
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
	PhotoServiceManager serviceManager = appContext
	.getPhotoServiceManager();
	EventManager eventManager = appContext.getEventManager();
	PhotoManager photoManager = appContext.getPhotoManager();
	CommentManager commentManager = appContext.getCommentManager();
	BookingManager bookingManager = appContext.getBookingManager();
%>
<!DOCTYPE html>

<head>
<title>Search Events</title>
<link type="text/css" rel="stylesheet" href="/stylesheets/eventfeed.css" />
<link type="text/css" rel="stylesheet" href="/stylesheets/style.css" />
<link rel="stylesheet" href="/stylesheets/jquery-ui.css">
<script src="/js/jquery-1.9.1.js"></script>
<script src="/js/jquery-ui.js"></script>
<script src="/js/jquery-ui-timepicker-addon.js"></script>
<script type="text/javascript">
	$(function() {
		$('#eventTime').datepicker();
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

	<!-- List Searched Events -->
	<div class="searchmsg">
		<p>Search Results:</p>
	</div>
	<%
		Results<ScoredDocument> events = (Results<ScoredDocument>) request
					.getAttribute("results");
			if (events != null) {
				int count = 0;
				for (ScoredDocument event : events) {
	%>

	<div class="home_img_div">
		<div class="column" style="margin: -1% 0 -5% 5%; width:90%">
			<h3>
				<c:out
					value="<%=event.getOnlyField(
								ServletUtils.REQUEST_PARAM_NAME_EVENT_TITLE)
								.getText()%>"
					escapeXml="true" />
			</h3></div>
			<div class="column" style="margin-left:5%; width:40%">
			<p>
				<c:out
					value="<%=event.getOnlyField(
								ServletUtils.REQUEST_PARAM_NAME_EVENT_LOCATION)
								.getText()%>"
					escapeXml="true" />
					</div><div class="column" style="width: 40%;
float: right;">
			<p>
				<c:out
					value="<%=event.getOnlyField(
								ServletUtils.REQUEST_PARAM_NAME_EVENT_TIME)
								.getDate()%>"
					escapeXml="true" />

				<%
					String eventId = event.getOnlyField(
										ServletUtils.REQUEST_PARAM_NAME_EVENT_ID)
										.getText();
								Iterable<Photo> photoIter = photoManager
										.getPhoto(ServletUtils.validateEventId(eventId));
								try {
									for (Photo photo : photoIter) {
				%>
		</div>	
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
			%>
		
	</div>
	<%
		}
			}
	%>
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
