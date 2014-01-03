package org.uob.event.showcase.model;

import com.google.appengine.api.blobstore.BlobKey;

/**
 * The photo entity interface.
 *
 */
public interface Photo extends ESEntity {
  Long getId();

  BlobKey getBlobKey();

  void setBlobKey(BlobKey blobKey);

  String getTitle();

  void setTitle(String title);

  String getOwnerName();

  void setOwnerName(String name);

  long getUploadTime();

  void setUploadTime(long uploadTime);

  boolean isActive();

  void setActive(boolean active);
  
  long getEventId();

  void setEventId(long eventId);

  String getEventOwnerId();

  void setEventOwnerId(String owner);

}
