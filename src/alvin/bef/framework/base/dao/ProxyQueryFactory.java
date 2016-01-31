package alvin.bef.framework.base.dao;

import org.hibernate.Query;
import org.hibernate.SQLQuery;

/**
 * 
 * @author Alvin
 *
 */
public interface ProxyQueryFactory {
   Query proxy(Query query);
   SQLQuery proxy(SQLQuery query);
}
