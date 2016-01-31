package alvin.bef.framework.base.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.springframework.orm.hibernate3.HibernateCallback;

import alvin.bef.framework.base.query.FSP;
import alvin.bef.framework.base.query.IFilter;
import alvin.bef.framework.base.query.IGroupBy;
import alvin.bef.framework.base.query.IJoin;
import alvin.bef.framework.base.query.IPage;
import alvin.bef.framework.base.query.ISelector;
import alvin.bef.framework.base.query.ISort;

/**
 * read data from read only database.
 * 
 * @author Alvin
 *
 */
public interface ReadOnlyDAO {

	/**
	 *  soft delete filter
	 * 
	 * @param <T>
	 * @param entityClass
	 * @param id
	 * @return
	 */
	<T> T getObject(Class<T> entityClass, Serializable id);

	/**
	 * soft delete filter
	 * 
	 * @param <T>
	 * @param <IJoin>
	 * @param clazz
	 * @param filter
	 * @param join
	 * @return
	 */
	<T, IJoin> int getRecordCount(final Class<T> clazz, final IFilter filter,
			final IJoin join);

	/**
	 * soft delete filter
	 * 
	 * @param <T>
	 * @param clazz
	 * @param filter
	 * @param sort
	 * @param page
	 * @param join
	 * @return
	 */
	<T> List<T> getObjects(final Class<T> clazz, final IFilter filter,
			final ISort sort, final IPage page, final IJoin join);

	/**
	 * soft delete filter
	 * 
	 * @param <T>
	 * @param clazz
	 * @param fsp
	 * @return
	 */
	<T> List<T> getObjects(final Class<T> clazz, final FSP fsp);

	/**
	 * the soft delete filter in your hql if
	 * it is necessary.
	 * 
	 * @param <T>
	 * @param hql
	 * @param parameters
	 * @param limit
	 * @return
	 */
	<T> T find(final String hql, final Object[] parameters, Limit limit);

	/**
	 * the soft delete filter in your hql if
	 * it is necessary.
	 * 
	 * @param <T>
	 * @param queryString
	 * @param first
	 * @param max
	 * @param params
	 * @return
	 */
	<T> T getObjects(final String hql, final Long first, final Long max,
			final Map<?, ?> params);

	/**
	 * the soft delete filter in your query
	 * if it is necessary.
	 * 
	 * @param <T>
	 * @param queryName
	 * @param paramNames
	 * @param values
	 * @param limit
	 * @return
	 */
	<T> T listByQueryName(final String queryName, final String[] paramNames,
			final Object[] values, final Limit limit);

	/**
	 * the soft delete filter in your query
	 * if it is necessary.
	 * 
	 * @param <T>
	 * @param queryName
	 * @param values
	 * @param limit
	 * @return
	 */
	<T> T listByQueryName(final String queryName, final Object[] values,
			final Limit limit);

	<T> T execute(HibernateCallback<T> action);

	/**
	 * 
	 * @param <T>
	 * @param entityClass
	 * @param selector
	 * @param filter
	 * @param sort
	 * @param join
	 * @param groupBy
	 * @param page
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T listResults(Class entityClass, ISelector selector,
			IFilter filter, ISort sort, IJoin join, IGroupBy groupBy,
			IPage page);

	/**
	 * only for using the Spring jdbcTemplate.queryForList in RO DB.  the soft delete filter in your query if it is
	 * necessary.
	 * 
	 * @param sql
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> queryForList(final String sql,
			final Object... params);
}
