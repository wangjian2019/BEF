package alvin.bef.framework.base.query.hql.query;

import java.util.List;

/**
 * DSL-style select().from().limit(10, 100)...
 * 
 * @author Alvin
 */
public class HLimit {

   final HQueryInfo query;

   HLimit(HQueryInfo query, int offset, int maxResults) {
      if (offset < 0) {
         throw new IllegalArgumentException("Invalid offset: " + offset);
      }
      if (maxResults < 1) {
         throw new IllegalArgumentException("Invalid maxResults: " + maxResults);
      }
      this.query = query;
      this.query.offset = offset;
      this.query.maxResults = maxResults;
   }

   HLimit(HQueryInfo query, int maxResults) {
      this(query, 0, maxResults);
   }

   /**
    * Get the results as list. An empty list is returned if result set is empty.
    * 
    * @return A type-safe list.
    */
   public <T> List<T> list() {
      return new HRunner(this.query).list();
   }

}
