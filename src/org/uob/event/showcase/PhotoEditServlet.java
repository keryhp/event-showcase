package org.uob.event.showcase;


import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.uob.event.showcase.model.Photo;
import org.uob.event.showcase.model.PhotoManager;

/**
 * Servlet to handle photo meta data update and photo delete.
 *
 */
public class PhotoEditServlet extends HttpServlet {
  private static final String REQUEST_PARAM_NAME_DELETE = "delete";
  private static final String REQUEST_PARAM_NAME_SAVE = "save";
  private static final String REQUEST_PARAM_NAME_TITLE = "title";
  private static final long serialVersionUID = 1L;

  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
    AppContext appContext = AppContext.getAppContext();
    PhotoManager photoManager = appContext.getPhotoManager();
    String user = req.getParameter(ServletUtils.REQUEST_PARAM_NAME_EVENT_OWNER_ID);
    Long eventId = Long.parseLong(req.getParameter(ServletUtils.REQUEST_PARAM_NAME_EVENT_ID));
    String id = req.getParameter(ServletUtils.REQUEST_PARAM_NAME_PHOTO_ID);
    Long photoId = ServletUtils.validatePhotoId(id);
    String save = req.getParameter(REQUEST_PARAM_NAME_SAVE);
    String delete = req.getParameter(REQUEST_PARAM_NAME_DELETE);
    boolean succeeded = false;
    if (photoId != null && eventId != null) {
      Photo photo = photoManager.getPhoto(eventId, photoId);
      if (photo != null) {
        if (save != null) {
          String title = req.getParameter(REQUEST_PARAM_NAME_TITLE);
          photo.setTitle(title);
          photoManager.upsertEntity(photo);
        } else if (delete != null) {
          photoManager.deactivePhoto(photo.getEventId(), photo.getId());
        }
        succeeded = true;
      }
    }
    if (succeeded) {
      if (delete != null) {
        res.sendRedirect(appContext.getPhotoServiceManager().getRedirectUrl(
            req.getParameter(ServletUtils.REQUEST_PARAM_NAME_TARGET_URL), null, null));
      } else {
        res.sendRedirect(appContext.getPhotoServiceManager().getRedirectUrl(
            req.getParameter(ServletUtils.REQUEST_PARAM_NAME_TARGET_URL), user, id));
      }
    } else {
      res.sendError(400, "Request cannot be handled.");
    }
  }
}
