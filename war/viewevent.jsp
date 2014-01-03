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
	PhotoManager photoManager = appContext.getPhotoManager();
	EventManager eventManager = appContext.getEventManager();
	CommentManager commentManager = appContext.getCommentManager();
%>
<!DOCTYPE html>

<head>
<script type="text/javascript">
	function onCommentChanged(id) {
		comment = document.getElementById("comment-input-" + id).value;
		if (comment == null || comment.trim() == "") {
			document.getElementById("comment-submit-" + id).setAttribute(
					"class", "inactive btn");
			document.getElementById("comment-submit-" + id).disabled = true;
		} else {
			document.getElementById("comment-submit-" + id).setAttribute(
					"class", "active btn");
			document.getElementById("comment-submit-" + id).disabled = false;
		}
	}

	function toggleCommentPost(id, expanded) {
		onCommentChanged(id);
		if (expanded) {
			document.getElementById("comment-input-" + id).setAttribute(
					"class", "post-comment expanded");
			document.getElementById("comment-submit-" + id).style.display = "inline-block";
			document.getElementById("comment-cancel-" + id).style.display = "inline-block";
		} else {
			document.getElementById("comment-input-" + id).value = ""
			document.getElementById("comment-input-" + id).setAttribute(
					"class", "post-comment collapsed");
			document.getElementById("comment-submit-" + id).style.display = "none";
			document.getElementById("comment-cancel-" + id).style.display = "none";
		}
	}
</script>
<title>Event Details</title>
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

	<c:choose>
		<c:when test="${eventdata != null}">



			<%
				Event event = (Event) request.getAttribute("eventdata");
							String bookingId = (String) request
									.getAttribute("bookingId");
							if (bookingId != null) {
			%>
			<div class="login_div_msg">

				<h3>Your booking Id is: ${bookingId}</h3>
			</div>

			<%
				}
			%>

			<div class="event_img_div">
				<div class="column">
					<h3 style="margin-left: 1%;">
						<c:out value="${eventdata.title}" escapeXml="true" />
					</h3>
				</div>
				<div class="column">
					<h3 style="margin-left: 1%;">
						<c:out value="${eventdata.location}" escapeXml="true" />
					</h3>
				</div>
				<div class="column">
					<h3 style="margin-left: 1%;">
						<c:out
							value="<%=ServletUtils.formatTimestamp(event
								.getEventTime())%>"
							escapeXml="true" />
					</h3>
				</div>

				<%
					String eventId = (String) request
										.getAttribute("eventId");
								Long ueventId = ServletUtils.validateEventId(eventId);
								Iterable<Photo> photoIter = photoManager
										.getPhoto(ueventId);
								try {
									for (Photo photo : photoIter) {
				%>
				<div style="margin: 2%;">
					<img class="home_img"
						src="<%=serviceManager
										.getImageDownloadUrl(photo)%>"
						alt="Photo Image" onclick="showEvent(<%=eventId%>)" />
				</div>
				<div class="event_desc">
					<p style="margin: 2%;">
						<c:out value="${eventdata.description}" escapeXml="true" />
					<p>
				</div>
				<div id="book-event" style="margin: 2%;">
					<div id="book-event-form">
						<form action="<%=configManager.getBookingPostUrl()%>"
							method="post">
							<input style="margin-bottom: 2%;" type="hidden" name="user"
								value="${user.userId}"><input type="hidden"
								name="eventId" value="<%=event.getId()%>" /> <input
								id="btn-post" class="active btn" type="submit"
								value="Book this Event">
						</form>
					</div>

				</div>
				<h3 style="margin-left: 2%">Comments:</h3>

				<%
					}
								} catch (DatastoreNeedIndexException e) {
									pageContext
											.forward(configManager
													.getErrorPageUrl(ConfigManager.ERROR_CODE_DATASTORE_INDEX_NOT_READY));
								}
				%>
				<%
					int count = 0;
								Iterable<Comment> comments = commentManager
										.getComments(event);
								for (Comment comment : comments) {
				%>
				<div class="post group" style="margin-left: 2%">
					<div class="usr">
						<div class="comment">
							<h3 style="margin-left: 2%"><%=ServletUtils.getProtectedUserName(comment
									.getCommentOwnerName())%></h3>
							<p style="margin-left: 2%">
								<c:out value="<%=comment.getContent()%>" escapeXml="true" />
							<p class="timestamp" style="margin-left: 2%"><%=ServletUtils.formatTimestamp(comment
									.getTimestamp())%></p>
						</div>
						<!-- /.comment -->
					</div>
					<!-- /.usr -->
				</div>
				<!-- /.post -->

				<%
					count++;
								}
				%>

				<div class="post group" style="margin-left: 2%">
					<div class="usr last" style="margin-left: 2%">
						<div class="comment" style="margin-left: 2%">
							<h3 style="margin-left: 2%"><%=ServletUtils.getProtectedUserName(currentUser
								.getName())%></h3>
							<form action="<%=configManager.getCommentPostUrl()%>"
								method="post">
								<input type="hidden" name="user" value="${user.userId}" /> <input
									type="hidden" name="eventId" value="<%=event.getId()%>" />
								<textarea style="margin: 5%;" id="comment-input-<%=count%>"
									class="post-comment collapsed" name="comment"
									placeholder="Post a comment"
									onclick="toggleCommentPost(<%=count%>, true)"
									onchange="onCommentChanged(<%=count%>)"
									onkeyup="onCommentChanged(<%=count%>)"
									onPaste="onCommentChanged(<%=count%>)"></textarea>
								<input style="margin: -2% 5% 0 5%;"
									id="comment-submit-<%=count%>" class="inactive btn"
									style="display: none" type="submit" name="send"
									value="Post comment"> <a id="comment-cancel-<%=count%>"
									class="cancel" style="display: none"
									onclick="toggleCommentPost(<%=count%>, false)">Cancel</a>
							</form>
						</div>
						<!-- /.comment -->
					</div>
					<!-- /.usr -->
				</div>
				<!-- /.post -->

			</div>
		</c:when>
	</c:choose>
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
