package org.uob.event.showcase.model;

import org.uob.event.showcase.ConfigManager;

/**
 * The entity manager factory interface.
 *
 * @author Michael Tang (ntang@google.com)
 */
public interface ESEntityManagerFactory {
  /**
   * Initiates the factory object.
   *
   * @param configManager the configuration manager.
   */
  void init(ConfigManager configManager);

  /**
   * Gets the photo manager.
   *
   * @return the photo manager object.
   */
  EventManager getEventManager();

  /**
   * Gets the photo manager.
   *
   * @return the photo manager object.
   */
  PhotoManager getPhotoManager();

  /**
   * Gets the comment manager.
   *
   * @return the comment manager object.
   */
  CommentManager getCommentManager();

  /**
   * Gets the booking manager.
   *
   * @return the booking manager object.
   */
  BookingManager getBookingManager();

  /**
   * Gets the demo user manager.
   *
   * @return the demo user manager object.
   */
  ESUserManager getESUserManager();
}
