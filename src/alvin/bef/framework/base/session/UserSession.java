package alvin.bef.framework.base.session;

import alvin.bef.framework.base.model.ShardContext;

/**
 * 
 * @author Alvin
 *
 */
public interface UserSession extends ShardContext {
	public static final String USER_SESSION_REQUEST_ATTRIBUTE = "userSession";
	String getUserId();
	String getUsername();
	
   boolean isPasswordExpired();
   boolean isPasswordReset(); 
}
