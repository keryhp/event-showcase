package org.uob.event.showcase.model.nosql;

import org.uob.event.showcase.model.ESUser;

import com.google.appengine.api.datastore.Entity;

/**
 * User entity class for NoSQL.
 *
 */
public class ESUserNoSql extends ESEntityNoSql  implements ESUser {
  private static final String FIELD_NAME_NAME = "name";
  private static final String FIELD_NAME_EMAIL = "email";
  public static final String FIELD_NAME_ROLE = "role";

  public ESUserNoSql(Entity entity) {
    super(entity);
  }

  public ESUserNoSql(String kind, String userId) {
    super(kind, userId);
  }

  @Override
  public String getUserId() {
    return entity.getKey().getName();
  }

  @Override
  public String getEmail() {
    return (String) entity.getProperty(FIELD_NAME_EMAIL);
  }

  @Override
  public void setEmail(String email) {
    entity.setProperty(FIELD_NAME_EMAIL, email);
  }

  @Override
  public String getName() {
    return (String) entity.getProperty(FIELD_NAME_NAME);
  }

  @Override
  public void setName(String name) {
    entity.setProperty(FIELD_NAME_NAME, name);
  }
  
  @Override
  public String getRole() {
    return (String) entity.getProperty(FIELD_NAME_ROLE);
  }

  @Override
  public void setRole(String role) {
    entity.setProperty(FIELD_NAME_ROLE, role);
  }

}
