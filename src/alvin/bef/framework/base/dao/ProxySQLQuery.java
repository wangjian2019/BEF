package alvin.bef.framework.base.dao;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.hibernate.CacheMode;
import org.hibernate.FlushMode;
import org.hibernate.LockMode;
import org.hibernate.LockOptions;
import org.hibernate.MappingException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.transform.ResultTransformer;
import org.hibernate.type.Type;

/**
 * 
 * @author Alvin
 *
 */
public class ProxySQLQuery implements SQLQuery {

	private final SQLQuery sqlQuery;

	public ProxySQLQuery(SQLQuery sqlQuery) {
		this.sqlQuery = sqlQuery;
	}

	public SQLQuery addEntity(Class entityClass) {
		sqlQuery.addEntity(entityClass);
		return this;
	}

	public SQLQuery addEntity(String alias, Class entityClass, LockMode lockMode) {
		sqlQuery.addEntity(alias, entityClass, lockMode);
		return this;
	}

	public SQLQuery addEntity(String alias, Class entityClass) {
		sqlQuery.addEntity(alias, entityClass);
		return this;
	}

	public SQLQuery addEntity(String alias, String entityName, LockMode lockMode) {
		sqlQuery.addEntity(alias, entityName, lockMode);
		return this;
	}

	public SQLQuery addEntity(String alias, String entityName) {
		sqlQuery.addEntity(alias, entityName);
		return this;
	}

	public SQLQuery addEntity(String entityName) {
		sqlQuery.addEntity(entityName);
		return this;
	}

	public SQLQuery addJoin(String alias, String path, LockMode lockMode) {
		sqlQuery.addJoin(alias, path, lockMode);
		return this;
	}

	public SQLQuery addJoin(String alias, String path) {
		sqlQuery.addJoin(alias, path);
		return this;
	}

	public SQLQuery addScalar(String columnAlias, Type type) {
		sqlQuery.addScalar(columnAlias, type);
		return this;
	}

	public SQLQuery addScalar(String columnAlias) {
		sqlQuery.addScalar(columnAlias);
		return this;
	}

	public SQLQuery addSynchronizedEntityClass(Class entityClass)
			throws MappingException {
		sqlQuery.addSynchronizedEntityClass(entityClass);
		return this;
	}

	public SQLQuery addSynchronizedEntityName(String entityName)
			throws MappingException {
		sqlQuery.addSynchronizedEntityName(entityName);
		return this;
	}

	public SQLQuery addSynchronizedQuerySpace(String querySpace) {
		sqlQuery.addSynchronizedQuerySpace(querySpace);
		return this;
	}

	public int executeUpdate() {
		return sqlQuery.executeUpdate();
	}

	public String[] getNamedParameters() {
		return sqlQuery.getNamedParameters();
	}

	public String getQueryString() {
		return sqlQuery.getQueryString();
	}

	public String[] getReturnAliases() {
		return sqlQuery.getReturnAliases();
	}

	public Type[] getReturnTypes() {
		return sqlQuery.getReturnTypes();
	}

	public Iterator iterate() {
		return sqlQuery.iterate();
	}

	public List list() {
		return sqlQuery.list();
	}

	public ScrollableResults scroll() {
		return sqlQuery.scroll();
	}

	public ScrollableResults scroll(ScrollMode scrollMode) {
		return sqlQuery.scroll(scrollMode);
	}

	public Query setBigDecimal(int position, BigDecimal number) {
		sqlQuery.setBigDecimal(position, number);
		return this;
	}

	public Query setBigDecimal(String name, BigDecimal number) {
		sqlQuery.setBigDecimal(name, number);
		return this;
	}

	public Query setBigInteger(int position, BigInteger number) {
		sqlQuery.setBigInteger(position, number);
		return this;
	}

	public Query setBigInteger(String name, BigInteger number) {
		sqlQuery.setBigInteger(name, number);
		return this;
	}

	public Query setBinary(int position, byte[] val) {
		sqlQuery.setBinary(position, val);
		return this;
	}

	public Query setBinary(String name, byte[] val) {
		sqlQuery.setBinary(name, val);
		return this;
	}

	public Query setBoolean(int position, boolean val) {
		sqlQuery.setBoolean(position, val);
		return this;
	}

	public Query setBoolean(String name, boolean val) {
		sqlQuery.setBoolean(name, val);
		return this;
	}

	public Query setByte(int position, byte val) {
		sqlQuery.setByte(position, val);
		return this;
	}

	public Query setByte(String name, byte val) {
		sqlQuery.setByte(name, val);
		return this;
	}

	public Query setCacheable(boolean cacheable) {
		sqlQuery.setCacheable(cacheable);
		return this;
	}

	public Query setCacheMode(CacheMode cacheMode) {
		sqlQuery.setCacheMode(cacheMode);
		return this;
	}

	public Query setCacheRegion(String cacheRegion) {
		sqlQuery.setCacheRegion(cacheRegion);
		return this;
	}

	public Query setCalendar(int position, Calendar calendar) {
		sqlQuery.setCalendar(position, calendar);
		return this;
	}

	public Query setCalendar(String name, Calendar calendar) {
		sqlQuery.setCalendar(name, calendar);
		return this;
	}

	public Query setCalendarDate(int position, Calendar calendar) {
		sqlQuery.setCalendarDate(position, calendar);
		return this;
	}

	public Query setCalendarDate(String name, Calendar calendar) {
		sqlQuery.setCalendarDate(name, calendar);
		return this;
	}

	public Query setCharacter(int position, char val) {
		sqlQuery.setCharacter(position, val);
		return this;
	}

	public Query setCharacter(String name, char val) {
		sqlQuery.setCharacter(name, val);
		return this;
	}

	public Query setComment(String comment) {
		sqlQuery.setComment(comment);
		return this;
	}

	public Query setDate(int position, Date date) {
		sqlQuery.setDate(position, date);
		return this;
	}

	public Query setDate(String name, Date date) {
		sqlQuery.setDate(name, date);
		return this;
	}

	public Query setDouble(int position, double val) {
		sqlQuery.setDouble(position, val);
		return this;
	}

	public Query setDouble(String name, double val) {
		sqlQuery.setDouble(name, val);
		return this;
	}

	public Query setEntity(int position, Object val) {
		sqlQuery.setEntity(position, val);
		return this;
	}

	public Query setEntity(String name, Object val) {
		sqlQuery.setEntity(name, val);
		return this;
	}

	public Query setFetchSize(int fetchSize) {
		sqlQuery.setFetchSize(fetchSize);
		return this;
	}

	public Query setFirstResult(int firstResult) {
		sqlQuery.setFirstResult(firstResult);
		return this;
	}

	public Query setFloat(int position, float val) {
		sqlQuery.setFloat(position, val);
		return this;
	}

	public Query setFloat(String name, float val) {
		sqlQuery.setFloat(name, val);
		return this;
	}

	public Query setFlushMode(FlushMode flushMode) {
		sqlQuery.setFlushMode(flushMode);
		return this;
	}

	public Query setInteger(int position, int val) {
		sqlQuery.setInteger(position, val);
		return this;
	}

	public Query setInteger(String name, int val) {
		sqlQuery.setInteger(name, val);
		return this;
	}

	public Query setLocale(int position, Locale locale) {
		sqlQuery.setLocale(position, locale);
		return this;
	}

	public Query setLocale(String name, Locale locale) {
		sqlQuery.setLocale(name, locale);
		return this;
	}

	public Query setLockMode(String alias, LockMode lockMode) {
		sqlQuery.setLockMode(alias, lockMode);
		return this;
	}

	public Query setLong(int position, long val) {
		sqlQuery.setLong(position, val);
		return this;
	}

	public Query setLong(String name, long val) {
		sqlQuery.setLong(name, val);
		return this;
	}

	public Query setMaxResults(int maxResults) {
		sqlQuery.setMaxResults(maxResults);
		return this;
	}

	public Query setParameter(int position, Object val, Type type) {
		sqlQuery.setParameter(position, val, type);
		return this;
	}

	public Query setParameter(int position, Object val) {
		sqlQuery.setParameter(position, val);
		return this;
	}

	public Query setParameter(String name, Object val, Type type) {
		sqlQuery.setParameter(name, val, type);
		return this;
	}

	public Query setParameter(String name, Object val) {
		sqlQuery.setParameter(name, val);
		return this;
	}

	public Query setParameterList(String name, Collection vals, Type type) {
		sqlQuery.setParameterList(name, vals, type);
		return this;
	}

	public Query setParameterList(String name, Collection vals) {
		sqlQuery.setParameterList(name, vals);
		return this;
	}

	public Query setParameterList(String name, Object[] vals, Type type) {
		sqlQuery.setParameterList(name, vals, type);
		return this;
	}

	public Query setParameterList(String name, Object[] vals) {
		sqlQuery.setParameterList(name, vals);
		return this;
	}

	public Query setParameters(Object[] values, Type[] types) {
		sqlQuery.setParameters(values, types);
		return this;
	}

	public Query setProperties(Map bean) {
		sqlQuery.setProperties(bean);
		return this;
	}

	public Query setProperties(Object bean) {
		sqlQuery.setProperties(bean);
		return this;
	}

	public Query setReadOnly(boolean readOnly) {
		sqlQuery.setReadOnly(readOnly);
		return this;
	}

	public SQLQuery setResultSetMapping(String name) {
		sqlQuery.setResultSetMapping(name);
		return this;
	}

	public Query setResultTransformer(ResultTransformer transformer) {
		sqlQuery.setResultTransformer(transformer);
		return this;
	}

	public Query setSerializable(int position, Serializable val) {
		sqlQuery.setSerializable(position, val);
		return this;
	}

	public Query setSerializable(String name, Serializable val) {
		sqlQuery.setSerializable(name, val);
		return this;
	}

	public Query setShort(int position, short val) {
		sqlQuery.setShort(position, val);
		return this;
	}

	public Query setShort(String name, short val) {
		sqlQuery.setShort(name, val);
		return this;
	}

	public Query setString(int position, String val) {
		sqlQuery.setString(position, val);
		return this;
	}

	public Query setString(String name, String val) {
		sqlQuery.setString(name, val);
		return this;
	}

	public Query setText(int position, String val) {
		sqlQuery.setText(position, val);
		return this;
	}

	public Query setText(String name, String val) {
		sqlQuery.setText(name, val);
		return this;
	}

	public Query setTime(int position, Date date) {
		sqlQuery.setTime(position, date);
		return this;
	}

	public Query setTime(String name, Date date) {
		sqlQuery.setTime(name, date);
		return this;
	}

	public Query setTimeout(int timeout) {
		sqlQuery.setTimeout(timeout);
		return this;
	}

	public Query setTimestamp(int position, Date date) {
		sqlQuery.setTimestamp(position, date);
		return this;
	}

	public Query setTimestamp(String name, Date date) {
		sqlQuery.setTimestamp(name, date);
		return this;
	}

	public Object uniqueResult() {
		sqlQuery.uniqueResult();
		return this;
	}

	@Override
	public LockOptions getLockOptions() {
		return sqlQuery.getLockOptions();
	}

	@Override
	public boolean isReadOnly() {
		return sqlQuery.isReadOnly();
	}

	@Override
	public Query setLockOptions(LockOptions arg0) {
		return sqlQuery.setLockOptions(arg0);
	}

	@Override
	public FetchReturn addFetch(String arg0, String arg1, String arg2) {
		return sqlQuery.addFetch(arg0, arg1, arg2);
	}

	@Override
	public SQLQuery addJoin(String arg0, String arg1, String arg2) {
		return sqlQuery.addJoin(arg0, arg1, arg2);
	}

	@Override
	public RootReturn addRoot(String arg0, String arg1) {
		return sqlQuery.addRoot(arg0, arg1);
	}

	@Override
	public RootReturn addRoot(String arg0, Class arg1) {
		return sqlQuery.addRoot(arg0, arg1);
	}
}
