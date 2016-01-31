package alvin.bef.framework.base.session;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

/**
 * 
 * @author Alvin
 *
 */
public class SessionSynchronizationManager {

	private static final Logger logger = Logger.getLogger(SessionSynchronizationManager.class);

	private static final ThreadLocal<Map<SessionSynchronizationType, SessionSynchronization>> synchronizations = new ThreadLocal<Map<SessionSynchronizationType, SessionSynchronization>>();
	private static final ThreadLocal<Set<SessionSynchronizationType>> synchronizationInterests = new ThreadLocal<Set<SessionSynchronizationType>>();

	public static void registerSynchronization(SessionSynchronization sessionSync) {
		getSynchronizations().put(sessionSync.getType(), sessionSync);
	}

	public static void setIsRunningTest(Boolean value) {
		isRunningTest.set(value);
	}

	public static boolean isRunningTest() {
		return isRunningTest.get() != null && isRunningTest.get().booleanValue();
	}

	public static Map<SessionSynchronizationType, SessionSynchronization> getSynchronizations() {
		if (synchronizations.get() == null) {
			synchronizations.set(new HashMap<SessionSynchronizationType, SessionSynchronization>());
		}
		return synchronizations.get();
	}

	public static void registerInterest(SessionSynchronizationType sessionSyncType) {
		getInterests().add(sessionSyncType);
	}

	private static boolean hasInterest(SessionSynchronizationType sessionSyncType) {
		return getInterests().contains(sessionSyncType);
	}

	private static Set<SessionSynchronizationType> getInterests() {
		if (synchronizationInterests.get() == null) {
			synchronizationInterests.set(new HashSet<SessionSynchronizationType>());
		}
		return synchronizationInterests.get();
	}

	public static void clearInterests() {
		getInterests().clear();
	}

	public static void afterStart() {
		try {
			Map<SessionSynchronizationType, SessionSynchronization> sessionSyncsMap = getSynchronizations();
			for (SessionSynchronizationType syncType : sessionSyncsMap.keySet()) {
				SessionSynchronization sessionSync = sessionSyncsMap.get(syncType);
				sessionSync.afterStart();
			}
		}
		catch (Throwable t) {
			logger.error("An error occurred while processing afterStart session event", t);
		}
	}

	public static void beforeCompletion() {
		try {
			if (getInterests().size() != 0) {
				Map<SessionSynchronizationType, SessionSynchronization> sessionSyncsMap = getSynchronizations();
				for (SessionSynchronizationType syncType : sessionSyncsMap.keySet()) {
					if (hasInterest(syncType)) {
						SessionSynchronization sessionSync = sessionSyncsMap.get(syncType);
						sessionSync.beforeCompletion();
					}
				}
			}
		}
		catch (Throwable t) {
			logger.error("An error occurred while processing beforeCompletion session event", t);
		}
		finally {
			clearInterests();
		}
	}
	
	/********************
	 * For Testing Only *
	 ********************/
	public static final Object TestLock = new Object();
	private static final ThreadLocal<Boolean> isRunningTest = new ThreadLocal<Boolean>();
	static {
		isRunningTest.set(false);
	}
}
