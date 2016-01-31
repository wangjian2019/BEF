package alvin.bef.framework.base.abstractmanager.impl;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.hibernate.FlushMode;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import alvin.bef.framework.base.abstractmanager.AbstractManager;
import alvin.bef.framework.base.dao.DAO;
import alvin.bef.framework.base.dao.ResultHandler;
import alvin.bef.framework.base.dao.hibernate.BaseDAOHibernate;
import alvin.bef.framework.base.enums.IEnum;
import alvin.bef.framework.base.exception.BackEndException;
import alvin.bef.framework.base.model.Auditable;
import alvin.bef.framework.base.model.SoftDelete;
import alvin.bef.framework.base.model.StandardObject;
import alvin.bef.framework.base.query.FSP;
import alvin.bef.framework.base.query.FilterFactory;
import alvin.bef.framework.base.query.IFilter;
import alvin.bef.framework.base.query.IGroupBy;
import alvin.bef.framework.base.query.IJoin;
import alvin.bef.framework.base.query.IPage;
import alvin.bef.framework.base.query.ISelector;
import alvin.bef.framework.base.query.ISort;
import alvin.bef.framework.base.query.hql.query.HQuery;
import alvin.bef.framework.base.query.hql.query.HQueryInfo;
import alvin.bef.framework.base.query.impl.PageInfo;
import alvin.bef.framework.base.query.impl.PageInfoExt;
import alvin.bef.framework.base.query.util.FilterUtil;
import alvin.bef.framework.base.session.UserSession;
import alvin.bef.framework.base.session.manager.SessionManager;
import alvin.bef.framework.base.spring.SpringHelper;
import alvin.bef.framework.base.utils.BeanUtil;
import alvin.bef.framework.base.utils.MetadataUtil;

/**
 * 
 * @author Alvin
 *
 * @param <T>
 */
public abstract class AbstractBaseManager<T> implements AbstractManager<T> {
	private static final String FIELD_DELETED = "deleted";

	protected final Logger log = Logger.getLogger(getClass());

	protected final Class<T> entityClass;

	private final IFilter deletedFilter;
	private final IFilter notDeletedFilter;
	private final boolean isSoftDelete;

	private final boolean isEnumType;

	private final boolean hasCreator;
	private final boolean hasCreatedOn;
	private final boolean hasUpdatedOn;
	private final boolean hasUpdatedBy;

	protected DAO dao = null;

	protected SessionManager sessionManager;

	public AbstractBaseManager() {
		this(null);
	}

	@SuppressWarnings("unchecked")
	public AbstractBaseManager(Class<T> entityClass) {
		if (entityClass == null) {
			log.info("try detect from generic super class...");
			entityClass = (Class<T>) ((ParameterizedType) getClass()
					.getGenericSuperclass()).getActualTypeArguments()[0];
			log.info("Detect generic class: " + entityClass.getName());
		}
		this.entityClass = entityClass;

		isEnumType = BeanUtil.isEnumType(this.entityClass);
		isSoftDelete = SoftDelete.class.isAssignableFrom(this.entityClass);

		deletedFilter = isSoftDelete ? FilterFactory.getSimpleFilter(
				FIELD_DELETED, false) : null;
		notDeletedFilter = isSoftDelete ? FilterFactory.getSimpleFilter(
				FIELD_DELETED, true) : null;

		hasCreator = MetadataUtil.hasCreatorFlag(this.entityClass);
		hasCreatedOn = MetadataUtil.hasField(this.entityClass,
				Auditable.FIELD_CREATED_ON);
		hasUpdatedBy = MetadataUtil.hasField(this.entityClass,
				Auditable.FIELD_UPDATED_BY);
		hasUpdatedOn = MetadataUtil.hasField(this.entityClass,
				Auditable.FIELD_UPDATED_ON);
	}

	public Class<T> getEntityClass() {
		return entityClass;
	}

	@Override
	public Serializable getObjectId(T o) {
		return dao.getObjectId(o);
	}

	int minPos(int length, int pos1, int pos2) {
		pos1 = (pos1 == -1) ? length : pos1;
		pos2 = (pos2 == -1) ? length : pos2;
		return Math.min(pos1, pos2);
	}

	String buildSimpleQueryString(final String query, boolean useCount) {
		final String queryString = " " + query;
		int posSelect = queryString.indexOf("select ");
		int posFrom = queryString.indexOf(" from ");
		if (posFrom == -1) {
			throw new IllegalArgumentException(
					"No \"from\" found in queryString: " + queryString);
		}
		int posWhere = queryString.indexOf(" where ");
		int posGroupBy = queryString.indexOf(" group by ");
		int posOrderBy = queryString.indexOf(" order by ");
		int posWhereEnds = minPos(queryString.length(), posGroupBy, posOrderBy);

		String newQuery = null;
		if (useCount) {
			if (posWhere == (-1)) {
				// remove order by:
				if (posOrderBy == (-1)) {
					posOrderBy = queryString.length();
				}
				// no where:
				newQuery = "select count(*) "
						+ queryString.substring(posFrom, posWhereEnds)
						+ " where 1=1 "
						+ queryString.substring(posWhereEnds, posOrderBy);
			} else {
				// remove order by:
				if (posOrderBy == (-1)) {
					posOrderBy = queryString.length();
				}
				// has where:
				newQuery = "select count(*) "
						+ queryString.substring(posFrom, posWhere + 7)
						+ " 1=1 and ("
						+ queryString.substring(posWhere + 7, posWhereEnds)
						+ ")" + queryString.substring(posWhereEnds, posOrderBy);
			}
		} else {
			if (posWhere == (-1)) {
				// "select * from xxx order by xxx"
				// =>
				// "select * from xxx where 1=1 order by xxx"
				newQuery = queryString.substring(0, posWhereEnds)
						+ " where 1=1 " + queryString.substring(posWhereEnds);
			} else {
				// "select * from xxx where a=1 or b=2 order by xxx"
				// =>
				// "select * from xxx where 1=1 and (a=1 or b=2) order by xxx"
				newQuery = queryString.substring(0, posWhere + 7) + "1=1 and ("
						+ queryString.substring(posWhere + 7, posWhereEnds)
						+ ")" + queryString.substring(posWhereEnds);
			}
		}
		log.info("\nHQL: " + query + "\n---> " + newQuery);
		return newQuery;
	}

	@SuppressWarnings("unchecked")
	public <K> List<K> simpleQuery(String queryString, Object... params) {
		queryString = this.buildSimpleQueryString(queryString, false);
		return ((BaseDAOHibernate) dao).getHibernateTemplate()
				.find(queryString);
	}

	public HQuery query() {
		HQueryInfo query = new HQueryInfo(
				((BaseDAOHibernate) dao).getHibernateTemplate());
		return new HQuery(query);
	}

	public <K> List<K> simplePageQuery(final String queryString,
			final PageInfoExt page, Object... params) {
		final String countQuery = this
				.buildSimpleQueryString(queryString, true);
		HibernateTemplate ht = ((BaseDAOHibernate) dao).getHibernateTemplate();
		int total = ht.execute(new HibernateCallback<Integer>() {

			@Override
			public Integer doInHibernate(Session hs) throws HibernateException,
					SQLException {
				Query query = hs.createQuery(countQuery);
				Number n = (Number) query.uniqueResult();
				return n.intValue();
			}
		});
		// adjust page object:
		page.setTotalRecords(total);
		int totalPages = page.getTotalPages();
		int currentPage = page.getCurrentPage();
		if (currentPage < 1) {
			currentPage = 1;
		} else if (currentPage > totalPages) {
			currentPage = totalPages;
		}
		page.setCurrentPage(currentPage);
		final String selectQuery = this.buildSimpleQueryString(queryString,
				false);
		@SuppressWarnings({ "unchecked", "rawtypes" })
		List<K> list = ht.execute(new HibernateCallback<List>() {
			@SuppressWarnings("rawtypes")
			@Override
			public List doInHibernate(Session hs) throws HibernateException,
					SQLException {
				Query query = hs.createQuery(selectQuery);
				query.setFirstResult(page.getStartRowPosition());
				query.setMaxResults(page.getRecordsPerPage());
				return query.list();
			}
		});
		return list;
	}

	@Override
	public List<T> getObjects() {
		return this.getObjects(null, null, null, true);
	}

	@Override
	public List<T> getObjects(IFilter filter) {
		return this.getObjects(filter, null, null, true);
	}

	@Override
	public List<T> getObjects(IFilter filter, IJoin join) {
		return this.getObjects(filter, join, null, null, true);
	}

	@Override
	public List<T> getNoMultiObjects(IFilter filter) {
		return this.getObjects(filter, null, null, false);
	}

	@Override
	public List<T> getNoMultiObjects(IFilter filter, ISort sort) {
		return this.getObjects(filter, sort, null, false);
	}

	@Override
	public List<T> getNoMultiObjects(IFilter filter, ISort sort, IPage page) {
		return this.getObjects(filter, sort, page, false);
	}

	@Override
	public List<T> getDeletedObjects(IFilter filter) {
		return this.getObjects(filter, null, null, true);
	}

	@Override
	public List<T> getObjects(IFilter filter, ISort sort) {
		return this.getObjects(filter, sort, null, true);
	}

	@Override
	public List<T> getObjects(IFilter filter, ISort sort, IPage page) {
		return this.getObjects(filter, sort, page, true);
	}

	@Override
	public List<T> getDeletedObjects(IFilter filter, ISort sort) {
		return this.getObjects(filter, sort, null, true);
	}

	@Override
	public Iterator<T> iterateNoMultiObjects(IFilter filter) {
		return this.iterateObjects(filter, null, null, true);
	}

	@Override
	public Iterator<T> iterateObjects(IFilter filter) {
		return this.iterateObjects(filter, null, null, true);
	}

	@Override
	public Iterator<T> iterateObjects(IFilter filter, ISort sort) {
		return this.iterateObjects(filter, sort, null, true);
	}

	@Override
	public T getObject(Serializable id) {
		return getObject(id, true);
	}

	@Override
	public T getNoMultiObject(Serializable id) {
		return getObject(id, false);
	}

	@Override
	public T getNoMultiObject(IFilter filter) {
		return getObject(filter, false);
	}

	public T getObject(Serializable id, Boolean filterDeleted) {
		String idName = dao.getIdPropertyName(entityClass);
		return getObject(FilterFactory.getSimpleFilter(idName, id),
				filterDeleted);
	}

	public T getObjectForUpdate(Serializable id, Boolean filterDeleted) {
		String idName = dao.getIdPropertyName(entityClass);
		return getObject(FilterFactory.getSimpleFilter(idName, id),
				filterDeleted);
	}

	@Override
	public T getObject(IFilter filter) {
		return getObject(filter, true);
	}

	public T getObject(IFilter filter, Boolean filterDeleted) {
		List<T> objects = getObjects(filter, null, null, filterDeleted);
		switch (objects.size()) {
		case 0:
			return null;
		case 1:
			return objects.get(0);
		default:
			throw new BackEndException("More than one matching object.");
		}
	}

	@Override
	public List<T> getObjectsById(Serializable... ids) {
		String idName = dao.getIdPropertyName(entityClass);
		return getObjects(
				FilterFactory.getInFilter(idName, Arrays.asList(ids)), null,
				null, true);
	}

	@Override
	public List<T> getObjects(FSP fsp) {
		if (fsp != null) {
			return this.getObjects(fsp.getFilter(), fsp.getSort(),
					fsp.getPage(), true);
		} else {
			return this.getObjects(null, null, null, true);
		}
	}

	@Override
	public List<?> getNoMultiPageObjects(IJoin join, FSP fsp) {
		try {
			return dao.getObjects(entityClass, null, join,
					appendStandardFilters(fsp.getFilter(), true),
					fsp.getSort(), fsp.getPage());
		} catch (FilterWouldEliminateAllObjectsException e) {
			return Collections.emptyList();
		}
	}

	@Override
	public List<?> getPageObjects(IJoin join, FSP fsp) {
		try {
			return dao.getObjects(entityClass, null, join,
					appendStandardFilters(fsp.getFilter(), true),
					fsp.getSort(), fsp.getPage());
		} catch (FilterWouldEliminateAllObjectsException e) {
			return Collections.emptyList();
		}
	}

	@Override
	public Iterator<T> iterateObjects(FSP fsp) {
		if (fsp != null) {
			return this.iterateObjects(fsp.getFilter(), fsp.getSort(),
					fsp.getPage(), true);
		} else {
			return this.iterateObjects(null, null, null, true);
		}
	}

	@SuppressWarnings("serial")
	private static class FilterWouldEliminateAllObjectsException extends
			Exception {
	}

	public List<?> listResults(ISelector selector, IFilter filter, ISort sort,
			IGroupBy groupBy, IPage page) {
		return this.listResults(selector, filter, sort, groupBy, page, null);
	}

	public List<?> listResults(ISelector selector, IFilter filter, ISort sort,
			IGroupBy groupBy, IPage page, IJoin join) {
		try {
			return dao.listResults(entityClass, selector,
					appendStandardFilters(filter, true), sort, groupBy, page,
					join);
		} catch (FilterWouldEliminateAllObjectsException e) {
			return Collections.emptyList();
		}
	}

	public List<?> listResults(ISelector selector, IFilter filter, ISort sort,
			IGroupBy groupBy, IPage page, Boolean filterDeleted) {
		try {
			return dao.listResults(entityClass, selector,
					appendStandardFilters(filter, filterDeleted), sort,
					groupBy, page);
		} catch (FilterWouldEliminateAllObjectsException e) {
			return Collections.emptyList();
		}
	}

	@Override
	public Iterator<?> iterateResults(ISelector selector, IFilter filter,
			ISort sort, IGroupBy groupBy, IPage page) {
		try {
			return dao.iterateResults(entityClass, selector,
					appendStandardFilters(filter, true), sort, groupBy, page);
		} catch (FilterWouldEliminateAllObjectsException e) {
			return Collections.emptyList().iterator();
		}
	}

	@Override
	public boolean isRecordExists(IFilter filter, IJoin join) {
		try {
			return dao.isRecordExists(entityClass, join,
					appendStandardFilters(filter, true), false);
		} catch (FilterWouldEliminateAllObjectsException e) {
			return false;
		}
	}

	@Override
	public int getRecordCount(IFilter filter, IJoin join) {
		try {
			return dao.getRecordCount(entityClass, join,
					appendStandardFilters(filter, true), false);
		} catch (FilterWouldEliminateAllObjectsException e) {
			return 0;
		}
	}

	@Override
	public int getRecordCount(String hql, Object... params) {
		return dao.getRecordCount(hql, params);
	}

	@Override
	public int getRecordCount(String hql, Map<String, ?> params) {
		return dao.getRecordCount(hql, params);
	}

	public List<T> getObjects(IFilter filter, ISort sort, IPage page,
			Boolean filterDeleted) {
		return getObjects(filter, null, sort, page, filterDeleted);
	}

	public List<T> getObjects(IFilter filter, IJoin join, ISort sort,
			IPage page, Boolean filterDeleted) {
		try {
			return dao.getObjects(entityClass, join,
					appendStandardFilters(filter, filterDeleted), sort, page);
		} catch (FilterWouldEliminateAllObjectsException e) {
			return Collections.emptyList();
		}
	}

	@SuppressWarnings("unchecked")
	public Iterator<T> iterateObjects(IFilter filter, ISort sort, IPage page,
			Boolean filterDeleted) {
		try {
			return dao.iterateObjects(entityClass,
					appendStandardFilters(filter, filterDeleted), sort, page);
		} catch (FilterWouldEliminateAllObjectsException e) {
			return (Iterator<T>) Collections.emptyList().iterator();
		}
	}

	@Override
	public void iterateObjects(IFilter filter, ISort sort,
			ResultHandler<T> resultHandler) {
		iterateObjects(filter, sort, true, resultHandler);
	}

	@Override
	public void iterateObjects(IFilter filter, ResultHandler<T> resultHandler) {
		iterateObjects(filter, null, true, resultHandler);
	}

	@Override
	public void iterateObjects(ResultHandler<T> resultHandler) {
		iterateObjects(null, null, true, resultHandler);
	}

	public void iterateObjects(IFilter filter, ISort sort,
			Boolean filterDeleted, ResultHandler<T> resultHandler) {
		try {
			dao.iterateObjects(entityClass,
					appendStandardFilters(filter, filterDeleted), sort, null,
					resultHandler);
		} catch (FilterWouldEliminateAllObjectsException e) {
			// do nothing
		}
	}

	private IFilter appendStandardFilters(IFilter filter, Boolean filterDeleted)
			throws FilterWouldEliminateAllObjectsException {
		if (filterDeleted != null) {
			// Hard deleted entity class and they only want to see the deleted
			// ones, which is none
			if (!filterDeleted && this.notDeletedFilter == null) {
				throw new FilterWouldEliminateAllObjectsException();
			}

			filter = FilterUtil.and(filter, filterDeleted ? this.deletedFilter
					: this.notDeletedFilter);
		}

		filter = FilterUtil.and(filter, getAdditionalFilters());

		return filter;
	}

	protected IFilter getAdditionalFilters() {
		return null;
	}

	protected Boolean isEnumType() {
		return isEnumType;
	}

	/**
	 * Invoked before object get saved. User can add customized logic here.
	 * 
	 * @param o
	 *            Object going to be saved.
	 */
	protected void preSaveObject(T saving) {
	}

	protected void preSaveObject(T old, T saving) {
		preSaveObject(saving);
	}

	private final void initSystemProperties(T o, boolean isNew) {
		final Timestamp timestamp = new Timestamp(System.currentTimeMillis());

		try {
			UserSession user = sessionManager.getUserSession();

			if (user == null) {
				log.warn("UserSession is null for new/updated object " + o);
			}

			if (isNew) {
				if (user != null) {
					if (hasCreator
							&& PropertyUtils.getProperty(o,
									StandardObject.FIELD_CREATED_BY) == null) {
						PropertyUtils.setProperty(o,
								StandardObject.FIELD_CREATED_BY,
								user.getUserId());
					}

					if (hasUpdatedBy
							&& PropertyUtils.getProperty(o,
									StandardObject.FIELD_UPDATED_BY) == null) {
						PropertyUtils.setProperty(o,
								StandardObject.FIELD_UPDATED_BY,
								user.getUserId());
					}
				}

				if (hasCreatedOn
						&& PropertyUtils.getProperty(o,
								StandardObject.FIELD_CREATED_ON) == null) {
					PropertyUtils.setProperty(o,
							StandardObject.FIELD_CREATED_ON, timestamp);
				}

				if (hasUpdatedOn
						&& PropertyUtils.getProperty(o,
								StandardObject.FIELD_UPDATED_ON) == null) {
					PropertyUtils.setProperty(o,
							StandardObject.FIELD_UPDATED_ON, timestamp);
				}

			} else {
				if (user != null && hasUpdatedBy) {
					PropertyUtils.setProperty(o,
							StandardObject.FIELD_UPDATED_BY, user.getUserId());
				}

				if (hasUpdatedOn) {
					PropertyUtils.setProperty(o,
							StandardObject.FIELD_UPDATED_ON, timestamp);
				}
			}
		} catch (NoSuchMethodException e) {
			if (log.isInfoEnabled()) {
				log.info("NoSuchMethodException happened.", e);
			}
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			if (log.isInfoEnabled()) {
				log.info("InvocationTargetException happened.", e);
			}
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			if (log.isInfoEnabled()) {
				log.info("IllegalAccessException happened.", e);
			}
			throw new RuntimeException(e);
		}
	}

	/**
	 * Invoked before object get saved. User can add customized logic here.
	 * 
	 * @param o
	 *            Object going to be saved.
	 */
	protected void abstractPreSaveObject(T o, boolean isNew) {
		initSystemProperties(o, isNew);
	}

	@Override
	public void saveObject(T saving) {
		saveObject(null, saving);
	}

	public void saveSimpleObjectWithSystemInfo(T saving) {
		boolean isNew = this.isNewObject(saving);
		this.initSystemProperties(saving, isNew);
		this.dao.saveObject(saving);
	}

	@Override
	public void saveObject(T old, T saving) {
		boolean isNew = this.isNewObject(saving);
		abstractPreSaveObject(saving, isNew);
		preSaveObject(old, saving);
		dao.saveObject(saving);
		postSaveObject(saving, isNew);
		postSaveObject(saving);
	}

	protected void postSaveObject(T o, boolean isNew) {
	}

	/**
	 * Invoked after object get saved. User can add customized logic here.
	 * 
	 * @param o
	 *            Object that been saved.
	 */
	protected void postSaveObject(T o) {
	}

	/**
	 * Invoked before object get removed. User can add customized logic here.
	 * 
	 * @param o
	 *            Object that going to be removed.
	 * @param hard
	 */
	protected void preRemoveObject(T o, boolean hard) {
	}

	protected void preUndeleteObject(T o) {
	}

	/**
	 * Invoked after object get removed. User can add customized logic here.
	 * 
	 * @param o
	 *            Object that been removed.
	 * @param hard
	 */
	protected void postRemoveObject(T o, boolean hard) {
	}

	@Override
	public void removeObjectById(Serializable id) {
		T object = getObject(id);
		if (object == null) {
			throw new BackEndException("invalid id");
		}
		removeObject(object);
	}

	@Override
	public void removeObjectsById(List<Serializable> ids) {
		if (ids == null) {
			return;
		}

		for (Serializable id : ids) {
			removeObjectById(id);
		}
	}

	@Override
	public void removeObjects(List<T> objects) {
		if (objects == null)
			return;
		for (Iterator<T> it = objects.iterator(); it.hasNext();) {
			removeObject(it.next());
		}
	}

	@Override
	public void removeNoMultiObject(T o) {
		removeNoMultiObject(o, false);
	}

	public void removeNoMultiObject(T o, boolean forceHard) {
		if (o == null) {
			return;
		}

		final boolean hard = forceHard || !isSoftDelete; // this used to check
															// for the hard
															// deleted override
															// flag in the
															// entity

		preRemoveObject(o, hard);
		try {
			if (hard) {
				dao.removeObject(o);
			} else {
				PropertyUtils.setProperty(o, FIELD_DELETED, Boolean.TRUE);
				initSystemProperties(o, false);
				dao.saveObject(o);
			}
		} catch (Exception e) {
			log.error("Failed to remove.");
			throw new RuntimeException(e);
		}
		postRemoveObject(o, hard);
	}

	@Override
	public void removeObject(T o) {
		removeObject(o, false);
	}

	public void removeObject(T o, boolean forceHard) {
		if (o == null) {
			return;
		}
		removeNoMultiObject(o, forceHard);
	}

	@Override
	public void refreshObject(T o) {
		dao.refreshObject(o);
	}

	@Override
	public boolean isNewObject(T o) {
		return dao.isNewObject(o);
	}

	@Override
	public final boolean hasSoftDelete() {
		return isSoftDelete;
	}

	@Override
	public T createNewObject() {
		T entity = createWithSystemInfo();
		initEnumDefaultValues(entity);
		initAdditionalProperties(entity);
		return entity;
	}

	@Override
	public List<T> getNoMultiObjects(FSP fsp) {
		// TODO Auto-generated method stub
		return null;
	}

	protected void initAdditionalProperties(T entity) {
	}

	public T createWithSystemInfo() {
		T entity;
		try {
			Constructor<?> cons = entityClass.getDeclaredConstructor(null);
			cons.setAccessible(true);
			entity = (T) cons.newInstance(null);
		} catch (Exception e) {
			log.error("Can not create instance of " + entityClass.getName(), e);
			throw new RuntimeException(e);
		}

		// This shouldn't be called, but right now we have objects that are
		// cascade-saved and depend on this
		initSystemProperties(entity, true);
		return entity;
	}

	protected String getErrorMessage() {
		StringBuilder sb = new StringBuilder();
		sb.append("Entity Class is: ").append(this.entityClass.getName());
		return sb.toString();
	}

	protected void logError(Throwable e) {
		if (log.isEnabledFor(Level.ERROR)) {
			log.error(getErrorMessage(), e);
		}
	}

	@SuppressWarnings("deprecation")
	protected void initEnumDefaultValues(Object o) {
		// rollback Jason's change about setting this method private and final,
		// PaymentMethodManagerImpl want to
		// override this method for initialing country.
		Set<Field> fields = MetadataUtil.getQualifiedFieldObjsByType(
				entityClass, IEnum.class);
		for (Field f : fields) {
			AbstractManager<?> manager = SpringHelper.getManagerByClass(f
					.getType());
			if (manager != null) {
				Object defaultValue = manager.getObject("0");
				try {
					BeanUtils.setProperty(o, f.getName(), defaultValue);
				} catch (IllegalAccessException e) {
					if (log.isDebugEnabled())
						log.debug("init enum default value " + f.getName()
								+ " failed!!");
					throw new RuntimeException(e);
				}

				catch (InvocationTargetException e) {
					if (log.isDebugEnabled())
						log.debug("init enum default value " + f.getName()
								+ " failed!!");
					throw new RuntimeException(e);
				}
			}
		}
	}

	@Override
	public List<?> initCollection(Collection<?> c, String queryString,
			Object... params) {
		return dao.initCollection(c, queryString, params);
	}

	@Override
	public Object getObjectsByQueryString(String query, boolean isUnique,
			Long first, Long max, Object... params) {
		return dao.getObjectsByQueryString(query, isUnique, first, max, params);
	}

	@Override
	public Object getObjectsByQueryStringNamedParameters(String query,
			boolean isUnique, Long first, Long max, Map params) {
		return dao.getObjectsByQueryStringNamedParameters(query, isUnique,
				first, max, params);
	}

	public List<?> listByQueryName(final String queryName,
			final Object[] values, final Integer startIndex,
			final Integer pageAmount) {
		return dao.listByQueryName(queryName, values, startIndex, pageAmount);
	}

	public List<?> listByQueryName(final String queryName,
			final Map<String, ?> params, final Integer startIndex,
			final Integer pageAmount) {
		return dao.listByQueryName(queryName, params, startIndex, pageAmount);
	}

	protected Session getCurrentSession() {
		return SessionFactoryUtils.getSession(SpringHelper.getSessionFactory(),
				false);
	}

	protected void setFlushMode(FlushMode fm) {
		getCurrentSession().setFlushMode(fm);
	}

	protected FlushMode getFlushMode() {
		return getCurrentSession().getFlushMode();
	}

	protected void update(Object o) {
		getCurrentSession().update(o);
	}

	private Object getOriginalPropValues(Serializable id,
			boolean filterDeleted, String... propNames) {
		if (BeanUtil.isBlank(id)) {
			return null;
		}
		if (propNames == null || propNames.length == 0) {
			throw new IllegalArgumentException("propNames is empty.");
		}

		List<Object> param = new ArrayList<Object>();
		param.add(id);
		StringBuilder sb = new StringBuilder("select o.").append(propNames[0]);
		for (int i = 1; i < propNames.length; i++) {
			sb.append(", o.").append(propNames[i]);
		}
		sb.append(" from ").append(this.getEntityClass().getName())
				.append(" as o");
		if (id instanceof String) {
			sb.append(" where o.id = ?");
		} else {
			sb.append(" where o.longId = ?");
		}

		if (filterDeleted) {
			sb.append(" and o.deleted = false");
		}

		Session session = getCurrentSession();
		FlushMode mode = session.getFlushMode();
		session.setFlushMode(FlushMode.COMMIT);
		try {
			return this.getObjectsByQueryString(sb.toString(), true, null,
					null, param.toArray());
		} finally {
			session.setFlushMode(mode);
		}
	}

	@Override
	public Object getOriginalPropValues(Serializable id, String... propNames) {
		return getOriginalPropValues(id, true, propNames);
	}

	protected Object getOriginalPropValueNotFlush(String propName, String id) {
		if (BeanUtil.isBlank(propName)) {
			return null;
		}

		return getOriginalPropValues(id, false, propName);
	}

	@Override
	public void evite(Object entity) {
		dao.evite(entity);
	}

	public RuntimeException reMapException(RuntimeException ex) {
		return ex;
	}

	@Override
	public Object getEnumerationObjectByIdentifier(Object id) {
		throw new UnsupportedOperationException(
				"programmer error: AbstractBaseManager.getEnumerationObjectByIdentifier() not implemented");
	}

	@Override
	public boolean isDatabaseEnum() {
		return false;
	}

	@Override
	public void lock(T t) {
		dao.lock(t);
	}

	protected T makeEntity() {
		try {
			return this.entityClass.newInstance();
		} catch (Exception e) {
			throw new RuntimeException(
					"invalid constructor for StandardObject: "
							+ entityClass.getName(), e);
		}
	}

	protected void populateDefaults(T object) {
	}

	@Required
	public void setDAO(DAO dao) {
		this.dao = dao;
	}

	@Autowired
	public void setSessionManager(SessionManager sessionManager) {
		this.sessionManager = sessionManager;
	}

	public boolean exist(IFilter filter) {
		PageInfo pi = new PageInfo();
		pi.setCurrentRecord(0);
		pi.setRecordsPerPage(1);
		List<T> list = getNoMultiObjects(filter, null, pi);
		if (list == null || list.size() == 0) {
			return false;
		} else {
			return true;
		}
	}

	// These are all the Crud methods:
	public int executeUpdate(final String queryName, final Object[] values) {
		return dao.executeUpdatebyQueryName(queryName, values);
	}

	@Override
	public void executeUpdate(final String queryName, Map<String, Object> params) {
		dao.executeUpdatebyQueryName(queryName, params);
	}

	@Override
	public T create() {
		T entity = makeEntity();
		populateDefaults(entity);
		return entity;
	}

	@Override
	public void delete(Serializable id) {
		this.removeObjectById(id);
	}

	@Override
	public T get(Serializable id) {
		return this.getObject(id);
	}

	@Override
	public void refresh(T object) {
		this.refreshObject(object);
	}

	@Override
	public void save(T object) {
		this.saveObject(object);
	}

	@Override
	public void update(T old, T object) {
		this.saveObject(old, object);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
	public T getOldObject(Serializable id) {
		return this.getObject(id);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
	public T getOldObject(Serializable id, Boolean filterDeleted) {
		return this.getObject(id, filterDeleted);
	}

	public java.sql.Timestamp getServerNowTime() {
		return this.dao.getServerNowTime();
	}
}