package alvin.bef.framework.base.session;


public interface SessionSynchronization {
	/**
	 * The session synchronization type.
	 * @return
	 */
	public SessionSynchronizationType getType();
	
	/**
	 * Synchronizes after the session starts.
	 */
	public void afterStart();
	
	/**
	 * Synchronizes before the session completes.
	 */
	public void beforeCompletion(); 
}
