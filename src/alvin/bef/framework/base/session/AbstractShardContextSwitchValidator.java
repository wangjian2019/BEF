package alvin.bef.framework.base.session;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.SessionHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.Assert;

import alvin.bef.framework.base.spring.SpringHelper;

/**
 * @author Alvin
 *
 */
public abstract class AbstractShardContextSwitchValidator implements IShardContextSwitchValidator {
   
   protected final Log log = LogFactory.getLog(getClass());

   private SessionFactory shardedSessionFactory;
   //private ServletNotification notification;
   
   protected boolean readyForCheck(){
      if(shardedSessionFactory == null && SpringHelper.isInit()){
         shardedSessionFactory = (SessionFactory)SpringHelper.getBean("knSessionFactory");//have to get it by SpringHelper to prevent circalur reference.
         Assert.notNull(shardedSessionFactory, "Configration error: can not find bean by id 'knSessionFactory'.");
      }
      return shardedSessionFactory!=null;
   }
   
   protected boolean existTransactionOnShard(){
      SessionHolder sessionHolder = (SessionHolder) TransactionSynchronizationManager.getResource(shardedSessionFactory);
      return (sessionHolder != null && sessionHolder.getTransaction() != null);
   }
   
   protected Session getCurrentShardHibernateSession(){
      SessionHolder sessionHolder = (SessionHolder) TransactionSynchronizationManager.getResource(shardedSessionFactory);
      return sessionHolder != null ?  sessionHolder.getSession() : null;
   }
   
   protected void handleValidateException(String errorMessage){
      RuntimeException exception = new RuntimeException(errorMessage);
//      if(notifyWithoutError())
//         sendNotification(errorMessage, ExceptionUtils.getRootCauseStackTrace(exception));
//      else
//         throw exception;
   }
   
   /**
    * in R142 it will always return true to unblock some existing non-multi-shard compatible code
    * @return
    */
   private boolean notifyWithoutError(){
      return false;
   }
   
//   private void sendNotification(String summary, String detail){
//      if(notification == null)//using springHelper to aviod circlar reference during appciation startup, don't change it.
//         notification = (ServletNotification)SpringHelper.getBean("notification");
//      
//      log.error(detail);
//      if(notification != null)
//         notification.sendNotification(summary, detail);
//   }
}
