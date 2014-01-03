package org.uob.event.showcase.model.nosql;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.SortDirection;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Nullable;

import org.uob.event.showcase.model.Booking;
import org.uob.event.showcase.model.BookingManager;
import org.uob.event.showcase.model.Event;

/**
 * Entity manager class to support comment datastore operations.
 *
 */
public class BookingManagerNoSql extends ESEntityManagerNoSql<Booking>
    implements BookingManager {
  private final ESUserManagerNoSql userManager;

  public BookingManagerNoSql(ESUserManagerNoSql userManager) {
    super(Booking.class);
    this.userManager = userManager;
  }

  @Override
  public Booking getBooking(String userId, Long id) {
    return getEntity(createBookingKey(userId, id));
  }

  @Override
  public Iterable<Booking> getBookings(Event event) {
    Query query = new Query(getKind());
    Query.Filter eventIdFilter =
        new Query.FilterPredicate(BookingNoSql.FIELD_NAME_EVENT_ID,
            FilterOperator.EQUAL, event.getId());
    query.setFilter(eventIdFilter);
    query.addSort(BookingNoSql.FIELD_NAME_TIMESTAMP, SortDirection.DESCENDING);
    FetchOptions options = FetchOptions.Builder.withLimit(100);
    return queryEntities(query, options);
  }

  @Override
  public Iterable<Booking> getUserBookings(String userId) {
    Query query = new Query(getKind());
    Query.Filter eventIdFilter =
        new Query.FilterPredicate(BookingNoSql.FIELD_NAME_BOOKING_OWNER_ID,
            FilterOperator.EQUAL, userId);
    query.setFilter(eventIdFilter);
    query.addSort(BookingNoSql.FIELD_NAME_TIMESTAMP, SortDirection.DESCENDING);
    FetchOptions options = FetchOptions.Builder.withLimit(100);
    return queryEntities(query, options);
  }

  /**
   * Creates a comment entity key.
   *
   * @param userId the user id. If null, no parent key is set.
   * @param id the comment id.
   * @return a datastore key object.
   */
  public Key createBookingKey(@Nullable String userId, long id) {
    if (userId != null) {
      Key parentKey = userManager.createESUserKey(userId);
      return KeyFactory.createKey(parentKey, getKind(), id);
    } else {
      return KeyFactory.createKey(getKind(), id);
    }
  }

  @Override
  public BookingNoSql fromParentKey(Key parentKey) {
    return new BookingNoSql(parentKey, getKind());
  }

  @Override
  public BookingNoSql newBooking(String userId) {
    return new BookingNoSql(userManager.createESUserKey(userId), getKind());
  }

  @Override
  protected BookingNoSql fromEntity(Entity entity) {
    return new BookingNoSql(entity);
  }
}
