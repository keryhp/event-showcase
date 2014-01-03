package org.uob.event.showcase.model;

/**
 * The comment entity interface.
 *
 */
public interface Comment extends ESEntity {
  Long getId();

  String getContent();

  void setContent(String content);

  long getTimestamp();

  void setTimestamp(long timestamp);

  long getEventId();

  void setEventId(long eventId);

  String getEventOwnerId();

  void setEventOwnerId(String owner);

  String getCommentOwnerName();

  void setCommentOwnerName(String owner);

  String getCommentOwnerId();
}
