package org.uob.event.showcase.model.nosql;

import static com.google.appengine.api.datastore.DatastoreServiceFactory.getDatastoreService;

import java.util.logging.Logger;

import org.uob.event.showcase.model.Event;
import org.uob.event.showcase.model.EventManager;
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
public class EventManagerNoSql extends ESEntityManagerNoSql<Event> implements EventManager {
  private static final Logger logger = Logger.getLogger(EventManagerNoSql.class.getCanonicalName());
  private ESUserManagerNoSql userManager;

  public EventManagerNoSql(ESUserManagerNoSql userManager) {
    super(Event.class);
    this.userManager = userManager;
  }

  @Override
  public Event getEvent(long eventId) {
    Key key = createEventKey(null, eventId);
    return getEntity(key);
  }

  public Key createEventKey(String userId, Long eventId) {
    Utils.assertTrue(eventId != null, "id cannot be null");
    if (userId != null) {
      Key parentKey = userManager.createESUserKey(userId);
      return KeyFactory.createKey(parentKey, getKind(), eventId);
    } else {
      return KeyFactory.createKey(getKind(), eventId);
    }
  }

  @Override
  public Iterable<Event> getActiveEvents() {
    Query query = new Query(getKind());
    Query.Filter filter = new Query.FilterPredicate(EventNoSql.FIELD_NAME_ACTIVE, FilterOperator.EQUAL, true);
    query.addSort(EventNoSql.FIELD_NAME_EVENT_TIME, SortDirection.DESCENDING);
    query.setFilter(filter);
    FetchOptions options = FetchOptions.Builder.withLimit(100);
    return queryEntities(query, options);    
  }

  @Override
  public Iterable<Event> getOwnedEvents(String userId) {
    Query query = new Query(getKind());
    //query.setAncestor(userManager.createESUserKey(userId));
    Query.Filter filter = new Query.FilterPredicate(EventNoSql.FIELD_NAME_OWNER_ID,
        FilterOperator.EQUAL, userId);
    query.setFilter(filter);
    FetchOptions options = FetchOptions.Builder.withLimit(100);
    return queryEntities(query, options);
  }

  @Override
  public Iterable<Event> getDeactivedEvents() {
    Query query = new Query(getKind());
    Query.Filter filter = new Query.FilterPredicate(EventNoSql.FIELD_NAME_ACTIVE,
        FilterOperator.EQUAL, false);
    query.setFilter(filter);
    FetchOptions options = FetchOptions.Builder.withLimit(100);
    return queryEntities(query, options);
  }

  @Override
  public EventNoSql fromParentKey(Key parentKey) {
    return new EventNoSql(parentKey, getKind());
  }

  @Override
  public EventNoSql newEvent(String userId) {
    return new EventNoSql(null, getKind());
  }

  @Override
  protected EventNoSql fromEntity(Entity entity) {
    return new EventNoSql(entity);
  }

  @Override
  public Event deactiveEvent(String userId, long id) {
    Utils.assertTrue(userId != null, "event id cannot be null");
    DatastoreService ds = getDatastoreService();
    Transaction txn = ds.beginTransaction();
    try {
      Entity entity = getDatastoreEntity(ds, createEventKey(userId, id));
      if (entity != null) {
        EventNoSql event = new EventNoSql(entity);
        if (event.isActive()) {
          event.setActive(false);
          ds.put(entity);
        }
        txn.commit();

        return event;
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
