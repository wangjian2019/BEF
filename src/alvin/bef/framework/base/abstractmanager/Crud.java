package alvin.bef.framework.base.abstractmanager;

import java.io.Serializable;

/**
 * 
 * @author Alvin
 *
 * @param <T>
 */
public interface Crud<T> {
   /**
    * Create a new instance.
    * This does not actually store a record in the DB. You need to save() for that.
    */
   T create();

   /**
    * Delete an existing instance by id.
    */
   void delete(Serializable id);

   /**
    * Get an existing instance by id.
    * If not found, returns null.
    */
   T get(Serializable id);

   /**
    * Refresh an existing instance from the DB.
    */
   void refresh(T object);

   /**
    * Update an existing instance.
    */
   void update(T old, T object);

   /**
    * Save a new instance in the DB.
    * The ID is stored in the instance after the method returns.
    */
   void save(T object);
}
