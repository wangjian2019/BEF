package alvin.bef.framework.base.dao;

import org.apache.log4j.Logger;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * 
 * @author Alvin
 *
 */
public class BaseDAO {

   private SessionFactory sessionFactory;

   protected final Logger logger = Logger.getLogger(getClass());

   /**
    * Get the current Hibernate session.
    */
   protected Session getHibernateSession() {
      return sessionFactory.getCurrentSession();
   }
   
   /**
    * open a new Hibernate session.
    */
   protected Session openNewHibernateSession() {
      return sessionFactory.openSession();
   }

   /**
    * Create a Query object from the given HQL.
    * The returned Query will automatically be instrumented for our performance logging infrastructure.
    */
   protected Query hql(String hql) {
      return getHibernateSession().createQuery(hql);
   }
   
   /**
    * Create a SQLQuery object from the given SQL.
    * The returned SQLQuery will automatically be instrumented for our performance logging infrastructure.
    */
   protected SQLQuery sql(String sql) {
      return getHibernateSession().createSQLQuery(sql);
   }

   /**
    * Acquire an exclusive (write) lock on an object in the curernt Hibernate session.
    * This is useful in cases where you need to prevent deadlocks.
    * If you aren't having deadlocks, don't use this.
    */
   protected void lockObject(Object o) {
      getHibernateSession().lock(o, LockMode.UPGRADE);
   }
   
   @Autowired
   @Qualifier("shardedSessionFactory")
   public void setSessionFactory(SessionFactory sessionFactory) {
      this.sessionFactory = sessionFactory;
   }
}
