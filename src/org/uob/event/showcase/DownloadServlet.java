package org.uob.event.showcase;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.uob.event.showcase.model.Photo;

/**
 * Photo download servlet.
 *
 */
public class DownloadServlet extends HttpServlet {
  private static final long serialVersionUID = 1L;

  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
    Long eventId = Long.parseLong(req.getParameter("eventId"));
    String id = req.getParameter("id");
    Long photoId = ServletUtils.validatePhotoId(id);
    if (photoId != null && eventId != null) {
      Photo photo = AppContext.getAppContext().getPhotoManager().getPhoto(eventId, photoId);
      BlobKey blobKey = photo.getBlobKey();
      BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
      blobstoreService.serve(blobKey, res);
    } else {
      res.sendError(400, "One or more parameters are not set");
    }
  }
}
