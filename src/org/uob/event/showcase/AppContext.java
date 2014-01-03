package org.uob.event.showcase;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;

import java.util.logging.Logger;

import org.uob.event.showcase.model.BookingManager;
import org.uob.event.showcase.model.CommentManager;
import org.uob.event.showcase.model.ESEntityManagerFactory;
import org.uob.event.showcase.model.ESUser;
import org.uob.event.showcase.model.ESUserManager;
import org.uob.event.showcase.model.EventManager;
import org.uob.event.showcase.model.PhotoManager;

/**
 * A convenient singleton context object for the application.
 *
 */
public class AppContext {
	private static final Logger logger = Logger.getLogger(AppContext.class.getCanonicalName());

	private static AppContext instance = new AppContext();

	private ConfigManager configManager;

	private PhotoServiceManager photoServiceManager;
	
	private EmailServiceManager emailServiceManager;

	private ESEntityManagerFactory entityManagerFactory;

	// Prevent the class being instantiated externally.
	private AppContext() {
		configManager = new ConfigManager();

		String clsName = configManager.getDemoEntityManagerFactory();
		try {
			Class<?> cls = Class.forName(clsName);
			entityManagerFactory = (ESEntityManagerFactory) cls.newInstance();
			entityManagerFactory.init(configManager);
		} catch (ClassNotFoundException e) {
			logger.severe("cannot find demo entity manager factory class:" + e.getMessage());
			throw new RuntimeException("cannot find demo entity manager factory class", e);
		} catch (InstantiationException e) {
			logger.severe("cannot create instance of entity manager factory");
			throw new RuntimeException("cannot create instance of entity manager factory", e);
		} catch (IllegalAccessException e) {
			logger.severe("cannot create instance of entity manager factory");
			throw new RuntimeException("cannot create instance of entity manager factory", e);
		}
		photoServiceManager = new PhotoServiceManager(configManager, getPhotoManager());
		emailServiceManager = new EmailServiceManager(configManager, getEventManager(), getBookingManager());
	}

	public static AppContext getAppContext() {
		return instance;
	}

	/**
	 * @return the demoUserManager
	 */
	public ESUserManager getESUserManager() {
		return entityManagerFactory.getESUserManager();
	}

	public EventManager getEventManager() {
		return entityManagerFactory.getEventManager();
	}

	public PhotoManager getPhotoManager() {
		return entityManagerFactory.getPhotoManager();
	}

	public CommentManager getCommentManager() {
		return entityManagerFactory.getCommentManager();
	}
	
	public BookingManager getBookingManager() {
		return entityManagerFactory.getBookingManager();
	}

	public ConfigManager getConfigManager() {
		return configManager;
	}

	public PhotoServiceManager getPhotoServiceManager() {
		return photoServiceManager;
	}
	
	public EmailServiceManager getEmailServiceManager() {
		return emailServiceManager;
	}

	public ESUser getCurrentUser() {
		ESUserManager esUserManager = entityManagerFactory.getESUserManager();
		User user = UserServiceFactory.getUserService().getCurrentUser();
		if(user == null){
			return null;
		}
		ESUser esUser = esUserManager.getUser(user.getUserId());
		if (esUser == null) {
			esUser = esUserManager.newUser(user.getUserId());
			esUser.setEmail(user.getEmail());
			esUser.setName(user.getNickname());
			esUser.setRole(ServletUtils.REQUEST_PARAM_NAME_USER_NORMAL);
			esUserManager.upsertEntity(esUser);
		}
		return esUser;
	}
}
