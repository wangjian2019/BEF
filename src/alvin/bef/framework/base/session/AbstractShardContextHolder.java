package alvin.bef.framework.base.session;

import org.springframework.beans.factory.annotation.Autowired;

import alvin.bef.framework.base.exception.ExceptionUtils;
import alvin.bef.framework.base.executor.Executable;
import alvin.bef.framework.base.model.ShardContext;

/**
 * @author Alvin
 *
 */
public abstract class AbstractShardContextHolder implements ShardContextHolder {

	private final ThreadLocal<ShardContext> localShardContext = new ThreadLocal<ShardContext>();

	@Autowired
	private ShardSwitchValidator shardSwitchValidator;

	@Override
	final public ShardContext getShardContext() {
		return localShardContext.get();
	}

	/**
	 * only used for some specific Junit test cases, NEVER call this from
	 * non-test code.
	 */
	final public void setShardContextForTesting(ShardContext shardContext) {
		setShardContext(shardContext);
	}

	final protected void setShardContext(ShardContext shardContext) {
		shardSwitchValidator.validateAndDisconnectHibernateSessionIfNecessary(
				localShardContext.get(), shardContext);
		localShardContext.set(shardContext);
	}

	/**
	 * Runs the executable logic with the shard context being passed in. It
	 * restores the original shard context on returning from the method.
	 * 
	 * @param <T>
	 * @param shardName
	 * @param context
	 * @param executable
	 * @return
	 */
	final public <T> T runWithShard(final String shardName, String context,
			Executable<T> executable) {
		final ShardContext oldShardContext = getShardContext();
		try {
			setShardContext(new ShardContext() {
				@Override
				public String getShardName() {
					return shardName;
				}
			});
			return executable.execute();
		} catch (Throwable e) {
			throw ExceptionUtils.wrap(e);
		} finally {
			setShardContext(oldShardContext);
		}
	}

	final public <T> T runOnShard(final String shardName, String context,
			Executable<T> executable) {
		try {
			return runWithShard(shardName, context, executable);
		} catch (Throwable e) {
			throw ExceptionUtils.wrap(e);
		}
	}
}
