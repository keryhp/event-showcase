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
%>
<!DOCTYPE html>

<head>
<title>Add Event Photo</title>
<link type="text/css" rel="stylesheet" href="/stylesheets/eventfeed.css" />
<link type="text/css" rel="stylesheet" href="/stylesheets/style.css" />
<link rel="stylesheet" href="/stylesheets/jquery-ui.css">
<script src="/js/jquery-1.9.1.js"></script>
<script src="/js/jquery-ui.js"></script>
<script src="/js/jquery-ui-timepicker-addon.js"></script>
<script type="text/javascript">
	function onFileSelected() {
		filename = document.getElementById("input-file").value;
		if (filename == null || filename == "") {
			document.getElementById("btn-post").setAttribute("class",
					"inactive btn");
			document.getElementById("btn-post").disabled = true;
		} else {
			document.getElementById("btn-post").setAttribute("class",
					"active btn");
			document.getElementById("btn-post").disabled = false;
		}
	}

	function togglePhotoPost(expanded) {
		onFileSelected();
		if (expanded) {
			document.getElementById("btn-choose-image").style.display = "none";
			document.getElementById("upload-form").style.display = "block";
		} else {
			document.getElementById("btn-choose-image").style.display = "inline-block";
			document.getElementById("upload-form").style.display = "none";
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

	<div class="welcome_msg">
		<p>Please Add the Event photo</p>
	</div>
	<div id="add-photo-form" class="addphoto_formdata">
		<div id="upload-form">
			<form action="<%=serviceManager.getUploadUrl()%>" method="post"
				enctype="multipart/form-data">
				<input id="input-file" class="inactive file btn" type="file"
					name="photo" onchange="onFileSelected()">
				<textarea name="title" placeholder="Write a description"></textarea>
				<input hidden="true" name="eventId"
					value="<c:out value="${eventId}"/>"> <input id="btn-post"
					class="active btn" type="submit" value="submit">
			</form>
		</div>

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
