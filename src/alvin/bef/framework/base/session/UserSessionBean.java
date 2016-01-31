package alvin.bef.framework.base.session;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 
 * @author Alvin
 *
 */
public class UserSessionBean implements UserSession, Serializable {

	private static final long serialVersionUID = -4592099868321609541L;


	private final String userId;
	private final String username;
	private final int mode;
	private final String shardName;

	private final boolean isPasswordExpired;
	private final boolean isPasswordReset;

	/**
	 * @return the isPasswordExpired
	 */
	public boolean isPasswordExpired() {
		return isPasswordExpired;
	}

	/**
	 * @return the isPasswordReset
	 */
	public boolean isPasswordReset() {
		return isPasswordReset;
	}

	/**
	 * I hate so many constructor and parameters. There is only one constructor
	 * for new() UserSessionBean by UserSessionFactory. Easy way to maintain
	 * codes if devs want to add other properties. If you want to add properties
	 * into UserSessionBean, please modify SessionProperties.and Modify
	 * constructor.
	 * 
	 * @param userId
	 * @param username
	 * @param appPrefs
	 * @param mode
	 */
	UserSessionBean(String userId, String username, int mode,
			boolean isPasswordExpired, boolean isPasswordReset, String shardName) {
		this.username = username;
		this.userId = userId;
		this.mode = mode;
		this.isPasswordExpired = isPasswordExpired;
		this.isPasswordReset = isPasswordReset;
		this.shardName = shardName;
	}

	public String getUsername() {
		return username;
	}

	public String getUserId() {
		return userId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result;
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserSessionBean other = (UserSessionBean) obj;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return username + "(" + userId + ")";
	}

	@Override
	public String getShardName() {
		// TODO Auto-generated method stub
		return shardName;
	}
}
