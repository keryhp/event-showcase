package org.uob.event.showcase.model;

/**
 * The photo entity manager interface.
 *
 */
public interface PhotoManager extends ESEntityManager<Photo> {

  /**
   * Lookups a specific photo.
   *
   * @param eventId the event's event id.
   * @param id the photo id.
   *
   * @return the photo entity; return null if photo is not found.
   */
  Photo getPhoto(Long eventId, long id);
  
  Iterable<Photo> getPhoto(Long eventId);

  /**
   * Queries an {@code Iterable} collection of photos owned by the event.
   *
   * @param eventId the event id.
   * @return an {@code Iterable} collection of photos.
   */
  Iterable<Photo> getEventPhotos(Long eventId);

  /**
   * Gets all deactived photos.
   *
   * @return an {@code Iterable} collection of deactived photos.
   */
  Iterable<Photo> getDeactivedPhotos();

  /**
   * Gets all active photos.
   *
   * @return an {@code Iterable} collection of active photos.
   */
  Iterable<Photo> getActivePhotos();

  /**
   * Creates a new photo object based on event id. The object is not yet
   * serialized to datastore yet.
   *
   * @param eventId the event id.
   *
   * @return a photo object.
   */
  Photo newPhoto(Long eventId);

  /**
   * Marks a photo inactive so that the photo is ready for delete.
   *
   * @return the deactived photo object; null if operation failed.
   */
  Photo deactivePhoto(Long eventId, long id);
}
