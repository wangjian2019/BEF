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
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.transform.ResultTransformer;
import org.hibernate.type.Type;

/**
 * 
 * @author Alvin
 *
 */
public class ProxyQuery implements Query {

	private Query query;

	public ProxyQuery(Query query) {
		this.query = query;
	}

	public int executeUpdate() throws HibernateException {
		return query.executeUpdate();
	}

	public String[] getNamedParameters() throws HibernateException {
		return query.getNamedParameters();
	}

	public String getQueryString() {
		return query.getQueryString();
	}

	public String[] getReturnAliases() throws HibernateException {
		return query.getReturnAliases();
	}

	public Type[] getReturnTypes() throws HibernateException {
		return query.getReturnTypes();
	}

	public Iterator iterate() throws HibernateException {
		return query.iterate();
	}

	public List list() throws HibernateException {
		return query.list();
	}

	public ScrollableResults scroll() throws HibernateException {
		return query.scroll();
	}

	public ScrollableResults scroll(ScrollMode scrollMode)
			throws HibernateException {
		return query.scroll(scrollMode);
	}

	public Query setBigDecimal(int position, BigDecimal number) {
		query.setBigDecimal(position, number);
		return this;
	}

	public Query setBigDecimal(String name, BigDecimal number) {
		query.setBigDecimal(name, number);
		return this;
	}

	public Query setBigInteger(int position, BigInteger number) {
		query.setBigInteger(position, number);
		return this;
	}

	public Query setBigInteger(String name, BigInteger number) {
		query.setBigInteger(name, number);
		return this;
	}

	public Query setBinary(int position, byte[] val) {
		query.setBinary(position, val);
		return this;
	}

	public Query setBinary(String name, byte[] val) {
		query.setBinary(name, val);
		return this;
	}

	public Query setBoolean(int position, boolean val) {
		query.setBoolean(position, val);
		return this;
	}

	public Query setBoolean(String name, boolean val) {
		query.setBoolean(name, val);
		return this;
	}

	public Query setByte(int position, byte val) {
		query.setByte(position, val);
		return this;
	}

	public Query setByte(String name, byte val) {
		query.setByte(name, val);
		return this;
	}

	public Query setCacheable(boolean cacheable) {
		query.setCacheable(cacheable);
		return this;
	}

	public Query setCacheMode(CacheMode cacheMode) {
		query.setCacheMode(cacheMode);
		return this;
	}

	public Query setCacheRegion(String cacheRegion) {
		query.setCacheRegion(cacheRegion);
		return this;
	}

	public Query setCalendar(int position, Calendar calendar) {
		query.setCalendar(position, calendar);
		return this;
	}

	public Query setCalendar(String name, Calendar calendar) {
		query.setCalendar(name, calendar);
		return this;
	}

	public Query setCalendarDate(int position, Calendar calendar) {
		query.setCalendarDate(position, calendar);
		return this;
	}

	public Query setCalendarDate(String name, Calendar calendar) {
		query.setCalendarDate(name, calendar);
		return this;
	}

	public Query setCharacter(int position, char val) {
		query.setCharacter(position, val);
		return this;
	}

	public Query setCharacter(String name, char val) {
		query.setCharacter(name, val);
		return this;
	}

	public Query setComment(String comment) {
		query.setComment(comment);
		return this;
	}

	public Query setDate(int position, Date date) {
		query.setDate(position, date);
		return this;
	}

	public Query setDate(String name, Date date) {
		query.setDate(name, date);
		return this;
	}

	public Query setDouble(int position, double val) {
		query.setDouble(position, val);
		return this;
	}

	public Query setDouble(String name, double val) {
		query.setDouble(name, val);
		return this;
	}

	public Query setEntity(int position, Object val) {
		query.setEntity(position, val);
		return this;
	}

	public Query setEntity(String name, Object val) {
		query.setEntity(name, val);
		return this;
	}

	public Query setFetchSize(int fetchSize) {
		query.setFetchSize(fetchSize);
		return this;
	}

	public Query setFirstResult(int firstResult) {
		query.setFirstResult(firstResult);
		return this;
	}

	public Query setFloat(int position, float val) {
		query.setFloat(position, val);
		return this;
	}

	public Query setFloat(String name, float val) {
		query.setFloat(name, val);
		return this;
	}

	public Query setFlushMode(FlushMode flushMode) {
		query.setFlushMode(flushMode);
		return this;
	}

	public Query setInteger(int position, int val) {
		query.setInteger(position, val);
		return this;
	}

	public Query setInteger(String name, int val) {
		query.setInteger(name, val);
		return this;
	}

	public Query setLocale(int position, Locale locale) {
		query.setLocale(position, locale);
		return this;
	}

	public Query setLocale(String name, Locale locale) {
		query.setLocale(name, locale);
		return this;
	}

	public Query setLockMode(String alias, LockMode lockMode) {
		query.setLockMode(alias, lockMode);
		return this;
	}

	public Query setLong(int position, long val) {
		query.setLong(position, val);
		return this;
	}

	public Query setLong(String name, long val) {
		query.setLong(name, val);
		return this;
	}

	public Query setMaxResults(int maxResults) {
		query.setMaxResults(maxResults);
		return this;
	}

	public Query setParameter(int position, Object val, Type type) {
		query.setParameter(position, val, type);
		return this;
	}

	public Query setParameter(int position, Object val)
			throws HibernateException {
		query.setParameter(position, val);
		return this;
	}

	public Query setParameter(String name, Object val, Type type) {
		query.setParameter(name, val, type);
		return this;
	}

	public Query setParameter(String name, Object val)
			throws HibernateException {
		query.setParameter(name, val);
		return this;
	}

	public Query setParameterList(String name, Collection vals, Type type)
			throws HibernateException {
		query.setParameterList(name, vals, type);
		return this;
	}

	public Query setParameterList(String name, Collection vals)
			throws HibernateException {
		query.setParameterList(name, vals);
		return this;
	}

	public Query setParameterList(String name, Object[] vals, Type type)
			throws HibernateException {
		query.setParameterList(name, vals, type);
		return this;
	}

	public Query setParameterList(String name, Object[] vals)
			throws HibernateException {
		query.setParameterList(name, vals);
		return this;
	}

	public Query setParameters(Object[] values, Type[] types)
			throws HibernateException {
		query.setParameters(values, types);
		return this;
	}

	public Query setProperties(Map bean) throws HibernateException {
		query.setProperties(bean);
		return this;
	}

	public Query setProperties(Object bean) throws HibernateException {
		query.setProperties(bean);
		return this;
	}

	public Query setReadOnly(boolean readOnly) {
		query.setReadOnly(readOnly);
		return this;
	}

	public Query setResultTransformer(ResultTransformer transformer) {
		query.setResultTransformer(transformer);
		return this;
	}

	public Query setSerializable(int position, Serializable val) {
		query.setSerializable(position, val);
		return this;
	}

	public Query setSerializable(String name, Serializable val) {
		query.setSerializable(name, val);
		return this;
	}

	public Query setShort(int position, short val) {
		query.setShort(position, val);
		return this;
	}

	public Query setShort(String name, short val) {
		query.setShort(name, val);
		return this;
	}

	public Query setString(int position, String val) {
		query.setString(position, val);
		return this;
	}

	public Query setString(String name, String val) {
		query.setString(name, val);
		return this;
	}

	public Query setText(int position, String val) {
		query.setText(position, val);
		return this;
	}

	public Query setText(String name, String val) {
		query.setText(name, val);
		return this;
	}

	public Query setTime(int position, Date date) {
		query.setTime(position, date);
		return this;
	}

	public Query setTime(String name, Date date) {
		query.setTime(name, date);
		return this;
	}

	public Query setTimeout(int timeout) {
		query.setTimeout(timeout);
		return this;
	}

	public Query setTimestamp(int position, Date date) {
		query.setTimestamp(position, date);
		return this;
	}

	public Query setTimestamp(String name, Date date) {
		query.setTimestamp(name, date);
		return this;
	}

	public Object uniqueResult() throws HibernateException {
		return query.uniqueResult();
	}

	@Override
	public boolean isReadOnly() {
		return query.isReadOnly();
	}

	@Override
	public Query setLockOptions(LockOptions arg0) {
		query.setLockOptions(arg0);
		return this;
	}

	@Override
	public LockOptions getLockOptions() {
		return query.getLockOptions();
	}

}
