package org.uob.event.showcase.model.nosql;

import org.uob.event.showcase.ConfigManager;
import org.uob.event.showcase.model.BookingManager;
import org.uob.event.showcase.model.CommentManager;
import org.uob.event.showcase.model.ESEntityManagerFactory;
import org.uob.event.showcase.model.ESUserManager;
import org.uob.event.showcase.model.EventManager;
import org.uob.event.showcase.model.PhotoManager;

/**
 * Entity manager factory implementation for NoSQL.
 *
 * @author Michael Tang (ntang@google.com)
 */
public class ESEntityManagerNoSqlFactory implements ESEntityManagerFactory {
  private ESUserManagerNoSql esUserManager;
  private EventManagerNoSql eventManager;
  private PhotoManagerNoSql photoManager;
  private CommentManagerNoSql commentManager;
  private BookingManagerNoSql bookingManager;
  private boolean initialized;

  @Override
  public PhotoManager getPhotoManager() {
    return photoManager;
  }

  @Override
  public CommentManager getCommentManager() {
    return commentManager;
  }
  
  @Override
  public BookingManager getBookingManager() {
    return bookingManager;
  }

  @Override
  public ESUserManager getESUserManager() {
    return esUserManager;
  }

  @Override
  public EventManager getEventManager() {
  	return eventManager;
  }
  
  @Override
  public void init(ConfigManager configManager) {
    if (!initialized) {
      esUserManager = new ESUserManagerNoSql();
      eventManager = new EventManagerNoSql(esUserManager);
      photoManager = new PhotoManagerNoSql(eventManager);
      commentManager = new CommentManagerNoSql(esUserManager);
      bookingManager = new BookingManagerNoSql(esUserManager);
      initialized = true;
    } else {
      throw new IllegalStateException("Should not initialize the factory more than once.");
    }
  }

}
