package alvin.bef.framework.base.query.hql.query;

import org.springframework.orm.hibernate3.HibernateTemplate;

/**
 * Holds query info.
 * 
 * @author Alvin
 */
public class HQueryInfo {

   final HibernateTemplate hibernateTemplate;

   String[] select = null;

   String from = null;
   String fromAs = null;

   HJoinInfo[] joins = null;   

   String where = null;
   Object[] whereParams = null;

   HOrderByInfo[] orders = null;

   int offset = 0;
   int maxResults = 0;

   public HQueryInfo(HibernateTemplate hibernateTemplate) {
      this.hibernateTemplate = hibernateTemplate;
   }

   void addJoin(String joinType, String join, String joinAs) {
      if (joins == null) {
         joins = new HJoinInfo[1];
      }
      else {
         HJoinInfo[] newJoins = new HJoinInfo[joins.length];
         System.arraycopy(joins, 0, newJoins, 0, joins.length);
         joins = newJoins;
      }
      joins[joins.length-1] = new HJoinInfo(joinType, join, joinAs);
   }

   void setJoinAs(String joinAs) {
      joins[joins.length-1].joinAs = joinAs;
   }

   void addOrderBy(String orderBy) {
      if (orders == null) {
         orders = new HOrderByInfo[1];
      }
      else {
         HOrderByInfo[] newOrders = new HOrderByInfo[orders.length + 1];
         System.arraycopy(orders, 0, newOrders, 0, orders.length);
         orders = newOrders;
      }
      orders[orders.length-1] = new HOrderByInfo(orderBy);
   }

   void setOrderByDesc() {
      orders[orders.length-1].desc = true;
   }

}
