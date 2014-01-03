package org.uob.event.showcase.model.nosql;

import org.uob.event.showcase.model.Booking;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;

/**
 * Booking entity for NoSQL.
 *
 */
public class BookingNoSql extends ESEntityNoSql implements Booking {
  public static final String FIELD_NAME_EVENT_ID = "eventId";
  public static final String FIELD_NAME_EVENT_OWNER_ID = "ownerId";
  public static final String FIELD_NAME_TIMESTAMP = "timestamp";
  public static final String FIELD_NAME_BOOKING_OWNER_ID = "ownerId";

  public BookingNoSql(Entity entity) {
    super(entity);
  }

  public BookingNoSql(Key parentKey, String kind) {
    super(parentKey, kind);
  }

  public static final String getKind() {
    return Booking.class.getSimpleName();
  }

  @Override
  public long getTimestamp() {
    Long timestamp = (Long) entity.getProperty(FIELD_NAME_TIMESTAMP);
    return timestamp == null ? 0 : timestamp;
  }

  @Override
  public void setTimestamp(long timestamp) {
    entity.setProperty(FIELD_NAME_TIMESTAMP, timestamp);
  }

  @Override
  public long getEventId() {
    Long eventId = (Long) entity.getProperty(FIELD_NAME_EVENT_ID);
    return eventId == null ? 0 : eventId;
  }

  @Override
  public void setEventId(long eventId) {
    entity.setProperty(FIELD_NAME_EVENT_ID, eventId);
  }

  @Override
  public String getEventOwnerId() {
    return (String) entity.getProperty(FIELD_NAME_EVENT_OWNER_ID);
  }

  @Override
  public void setEventOwnerId(String owner) {
    entity.setProperty(FIELD_NAME_EVENT_OWNER_ID, owner);
  }

  @Override
  public String getBookingOwnerName() {
    return (String) entity.getProperty(FIELD_NAME_BOOKING_OWNER_ID);
  }

  @Override
  public void setBookingOwnerName(String owner) {
    entity.setProperty(FIELD_NAME_BOOKING_OWNER_ID, owner);
  }

  @Override
  public Long getId() {
    return entity.getKey().getId();
  }

  @Override
  public String getBookingOwnerId() {
    return entity.getParent().getName();
  }
}
