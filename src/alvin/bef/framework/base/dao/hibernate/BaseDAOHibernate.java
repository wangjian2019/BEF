package alvin.bef.framework.base.dao.hibernate;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.hql.internal.ast.ASTQueryTranslatorFactory;
import org.hibernate.hql.spi.QueryTranslator;
import org.hibernate.hql.spi.QueryTranslatorFactory;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.orm.hibernate4.SessionFactoryUtils;

import alvin.bef.framework.base.annotation.SessionSynchronizationInterest;
import alvin.bef.framework.base.dao.DAO;
import alvin.bef.framework.base.dao.Limit;
import alvin.bef.framework.base.dao.ResultHandler;
import alvin.bef.framework.base.dao.ScrollUtil;
import alvin.bef.framework.base.exception.BackEndException;
import alvin.bef.framework.base.model.GeneratedIdAbstractAuditable;
import alvin.bef.framework.base.model.GeneratedIdEntity;
import alvin.bef.framework.base.query.FSP;
import alvin.bef.framework.base.query.IFilter;
import alvin.bef.framework.base.query.IGroupBy;
import alvin.bef.framework.base.query.IJoin;
import alvin.bef.framework.base.query.IPage;
import alvin.bef.framework.base.query.ISelector;
import alvin.bef.framework.base.query.ISort;
import alvin.bef.framework.base.query.InvalidFilterException;
import alvin.bef.framework.base.query.PerformanceContext;
import alvin.bef.framework.base.session.SessionSynchronizationManager;
import alvin.bef.framework.base.session.SessionSynchronizationType;
import alvin.bef.framework.base.spring.SpringHelper;

/**
 * 
 * @author Alvin
 *
 */
public class BaseDAOHibernate extends HibernateDaoSupport implements DAO {

	private static final String ALIAS = IFilter.DEFAULT_ALIAS;

	protected final Log log = LogFactory.getLog(getClass());

	private boolean useBackwardCompatibleIsNewObject;

	protected <T> void scroll(Query query, ResultConverter<T> converter,
			ResultHandler<T> handler) {
		ScrollUtil.scroll(query, converter, handler);
	}

	private Query buildQuery(Class<?> clazz, ISelector selector,
			IFilter filter, IGroupBy groupBy, ISort sort, Limit limit) {
		StringBuilder querySB = new StringBuilder();

		if (selector != null) {
			String sqlSelect = selector.toString(ALIAS);
			if (sqlSelect != null) {
				querySB.append(sqlSelect).append(' ');
			}
		}

		querySB.append("from ").append(clazz.getName()).append(' ')
				.append(ALIAS);

		if (filter != null) {
			String filterString = filter.getString();
			querySB.append(" where ").append(filterString);
		}

		if (groupBy != null) {
			querySB.append(groupBy);
		}

		if (sort != null) {
			querySB.append(" order by ").append(sort.getSortString());
		}

		Query query = getSession(false).createQuery(querySB.toString())
				.setCacheable(true);

		if (filter != null) {
			setParameters(query, filter.getValues().toArray());
		}

		setLimit(query, limit);

		return query;
	}

	private Query buildSelectQuery(Class<?> clazz, ISelector selector,
			IFilter filter, IGroupBy groupBy, ISort sort, Limit limit) {
		return buildQuery(clazz, selector, filter, groupBy, sort, limit);
	}

	private Query getNamedQuery(String queryName) {
		return getSession(false).getNamedQuery(queryName);
	}

	private void setLimit(Query query, Limit limit) {
		if (limit != null) {
			query.setFirstResult(limit.getFirstResult());
			query.setMaxResults(limit.getMaxResults());
		}
	}

	private void setParameters(Query query, Object... params) {
		for (int i = 0; i < params.length; i++) {
			// TODO Should this be a call to setParameter(Query, int, Object)
			// instead? Why?
			// Can't do parameter list here -- bummer
			query.setParameter(i, params[i]);
		}
	}

	private void setParameters(Query query, Map<String, ?> params) {
		for (Entry<String, ?> param : params.entrySet()) {
			// TODO Should this be a call to setParameter(Query, int, Object)
			// instead? Why?
			if (param.getValue() instanceof Object[]) {
				query.setParameterList(param.getKey(),
						(Object[]) param.getValue());
			} else {
				query.setParameter(param.getKey(), param.getValue());
			}
		}
	}

	@Override
	public <T> void iterateObjects(Class<T> clazz, IFilter filter, ISort sort,
			Limit limit, ResultHandler<T> handler) {
		Query query = buildSelectQuery(clazz, null, filter, null, sort, limit);
		ResultConverter<T> resultConverter = new FirstColumnResultConverter<T>();
		scroll(query, resultConverter, handler);
	}

	@Override
	public void iterateResults(Class<?> clazz, ISelector selector,
			IFilter filter, IGroupBy groupBy, ISort sort, Limit limit,
			ResultHandler<Object[]> handler) {
		Query query = buildSelectQuery(clazz, selector, filter, groupBy, sort,
				limit);
		scroll(query, new ToObjectArrayResultConverter(), handler);
	}

	@Override
	public Object uniqueResultByQueryName(String queryName, Object... params) {
		Query query = getNamedQuery(queryName);
		setParameters(query, params);
		return query.uniqueResult();
	}

	@Override
	public void iterateByQueryName(String queryName,
			ResultHandler<Object[]> handler, Limit limit, Object... params) {
		Query query = getNamedQuery(queryName);
		setParameters(query, params);
		setLimit(query, limit);
		scroll(query, new ToObjectArrayResultConverter(), handler);
	}

	@Override
	public void iterateByQueryName(String queryName,
			ResultHandler<Object[]> handler, Limit limit, Map<String, ?> params) {
		Query query = getNamedQuery(queryName);
		setParameters(query, params);
		setLimit(query, limit);
		scroll(query, new ToObjectArrayResultConverter(), handler);
	}

	@Override
	public <T> void iterate(Query query, ResultConverter<T> converter,
			ResultHandler<T> handler) {
		scroll(query, converter, handler);
	}

	@Override
	public <T> void iterate(String hql, Map<String, ?> parameters,
			ResultConverter<T> converter, ResultHandler<T> handler) {
		Query query = getSession().createQuery(hql);

		if (parameters != null) {
			for (Entry<String, ?> parameter : parameters.entrySet()) {
				query.setParameter(parameter.getKey(), parameter.getValue()); // TODO
																				// handle
																				// lists
			}
		}

		iterate(query, converter, handler);
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> getObjects(Class<T> clazz) {
		return getHibernateTemplate().loadAll(clazz);
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
		return getObjects(clazz, filter, sort, page);
	}

	public <T> Iterator<T> iterateObjects(final Class<T> clazz, final FSP fsp) {
		IFilter filter = null;
		ISort sort = null;
		IPage page = null;
		if (fsp != null) {
			filter = fsp.getFilter();
			sort = fsp.getSort();
			page = fsp.getPage();
		}
		return iterateObjects(clazz, filter, sort, page);
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> getObjects(final Class<T> clazz, final IFilter filter,
			final ISort sort, final IPage page) {
		return getObjects(clazz, null, filter, sort, page);
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> getObjects(final Class<T> clazz, final IJoin join,
			final IFilter filter, final ISort sort, final IPage page) {
		HibernateTemplate ht = getHibernateTemplate();

		List<T> ret = ht.executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				return DAOUtil.processFind(session, clazz, join, filter, sort,
						page);
			}
		});
		return ret;
	}

	@SuppressWarnings("unchecked")
	public List getObjects(final Class clazz, final IJoin join,
			final IFilter filter, final ISort sort, final IPage page,
			final ISelector selector) {
		HibernateTemplate ht = getHibernateTemplate();

		List ret = ht.executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				return DAOUtil.processFind(session, clazz, selector, join,
						filter, sort, null, page);
			}
		});
		return ret;
	}

	@SuppressWarnings("unchecked")
	public List getObjects(final Class clazz, final IJoin join,
			final IFilter filter, final ISort sort, final IGroupBy groupBy,
			final IPage page, final ISelector selector) {
		HibernateTemplate ht = getHibernateTemplate();

		List ret = ht.executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				return DAOUtil.processFind(session, clazz, selector, join,
						filter, sort, groupBy, page);
			}
		});
		return ret;
	}

	@SuppressWarnings("unchecked")
	public <T> Iterator<T> iterateObjects(final Class<T> clazz,
			final IFilter filter, final ISort sort, final IPage page) {
		HibernateTemplate ht = getHibernateTemplate();

		Iterator<T> ret = (Iterator<T>) ht.execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				return processIterateFind(session, clazz, filter, sort, page);
			}
		});
		return ret;
	}

	@SuppressWarnings("unchecked")
	private <T> Iterator<T> processIterateFind(Session session, Class<T> clazz,
			IFilter filter, ISort sort, IPage page) {
		return (Iterator<T>) processIterateFind(session, clazz, null, filter,
				sort, null, page);
	}

	private Iterator<?> processIterateFind(Session session, Class<?> clazz,
			ISelector selector, IFilter filter, ISort sort, IGroupBy groupBy,
			IPage page) {
		Query[] queries = DAOUtil.setupQuery(session, clazz, null, selector,
				null, filter, sort, groupBy, page);
		Query query = queries[0];
		Query countQuery = queries[1];
		if (page != null && countQuery != null) {
			long begin = System.currentTimeMillis();
			Long size = (Long) countQuery.uniqueResult();
			long time = System.currentTimeMillis() - begin;
			PerformanceContext.logSqlTime(time);
			if (log.isTraceEnabled()) {
				log.trace("Query count string: " + countQuery.getQueryString()
						+ "\n\tUse: " + (time) + "ms");
			}
			if (size != null) {
				page.setTotalRecords(size.intValue());
			}
			query.setFirstResult(page.getStartRowPosition());
			if (page.getRecordsPerPage() > 0)
				query.setMaxResults(page.getRecordsPerPage());
		}
		long begin = System.currentTimeMillis();
		Iterator<?> ret = query.iterate();
		long time = System.currentTimeMillis() - begin;
		PerformanceContext.logSqlTime(time);
		if (log.isTraceEnabled()) {
			log.trace("Query string: " + query.getQueryString() + "\n\tUse: "
					+ (time) + "ms");
		}
		return ret;
	}

	@SuppressWarnings("unchecked")
	public <T> T getUniqueObject(final Class<T> clazz, final IFilter filter) {
		HibernateTemplate ht = getHibernateTemplate();

		T ret = (T) ht.execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				return processFindUnique(session, clazz, filter);
			}
		});
		return ret;
	}

	@SuppressWarnings("unchecked")
	private <T> T processFindUnique(Session session, Class<T> clazz,
			IFilter filter) {
		Query[] queries = DAOUtil.setupQuery(session, clazz, null, null,
				filter, null, null);
		Query query = queries[0];
		long begin = System.currentTimeMillis();
		T ret = (T) query.uniqueResult();
		long time = System.currentTimeMillis() - begin;
		PerformanceContext.logSqlTime(time);
		if (log.isTraceEnabled()) {
			log.trace("Query string: " + query.getQueryString() + "\n\tUsed "
					+ (time) + "ms");
		}
		return ret;
	}

	@SuppressWarnings("unchecked")
	public <T> Iterator<T> getObjects(final Class<T> entityClass,
			final String alias, final IJoin join, final FSP fsp) {
		HibernateTemplate ht = getHibernateTemplate();

		return (Iterator<T>) ht.execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				return DAOUtil.processJoinFind(session, entityClass, alias,
						join, fsp.getFilter(), fsp.getSort(), fsp.getPage());
			}
		});

	}

	@SuppressWarnings("unchecked")
	public List<?> getObjects(final Class<?> entityClass, final String alias,
			final IJoin join, final IFilter filter, final ISort sort,
			final IPage page) {
		HibernateTemplate ht = getHibernateTemplate();

		return (List<?>) ht.execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				return DAOUtil.processJoinFind(session, entityClass, alias,
						join, filter, sort, page);
			}
		});

	}

	@SuppressWarnings("unchecked")
	public <T> T getObject(Class<T> clazz, Serializable id) {
		return (T) getHibernateTemplate().get(clazz, id);
	}

	@Override
	public <T> T getObjectForUpdate(Class<T> clazz, Serializable id) {
		return (T) getHibernateTemplate().get(clazz, id, LockMode.UPGRADE);
	}

	// Jason Axtell: This method used to ALWAYS return false for any object with
	// a non-null ID, regardless of whether it was in the DB or not. This
	// behavior was wrong,
	// but we have code that counts on it, so at Param's suggestion, I am
	// including both versions and
	// letting you pick.
	public boolean isNewObject(Object o) {
		if (useBackwardCompatibleIsNewObject && o instanceof GeneratedIdEntity) {
			return getObjectId(o) == null;
		} else {
			Serializable id = getObjectId(o);
			if (id != null) {
				Class<?> clazz = SpringHelper.getOriginalClass(o.getClass());
				String idName = getIdPropertyName(clazz);
				StringBuilder countQuery = new StringBuilder(
						"select count(*) from ").append(clazz.getName())
						.append(" where ").append(idName).append(" = :id");
				int count = ((Number) getSession()
						.createQuery(countQuery.toString())
						.setParameter("id", id).uniqueResult()).intValue();

				return count == 0;
			}
			return true;
		}
	}

	public void saveObject(Object o) {
		if (o != null) {
			// if (!isNewObject(o) && getSession().contains(o)){
			// getSession().merge(o);
			// }else
			getSession().saveOrUpdate(o);
			postPersist(o);
		}
	}

	public void updateObject(Object o) {
		if (o != null) {
			getSession().update(o);
			postPersist(o);
		}
	}

	public <T> void removeObjectById(Class<T> clazz, Serializable id) {
		getHibernateTemplate().delete(getObject(clazz, id));
		postPersist(clazz);
	}

	public void removeObject(Object o) {
		if (o != null) {
			if (!getHibernateTemplate().contains(o)) {
				try {
					getHibernateTemplate().merge(o);
				} catch (Exception e) {
					if (log.isErrorEnabled()) {
						log.error(
								"Can not merge object into hibernate session.",
								e);
					}
				}
			}
			getHibernateTemplate().delete(o);
			postPersist(o);
		}
	}

	@Override
	public <T> String getIdPropertyName(Class<T> clazz) {
		String idName = "id";
		SessionFactory sf = getSessionFactory();
		if (sf != null) {
			ClassMetadata cm = sf.getClassMetadata(clazz);
			if (cm != null) {
				idName = cm.getIdentifierPropertyName();
			}
		}
		return idName;
	}

	public Serializable getObjectId(Object o) {
		SessionFactory sf = getHibernateTemplate().getSessionFactory();
		Serializable id = null;
		if (o != null && sf != null) {
			Class<?> clazz = SpringHelper.getOriginalClass(o.getClass());
			ClassMetadata cm = sf.getClassMetadata(clazz);
			if (cm != null) {
				// id = cm.getIdentifier(o, EntityMode.POJO);
				// hibernate 4, add by Alvin
				id = cm.getIdentifier(o);

			}
		}
		return id;
	}

	public void refreshObject(Object o) {
		if (o != null) {
			getHibernateTemplate().refresh(o);
		}
	}

	@Override
	public List<?> initCollection(Collection<?> c, String filterString,
			Object... params) {
		Query q = getSession().createFilter(c, filterString); // The
																// filterString
																// can transform
																// the input, so
																// the type can
																// change
		if (params != null) {
			for (int i = 0; i < params.length; i++) {
				q.setParameter(i, params[i]);
			}
		}
		return q.list();
	}

	@Override
	public <T> boolean isRecordExists(final Class<T> clazz, final IJoin join,
			final IFilter filter, final boolean cacheable) {
		HibernateTemplate ht = getHibernateTemplate();

		return (Boolean) ht.execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				try {
					Query existsQuery = DAOUtil.getExistsQuery(session, clazz,
							null, join, filter, cacheable);
					DAOUtil.processFilterParameter(existsQuery, null, filter);
					String recordId = (String) existsQuery.uniqueResult();
					return recordId == null ? false : true;
				} catch (InvalidFilterException e) {
					throw new HibernateException(e);
				}
			}
		});
	}

	public <T> int getRecordCount(final Class<T> clazz, final IJoin join,
			final IFilter filter, final boolean cacheable) {
		HibernateTemplate ht = getHibernateTemplate();

		return (Integer) ht.execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				try {
					Query countQuery = DAOUtil.getCountQuery(session, clazz,
							null, join, filter, cacheable);
					DAOUtil.processFilterParameter(countQuery, null, filter);
					return ((Number) countQuery.uniqueResult()).intValue();
				} catch (InvalidFilterException e) {
					throw new HibernateException(e);
				}
			}
		});
	}

	public <T> int getRecordCount(final String hql, final Object... params) {
		HibernateTemplate ht = getHibernateTemplate();

		return (Integer) ht.execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query countQuery = session.createQuery(hql).setCacheable(false);
				for (int i = 0; params != null && i < params.length; i++) {
					countQuery.setParameter(i, params[i]);
				}
				return ((Number) countQuery.uniqueResult()).intValue();
			}
		});
	}

	public <T> int getRecordCount(final String hql, final Map<String, ?> params) {
		HibernateTemplate ht = getHibernateTemplate();

		return (Integer) ht.execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query countQuery = session.createQuery(hql).setCacheable(false);
				if (params != null) {
					Set<String> keys = params.keySet();
					for (String key : keys) {
						Object value = params.get(key);
						if (key.equals(POSITIONAL_PARAMS_KEY)) {
							if (value instanceof Object[]) {
								Object[] vs = ((Object[]) value);
								for (int i = 0; i < vs.length; i++) {
									countQuery.setParameter(i, vs[i]);
								}
							} else {
								countQuery.setParameter(0, value);
							}
						} else {
							if (value instanceof Object[]) {
								countQuery.setParameterList(key,
										(Object[]) value);
							} else {
								countQuery.setParameter(key, value);
							}
						}
					}
				}
				return ((Number) countQuery.uniqueResult()).intValue();
			}
		});
	}

	public void evite(Object entity) {
		getHibernateTemplate().evict(entity);
	}

	public Object getObjectsByQueryString(final String queryString,
			final boolean ifUnique, final Long first, final Long max,
			final Object... params) {
		HibernateTemplate ht = getHibernateTemplate();
		return ht.execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query query = session.createQuery(queryString);
				if (first != null && max != null) {
					query.setFirstResult(first.intValue());
					query.setMaxResults(max.intValue());
				}
				if (params != null) {
					for (int i = 0; i < params.length; i++) {
						query.setParameter(i, params[i]);
					}
				}
				if (ifUnique) {
					return query.uniqueResult();
				}
				return query.list();
			}
		});
	}

	public Object getObjectsByQueryStringNamedParameters(
			final String queryString, final boolean ifUnique, final Long first,
			final Long max, final Map<?, ?> params) {
		HibernateTemplate ht = getHibernateTemplate();
		return ht.execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query query = session.createQuery(queryString);
				if (first != null && max != null) {
					query.setFirstResult(first.intValue());
					query.setMaxResults(max.intValue());
				}
				if (params != null) {
					Set<?> keys = params.keySet();
					for (Object key : keys) {
						Object value = params.get(key);
						if (key.equals(POSITIONAL_PARAMS_KEY)) {
							if (value instanceof Object[]) {
								Object[] vs = ((Object[]) value);
								for (int i = 0; i < vs.length; i++) {
									query.setParameter(i, vs[i]);
								}
							} else {
								query.setParameter(0, value);
							}
						} else {
							if (value instanceof Object[]) {
								query.setParameterList(key.toString(),
										(Object[]) value);
							} else {
								if (key instanceof Integer)
									query.setParameter((Integer) key, value);
								else
									query.setParameter(key.toString(), value);
							}
						}
					}
				}
				if (ifUnique) {
					return query.uniqueResult();
				}
				return query.list();
			}
		});
	}

	public List<?> listResults(final Class<?> clazz, final ISelector selector,
			final IFilter filter, final ISort sort, final IGroupBy groupBy,
			final IPage page, final IJoin join) {
		HibernateTemplate ht = getHibernateTemplate();

		List<?> ret = ht.executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				return DAOUtil.processFind(session, clazz, selector, join,
						filter, sort, groupBy, page);
			}
		});
		return ret;
	}

	public List<?> listResults(final Class<?> clazz, final ISelector selector,
			final IFilter filter, final ISort sort, final IGroupBy groupBy,
			final IPage page) {
		return listResults(clazz, selector, filter, sort, groupBy, page, null);
	}

	public Iterator<?> iterateResults(final Class<?> clazz,
			final ISelector selector, final IFilter filter, final ISort sort,
			final IGroupBy groupBy, final IPage page) {
		HibernateTemplate ht = getHibernateTemplate();

		Iterator<?> ret = (Iterator<?>) ht.execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				return processIterateFind(session, clazz, selector, filter,
						sort, groupBy, page);
			}
		});
		return ret;

	}

	public void setUseBackwardCompatibleIsNewObject(
			boolean useBackwardCompatibleIsNewObject) {
		this.useBackwardCompatibleIsNewObject = useBackwardCompatibleIsNewObject;
	}

	public List<?> listByQueryName(final String queryName,
			final Object[] values, final Integer startIndex,
			final Integer pageAmount) {

		HibernateTemplate ht = getHibernateTemplate();
		List<?> ret = ht.executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query query = session.getNamedQuery(queryName);
				if (values != null) {
					for (int i = 0; i < values.length; i++) {
						query.setParameter(i, values[i]);
					}
				}
				if (startIndex != null && pageAmount != null
						&& pageAmount.intValue() > 0) {
					query.setFirstResult(startIndex.intValue());
					query.setMaxResults(pageAmount.intValue());
				}
				return query.list();
			}
		});
		return ret;
	}

	public List<?> listByQueryName(final String queryName,
			final Map<String, ?> params, final Integer startIndex,
			final Integer pageAmount) {
		HibernateTemplate ht = getHibernateTemplate();
		List<?> ret = ht.executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query query = session.getNamedQuery(queryName);
				if (params != null) {
					for (String key : params.keySet()) {
						Object value = params.get(key);
						if (value instanceof Object[]) {
							query.setParameterList(key, (Object[]) value);
						} else {
							query.setParameter(key, value);
						}
					}
				}
				if (startIndex != null && pageAmount != null
						&& pageAmount.intValue() > 0) {
					query.setFirstResult(startIndex.intValue());
					query.setMaxResults(pageAmount.intValue());
				}
				return query.list();
			}
		});
		return ret;
	}

	public void executeUpdate(String sql) {
		Connection con = getConnection();
		try {
			Statement statement = con.createStatement();
			statement.execute(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}

	}

	public int executeUpdate(String sql, Object... objects) {
		Connection con = getConnection();
		try {
			PreparedStatement preStatement = con.prepareStatement(sql);
			int paramIndex = 1;
			if (objects != null) {
				for (Object obj : objects) {
					preStatement.setObject(paramIndex, obj);
					paramIndex++;
				}
			}
			return preStatement.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings("deprecation")
	private Connection getConnection() {
		Session session = this.getHibernateTemplate().getSessionFactory()
				.getCurrentSession();
		try {
			return SessionFactoryUtils.getDataSource(getSessionFactory())
					.getConnection();
		} catch (SQLException e) {
			throw new BackEndException("lost connection");
		}
	}

	@Override
	public List<Object[]> executeQuery(String sql, Object... objects) {
		// TODO Auto-generated method stub
		Connection con = getConnection();
		List<Object[]> list = new ArrayList<Object[]>();
		PreparedStatement preStatement = null;
		ResultSet resultSet = null;
		try {
			preStatement = con.prepareStatement(sql);
			int paramIndex = 1;
			if (objects != null) {
				for (Object obj : objects) {
					preStatement.setObject(paramIndex, obj);
					paramIndex++;
				}
			}
			resultSet = preStatement.executeQuery();
			if (resultSet == null)
				return list;
			ResultSetMetaData resultMetaData = resultSet.getMetaData();
			int columnCount = resultMetaData.getColumnCount();

			while (resultSet.next()) {
				Object obj[] = new Object[columnCount];
				for (int i = 0; i < columnCount; i++) {
					obj[i] = resultSet.getObject(i + 1);
				}
				list.add(obj);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		} finally {
			if (resultSet != null) {
				try {
					resultSet.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					log.error("can not close result", e);
				}
			}
			if (preStatement != null) {
				try {
					preStatement.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					log.error("can not close result", e);
				}
			}
		}
		return list;
	}

	@Override
	public List<Object[]> executeQuery(String sql) {
		// TODO Auto-generated method stub
		return executeQuery(sql, new Object[] {});
	}

	public int executeUpdatebyQueryName(final String queryName,
			final Object[] values) {
		return executeUpdatebyQueryName(queryName, values, null, null);
	}

	public int executeUpdatebyQueryName(final String queryName,
			final Object[] values, final Integer startIndex,
			final Integer pageAmount) {
		HibernateTemplate ht = getHibernateTemplate();
		return ht.execute(new HibernateCallback<Integer>() {
			public Integer doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query query = session.getNamedQuery(queryName);
				if (startIndex != null && pageAmount != null
						&& pageAmount.intValue() > 0) {
					query.setFirstResult(startIndex.intValue());
					query.setMaxResults(pageAmount.intValue());
				}
				if (values != null) {
					for (int i = 0; i < values.length; i++) {
						query.setParameter(i, values[i]);
					}
					return query.executeUpdate();
				}
				return 0;
			}
		});
	}

	public int executeUpdatebyQueryName(final String queryName,
			final Map<String, ?> params) {
		HibernateTemplate ht = getHibernateTemplate();
		return ht.execute(new HibernateCallback<Integer>() {
			public Integer doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query query = session.getNamedQuery(queryName);
				if (params != null) {
					for (String key : params.keySet()) {
						if (params.get(key) instanceof Collection<?>) {
							query.setParameterList(key,
									(Collection<?>) params.get(key));
						} else {
							query.setParameter(key, params.get(key));
						}
					}
					return query.executeUpdate();
				}
				return 0;
			}
		});
	}

	public void executeUpdatebyQueryString(final String queryString,
			final Map<String, ?> params) {
		HibernateTemplate ht = getHibernateTemplate();
		ht.execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query query = session.createQuery(queryString);
				if (params != null) {
					for (String key : params.keySet()) {
						if (params.get(key) instanceof Collection<?>) {
							query.setParameterList(key,
									(Collection<?>) params.get(key));
						} else {
							query.setParameter(key, params.get(key));
						}
					}
					return query.executeUpdate();
				}
				return null;
			}
		});
	}

	public void clearCurrentSession() {
		HibernateTemplate ht = getHibernateTemplate();
		SessionFactory sessionFactory = ht.getSessionFactory();
		if (sessionFactory == null)
			return;
		Session session = sessionFactory.getCurrentSession();
		if (session == null)
			return;
		session.clear();
	}

	public void flushCurrentSession() {
		HibernateTemplate ht = getHibernateTemplate();
		SessionFactory sessionFactory = ht.getSessionFactory();
		if (sessionFactory == null)
			return;
		Session session = sessionFactory.getCurrentSession();
		if (session == null)
			return;
		session.flush();
	}

	@Override
	public void lock(Object o) {
		getSession().lock(o, LockMode.UPGRADE);
	}

	public void removeObjectsByIds(
			Class<? extends GeneratedIdAbstractAuditable> clazz,
			List<Serializable> idList) {
		String idName = getIdPropertyName(clazz);
		removeObjectsByPropertyValues(clazz, idList, idName);
	}

	public <T> void removeObjectsByPropertyValues(Class<T> clazz,
			List<Serializable> valueList, String propertyName) {
		Map<String, Object> valueMap = new HashMap<String, Object>();
		String valueListName = "valueList";
		valueMap.put(valueListName, valueList);
		StringBuilder queryBuilder = new StringBuilder("DELETE FROM ")
				.append(clazz.getName()).append(" WHERE ").append(propertyName)
				.append(" IN (:").append(valueListName).append(")");

		this.executeUpdatebyQueryString(queryBuilder.toString(), valueMap);
		postPersist(clazz);
	}

	private void postPersist(Class<?> clazz) {
		registerSessionSyncInterests(clazz);
	}

	private void postPersist(Object entity) {
		if (entity == null)
			return;
		Class clazz = entity.getClass();
		if (entity instanceof HibernateProxy) {
			clazz = ((HibernateProxy) entity).getHibernateLazyInitializer()
					.getPersistentClass();
		}
		registerSessionSyncInterests(clazz);
	}

	private void registerSessionSyncInterests(Class<?> clazz) {
		if (clazz.isAnnotationPresent(SessionSynchronizationInterest.class)) {
			SessionSynchronizationInterest sessionSyncAnnotation = clazz
					.getAnnotation(SessionSynchronizationInterest.class);
			for (SessionSynchronizationType sessionSyncType : sessionSyncAnnotation
					.type()) {
				SessionSynchronizationManager.registerInterest(sessionSyncType);
			}
		}
	}

	@Override
	public Query createHibernateQuery(String queryString) {
		Query query = getSession().createQuery(queryString);
		return query;
	}

	@Override
	public String convertHqlToSql(String hql) {
		QueryTranslatorFactory translatorFactory = new ASTQueryTranslatorFactory();
		SessionFactoryImplementor factory = (SessionFactoryImplementor) this
				.getHibernateTemplate().getSessionFactory();
		final QueryTranslator translator = translatorFactory
				.createQueryTranslator(hql, hql, Collections.EMPTY_MAP, factory);
		translator.compile(Collections.EMPTY_MAP, false);
		return translator.getSQLString();
	}

	public Timestamp getServerNowTime() {
		HibernateTemplate ht = getHibernateTemplate();
		return ht.execute(new HibernateCallback<Timestamp>() {
			public Timestamp doInHibernate(Session session)
					throws HibernateException, SQLException {
				return (Timestamp) session.createSQLQuery("select now()")
						.uniqueResult();
			}
		});
	}
}
