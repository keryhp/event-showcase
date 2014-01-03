package org.uob.event.showcase.model;

/**
 * The photo entity interface.
 *
 */
public interface Event extends ESEntity {
  Long getId();

  String getTitle();

  void setTitle(String title);

  String getDescription();

  void setDescription(String description);

  String getLocation();

  void setLocation(String location);

  String getOwnerName();

  void setOwnerName(String name);

  String getOwnerId();
  
  void setOwnerId(String userId);

  long getUploadTime();

  void setUploadTime(long uploadTime);

  long getEventTime();

  void setEventTime(long eventTime);

  boolean isActive();

  void setActive(boolean active);
}