package alvin.bef.framework.base.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;

import alvin.bef.framework.base.dao.hibernate.ResultConverter;
import alvin.bef.framework.base.model.GeneratedIdAbstractAuditable;
import alvin.bef.framework.base.query.FSP;
import alvin.bef.framework.base.query.IFilter;
import alvin.bef.framework.base.query.IGroupBy;
import alvin.bef.framework.base.query.IJoin;
import alvin.bef.framework.base.query.IPage;
import alvin.bef.framework.base.query.ISelector;
import alvin.bef.framework.base.query.ISort;

/**
 * 
 * @author Alvin
 *
 */
public interface DAO {

    /**
     * Generic method used to get objects list of a particular type by filter.
     * 
     * @param clazz
     *            the type of objects (a.k.a. while class) to get data from
     * @param filter
     *            filter object used to query objects.
     * @return List of populated objects
     */
   public <T> List<T> getObjects(final Class<T> clazz, final IFilter filter, final ISort sort, final IPage page);

   public <T> List<T> getObjects(final Class<T> clazz, final IJoin join, final IFilter filter, final ISort sort, final IPage page);
   
   public <T> List<T> getObjects(final Class<T> clazz, final IJoin join, final IFilter filter, final ISort sort, final IPage page,final ISelector selector);
   
   public <T> List<T> getObjects(final Class<T> clazz, final IJoin join, final IFilter filter, final ISort sort, final IGroupBy groupBy, final IPage page,final ISelector selector);

    /**
     * Generic method used to get objects iterator of a particular type by
     * filter.
     * 
     * @param clazz
     *            the type of objects (a.k.a. while class) to get data from
     * @param filter
     *            filter object used to query objects.
     * @param sort
     *            sorting object.
     * @param page
     *            pagination object.
     * @return
     */
    public <T> Iterator<T> iterateObjects(final Class<T> clazz, final IFilter filter, final ISort sort, final IPage page);

    /**
     * Generic method used to get objects list of a particular type by FSP.
     * 
     * @param clazz
     *            the type of objects (a.k.a. while class) to get data from
     * @param fsp
     *            FSP object used to query objects.
     * @return
     */
    public <T> List<T> getObjects(final Class<T> clazz, final FSP fsp);

    /**
     * Generic method used to get objects iterator of a particular type by FSP.
     * 
     * @param clazz
     *            the type of objects (a.k.a. while class) to get data from
     * @param fsp
     *            FSP object used to query objects.
     * @return
     */
    public <T> Iterator<T> iterateObjects(final Class<T> clazz, final FSP fsp);

    /**
     * Generic method used to get objects iterator of a particular type by IJoin
     * and fsp.
     * 
     * @param clazz
     *            the type of objects to get data from
     * @param alias
     *            Alias that will be used for clazz
     * @param join
     *            IJoin object.
     * @param fsp
     *            FSP object used to query objects.
     * @return
     */
    public <T> Iterator<T> getObjects(Class<T> clazz, String alias, IJoin join, FSP fsp);
    public List<?> getObjects(final Class<?> entityClass, final String alias, final IJoin join, final IFilter filter, final ISort sort, final IPage page);

    /**
     * Generic method used to get all objects list of a particular type.
     * 
     * @param clazz
     *            the type of objects (a.k.a. while class) to get data from
     * @return
     */
    public <T> List<T> getObjects(Class<T> clazz);

    /**
     * Generic method used to get unique objects of a particular type. Be
     * careful with this method. If filter can not unique a object (multiple
     * object match the filter) User you get exception.
     * 
     * @param clazz
     *            the type of objects (a.k.a. while class) to get data from
     * @param filter
     *            IFilter object.
     * @return
     */
    public <T> T getUniqueObject(Class<T> clazz, final IFilter filter);

    /**
     * Generic method to get an object based on class and identifier. An
     * ObjectRetrievalFailureException Runtime Exception is thrown if nothing is
     * found.
     * 
     * @param clazz
     *            model class to lookup
     * @param id
     *            the identifier (primary key) of the class
     * @return a populated object
     * @see org.springframework.orm.ObjectRetrievalFailureException
     */
    public <T> T getObject(Class<T> clazz, Serializable id);
    
    /**
     * Generic method to save an object - handles both update and insert.
     * 
     * @param o
     *            the object to save
     */
    public void saveObject(Object o);

    public void updateObject(Object o);
    
    /**
     * Generic method to delete an object based on class and id
     * 
     * @param clazz
     *            model class to lookup
     * @param id
     *            the identifier (primary key) of the class
     */
    public <T> void removeObjectById(Class<T> clazz, Serializable id);
    
    /**
     * Delete the objects by id list.
     */
    public void removeObjectsByIds(Class<? extends GeneratedIdAbstractAuditable> clazz, List<Serializable> idList);
    
    /**
     * Delete the objects by id list.
     */
    public <T> void removeObjectsByPropertyValues(Class<T> clazz, List<Serializable> idList, String propertyName);
    

    /**
     * Generic method to delete an object
     * 
     * @param o
     *            the object to remove
     */
    public void removeObject(Object o);

    /**
     * Get identifer of object.
     * 
     * @param o
     * @return id of object
     */
    public Serializable getObjectId(Object o);
    <T> String getIdPropertyName(Class<T> clazz);

    /**
     * Check if an object is not exist in datasource.
     * 
     * @param o
     * @return
     */
    public boolean isNewObject(Object o);

    /**
     * Refresh object from datasource.
     * 
     * @param o
     */
    public void refreshObject(Object o);
    
    public <T> boolean isRecordExists(final Class<T> clazz, final IJoin join, final IFilter filter,final boolean cacheable);
    
    public <T> int getRecordCount(final Class<T> clazz, final IJoin join, final IFilter filter,final boolean cacheable);
    public <T> int getRecordCount(String hql, Object ... params);
    public <T> int getRecordCount(final String hql, final Map<String, ?> params);
    
    public List<?> initCollection(Collection<?> c ,String filterString,Object ... params);
    public Object getObjectsByQueryString(final String queryString,final boolean ifUnique,Long first,Long max,Object ...params);

    /**
     * public Object getObjectsByQueryStringNamedParameters(final String queryString,final boolean ifUnique,Long first,Long max,Map<String,?> params);
     * Use POSITIONAL_PARAMS_KEY as key to put an array into Map<String,?> to support hql positional parameters mixed named parameters. 
     */
    public static final String POSITIONAL_PARAMS_KEY="__POSITIONAL_PARAMS__KEY";
    public Object getObjectsByQueryStringNamedParameters(final String queryString,final boolean ifUnique,Long first,Long max,Map<?,?> params);
    public void evite(Object entity);
    public List<?> listResults(final Class<?> clazz, final ISelector selector, final IFilter filter, final ISort sort, final IGroupBy groupBy, final IPage page,final IJoin join);
    public List<?> listResults(final Class<?> clazz, final ISelector selector,final IFilter filter,final ISort sort,final IGroupBy groupBy,final IPage page);
    public Iterator<?> iterateResults(final Class<?> clazz, final ISelector selector,final IFilter filter,final ISort sort,final IGroupBy groupBy,final IPage page);
    public List<?> listByQueryName(final String queryName,final Object[] values,final Integer startIndex, final Integer pageAmount);
    public List<?> listByQueryName(final String queryName,final Map<String,?> params,final Integer startIndex, final Integer pageAmount);
    
    public void executeUpdate(String sql);
	public List<Object[]> executeQuery(String sql) ;
	public int executeUpdate(String sql, Object...objects);
	public List<Object[]> executeQuery(String sql, Object... objects) ;
	public int executeUpdatebyQueryName(final String queryName,final Object[] values);
	public int executeUpdatebyQueryName(final String queryName,final Object[] values,final Integer startIndex,final Integer pageAmount);
	public int executeUpdatebyQueryName(final String queryName,final Map<String,?> params);

    <T> void iterateObjects(Class<T> clazz, IFilter filter, ISort sort, Limit limit, ResultHandler<T> handler);
    void iterateResults(Class<?> clazz, ISelector selector, IFilter filter, IGroupBy groupBy, ISort sort, Limit limit, ResultHandler<Object[]> handler);
    void iterateByQueryName(String queryName, ResultHandler<Object[]> handler, Limit limit, Object... params);
    void iterateByQueryName(String queryName, ResultHandler<Object[]> handler, Limit limit, Map<String, ?> params);

    <T> void iterate(Query query, ResultConverter<T> converter, ResultHandler<T> handler);
    <T> void iterate(String hql, Map<String, ?> parameters, ResultConverter<T> converter, ResultHandler<T> handler);
    
    void clearCurrentSession();
    void flushCurrentSession();
    void lock(Object o);

   Object uniqueResultByQueryName(String queryName, Object... params);
   
   public Query createHibernateQuery(String queryString);
   
   public String convertHqlToSql(String hql);
   
   public <T> T getObjectForUpdate(Class<T> clazz, Serializable id);
   /**
    * we use this query as a heart-beat to keep long connection alive, 
    * if no action on connection for a long time the connection will be lost automatically
    * in most cases it will not occur but in b2b it might happen
    * @return
    */
   public java.sql.Timestamp getServerNowTime();
}
