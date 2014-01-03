package org.uob.event.showcase;

import java.io.FileNotFoundException;
import java.util.logging.Logger;

import org.uob.event.showcase.model.Photo;
import org.uob.event.showcase.model.PhotoManager;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.blobstore.UploadOptions;
import com.google.appengine.api.files.AppEngineFile;
import com.google.appengine.api.files.FileService;
import com.google.appengine.api.files.FileServiceFactory;
import com.google.appengine.api.files.FileStat;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.ImagesServiceFailureException;
import com.google.appengine.api.images.ServingUrlOptions;

/**
 * The main service class providing some helper method for photo management.
 *
 */
public class PhotoServiceManager {
  private static final Logger logger =
      Logger.getLogger(PhotoServiceManager.class.getCanonicalName());
  private ConfigManager configManager;
  private PhotoManager photoManager;

  public PhotoServiceManager(ConfigManager configManager, PhotoManager photoManager) {
    this.configManager = configManager;
    this.photoManager = photoManager;
  }

  public String getUploadUrl() {
    BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
    UploadOptions uploadOptions = null;
    String bucket = configManager.getGoogleStorageBucket();
    if (bucket == null || bucket.isEmpty()) {
    	System.out.println("Yoohooo!");
      uploadOptions = UploadOptions.Builder.withDefaults();
    } else {
      uploadOptions = UploadOptions.Builder.withGoogleStorageBucketName(bucket);
    }
    return blobstoreService.createUploadUrl(configManager.getUploadHandlerUrl(), uploadOptions);
  }

  public String getThumbnailUrl(BlobKey blobKey) {
    ServingUrlOptions options = ServingUrlOptions.Builder.withBlobKey(blobKey);
    options.imageSize(configManager.getPhotoThumbnailSizeInPixels());
    options.crop(configManager.isPhotoThumbnailCrop());
    try {
      return ImagesServiceFactory.getImagesService().getServingUrl(options);
    } catch (ImagesServiceFailureException e) {
      logger.severe("Failed to get image serving url: " + e.getMessage());
      return "";
    } catch (Exception e) {
      logger.severe("Invalid blob key: " + e.getMessage());
      return "";
    }
  }

  public String getPhotoDisplayUrl(BlobKey blobKey) {
    ServingUrlOptions options = ServingUrlOptions.Builder.withBlobKey(blobKey);
    options.imageSize(configManager.getPhotoThumbnailSizeInPixels());
    options.crop(configManager.isPhotoThumbnailCrop());
    try {
      return ImagesServiceFactory.getImagesService().getServingUrl(options);
    } catch (ImagesServiceFailureException e) {
      logger.severe("Failed to get image serving url: " + e.getMessage());
      return "";
    }
  }

  /**
   * Gets the photo menu link url.
   *
   * @param targetUrl the target page url.
   * @param photo the photo object.
   * @return a photo url string.
   */
  public String getMenuLinkUrl(String targetUrl, Photo photo) {
    if (targetUrl == null) {
      targetUrl = configManager.getMainPageUrl();
    }
    return new StringBuilder(targetUrl).append("?")
        .append(ServletUtils.REQUEST_PARAM_NAME_PHOTO_ID)
        .append("=")
        .append(photo.getId())
        .append("&")
        .append(ServletUtils.REQUEST_PARAM_NAME_EVENT_OWNER_ID)
        .append("=")
        .append(photo.getEventOwnerId()).toString();
  }

  public String getImageDownloadUrl(Photo photo) {
    return new StringBuilder(configManager.getDownloadHandlerUrl()).append("?")
        .append(ServletUtils.REQUEST_PARAM_NAME_PHOTO_ID)
        .append("=")
        .append(photo.getId())
        .append("&")
        .append(ServletUtils.REQUEST_PARAM_NAME_EVENT_OWNER_ID)
        .append("=")
        .append(photo.getEventOwnerId())
        .append("&")
        .append(ServletUtils.REQUEST_PARAM_NAME_EVENT_ID)
        .append("=")
        .append(photo.getEventId()).toString();
  }

  /**
   * Constructs a redirect url to specific photo. if the photo information is not available, return
   * to the main page.
   *
   * @param targetUrl target url. If null,
   * @param userId the photo owner id
   * @param id the photo id.
   *
   * @return the url string to the main page.
   */
  public String getRedirectUrl(String targetUrl, String userId, String id) {
    if (targetUrl == null) {
      targetUrl = configManager.getMainPageUrl();
    }
    StringBuilder builder = new StringBuilder(targetUrl);
    if (userId != null || id != null) {
      builder.append("?")
          .append(ServletUtils.REQUEST_PARAM_NAME_EVENT_ID)
          .append("=")
          .append(id)
          .append("&")
          .append(ServletUtils.REQUEST_PARAM_NAME_EVENT_OWNER_ID)
          .append("=")
          .append(userId);
    }
    return builder.toString();
  }
  
  public String getBookedRedirectUrl(String targetUrl, String userId, String id, String bookingId) {
	    if (targetUrl == null) {
	      targetUrl = configManager.getMainPageUrl();
	    }
	    StringBuilder builder = new StringBuilder(targetUrl);
	    if (userId != null || id != null) {
	      builder.append("?")
	          .append(ServletUtils.REQUEST_PARAM_NAME_EVENT_ID)
	          .append("=")
	          .append(id)
	          .append("&")
	          .append(ServletUtils.REQUEST_PARAM_NAME_EVENT_OWNER_ID)
	          .append("=")
	          .append(userId)
	          .append("&")
	          .append(ServletUtils.REQUEST_PARAM_NAME_BOOKING_ID)
	          .append("=")
	          .append(bookingId);
	    }
	    return builder.toString();
	  }

  public void cleanDeatctivedPhotos() {
    Iterable<Photo> photos = photoManager.getDeactivedPhotos();
    if (photos != null) {
      for (Photo photo : photos) {
        removeDeactivedPhoto(photo);
      }
    }
  }
  private void removeDeactivedPhoto(Photo photo) {
    if (photo != null && !photo.isActive()) {
      try {
        FileService fileService = FileServiceFactory.getFileService();
        BlobKey blobKey = photo.getBlobKey();
        AppEngineFile file = fileService.getBlobFile(blobKey);
        FileStat stat = fileService.stat(file);
        if (stat != null) {
          logger.fine("photo:" + photo.getId() + " blob file stat is not null");
          BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
          blobstoreService.delete(blobKey);
          logger.info("The blob is deleted. try to delete the entity from datastore.");
          photoManager.deleteEntity(photo);
        }
      } catch (FileNotFoundException e) {
        logger.info("The blob is alrady deleted. try to delete the entity from datastore.");
        photoManager.deleteEntity(photo);
      } catch (Exception e) {
        logger.severe("Failed to delete the blob storge for photo " + photo.getId() +
            ":" + e.getMessage());
      }
    }
  }
}
