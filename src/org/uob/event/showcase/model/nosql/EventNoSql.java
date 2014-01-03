package org.uob.event.showcase.model.nosql;

import org.uob.event.showcase.model.Event;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;

/**
 * Event entity for NoSQL.
 *
 */
public class EventNoSql extends ESEntityNoSql implements Event {

	public static final String FIELD_NAME_EVENT_ID = "eventId";
	public static final String FIELD_NAME_OWNER_ID = "ownerId";
	public static final String FIELD_NAME_OWNER_NICKNAME = "owner";
	public static final String FIELD_NAME_TITLE = "title";
	public static final String FIELD_NAME_BLOB_KEY = "blobKey";
	public static final String FIELD_NAME_UPLOAD_TIME = "uploadTime";
	public static final String FIELD_NAME_EVENT_TIME = "eventTime";
	public static final String FIELD_NAME_ACTIVE = "active";
	public static final String FIELD_NAME_LOCATION = "location";
	public static final String FIELD_NAME_DESCRIPTION = "description";


	public EventNoSql(Entity entity) {
		super(entity);
	}

	public EventNoSql(Key parentKey, String kind) {
		super(parentKey, kind);
		setActive(true);
		//entity.setProperty(FIELD_NAME_OWNER_ID, parentKey.getName());
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
	public String getOwnerId() {
		return (String) entity.getProperty(FIELD_NAME_OWNER_ID);
	}
	
	@Override
	public void setOwnerId(String userId) {
		entity.setProperty(FIELD_NAME_OWNER_ID, userId);
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
	public String getDescription() {
		return (String) entity.getProperty(FIELD_NAME_DESCRIPTION);
	}

	@Override
	public void setDescription(String description) {
		entity.setProperty(FIELD_NAME_DESCRIPTION, description);

	}

	@Override
	public String getLocation() {
		return (String) entity.getProperty(FIELD_NAME_LOCATION);
	}

	@Override
	public void setLocation(String location) {
		entity.setProperty(FIELD_NAME_LOCATION, location);

	}

	@Override
	public long getEventTime() {
		if(entity.getProperty(FIELD_NAME_EVENT_TIME) == null)
			return 0;
		else
		return (Long) entity.getProperty(FIELD_NAME_EVENT_TIME);
	}

	@Override
	public void setEventTime(long eventTime) {
		entity.setProperty(FIELD_NAME_EVENT_TIME, eventTime);	
	}

}
