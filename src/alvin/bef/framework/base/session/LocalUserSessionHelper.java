package alvin.bef.framework.base.session;

/**
 * @author Alvin
 *
 */
public interface LocalUserSessionHelper {
	public UserSession createUserSession(String userId);

	public UserSession recreateCompleteSession(String userId);

	public UserSession createUserSessionForJob(String userId,
			boolean createDefault);

	public UserSession completeGlobalSession(UserSession globalSession);

	public UserProfile getScheduledJobUser();

	public UserSession createSessionWithoutValidation(String userId);

	public final static String SCHEDULE_JOB_USER_ID = "3";

	public UserSession completeGlobalSession(UserSession globalSession,
			boolean refreshGlobalAttributes);

}
