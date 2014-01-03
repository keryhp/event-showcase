package org.uob.event.showcase.model.nosql;

import org.uob.event.showcase.model.Comment;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;

/**
 * Comment entity for NoSQL.
 *
 */
public class CommentNoSql extends ESEntityNoSql implements Comment {
  public static final String FIELD_NAME_EVENT_ID = "eventId";
  public static final String FIELD_NAME_EVENT_OWNER_ID = "ownerId";
  public static final String FIELD_NAME_TIMESTAMP = "timestamp";
  public static final String FIELD_NAME_CONTENT = "content";
  public static final String FIELD_NAME_COMMENT_OWNER_NAME = "owner";

  public CommentNoSql(Entity entity) {
    super(entity);
  }

  public CommentNoSql(Key parentKey, String kind) {
    super(parentKey, kind);
  }

  public static final String getKind() {
    return Comment.class.getSimpleName();
  }

  @Override
  public String getContent() {
    return (String) entity.getProperty(FIELD_NAME_CONTENT);
  }

  @Override
  public void setContent(String content) {
    entity.setProperty(FIELD_NAME_CONTENT, content);
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
  public String getCommentOwnerName() {
    return (String) entity.getProperty(FIELD_NAME_COMMENT_OWNER_NAME);
  }

  @Override
  public void setCommentOwnerName(String owner) {
    entity.setProperty(FIELD_NAME_COMMENT_OWNER_NAME, owner);
  }

  @Override
  public Long getId() {
    return entity.getKey().getId();
  }

  @Override
  public String getCommentOwnerId() {
    return entity.getParent().getName();
  }
}
