package org.uob.event.showcase.model.nosql;

import org.uob.event.showcase.model.Photo;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;

/**
 * Photo entity for NoSQL.
 *
 */
public class PhotoNoSql extends ESEntityNoSql implements Photo {

	public static final String FIELD_NAME_EVENT_ID = "eventId";
	public static final String FIELD_NAME_OWNER_ID = "ownerId";
	public static final String FIELD_NAME_OWNER_NICKNAME = "owner";
	public static final String FIELD_NAME_TITLE = "title";
	public static final String FIELD_NAME_BLOB_KEY = "blobKey";
	public static final String FIELD_NAME_UPLOAD_TIME = "uploadTime";
	public static final String FIELD_NAME_ACTIVE = "active";


	public PhotoNoSql(Entity entity) {
		super(entity);
	}

	public PhotoNoSql(Key parentKey, String kind) {
		super(parentKey, kind);
		setActive(true);
		entity.setProperty(FIELD_NAME_OWNER_ID, parentKey.getName());
	}

	@Override
	public BlobKey getBlobKey() {
		return (BlobKey) entity.getProperty(FIELD_NAME_BLOB_KEY);
	}

	@Override
	public void setBlobKey(BlobKey blobKey) {
		entity.setProperty(FIELD_NAME_BLOB_KEY, blobKey);
	}

	@Override
	public String getTitle() {
		return (String) entity.getProperty(FIELD_NAME_TITLE);
	}

	@Override
	public void setTitle(String title) {
		entity.setProperty(FIELD_NAME_TITLE, title);
	}

	@Override
	public String getOwnerName() {
		return (String) entity.getProperty(FIELD_NAME_OWNER_NICKNAME);
	}

	@Override
	public void setOwnerName(String name) {
		entity.setProperty(FIELD_NAME_OWNER_NICKNAME, name);
	}

	@Override
	public String getEventOwnerId() {
		return (String) entity.getProperty(FIELD_NAME_OWNER_ID);
	}

	@Override
	public long getUploadTime() {
		return (Long) entity.getProperty(FIELD_NAME_UPLOAD_TIME);
	}

	@Override
	public void setUploadTime(long uploadTime) {
		entity.setProperty(FIELD_NAME_UPLOAD_TIME, uploadTime);
	}

	@Override
	public Long getId() {
		return entity.getKey().getId();
	}

	@Override
	public boolean isActive() {
		Boolean active = (Boolean) entity.getProperty(FIELD_NAME_ACTIVE);
		// By default, if not set false, a photo is active.
		return active != null && active;
	}

	@Override
	public void setActive(boolean active) {
		entity.setProperty(FIELD_NAME_ACTIVE, active);
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
	public void setEventOwnerId(String owner) {
		entity.setProperty(FIELD_NAME_OWNER_ID, owner);
	}
}
