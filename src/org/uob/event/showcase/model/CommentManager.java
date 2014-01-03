package org.uob.event.showcase.model;

/**
 * Entity manager class to support comment datastore operations.
 *
 */
public interface CommentManager extends ESEntityManager<Comment> {
  /**
   * Gets a comment based on the user id and comment id.
   *
   * @param userId the user id of the owner.
   * @param id the event id.
   *
   * @return the comment entity; return null if entity does not exist.
   */
  Comment getComment(String userId, Long id);

  /**
   * Gets an {@code Iterable} collection of comments for a event.
   *
   * @param event a event entity.
   * @return an {@code Iterable} collection of comments on the event.
   */
  Iterable<Comment> getComments(Event event);

  /**
   * Creates a new comment object based on the user id. The object is not yet
   * serialized to datastore.
   *
   * @param userId the user id of the owner.
   *
   * @return a comment entity.
   */
  Comment newComment(String userId);
}
