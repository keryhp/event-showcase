package org.uob.event.showcase;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.uob.event.showcase.model.ESUser;
import org.uob.event.showcase.model.Photo;
import org.uob.event.showcase.model.PhotoManager;


/**
 * Servlet to handle photo upload to Google Cloud Storage.
 *
 */
public class UploadHandlerServlet extends HttpServlet {
  @Override
  public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
    AppContext appContext = AppContext.getAppContext();
    ESUser user = appContext.getCurrentUser();
    if (user == null) {
      res.sendError(401, "You have to login to upload image.");
      return;
    }
    BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
    Map<String, List<BlobKey>> blobs = blobstoreService.getUploads(req);
    String eventId = req.getParameter(ServletUtils.REQUEST_PARAM_NAME_EVENT_ID);
    List<BlobKey> keys = blobs.get("photo");
    String id = null;
    boolean succeeded = false;
    if (keys != null && keys.size() > 0) {
      PhotoManager photoManager = appContext.getPhotoManager();
      Photo photo = photoManager.newPhoto(ServletUtils.validateEventId(eventId));
      String title = req.getParameter("title");
      if (title != null) {
        photo.setTitle(title);
      }

      photo.setOwnerName(ServletUtils.getProtectedUserName(user.getName()));
      photo.setEventId(ServletUtils.validateEventId(eventId));
      photo.setEventOwnerId(user.getUserId());

      BlobKey blobKey = keys.get(0);
      photo.setBlobKey(blobKey);

      photo.setUploadTime(System.currentTimeMillis());

      photo = photoManager.upsertEntity(photo);
      id = photo.getId().toString();
      succeeded = true;
    }
    if (succeeded) {
      res.sendRedirect(appContext.getPhotoServiceManager().getRedirectUrl(
          appContext.getConfigManager().getViewEventUrl(), user.getUserId(), eventId));
    } else {
      res.sendError(400, "Request cannot be handled.");
    }
  }
}
