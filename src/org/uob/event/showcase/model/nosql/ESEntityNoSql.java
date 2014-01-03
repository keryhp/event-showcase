package org.uob.event.showcase.model.nosql;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;

/**
 * Base entity class for NoSQL.
 *
 */
public abstract class ESEntityNoSql {
  protected final Entity entity;

  protected ESEntityNoSql(Entity entity) {
    this.entity = entity;
  }

  protected ESEntityNoSql(Key parentKey, String kind) {
    this.entity = new Entity(kind, parentKey);
  }

  protected ESEntityNoSql(String kind, String keyName) {
    this.entity = new Entity(kind, keyName);
  }

  public Entity getEntity() {
    return entity;
  }
}
