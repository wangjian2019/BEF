package alvin.bef.framework.base.query.hql.query;

/**
 * Start a DSL-style query.
 * 
 * @author Alvin
 */
public class HQuery {

   final HQueryInfo query;

   public HQuery(HQueryInfo query) {
      this.query = query;
   }

   public HSelect select(String entity, String... names) {
      return new HSelect(this.query, entity, names);
   }

   /**
    * start HQL with:
    *   from ...
    * 
    * @param entity Entity name.
    * @return Chained object.
    */
   public HFrom from(String entity) {
      return new HFrom(this.query, entity);
   }

   /**
    * start HQL with:
    *   from class ...
    * 
    * @param entity Entity class.
    * @return Chained object.
    */
   public HFrom from(Class<?> entity) {
      return new HFrom(this.query, entity);
   }

}
