package org.uob.event.showcase;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.uob.event.showcase.model.Comment;
import org.uob.event.showcase.model.CommentManager;
import org.uob.event.showcase.model.ESUser;
import org.uob.event.showcase.model.Event;
import org.uob.event.showcase.model.EventManager;

/**
 * Servlet to handle comment post.
 *
 */
public class CommentPostServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
		AppContext appContext = AppContext.getAppContext();
		ESUser currentUser = appContext.getCurrentUser();
		String id = req.getParameter(ServletUtils.REQUEST_PARAM_NAME_EVENT_ID);
		String content = req.getParameter(ServletUtils.REQUEST_PARAM_NAME_COMMENT);
		boolean succeeded = false;
		Long eventId = ServletUtils.validateEventId(id);
		StringBuilder builder = new StringBuilder();
		if (eventId != null && currentUser != null && content != null) {
			content = content.trim();
			if (!content.isEmpty()) {
				EventManager eventManager = appContext.getEventManager();
				Event event = eventManager.getEvent(eventId);
				if (event != null) {
					CommentManager commentManager = appContext.getCommentManager();
					Comment comment = commentManager.newComment(currentUser.getUserId());
					comment.setEventId(eventId);
					comment.setEventOwnerId(currentUser.getName());
					comment.setTimestamp(System.currentTimeMillis());
					comment.setContent(content);
					comment.setCommentOwnerName(currentUser.getName());
					commentManager.upsertEntity(comment);
					succeeded = true;
				} else {
					builder.append("Request cannot be handled.");
				}
			} else {
				builder.append("Comment could not be empty");
			}
		} else {
			builder.append("Bad parameters");
		}
		if (succeeded) {
			res.sendRedirect(appContext.getPhotoServiceManager().getRedirectUrl(appContext.getConfigManager().getViewEventUrl(), currentUser.getUserId(), id));
		} else {
			res.sendError(400, builder.toString());
		}
	}
}
