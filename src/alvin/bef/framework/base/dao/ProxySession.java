package alvin.bef.framework.base.dao;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;

import org.hibernate.CacheMode;
import org.hibernate.Criteria;
import org.hibernate.EntityMode;
import org.hibernate.Filter;
import org.hibernate.FlushMode;
import org.hibernate.HibernateException;
import org.hibernate.IdentifierLoadAccess;
import org.hibernate.LobHelper;
import org.hibernate.LockMode;
import org.hibernate.LockOptions;
import org.hibernate.NaturalIdLoadAccess;
import org.hibernate.Query;
import org.hibernate.ReplicationMode;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionEventListener;
import org.hibernate.SessionFactory;
import org.hibernate.SharedSessionBuilder;
import org.hibernate.SimpleNaturalIdLoadAccess;
import org.hibernate.Transaction;
import org.hibernate.TypeHelper;
import org.hibernate.UnknownProfileException;
import org.hibernate.Session.LockRequest;
import org.hibernate.jdbc.ReturningWork;
import org.hibernate.jdbc.Work;
import org.hibernate.stat.SessionStatistics;
import org.hibernate.type.Type;
import org.springframework.orm.hibernate4.SessionFactoryUtils;

/**
 * 
 * @author Alvin
 *
 */
public class ProxySession implements Session {
	private final Session delegate;

	public ProxySession(Session delegate) {
		this.delegate = delegate;
	}

	public Transaction beginTransaction() throws HibernateException {
		return delegate.beginTransaction();
	}

	public void cancelQuery() throws HibernateException {
		delegate.cancelQuery();
	}

	public void clear() {
		delegate.clear();
	}

	public Connection close() throws HibernateException {
		return delegate.close();
	}

	public Connection connection() throws HibernateException, SQLException {
		// return delegate.connection();
		// hibernate4 don`t support to get connection from session
		return SessionFactoryUtils.getDataSource(getSessionFactory())
				.getConnection();
	}

	public boolean contains(Object arg0) {
		return delegate.contains(arg0);
	}

	public Criteria createCriteria(Class arg0, String arg1) {
		return delegate.createCriteria(arg0, arg1);
	}

	public Criteria createCriteria(Class arg0) {
		return delegate.createCriteria(arg0);
	}

	public Criteria createCriteria(String arg0, String arg1) {
		return delegate.createCriteria(arg0, arg1);
	}

	public Criteria createCriteria(String arg0) {
		return delegate.createCriteria(arg0);
	}

	public Query createFilter(Object arg0, String arg1)
			throws HibernateException {
		return delegate.createFilter(arg0, arg1);
	}

	public Query createQuery(String arg0) throws HibernateException {
		return delegate.createQuery(arg0);
	}

// Hibernate4 can not support
//	public Query createSQLQuery(String arg0, String arg1, Class arg2) {
//		delegate.cr
//		return delegate.createSQLQuery(arg0, arg1, arg2);
//	}
//
//	public Query createSQLQuery(String arg0, String[] arg1, Class[] arg2) {
//		return delegate.createSQLQuery(arg0, arg1, arg2);
//	}

	public SQLQuery createSQLQuery(String arg0) throws HibernateException {
		return delegate.createSQLQuery(arg0);
	}

	public void delete(Object arg0) throws HibernateException {
		delegate.delete(arg0);
	}

// Hibernate4 can not support	
//	public int delete(String arg0, Object arg1, Type arg2)
//			throws HibernateException {
//		return delegate.delete(arg0, arg1, arg2);
//	}
//
//	public int delete(String arg0, Object[] arg1, Type[] arg2)
//			throws HibernateException {
//		return delegate.delete(arg0, arg1, arg2);
//	}
//
//	public int delete(String arg0) throws HibernateException {
//		return delegate.delete(arg0);
//	}
	
	public void delete(String arg0, Object arg1) throws HibernateException {
		delegate.delete(arg0, arg1);
	}

	public void disableFilter(String arg0) {
		delegate.disableFilter(arg0);
	}

	public Connection disconnect() throws HibernateException {
		return delegate.disconnect();
	}

	public Filter enableFilter(String arg0) {
		return delegate.enableFilter(arg0);
	}

	public void evict(Object arg0) throws HibernateException {
		delegate.evict(arg0);
	}

// Hibernate4 can not support	
//	public Collection filter(Object arg0, String arg1, Object arg2, Type arg3)
//			throws HibernateException {
//		return delegate.filter(arg0, arg1, arg2, arg3);
//	}
//
//	public Collection filter(Object arg0, String arg1, Object[] arg2,
//			Type[] arg3) throws HibernateException {
//		return delegate.filter(arg0, arg1, arg2, arg3);
//	}
//
//	public Collection filter(Object arg0, String arg1)
//			throws HibernateException {
//		return delegate.filter(arg0, arg1);
//	}
//
//	public List find(String arg0, Object arg1, Type arg2)
//			throws HibernateException {
//		return delegate.find(arg0, arg1, arg2);
//	}
//
//	public List find(String arg0, Object[] arg1, Type[] arg2)
//			throws HibernateException {
//		return delegate.find(arg0, arg1, arg2);
//	}
//
//	public List find(String arg0) throws HibernateException {
//		return delegate.find(arg0);
//	}

	public void flush() throws HibernateException {
		delegate.flush();
	}

	public Object get(Class arg0, Serializable arg1, LockMode arg2)
			throws HibernateException {
		return delegate.get(arg0, arg1, arg2);
	}

	public Object get(Class arg0, Serializable arg1) throws HibernateException {
		return delegate.get(arg0, arg1);
	}

	public Object get(String arg0, Serializable arg1, LockMode arg2)
			throws HibernateException {
		return delegate.get(arg0, arg1, arg2);
	}

	public Object get(String arg0, Serializable arg1) throws HibernateException {
		return delegate.get(arg0, arg1);
	}

	public CacheMode getCacheMode() {
		return delegate.getCacheMode();
	}

	public LockMode getCurrentLockMode(Object arg0) throws HibernateException {
		return delegate.getCurrentLockMode(arg0);
	}

	public Filter getEnabledFilter(String arg0) {
		return delegate.getEnabledFilter(arg0);
	}

	// Hibernate4 can not support
//	public EntityMode getEntityMode() {
//		return delegate.getEntityMode();
//	}

	public String getEntityName(Object arg0) throws HibernateException {
		return delegate.getEntityName(arg0);
	}

	public FlushMode getFlushMode() {
		return delegate.getFlushMode();
	}

	public Serializable getIdentifier(Object arg0) throws HibernateException {
		return delegate.getIdentifier(arg0);
	}

	public Query getNamedQuery(String arg0) throws HibernateException {
		return delegate.getNamedQuery(arg0);
	}

	// hibernate4 can not support
//	public org.hibernate.Session getSession(EntityMode arg0) {
//		return delegate.getSession(arg0);
//	}

	public SessionFactory getSessionFactory() {
		return delegate.getSessionFactory();
	}

	public SessionStatistics getStatistics() {
		return delegate.getStatistics();
	}

	public Transaction getTransaction() {
		return delegate.getTransaction();
	}

	public boolean isConnected() {
		return delegate.isConnected();
	}

	public boolean isDirty() throws HibernateException {
		return delegate.isDirty();
	}

	public boolean isOpen() {
		return delegate.isOpen();
	}

	public Iterator iterate(String arg0, Object arg1, Type arg2)
			throws HibernateException {
		//return delegate.iterate(arg0, arg1, arg2);
        // hibernate4 need to iterate via Query, instead of Session
		SQLQuery query = this.createSQLQuery(arg0);
		return query.iterate();
	}
// hibernate4 cannot support
//	public Iterator iterate(String arg0, Object[] arg1, Type[] arg2)
//			throws HibernateException {
//		return delegate.iterate(arg0, arg1, arg2);
//	}
//
//	public Iterator iterate(String arg0) throws HibernateException {
//		return delegate.iterate(arg0);
//	}

	public Object load(Class arg0, Serializable arg1, LockMode arg2)
			throws HibernateException {
		return delegate.load(arg0, arg1, arg2);
	}

	public Object load(Class arg0, Serializable arg1) throws HibernateException {
		return delegate.load(arg0, arg1);
	}

	public void load(Object arg0, Serializable arg1) throws HibernateException {
		delegate.load(arg0, arg1);
	}

	public Object load(String arg0, Serializable arg1, LockMode arg2)
			throws HibernateException {
		return delegate.load(arg0, arg1, arg2);
	}

	public Object load(String arg0, Serializable arg1)
			throws HibernateException {
		return delegate.load(arg0, arg1);
	}

	public void lock(Object arg0, LockMode arg1) throws HibernateException {
		delegate.lock(arg0, arg1);
	}

	public void lock(String arg0, Object arg1, LockMode arg2)
			throws HibernateException {
		delegate.lock(arg0, arg1, arg2);
	}

	public Object merge(Object arg0) throws HibernateException {
		return delegate.merge(arg0);
	}

	public Object merge(String arg0, Object arg1) throws HibernateException {
		return delegate.merge(arg0, arg1);
	}

	public void persist(Object arg0) throws HibernateException {
		delegate.persist(arg0);
	}

	public void persist(String arg0, Object arg1) throws HibernateException {
		delegate.persist(arg0, arg1);
	}

	// hibernate4 don`t support to get connection from session
//	public void reconnect() throws HibernateException {
//		//delegate.reconnect();
//	}

	public void reconnect(Connection arg0) throws HibernateException {
		delegate.reconnect(arg0);
	}

	public void refresh(Object arg0, LockMode arg1) throws HibernateException {
		delegate.refresh(arg0, arg1);
	}

	public void refresh(Object arg0) throws HibernateException {
		delegate.refresh(arg0);
	}

	public void replicate(Object arg0, ReplicationMode arg1)
			throws HibernateException {
		delegate.replicate(arg0, arg1);
	}

	public void replicate(String arg0, Object arg1, ReplicationMode arg2)
			throws HibernateException {
		delegate.replicate(arg0, arg1, arg2);
	}

	public Serializable save(Object arg0) throws HibernateException {
		return delegate.save(arg0);
	}

	// hibernate4 can not support 
//	public void save(String arg0, Object arg1, Serializable arg2)
//			throws HibernateException {
//		delegate.save(arg0, arg1, arg2);
//	}
//	
//	public void save(Object arg0, Serializable arg1) throws HibernateException {
//		delegate.save(arg0, arg1);
//	}

	public Serializable save(String arg0, Object arg1)
			throws HibernateException {
		return delegate.save(arg0, arg1);
	}

	public void saveOrUpdate(Object arg0) throws HibernateException {
		delegate.saveOrUpdate(arg0);
	}

	public void saveOrUpdate(String arg0, Object arg1)
			throws HibernateException {
		delegate.saveOrUpdate(arg0, arg1);
	}

	// hibernate4 can not support
//	public Object saveOrUpdateCopy(Object arg0, Serializable arg1)
//			throws HibernateException {
//		return delegate.saveOrUpdateCopy(arg0, arg1);
//	}
//
//	public Object saveOrUpdateCopy(Object arg0) throws HibernateException {
//		return delegate.saveOrUpdateCopy(arg0);
//	}
//
//	public Object saveOrUpdateCopy(String arg0, Object arg1, Serializable arg2)
//			throws HibernateException {
//		return delegate.saveOrUpdateCopy(arg0, arg1, arg2);
//	}
//
//	public Object saveOrUpdateCopy(String arg0, Object arg1)
//			throws HibernateException {
//		return delegate.saveOrUpdateCopy(arg0, arg1);
//	}

	public void setCacheMode(CacheMode arg0) {
		delegate.setCacheMode(arg0);
	}

	public void setFlushMode(FlushMode arg0) {
		delegate.setFlushMode(arg0);
	}

	public void setReadOnly(Object arg0, boolean arg1) {
		delegate.setReadOnly(arg0, arg1);
	}

	public void update(Object arg0) throws HibernateException {
		delegate.update(arg0);
	}
	
	// hibernate4 cannot support
//	public void update(Object arg0, Serializable arg1)
//			throws HibernateException {
//		delegate.update(arg0, arg1);
//	}
//
//	public void update(String arg0, Object arg1, Serializable arg2)
//			throws HibernateException {
//		delegate.update(arg0, arg1, arg2);
//	}

	public void update(String arg0, Object arg1) throws HibernateException {
		delegate.update(arg0, arg1);
	}

	@Override
	public String getTenantIdentifier() {
		return delegate.getTenantIdentifier();
	}

	@Override
	public void addEventListeners(SessionEventListener... arg0) {
		delegate.addEventListeners(arg0);
		
	}

	@Override
	public LockRequest buildLockRequest(LockOptions arg0) {
		return delegate.buildLockRequest(arg0);
	}

	@Override
	public IdentifierLoadAccess byId(String arg0) {
		return delegate.byId(arg0);
	}

	@Override
	public IdentifierLoadAccess byId(Class arg0) {
		return delegate.byId(arg0);
	}

	@Override
	public NaturalIdLoadAccess byNaturalId(String arg0) {
		return delegate.byNaturalId(arg0);
	}

	@Override
	public NaturalIdLoadAccess byNaturalId(Class arg0) {
		return delegate.byNaturalId(arg0);
	}

	@Override
	public SimpleNaturalIdLoadAccess bySimpleNaturalId(String arg0) {
		return delegate.bySimpleNaturalId(arg0);
	}

	@Override
	public SimpleNaturalIdLoadAccess bySimpleNaturalId(Class arg0) {
		return delegate.bySimpleNaturalId(arg0);
	}

	@Override
	public void disableFetchProfile(String arg0) throws UnknownProfileException {
		delegate.disableFetchProfile(arg0);
	}

	@Override
	public <T> T doReturningWork(ReturningWork<T> arg0)
			throws HibernateException {
		return delegate.doReturningWork(arg0);
	}

	@Override
	public void doWork(Work arg0) throws HibernateException {
		delegate.doWork(arg0);
	}

	@Override
	public void enableFetchProfile(String arg0) throws UnknownProfileException {
		delegate.enableFetchProfile(arg0);
	}

	@Override
	public Object get(Class arg0, Serializable arg1, LockOptions arg2) {
		return delegate.get(arg0, arg1,arg2);
	}

	@Override
	public Object get(String arg0, Serializable arg1, LockOptions arg2) {
		return delegate.get(arg0, arg1,arg2);
	}

	@Override
	public LobHelper getLobHelper() {
		return delegate.getLobHelper();
	}

	@Override
	public TypeHelper getTypeHelper() {
		return delegate.getTypeHelper();
	}

	@Override
	public boolean isDefaultReadOnly() {
		return delegate.isDefaultReadOnly();
	}

	@Override
	public boolean isFetchProfileEnabled(String arg0)
			throws UnknownProfileException {
		return delegate.isFetchProfileEnabled(arg0);
	}

	@Override
	public boolean isReadOnly(Object arg0) {
		return delegate.isReadOnly(arg0);
	}

	@Override
	public Object load(Class arg0, Serializable arg1, LockOptions arg2) {
		return delegate.load(arg0,arg1,arg2);
	}

	@Override
	public Object load(String arg0, Serializable arg1, LockOptions arg2) {
		return delegate.load(arg0,arg1,arg2);
	}

	@Override
	public void refresh(String arg0, Object arg1) {
		delegate.refresh(arg0,arg1);
	}

	@Override
	public void refresh(Object arg0, LockOptions arg1) {
		delegate.refresh(arg0,arg1);
	}

	@Override
	public void refresh(String arg0, Object arg1, LockOptions arg2) {
		delegate.refresh(arg0,arg1,arg2);
	}

	@Override
	public SharedSessionBuilder sessionWithOptions() {
		return delegate.sessionWithOptions();
	}

	@Override
	public void setDefaultReadOnly(boolean arg0) {
		delegate.setDefaultReadOnly(arg0);
	}
   
}
