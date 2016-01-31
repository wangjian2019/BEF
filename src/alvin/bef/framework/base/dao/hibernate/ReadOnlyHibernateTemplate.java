package alvin.bef.framework.base.dao.hibernate;

import java.sql.SQLException;
import java.util.Collection;

import javax.sql.DataSource;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.util.Assert;

import alvin.bef.framework.base.dao.Limit;

/**
 * 
 * @author Alvin
 * Don't expose this class, don't get session factory or connection directly, if you want to get connection, please call execute method,
 * then in callback, you can get hibernate session, then get connection from hibernate session.
 */
class ReadOnlyHibernateTemplate {

	private ReadOnlySessionFactory roSF = new ReadOnlySessionFactory();

    

	public void setSessionFactory(SessionFactory sessionFactory) {
		roSF.setSessionFactory(sessionFactory);
	}

	public void setDataSource(DataSource dataSource) {
		roSF.setDataSource(dataSource);
	}

	@SuppressWarnings("unchecked")
	public <T> T find(final String queryString, final Object[] values,
			final Limit limit) {
		return (T) execute(new HibernateCallback<Object>() {
			public Object doInHibernate(Session session)
					throws HibernateException {
				Query queryObject = session.createQuery(queryString);
				setLimit(queryObject, limit);
				if (values != null) {
					for (int i = 0; i < values.length; i++) {
						queryObject.setParameter(i, values[i]);
					}
				}
				return queryObject.list();
			}
		});
	}

	@SuppressWarnings("unchecked")
	public <T> T findByNamedParam(final String queryString,
			final String[] paramNames, final Object[] values, final Limit limit) {

		if (paramNames.length != values.length) {
			throw new IllegalArgumentException(
					"Length of paramNames array must match length of values array");
		}
		return execute(new HibernateCallback<T>() {
			public T doInHibernate(Session session)
					throws HibernateException {
				Query queryObject = session.createQuery(queryString);
				setLimit(queryObject, limit);
				if (values != null) {
					for (int i = 0; i < values.length; i++) {
						applyNamedParameterToQuery(queryObject, paramNames[i],
								values[i]);
					}
				}
				return (T)queryObject.list();
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	public <T> T findByNamedQuery(final String queryName, final Object[] values, final Limit limit)  {
		return execute(new HibernateCallback<T>() {
			public T doInHibernate(Session session) throws HibernateException {
				Query queryObject = session.getNamedQuery(queryName);
				setLimit(queryObject, limit);
				if (values != null) {
					for (int i = 0; i < values.length; i++) {
						queryObject.setParameter(i, values[i]);
					}
				}
				return (T)queryObject.list();
			}
		});
	}


	@SuppressWarnings("unchecked")
	public <T> T findByNamedQueryAndNamedParam(
			final String queryName, final String[] paramNames, final Object[] values, final Limit limit)
			{

		if (paramNames != null && values != null && paramNames.length != values.length) {
			throw new IllegalArgumentException("Length of paramNames array must match length of values array");
		}
		return  execute(new HibernateCallback<T>() {
			public T doInHibernate(Session session) throws HibernateException {
				Query queryObject = session.getNamedQuery(queryName);
				setLimit(queryObject, limit);
				if (values != null) {
					for (int i = 0; i < values.length; i++) {
						applyNamedParameterToQuery(queryObject, paramNames[i], values[i]);
					}
				}
				return (T)queryObject.list();
			}
		});
	}

	@SuppressWarnings("rawtypes")
	protected void applyNamedParameterToQuery(Query queryObject,
			String paramName, Object value) throws HibernateException {

		if (value instanceof Collection) {
			queryObject.setParameterList(paramName, (Collection) value);
		} else if (value instanceof Object[]) {
			queryObject.setParameterList(paramName, (Object[]) value);
		} else {
			queryObject.setParameter(paramName, value);
		}
	}

	private void setLimit(Query query, Limit limit) {
		if (limit != null) {
			query.setFirstResult(limit.getFirstResult());
			query.setMaxResults(limit.getMaxResults());
		}
	}


	public <T> T execute(HibernateCallback<T> action) {
		Assert.notNull(action, "Callback object must not be null");
		Session session = roSF.createSession();
		try {
			T result = action.doInHibernate(session);
			return result;
		} catch (HibernateException ex) {
			throw SessionFactoryUtils.convertHibernateAccessException(ex);
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		} catch (RuntimeException ex) {
			// Callback code threw application exception...
			throw ex;
		} finally {
			SessionFactoryUtils.closeSession(session);
		}
	}
}
