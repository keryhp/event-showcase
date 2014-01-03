package org.uob.event.showcase.model.nosql;

import org.uob.event.showcase.ServletUtils;
import org.uob.event.showcase.model.ESModelException;
import org.uob.event.showcase.model.ESUser;
import org.uob.event.showcase.model.ESUserManager;
import org.uob.event.showcase.model.Utils;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.SortDirection;

/**
 * User manager class for NoSQL.
 *
 */
public class ESUserManagerNoSql extends ESEntityManagerNoSql<ESUser>
implements ESUserManager {
	public ESUserManagerNoSql() {
		super(ESUser.class);
	}

	public Key createESUserKey(String userId) {
		return KeyFactory.createKey(getKind(), userId);
	}

	@Override
	protected ESUserNoSql fromParentKey(Key parentKey) {
		throw new ESModelException("Demo User is entity group root, so it cannot have parent key");
	}

	@Override
	protected ESUserNoSql fromEntity(Entity entity) {
		return new ESUserNoSql(entity);
	}

	@Override
	public ESUser getUser(String userId) {
		Utils.assertTrue(userId != null, "userId is null!");
		return getEntity(createESUserKey(userId));
	}

	@Override
	public ESUser newUser(String userId) {
		return new ESUserNoSql(getKind(), userId);
	}

	@Override
	public Iterable<ESUser> getAllUsers() {
	    Query query = new Query(getKind());
	    Query.Filter filter = new Query.FilterPredicate(ESUserNoSql.FIELD_NAME_ROLE, FilterOperator.EQUAL, ServletUtils.REQUEST_PARAM_NAME_USER_ORGANIZER);
	    query.setFilter(filter);
	    FetchOptions options = FetchOptions.Builder.withLimit(100);
	    return queryEntities(query, options);    
	}
}
