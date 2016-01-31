package alvin.bef.framework.base.query.hql.query;

/**
 * Holds orderby info.
 * 
 * @author Alvin
 */
class HOrderByInfo {

   final String orderBy;
   boolean desc = false;

   HOrderByInfo(String orderBy) {
      this.orderBy = orderBy;
   }

   public String toString() {
      return desc ? (orderBy + " desc") : orderBy;
   }
}
