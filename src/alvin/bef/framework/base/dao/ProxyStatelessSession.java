package alvin.bef.framework.base.dao;

import java.io.Serializable;
import java.sql.Connection;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.StatelessSession;
import org.hibernate.Transaction;

/**
 * 
 * @author Alvin
 *
 */
public class ProxyStatelessSession implements StatelessSession {
   private final StatelessSession delegate;

   public ProxyStatelessSession(StatelessSession delegate) {
      this.delegate = delegate;
   }

   public Transaction beginTransaction() {
      return delegate.beginTransaction();
   }

   public void close() {
      delegate.close();
   }

   public Connection connection() {
      return delegate.connection();
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

   public Query createQuery(String arg0) {
      return delegate.createQuery(arg0);
   }

   public SQLQuery createSQLQuery(String arg0) throws HibernateException {
      return delegate.createSQLQuery(arg0);
   }

   public void delete(Object arg0) {
      delegate.delete(arg0);
   }

   public void delete(String arg0, Object arg1) {
      delegate.delete(arg0, arg1);
   }

   public Object get(Class arg0, Serializable arg1, LockMode arg2) {
      return delegate.get(arg0, arg1, arg2);
   }

   public Object get(Class arg0, Serializable arg1) {
      return delegate.get(arg0, arg1);
   }

   public Object get(String arg0, Serializable arg1, LockMode arg2) {
      return delegate.get(arg0, arg1, arg2);
   }

   public Object get(String arg0, Serializable arg1) {
      return delegate.get(arg0, arg1);
   }

   public Query getNamedQuery(String arg0) {
      return delegate.getNamedQuery(arg0);
   }

   public Transaction getTransaction() {
      return delegate.getTransaction();
   }

   public Serializable insert(Object arg0) {
      return delegate.insert(arg0);
   }

   public Serializable insert(String arg0, Object arg1) {
      return delegate.insert(arg0, arg1);
   }

   public void refresh(Object arg0, LockMode arg1) {
      delegate.refresh(arg0, arg1);
   }

   public void refresh(Object arg0) {
      delegate.refresh(arg0);
   }

   public void refresh(String arg0, Object arg1, LockMode arg2) {
      delegate.refresh(arg0, arg1, arg2);
   }

   public void refresh(String arg0, Object arg1) {
      delegate.refresh(arg0, arg1);
   }

   public void update(Object arg0) {
      delegate.update(arg0);
   }

   public void update(String arg0, Object arg1) {
      delegate.update(arg0, arg1);
   }

@Override
public String getTenantIdentifier() {
	delegate.getTenantIdentifier();
	return null;
}
   
}
