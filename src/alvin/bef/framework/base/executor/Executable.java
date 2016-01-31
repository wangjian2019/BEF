package alvin.bef.framework.base.executor;

import alvin.bef.framework.base.session.UserSession;
import alvin.bef.framework.base.session.manager.SessionManager;


/**
 * Generic interface for a block of executable code that can return an arbitrary value and throw an arbitrary exception.
 * Usually used as an anonymous inner class passed into {@link SessionManager#runWithUserSession(UserSession, String, Executable)}
 * @author Alvin
 *
 * @param <T>
 */
public interface Executable<T> {
    /**
     * Execute some code.
     */
	T execute() throws Exception;
}
