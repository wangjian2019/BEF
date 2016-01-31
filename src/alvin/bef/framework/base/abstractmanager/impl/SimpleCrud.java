package alvin.bef.framework.base.abstractmanager.impl;

import java.io.Serializable;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import alvin.bef.framework.base.abstractmanager.Crud;
import alvin.bef.framework.base.annotation.Filtered;
import alvin.bef.framework.base.dao.BaseDAO;
import alvin.bef.framework.base.exception.NoSessionException;
import alvin.bef.framework.base.model.Auditable;
import alvin.bef.framework.base.model.SoftDelete;
import alvin.bef.framework.base.session.UserSession;
import alvin.bef.framework.base.session.manager.SessionManager;

/**
 * 
 * @author Alvin
 *
 * @param <T>
 */
@Filtered
public class SimpleCrud<T> extends BaseDAO implements Crud<T> {

   private SessionManager sessionManager;
   private final Class<T> entityClass;
   
   protected SessionManager getSessionManager() {
      return sessionManager;
   }
   
   public SimpleCrud(Class<T> entityClass) {
      this.entityClass = entityClass;
   }
   
   protected UserSession getUserSession() {
      UserSession userSession = sessionManager.getUserSession();
      
      if (userSession == null) {
         throw new NoSessionException();
      }
      
      return userSession;
   }
   
   protected void auditUpdate(T object) {
      if (object instanceof Auditable) {
         ((Auditable) object).setAuditFieldsOnUpdate(getUserSession().getUserId());
      }
   }
      
   protected Class<T> getEntityClass() {
      return entityClass;
   }
   
   @Override
   public T create() {
      try {
         return entityClass.newInstance();
      }
      catch (InstantiationException e) {
         throw new RuntimeException(e);
      }
      catch (IllegalAccessException e) {
         throw new RuntimeException(e);
      }
   }

   @Override
   @Transactional
   public void delete(Serializable id) {
      if (SoftDelete.class.isAssignableFrom(entityClass)) {
         T object = get(id);
         if (object == null) {
            return;
         }
         
         SoftDelete softDeleteObject = (SoftDelete) object;
         if (!softDeleteObject.isDeleted()) {
            softDeleteObject.setDeleted(true);
            auditUpdate(object);
         }
      }
      else {
         getHibernateSession().delete(entityClass.getCanonicalName(), id);
      }
   }

   @Override
   @Transactional
   public T get(Serializable id) {
      return (T) getHibernateSession().get(entityClass, id);
   }

   @Override
   public void refresh(T object) {
      getHibernateSession().refresh(object);
   }

   @Override
   @Transactional
   public void save(T object) {
      if (object instanceof Auditable) {
         ((Auditable) object).setAuditFieldsOnCreate(getUserSession().getUserId());
      }
      
      getHibernateSession().save(object);
   }

   @Transactional
   protected void update(T object) {
      
      auditUpdate(object);

      Session hibernateSession = getHibernateSession();
      if (!hibernateSession.contains(object)) {
         hibernateSession.update(object);
      }
   }

   @Override
   @Transactional
   public void update(T object, T old) {
      update(object);
   }

   @Autowired
   public void setSessionManager(SessionManager sessionManager) {
      this.sessionManager = sessionManager;
   }

}
