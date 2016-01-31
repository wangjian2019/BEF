package alvin.bef.framework.base.session;

import alvin.bef.framework.base.executor.Executable;
import alvin.bef.framework.base.model.ShardContext;



/**
 * 
 * @author Alvin
 *
 */
public interface ShardContextHolder {
   ShardContext getShardContext();
   <T> T runWithShard(final String shardName, String context, Executable<T> executable);
}
