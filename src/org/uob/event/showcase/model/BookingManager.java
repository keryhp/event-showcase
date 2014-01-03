package org.uob.event.showcase.model;

/**
 * Entity manager class to support booking datastore operations.
 *
 */
public interface BookingManager extends ESEntityManager<Booking> {
  /**
   * Gets a booking based on the user id and booking id.
   *
   * @param userId the user id of the owner.
   * @param id the event id.
   *
   * @return the booking entity; return null if entity does not exist.
   */
  Booking getBooking(String userId, Long id);

  /**
   * Gets an {@code Iterable} collection of bookings for a event.
   *
   * @param event a event entity.
   * @return an {@code Iterable} collection of bookings on the event.
   */
  Iterable<Booking> getBookings(Event event);

  /**
   * Gets an {@code Iterable} collection of bookings for a user.
   *
   * @param event a event entity.
   * @return an {@code Iterable} collection of bookings on the event.
   */
  Iterable<Booking> getUserBookings(String userId);

  /**
   * Creates a new booking object based on the user id. The object is not yet
   * serialized to datastore.
   *
   * @param userId the user id of the owner.
   *
   * @return a booking entity.
   */
  Booking newBooking(String userId);
}
