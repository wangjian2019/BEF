package alvin.bef.framework.base.session;

import alvin.bef.framework.base.model.ShardContext;

/**
 * @author Alvin
 *
 */
public interface IShardContextSwitchValidator {
   void validateAndDisconnectHibernateSessionIfNecessary(ShardContext currentContext, ShardContext newContext);
}
