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

import org.uob.event.showcase.model.Comment;
import org.uob.event.showcase.model.CommentManager;
import org.uob.event.showcase.model.Event;

/**
 * Entity manager class to support comment datastore operations.
 *
 */
public class CommentManagerNoSql extends ESEntityManagerNoSql<Comment>
    implements CommentManager {
  private final ESUserManagerNoSql userManager;

  public CommentManagerNoSql(ESUserManagerNoSql userManager) {
    super(Comment.class);
    this.userManager = userManager;
  }

  @Override
  public Comment getComment(String userId, Long id) {
    return getEntity(createCommentKey(userId, id));
  }

  @Override
  public Iterable<Comment> getComments(Event event) {
    Query query = new Query(getKind());
    Query.Filter eventIdFilter =
        new Query.FilterPredicate(CommentNoSql.FIELD_NAME_EVENT_ID,
            FilterOperator.EQUAL, event.getId());
    query.setFilter(eventIdFilter);
    query.addSort(CommentNoSql.FIELD_NAME_TIMESTAMP, SortDirection.DESCENDING);
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
  public Key createCommentKey(@Nullable String userId, long id) {
    if (userId != null) {
      Key parentKey = userManager.createESUserKey(userId);
      return KeyFactory.createKey(parentKey, getKind(), id);
    } else {
      return KeyFactory.createKey(getKind(), id);
    }
  }

  @Override
  public CommentNoSql fromParentKey(Key parentKey) {
    return new CommentNoSql(parentKey, getKind());
  }

  @Override
  public CommentNoSql newComment(String userId) {
    return new CommentNoSql(userManager.createESUserKey(userId), getKind());
  }

  @Override
  protected CommentNoSql fromEntity(Entity entity) {
    return new CommentNoSql(entity);
  }
}
