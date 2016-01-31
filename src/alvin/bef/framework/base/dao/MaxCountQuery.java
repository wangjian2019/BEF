package alvin.bef.framework.base.dao;

import org.hibernate.HibernateException;
import org.hibernate.Query;

/**
 * 
 * @author Alvin
 *
 */
public class MaxCountQuery extends ProxyQuery {

   private final Long maxCount;
   
   public MaxCountQuery(Query query, long maxCount) {
      super(query);
      this.maxCount = maxCount;
   }

   @Override
   public Object uniqueResult() throws HibernateException {
      Long result = (Long)super.uniqueResult();
      return result > maxCount ? maxCount : result;
   }

}
