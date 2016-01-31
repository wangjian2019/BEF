package alvin.bef.framework.base.query.hql.query;

/**
 * DSL-style select()...
 * 
 * @author Alvin
 */
public class HSelect {

   final HQueryInfo query;

   public HSelect(HQueryInfo query, Class<?> entityClass) {
      this.query = query;
      this.query.select = new String[] { entityClass.getName() };
   }

   public HSelect(HQueryInfo query, String name, String... names) {
      this.query = query;
      this.query.select = concat(name, names);
   }

   public HFrom from(String entity) {
      return new HFrom(query, entity);
   }

   public HFrom from(Class<?> entity) {
      return new HFrom(query, entity);
   }

   String[] concat(String name, String[] names) {
      if (names.length == 0) {
         return new String[] { name };
      }
      String[] arr = new String[names.length + 1];
      arr[0] = name;
      System.arraycopy(names, 0, arr, 1, names.length);
      return arr;
   }
}
