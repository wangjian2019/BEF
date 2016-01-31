package alvin.bef.framework.base.dao;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.springframework.beans.factory.annotation.Required;

/**
 * 
 * @author Alvin
 *
 */
public class ProxyQueryBaseDAO extends BaseDAO {
   private ProxyQueryFactory proxyQueryFactory;

   @Override
   protected Query hql(String hql) {
      return proxy(super.hql(hql));
   }

   @Override
   protected SQLQuery sql(String sql) {
      return proxy(super.sql(sql));
   }

   protected Query proxy(Query query) {
      return proxyQueryFactory.proxy(query);
   }

   protected SQLQuery proxy(SQLQuery query) {
      return proxyQueryFactory.proxy(query);
   }

   @Required
   public void setProxyQueryFactory(ProxyQueryFactory proxyQueryFactory) {
      this.proxyQueryFactory = proxyQueryFactory;
   }
}
