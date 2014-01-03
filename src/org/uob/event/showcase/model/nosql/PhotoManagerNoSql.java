package org.uob.event.showcase.model.nosql;

import static com.google.appengine.api.datastore.DatastoreServiceFactory.getDatastoreService;

import java.util.logging.Logger;

import org.uob.event.showcase.model.Photo;
import org.uob.event.showcase.model.PhotoManager;
import org.uob.event.showcase.model.Utils;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.datastore.Transaction;

/**
 * The photo entity manager class for NoSQL.
 *
 */
public class PhotoManagerNoSql extends ESEntityManagerNoSql<Photo> implements PhotoManager {
  private static final Logger logger = Logger.getLogger(PhotoManagerNoSql.class.getCanonicalName());
  private EventManagerNoSql eventManager;

  public PhotoManagerNoSql(EventManagerNoSql eventManager) {
    super(Photo.class);
    this.eventManager = eventManager;
  }

  @Override
  public Photo getPhoto(Long eventId, long id) {
    Key key = createPhotoKey(eventId, id);
    return getEntity(key);
  }
  
  @Override
  public Iterable<Photo> getPhoto(Long eventId) {
	    Query query = new Query(getKind()).setAncestor(eventManager.createEventKey(null, eventId));
	    Query.Filter filter = new Query.FilterPredicate(PhotoNoSql.FIELD_NAME_ACTIVE,
	        FilterOperator.EQUAL, true);
	    query.addSort(PhotoNoSql.FIELD_NAME_UPLOAD_TIME, SortDirection.DESCENDING);
	    query.setFilter(filter);
	    FetchOptions options = FetchOptions.Builder.withLimit(100);
	    return queryEntities(query, options);    
  }

  public Key createPhotoKey(Long eventId, Long id) {
    Utils.assertTrue(id != null, "id cannot be null");
    if (eventId != null) {
      Key parentKey = eventManager.createEventKey(null, eventId);
      return KeyFactory.createKey(parentKey, getKind(), id);
    } else {
      return KeyFactory.createKey(getKind(), id);
    }
  }

  @Override
  public Iterable<Photo> getActivePhotos() {
    Query query = new Query(getKind());
    Query.Filter filter = new Query.FilterPredicate(PhotoNoSql.FIELD_NAME_ACTIVE,
        FilterOperator.EQUAL, true);
    query.addSort(PhotoNoSql.FIELD_NAME_UPLOAD_TIME, SortDirection.DESCENDING);
    query.setFilter(filter);
    FetchOptions options = FetchOptions.Builder.withLimit(100);
    return queryEntities(query, options);    
  }

  @Override
  public Iterable<Photo> getEventPhotos(Long eventId) {
    Query query = new Query(getKind());
    query.setAncestor(eventManager.createEventKey(null, eventId));
    Query.Filter filter = new Query.FilterPredicate(PhotoNoSql.FIELD_NAME_ACTIVE,
        FilterOperator.EQUAL, true);
    query.setFilter(filter);
    FetchOptions options = FetchOptions.Builder.withLimit(100);
    return queryEntities(query, options);
  }

  @Override
  public Iterable<Photo> getDeactivedPhotos() {
    Query query = new Query(getKind());
    Query.Filter filter = new Query.FilterPredicate(PhotoNoSql.FIELD_NAME_ACTIVE,
        FilterOperator.EQUAL, false);
    query.setFilter(filter);
    FetchOptions options = FetchOptions.Builder.withLimit(100);
    return queryEntities(query, options);
  }

  @Override
  public PhotoNoSql fromParentKey(Key parentKey) {
    return new PhotoNoSql(parentKey, getKind());
  }

  @Override
  public PhotoNoSql newPhoto(Long eventId) {
    return new PhotoNoSql(eventManager.createEventKey(null, eventId), getKind());
  }

  @Override
  protected PhotoNoSql fromEntity(Entity entity) {
    return new PhotoNoSql(entity);
  }

  @Override
  public Photo deactivePhoto(Long eventId, long id) {
    Utils.assertTrue(eventId != null, "event id cannot be null");
    DatastoreService ds = getDatastoreService();
    Transaction txn = ds.beginTransaction();
    try {
      Entity entity = getDatastoreEntity(ds, createPhotoKey(eventId, id));
      if (entity != null) {
        PhotoNoSql photo = new PhotoNoSql(entity);
        if (photo.isActive()) {
          photo.setActive(false);
          ds.put(entity);
        }
        txn.commit();

        return photo;
      }
    } catch (Exception e) {
      logger.severe("Failed to delete entity from datastore:" + e.getMessage());
    } finally {
      if (txn.isActive()) {
        txn.rollback();
      }
    }
    return null;
  }
}
