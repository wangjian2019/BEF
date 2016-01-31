package alvin.bef.framework.base.abstractmanager;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import alvin.bef.framework.base.dao.ResultHandler;
import alvin.bef.framework.base.query.FSP;
import alvin.bef.framework.base.query.IFilter;
import alvin.bef.framework.base.query.IGroupBy;
import alvin.bef.framework.base.query.IJoin;
import alvin.bef.framework.base.query.IPage;
import alvin.bef.framework.base.query.ISelector;
import alvin.bef.framework.base.query.ISort;
import alvin.bef.framework.base.query.hql.query.HQuery;
import alvin.bef.framework.base.query.impl.PageInfoExt;

/**
 * 
 * @author Alvin
 *
 * @param <T>
 */
public interface AbstractManager<T> extends Crud<T> {

   /**
    * Advanced DSL-style query interface for HQL. Example:
    * 
    * List<User> list = aManager.query()
    *                   .select("u")
    *                   .from(User.class).as("u")
    *                   .innerJoin("u.books").as("b")
    *                   .where("b.publish>? and u.type=?", 20150101, "AUTHOR")
    *                   .orderBy("u.name").desc()
    *                   .orderBy("b.id")
    *                   .list();
    * @return A continue-able object to complete the HQL.
    */
   public HQuery query();

    /**
     * For example:
     * 
     * List<BillingAccount> accounts = simpleQuery("from BillingAccount where created_at > ?", 12345678);
     * 
     * @param queryString HQL query string.
     * @param params The parameters which matches the position of '?' in HQL.
     * @return
     */
    public <K> List<K> simpleQuery(String queryString, Object... params);

    /**
     * For example:
     * 
     * // page 1, 20 records per page:
     * PageInfoExt page = new Page(1, 20);
     * List<BillingAccount> accounts = simplePageQuery("from BillingAccount where created_at > ? and isDeleted = ?", page, 12345678, false);
     * // after query returns, page object was set with totalRecords and totalPages:
     * page.totalRecords() // 1230
     * page.totalPages() // 62
     * 
     * @param queryString HQL query string.
     * @param page A PageInfoExt object with currentPage and itemsPerPage set.
     * @param params The parameters which matches the position of '?' in HQL.
     * @return
     */
    public <K> List<K> simplePageQuery(String queryString, PageInfoExt page, Object... params);

    /**
     * Get the class that is managed by this manager.
     */
    public Class<T> getEntityClass();
    
    /**
     * Get list of objects based on the given custom filter.
     * Soft-deleted are automatically included when applicable.
     */
    public List<T> getObjects(IFilter filter);
    
    /**
     * Get list of soft-deleted objects based on the given custom filter.
     * If the managed entity type does not support soft-delete, then this will always return empty list.
     */
    public List<T> getDeletedObjects(IFilter filter);

    /**
     * Get list of objects based on the given custom filter and sort.
     */
    public List<T> getObjects(IFilter filter,ISort sort);

    /**
     * Get list of objects based on the given custom filter and join.
     */
    public List<T> getObjects(IFilter filter, IJoin join);
    
    /**
     * Get list of objects based on the given custom filter, sort, and pagination.
     */
    public List<T> getObjects(IFilter filter,ISort sort,IPage page);
    
    /**
     * Get list of soft-deleted objects based on the given custom filter and sort.
     * If the managed entity type does not support soft-delete, then this will always return empty list.
     */
    public List<T> getDeletedObjects(IFilter filter, ISort sort);

    /**
     * Iterate objects based on the given custom filter. 
     */
    public Iterator<T> iterateObjects(IFilter filter);

    /**
     * Iterate objects based on the given custom filter and sort. 
     */
    public Iterator<T> iterateObjects(IFilter filter,ISort sort);
    
    /**
     * Get list of objects based on the given custom filter.
     * Soft-deleted filter is automatically included when applicable.
     */
    public List<T> getNoMultiObjects(IFilter filter);
    public List<T> getNoMultiObjects(IFilter filter,ISort sort);
    public List<T> getNoMultiObjects(IFilter filter,ISort sort, IPage page);
    
    /**
     * Iterate objects based on the given custom filter.
     * Soft-deleted filter is automatically included when applicable.
     */
    public Iterator<T> iterateNoMultiObjects(IFilter filter);
    
    /**
     * Get an object's identifier.
     */
    public Serializable getObjectId(T o);

    /**
     * Get list of objects based on the given custom FSP (filter, sort, and pagination).
     * This method is exactly the same as {@link #getObjects(IFilter, ISort, IPage)
     */
    public List<T> getObjects(FSP fsp);

    /**
     * Iterate objects based on the given custom FSP (filter, sort, and pagination).
     * This method is exactly the same as {@link #iterateObjects(IFilter, ISort, IPage)
     */
    public Iterator<T> iterateObjects(FSP fsp);
    
    public List<?> getNoMultiPageObjects(IJoin join, FSP fsp);

    public List<?> getPageObjects(IJoin join, FSP fsp);
    /**
     * Get a list of all the objects in the database.
     */
    public List<T> getObjects();

    /**
     * Get a single object based on the given custom filter.
     * @return If one object is found, that object. If no objects are found, then null.
     * @throws GenericException More than one object was found.
     */
    public T getObject(IFilter filter);

    /**
     * Get the =object with the given id.
     * @return The object if found; null otherwise.
     */
    public T getObject(Serializable id);
    
    /**
     * Get a single object based on the given custom filter.
     * Soft-deleted filter is automatically included when applicable.
     * @return If one object is found, that object. If no objects are found, then null.
     * @throws GenericException More than one object was found.
     */
    T getNoMultiObject(IFilter filter);

    /**
     * Get the object with the given id.
     * Soft-deleted filter is automatically included when applicable.
     * @return The object if found; null otherwise.
     */
    public T getNoMultiObject(Serializable id);

    /**
     * Save or update an object.
     * 
     * @param saving
     *            the object to save
     */
    public void saveObject(T saving);
   
    /**
     * Save or update an object, given the old object it replaces.
     * 
     * @param saving
     *            the object to save
     */
    public void saveObject(T old, T saving);

    /**
     * Delete an object based on id
     * 
     * @param id
     *            the identifier of the class
     */
    public void removeObjectById(Serializable id);

    /**
     * Delete an object.
     * 
     * @param o The object to remove.
     */
    public void removeObject(T o);
    public void removeObject(T o, boolean forceHard);
    
    /**
     * Delete an object.
     * 
     * @param o
     */
    public void removeNoMultiObject(T o);
    public void removeNoMultiObject(T o, boolean forceHard);
    
    /**
     * Delete several objects based on id
     * 
     * @param ids
     *            the identifier of the class
     */
    public void removeObjectsById(List<Serializable> ids);
    public void removeObjects(List<T> objects);

    /**
     * Refresh an object.
     * 
     * @param o
     *            the object to refresh
     */
    public void refreshObject(T o);

    public boolean isNewObject(T o);

    
    /**
     * Create a new instance of entity class.
     * Some fields of the returned object may already be initialized.
     * 
     * @return
     */
    public T createNewObject();

    
    /**
     * @param filter
     * @param join
     * @return 0 if empty set, otherwise greater then 0.
     */
    public boolean isRecordExists(IFilter filter,IJoin join);
    
    /**
     * Get the number of objects that match the given filter, join.
     * @param filter
     * @param join
     * @return 0 if empty set, otherwise greater then 0.
     */
    public int getRecordCount(IFilter filter,IJoin join);
    
    public int getRecordCount(String hql, Object ... params);
    
    public int getRecordCount(String hql, Map<String, ?> params);
    
    public List<?> initCollection(Collection<?> c, String queryString, Object... params);
    
    public Object getObjectsByQueryString(String query, boolean isUnique, Long first, Long max, Object... params);
    
    public Object getObjectsByQueryStringNamedParameters(String query, boolean isUnique, Long first, Long max, Map params);
    
    public boolean hasSoftDelete();

    public void evite(Object entity);
    public RuntimeException reMapException(RuntimeException ex);

    public Object getEnumerationObjectByIdentifier(Object id);
   public boolean isDatabaseEnum();

   public List<T> getObjectsById(Serializable... ids);

   public List<?> listResults(ISelector selector,IFilter filter,ISort sort,IGroupBy groupBy,IPage page);
   public List<?> listResults(ISelector selector, IFilter filter, ISort sort, IGroupBy groupBy, IPage page,IJoin join);
   public Iterator<?> iterateResults(ISelector selector,IFilter filter,ISort sort,IGroupBy groupBy,IPage page);

   List<T> getNoMultiObjects(FSP fsp);
   public List<?> listByQueryName(final String queryName,final Object[] values,final Integer startIndex, final Integer pageAmount);
   public List<?> listByQueryName(final String queryName,final Map<String,?> params,final Integer startIndex, final Integer pageAmount);

    void iterateObjects(IFilter filter, ISort sort, ResultHandler<T> resultHandler);
    void iterateObjects(IFilter filter, ResultHandler<T> resultHandler);
    void iterateObjects(ResultHandler<T> resultHandler);
    
    void lock(T entity);
    /**
     * Get the un-changed object.
     * This method is useful to compare the changed-object in current session and the original object in DB. 
     * This method will be executed in a new transaction.
     * @param id
     * @return
     */
    T getOldObject(Serializable id);
    T getOldObject(Serializable id, Boolean filterDeleted);
    int executeUpdate(final String queryName,final Object[] values);
    void executeUpdate(final String queryName, Map<String, Object> params);
    
   public Object getOriginalPropValues(Serializable id, String... propNames);
   
   /**
    * we use this query as a heart-beat to keep long connection alive, 
    * if no action on connection for a long time the connection will be lost automatically
    * in most cases it will not occur but in b2b it might happen
    * @return
    */
   public java.sql.Timestamp getServerNowTime();
}
