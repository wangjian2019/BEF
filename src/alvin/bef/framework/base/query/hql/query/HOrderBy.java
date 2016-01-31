package alvin.bef.framework.base.query.hql.query;

import java.util.List;

/**
 * DSL-style select().from().orderBy()...
 * 
 * @author Alvin
 */
public class HOrderBy {

   final HQueryInfo query;

   public HOrderBy(HQueryInfo query, String orderBy) {
      this.query = query;
      this.query.addOrderBy(orderBy);
   }

   public HOrderBy orderBy(String name) {
      return new HOrderBy(this.query, name);
   }

   public HOrderByDesc desc() {
      return new HOrderByDesc(this.query);
   }

   public HLimit limit(int offset, int maxResults) {
      return new HLimit(this.query, offset, maxResults);
   }

   public HLimit limit(int maxResults) {
      return new HLimit(this.query, maxResults);
   }

   /**
    * Get the first result, or null if results is empty.
    * 
    * @return The first result, or null.
    */
   public <T> T first() {
      return new HRunner(this.query).first();
   }

   /**
    * Get the results as list. An empty list is returned if result set is empty.
    * 
    * @return A type-safe list.
    */
   public <T> List<T> list() {
      return new HRunner(this.query).list();
   }

   /**
    * Get the results as list, but only retrieve the range defined by Page object.
    * 
    * @param page Page object.
    * @return A type-safe list, and Page object also was modified by set total records.
    */
   public <T> List<T> list(Page page) {
      return new HRunner(this.query).list(page);
   }

   /**
    * To HQL string for debug purpose.
    * 
    * @return HQL string.
    */
   public String toHql() {
      return new HRunner(this.query).toHql();
   }
}
