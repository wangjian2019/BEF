package alvin.bef.framework.base.dao.hibernate;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate3.HibernateCallback;

import alvin.bef.framework.base.dao.Limit;
import alvin.bef.framework.base.dao.ReadOnlyDAO;
import alvin.bef.framework.base.exception.BackEndException;
import alvin.bef.framework.base.model.SoftDelete;
import alvin.bef.framework.base.query.FSP;
import alvin.bef.framework.base.query.FilterFactory;
import alvin.bef.framework.base.query.IFilter;
import alvin.bef.framework.base.query.IGroupBy;
import alvin.bef.framework.base.query.IJoin;
import alvin.bef.framework.base.query.IPage;
import alvin.bef.framework.base.query.ISelector;
import alvin.bef.framework.base.query.ISort;
import alvin.bef.framework.base.query.InvalidFilterException;
import alvin.bef.framework.base.query.util.FilterUtil;
import alvin.bef.framework.base.session.UserSession;
import alvin.bef.framework.base.session.manager.SessionManager;

/**
 * 
 * @author Alvin
 *
 */
public class ReadOnlyDAOHibernate implements ReadOnlyDAO {
	private static String ID = "id";
	private static String FIELD_DELETED = "deleted";
	private ReadOnlyHibernateTemplate hibernateTemplate = null;
	private SessionManager sessionManager = null;

	/**
	 * the jdbc connection is readonly
	 */
	private JdbcTemplate jdbcTemplate;

	public void setHibernateTemplate(ReadOnlyHibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public void setSessionManager(SessionManager sessionManager) {
		this.sessionManager = sessionManager;
	}

	@SuppressWarnings("unchecked")
	public <T> T getObjects(final String queryString, final Long first,
			final Long max, final Map<?, ?> params) {
		return (T) hibernateTemplate.execute(new HibernateCallback<T>() {
			public T doInHibernate(Session session) {
				Query hqlQuery = session.createQuery(queryString);

				if (first != null && first.intValue() != 0)
					hqlQuery.setFirstResult(first.intValue());
				if (max != null)
					hqlQuery.setMaxResults(max.intValue());

				if (params != null)
					for (Object key : params.keySet()) {
						Object value = params.get(key);
						if (value instanceof Object[])
							hqlQuery.setParameterList(key.toString(),
									(Object[]) value);
						else if (key instanceof Integer)
							hqlQuery.setParameter((Integer) key, value);
						else
							hqlQuery.setParameter(key.toString(), value);
					}

				return (T) hqlQuery.list();
			}
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getObject(Class<T> entityClass, Serializable id) {
		// TODO Auto-generated method stub
		StringBuilder hql = new StringBuilder();
		hql.append(" FROM ").append(entityClass.getName()).append(" WHERE ")
				.append(ID).append("=? ").append("1").append("=1 ");
		if (hasSoftDelete(entityClass)) {
			hql.append(" AND ").append(FIELD_DELETED).append("=false");
		}
		UserSession userSession = sessionManager.getUserSession();
		List<T> objects = (List<T>) hibernateTemplate.find(hql.toString(),
				new Object[] { id }, null);
		switch (objects.size()) {
		case 0:
			return null;
		case 1:
			return objects.get(0);
		default:
			throw new BackEndException("More than one matching object.");
		}

	}

	@SuppressWarnings("unchecked")
	public <T> T find(final String hql, final Object[] parameters, Limit limit) {
		return (T) hibernateTemplate.find(hql, parameters, limit);
	}

	public <T> int getRecordCount(final Class<T> clazz, final IFilter filter,
			final IJoin join) {
		return hibernateTemplate.execute(new HibernateCallback<Integer>() {
			public Integer doInHibernate(Session session)
					throws HibernateException, SQLException {
				try {
					Query countQuery = DAOUtil.getCountQuery(session, clazz,
							null, join, appendStandardFilters(clazz, filter),
							false);
					DAOUtil.processFilterParameter(countQuery, null, filter);
					return ((Number) countQuery.uniqueResult()).intValue();
				} catch (InvalidFilterException e) {
					throw new HibernateException(e);
				}
			}
		});
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> getObjects(final Class<T> clazz, final IFilter filter,
			final ISort sort, final IPage page, final IJoin join) {
		List<T> ret = (List<T>) hibernateTemplate
				.execute(new HibernateCallback<Object>() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						return DAOUtil.processFind(session, clazz, join,
								appendStandardFilters(clazz, filter), sort,
								page);
					}
				});
		return ret;
	}

	public <T> List<T> getObjects(final Class<T> clazz, final FSP fsp) {
		IFilter filter = null;
		ISort sort = null;
		IPage page = null;
		if (fsp != null) {
			filter = fsp.getFilter();
			sort = fsp.getSort();
			page = fsp.getPage();
		}
		return getObjects(clazz, filter, sort, page, null);
	}

	@SuppressWarnings("unchecked")
	public <T> T listByQueryName(final String queryName,
			final String[] paramNames, final Object[] values, final Limit limit) {
		return (T) hibernateTemplate.findByNamedQueryAndNamedParam(queryName,
				paramNames, values, limit);
	}

	@SuppressWarnings("unchecked")
	public <T> T listByQueryName(final String queryName, final Object[] values,
			final Limit limit) {
		return (T) hibernateTemplate.findByNamedQuery(queryName, values, limit);
	}

	public <T> T execute(HibernateCallback<T> action) {
		return (T) hibernateTemplate.execute(action);
	}

	private IFilter appendStandardFilters(
			@SuppressWarnings("rawtypes") Class clazz, IFilter filter) {
		if (hasSoftDelete(clazz))
			filter = FilterUtil.and(filter,
					FilterFactory.getSimpleFilter(FIELD_DELETED, false));
		return filter;
	}

	@SuppressWarnings("rawtypes")
	private boolean hasSoftDelete(Class clazz) {
		return SoftDelete.class.isAssignableFrom(clazz);
	}

	@SuppressWarnings("unchecked")
	public <T> T listResults(final Class entityClass, final ISelector selector,
			final IFilter filter, final ISort sort, final IJoin join,
			final IGroupBy groupBy, final IPage page) {
		return hibernateTemplate.execute(new HibernateCallback<T>() {
			public T doInHibernate(Session session) throws HibernateException,
					SQLException {
				return (T) DAOUtil.processFind(session, entityClass, selector,
						join, appendStandardFilters(entityClass, filter), sort,
						groupBy, page);
			}
		});
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryForList(final String sql,
			final Object... params) {
		return jdbcTemplate.queryForList(sql, params);
	}

	@Override
	public <T, IJoin> int getRecordCount(Class<T> clazz, IFilter filter,
			IJoin join) {
		// Not implements
		return 0;
	}
}