package alvin.bef.framework.base.session.manager;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;

import alvin.bef.framework.base.exception.ExceptionUtils;
import alvin.bef.framework.base.executor.Executable;
import alvin.bef.framework.base.executor.Executor;
import alvin.bef.framework.base.model.ShardContext;
import alvin.bef.framework.base.session.AbstractShardContextHolder;
import alvin.bef.framework.base.session.SessionSynchronizationManager;
import alvin.bef.framework.base.session.UserSession;

/**
 * Manages thread local user sessions.
 * 
 * @author Alvin
 */
public class SessionManager extends AbstractShardContextHolder {
	private static final Logger logger = Logger.getLogger(SessionManager.class);

	private final ThreadLocal<UserSession> tlUserSession = new ThreadLocal<UserSession>();
	private final ThreadLocal<String> baseUrl = new ThreadLocal<String>();
	@Value("${global.base.url}")
	private String globalBaseUrl;

	/**
	 * If the globalBaseUrl is not defined, we will take it from the
	 * sessionManager. This is primarily for testing. Right now we don't see a
	 * reason not to set the globalBaseUrl in production.
	 * 
	 * Since baseUrl is set in initialFilter, don't call it in batch process
	 * which runs on batch server.
	 */
	public final String getBaseUrl() {
		return (globalBaseUrl == null || "".equals(globalBaseUrl)) ? baseUrl
				.get() : globalBaseUrl;
	}

	public void setBaseUrl(String url) {
		baseUrl.set(url);
	}

	/**
	 * Called only from within unit tests when the normal session management
	 * mechanism is not practical. NEVER call this from non-test code.
	 */
	public final void setUserSessionForTesting(UserSession userSession) {
		setUserSession(userSession, userSession, "TESTING!");
	}

	private void setUserSession(UserSession userSession,
			ShardContext shardContext, String message) {
		UserSession oldUserSession = getUserSession();
		tlUserSession.set(userSession);
		if (logger.isDebugEnabled()) {
			String logMessage = String.format(
					"User session changed from %s to %s. Details: %s",
					oldUserSession != null ? oldUserSession : "[anonymous]",
					userSession != null ? userSession : "[anonymous]", message);
			logger.debug(logMessage);
		}
	}

	/**
	 * Get associated UserSession instance from current thread
	 */
	public UserSession getUserSession() {
		return tlUserSession.get();
	}

	/**
	 * Run some code as the specified UserSession. If there is already a
	 * UserSession associated with the current thread, then it will be switched
	 * temporarily while this method executes, then restored when it finishes.
	 * 
	 * @param <T>
	 *            The return type of this method and of the passed-in Executable
	 *            instance. If you don't need to return a value, then just
	 *            specify Object and return null.
	 * @param userSession
	 *            The UserSession to run with.
	 * @param context
	 *            A description of why the session is being switched. This will
	 *            be logged.
	 * @param executable
	 *            The code to execute as the given UserSession.
	 * @return Whatever the passed-in Executable returns.
	 * @throws RuntimeException
	 *             If the Executable throws a checked exception, it will be
	 *             wrapped in RuntimeException and rethrown. If the Executable
	 *             throws an unchecked exception, then it will be allowed to
	 *             bubble up unchanged.
	 */
	public <T> T runWithUserSession(UserSession userSession, String context,
			Executable<T> executable) {
		final UserSession oldSession = getUserSession();
		final ShardContext oldShardContext = getShardContext();
		try {
			setUserSession(userSession, userSession, "Begin: " + context);
			T result = executable.execute();
			SessionSynchronizationManager.beforeCompletion();
			return result;
		} catch (Throwable t) {
			throw ExceptionUtils.wrap(t);
		} finally {
			setUserSession(oldSession, oldShardContext,
					"End: " + context);
		}
	}

	public final Executor getExecutor(final String context) {
		final UserSession userSession = getUserSession();
		return new Executor() {
			public <T> T execute(Executable<T> executable) {
				return runWithUserSession(userSession, context, executable);
			}
		};
	}
}