package org.uob.event.showcase.model;

/**
 * The event entity manager interface.
 *
 */
public interface EventManager extends ESEntityManager<Event> {
  /**
   * Lookups a specific event.
   *
   * @param userId the owner's user id.
   * @param eventId the event id.
   *
   * @return the event entity; return null if event is not found.
   */
  Event getEvent(long eventId);

  /**
   * Queries an {@code Iterable} collection of events owned by the user.
   *
   * @param userId the user id.
   * @return an {@code Iterable} collection of events.
   */
  Iterable<Event> getOwnedEvents(String userId);

  /**
   * Gets all deactived events.
   *
   * @return an {@code Iterable} collection of deactived events.
   */
  Iterable<Event> getDeactivedEvents();

  /**
   * Gets all active events.
   *
   * @return an {@code Iterable} collection of active events.
   */
  Iterable<Event> getActiveEvents();

  /**
   * Creates a new event object based on user id. The object is not yet
   * serialized to datastore yet.
   *
   * @param userId the user id.
   *
   * @return a event object.
   */
  Event newEvent(String userId);

  /**
   * Marks a event inactive so that the event is ready for delete.
   *
   * @return the deactived event object; null if operation failed.
   */
  Event deactiveEvent(String userId, long id);
}
