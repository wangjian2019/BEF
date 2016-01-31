package alvin.bef.framework.base.session;


/**
 * 
 * @author Alvin
 *
 */
public abstract class UserSessionFactory {

	public static int NUM_OF_PROPERTIES = 9;
	public static int PRE_TZ_NUM_OF_PROPERTIES = 8;

	/**
	 * 
	 * 
	 * @param userId
	 * @param username
	 * @param appPerms
	 * @param appPrefs
	 * @param locale
	 * @param mode
	 */

	/**
	 * Get SessionSession, set parameters that you want.
	 * 
	 * @param SessionProperties
	 *            prop, Use UserSessionFactory.getSessionProperties()
	 */
	public static UserSession getUserSession(SessionProperties prop) {
		return new UserSessionBean(prop.getUserId(), prop.getUsername(),
				prop.getMode(), prop.isPasswordExpired(),
				prop.isPasswordReset(), prop.getShardName());
	}

	public static SessionProperties getSessionProperties() {
		return SessionProperties.getSessionProperties();
	}

	public static class SessionProperties {
		private SessionProperties() {
		}

		/**
		 * @return
		 */

		private String userId;
		private String username;
		private int mode;
		private String shardName;

		public SessionProperties setUserId(String userId) {
			this.userId = userId;
			return this;
		}

		public SessionProperties setUsername(String username) {
			this.username = username;
			return this;
		}

		public SessionProperties setMode(int mode) {
			this.mode = mode;
			return this;
		}

		public SessionProperties setShardName(String shardName) {
			this.shardName = shardName;
			return this;
		}

		private String getUserId() {
			return userId;
		}

		private String getUsername() {
			return username;
		}

		private int getMode() {
			return mode;
		}

		public String getShardName() {
			return shardName;
		}

		private boolean isPasswordExpired;
		private boolean isPasswordReset;

		/**
		 * @return the isPasswordExpired
		 */
		public boolean isPasswordExpired() {
			return isPasswordExpired;
		}

		/**
		 * @param isPasswordExpired
		 *            the isPasswordExpired to set
		 */
		public SessionProperties setPasswordExpired(boolean isPasswordExpired) {
			this.isPasswordExpired = isPasswordExpired;
			return this;
		}

		/**
		 * @return the isPasswordReset
		 */
		public boolean isPasswordReset() {
			return isPasswordReset;
		}

		/**
		 * @param isPasswordReset
		 *            the isPasswordReset to set
		 */
		public SessionProperties setPasswordReset(boolean isPasswordReset) {
			this.isPasswordReset = isPasswordReset;
			return this;
		}

		private static SessionProperties getSessionProperties() {
			return new SessionProperties();
		}
	}

	public static String[] getUserSessionPropertyValues(
			UserSession userSession) {

		String permString = "";
		String[] returnArray;
		returnArray = new String[] { userSession.getUserId(),
				Boolean.toString(userSession.isPasswordExpired()),
				Boolean.toString(userSession.isPasswordReset()),
				userSession.getShardName(), permString };

		if (returnArray.length != NUM_OF_PROPERTIES) {
			throw new Error(
					"Not all user session properties are put into the value array, please fix the code");
		}
		return returnArray;
	}

}
