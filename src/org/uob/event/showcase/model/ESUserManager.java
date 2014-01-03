package org.uob.event.showcase.model;

/**
 * User manager interface.
 *
 */
public interface ESUserManager extends ESEntityManager<ESUser> {
  /**
   * Gets the user entity based on user id.
   *
   * @param userId the user id.
   *
   * @return the user entity; return null if user is not found.
   */
  ESUser getUser(String userId);

  /**
   * Gets all the user entities based.
   *
   * @return the user entity list; return null if user is not found.
   */
  Iterable<ESUser> getAllUsers();

  /**
   * Creates a new user object. The object is not serialized to datastore yet.
   *
   * @param userId the user id.
   * @return the user object.
   */
  ESUser newUser(String userId);
}
