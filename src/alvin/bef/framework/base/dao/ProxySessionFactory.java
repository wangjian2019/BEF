package alvin.bef.framework.base.dao;

import java.io.Serializable;
import java.sql.Connection;
import java.util.Map;
import java.util.Set;

import javax.naming.NamingException;
import javax.naming.Reference;

import org.hibernate.Cache;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionBuilder;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.StatelessSessionBuilder;
import org.hibernate.TypeHelper;
import org.hibernate.engine.spi.FilterDefinition;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.metadata.CollectionMetadata;
import org.hibernate.stat.Statistics;

/**
 * 
 * @author Alvin
 *
 */
public class ProxySessionFactory implements SessionFactory {
	private final SessionFactory delegate;

	public ProxySessionFactory(SessionFactory delegate) {
		this.delegate = delegate;
	}

	public void close() throws HibernateException {
		delegate.close();
	}

	public void evict(Class arg0, Serializable arg1) throws HibernateException {
		delegate.evict(arg0, arg1);
	}

	public void evict(Class arg0) throws HibernateException {
		delegate.evict(arg0);
	}

	public void evictCollection(String arg0, Serializable arg1)
			throws HibernateException {
		delegate.evictCollection(arg0, arg1);
	}

	public void evictCollection(String arg0) throws HibernateException {
		delegate.evictCollection(arg0);
	}

	public void evictEntity(String arg0, Serializable arg1)
			throws HibernateException {
		delegate.evictEntity(arg0, arg1);
	}

	public void evictEntity(String arg0) throws HibernateException {
		delegate.evictEntity(arg0);
	}

	public void evictQueries() throws HibernateException {
		delegate.evictQueries();
	}

	public void evictQueries(String arg0) throws HibernateException {
		delegate.evictQueries(arg0);
	}

	public Map getAllClassMetadata() throws HibernateException {
		return delegate.getAllClassMetadata();
	}

	public Map getAllCollectionMetadata() throws HibernateException {
		return delegate.getAllCollectionMetadata();
	}

	public ClassMetadata getClassMetadata(Class arg0) throws HibernateException {
		return delegate.getClassMetadata(arg0);
	}

	public ClassMetadata getClassMetadata(String arg0)
			throws HibernateException {
		return delegate.getClassMetadata(arg0);
	}

	public CollectionMetadata getCollectionMetadata(String arg0)
			throws HibernateException {
		return delegate.getCollectionMetadata(arg0);
	}

	public Set getDefinedFilterNames() {
		return delegate.getDefinedFilterNames();
	}

	public FilterDefinition getFilterDefinition(String arg0)
			throws HibernateException {
		return delegate.getFilterDefinition(arg0);
	}

	public Reference getReference() throws NamingException {
		return delegate.getReference();
	}

	public Statistics getStatistics() {
		return delegate.getStatistics();
	}

	public boolean isClosed() {
		return delegate.isClosed();
	}

	// Hibernate4 can not support
	// public Session getCurrentSession() throws HibernateException {
	// return delegate.getCurrentSession();
	// }
	//
	// public org.hibernate.classic.Session openSession() throws
	// HibernateException {
	// return delegate.openSession();
	// }
	//
	// public org.hibernate.classic.Session openSession(Connection arg0,
	// Interceptor arg1) {
	// return delegate.openSession(arg0, arg1);
	// }
	//
	// public org.hibernate.classic.Session openSession(Connection arg0) {
	// return delegate.openSession(arg0);
	// }
	//
	// public org.hibernate.classic.Session openSession(Interceptor arg0) throws
	// HibernateException {
	// return delegate.openSession(arg0);
	// }

	public StatelessSession openStatelessSession() {
		return delegate.openStatelessSession();
	}

	public StatelessSession openStatelessSession(Connection arg0) {
		return delegate.openStatelessSession(arg0);
	}

	@Override
	public boolean containsFetchProfileDefinition(String arg0) {
		return delegate.containsFetchProfileDefinition(arg0);
	}

	@Override
	public Cache getCache() {
		return delegate.getCache();
	}

	@Override
	public Session getCurrentSession() throws HibernateException {
		return delegate.getCurrentSession();
	}

	@Override
	public SessionFactoryOptions getSessionFactoryOptions() {
		return delegate.getSessionFactoryOptions();
	}

	@Override
	public TypeHelper getTypeHelper() {
		return delegate.getTypeHelper();
	}

	@Override
	public Session openSession() throws HibernateException {
		return delegate.openSession();
	}

	@Override
	public SessionBuilder withOptions() {
		return delegate.withOptions();
	}

	@Override
	public StatelessSessionBuilder withStatelessOptions() {
		return delegate.withStatelessOptions();
	}

}
