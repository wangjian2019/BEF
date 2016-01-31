package alvin.bef.framework.base.session;

import org.hibernate.Session;
import org.springframework.stereotype.Component;

import alvin.bef.framework.base.model.ShardContext;

/**
 * 
 * @author Alvin
 *
 */
@Component
public class ShardSwitchValidator extends AbstractShardContextSwitchValidator{

   @Override
   public void validateAndDisconnectHibernateSessionIfNecessary(ShardContext currentContext, ShardContext newContext) {
      if (readyForCheck()) {
         
         boolean isStrictSameShard = currentContext != null && newContext != null 
                              && currentContext.getShardName() != null && newContext.getShardName() != null
                              && currentContext.getShardName().equals(newContext.getShardName());
         
         if (!isStrictSameShard) {
            if(existTransactionOnShard())
               handleValidateException("Can not switch shard context inside a transaction.");
            else{
               /**
                * Inside an openSessionInView, if there is not outside transaction on current session, 
                * then after a db statement been executed, a connection will combined with current session, 
                * the db connection will not been re-fetched after shard context switched in this case.
                * 
                * Manually disconnect the session here, it will release the connection if it has combined 
                * so that could make the connection been switched depends on the new shard context.
                * 
                * this operation should only happen when: shard context has changed and not transaction in current thread.
                */
               Session shardHibernateSession = getCurrentShardHibernateSession();
               if(shardHibernateSession != null)
                  shardHibernateSession.disconnect();
            }
         }
      }
   }
   
}
