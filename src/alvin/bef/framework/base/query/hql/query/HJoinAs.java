package alvin.bef.framework.base.query.hql.query;

import java.util.List;

/**
 * DSL-style select().from().innerJoin().as()...
 * 
 * @author Alvin
 */
public class HJoinAs {

   final HQueryInfo query;

   HJoinAs(HQueryInfo query, String joinAs) {
      this.query = query;
      this.query.setJoinAs(joinAs);
   }

   public HJoin innerJoin(String joinTable) {
      return new HJoin(this.query, "inner join", joinTable);
   }

   public HJoin leftJoin(String joinTable) {
      return new HJoin(this.query, "left join", joinTable);
   }

   public HJoin rightJoin(String joinTable) {
      return new HJoin(this.query, "right join", joinTable);
   }

   public HJoin leftOuterJoin(String joinTable) {
      return new HJoin(this.query, "left outer join", joinTable);
   }

   public HJoin rightOuterJoin(String joinTable) {
      return new HJoin(this.query, "right outer join", joinTable);
   }

   public HWhere where(String whereClause, Object... whereParams) {
      return new HWhere(this.query, whereClause, whereParams);
   }

   public HOrderBy orderBy(String name) {
      return new HOrderBy(this.query, name);
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
    * Get the unique result. 
    * Throw BeanNotExistException if results is empty. 
    * Throw NonUniqueBeanException if more than 1 results returned.
    * 
    * @return The unique result.
    */
   public <T> T unique() {
      return new HRunner(this.query).unique();
   }

   /**
    * Get the number of the results.
    * 
    * @return The number returned by 'count(*)'.
    */
   public int count() {
      return new HRunner(this.query).count();
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
