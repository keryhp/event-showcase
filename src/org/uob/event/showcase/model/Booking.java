package org.uob.event.showcase.model;

/**
 * The comment entity interface.
 *
 */
public interface Booking extends ESEntity {
  Long getId();

  long getTimestamp();

  void setTimestamp(long timestamp);

  long getEventId();

  void setEventId(long eventId);

  String getEventOwnerId();

  void setEventOwnerId(String owner);

  String getBookingOwnerName();

  void setBookingOwnerName(String owner);

  String getBookingOwnerId();
}
