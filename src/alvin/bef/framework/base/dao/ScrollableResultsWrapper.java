package alvin.bef.framework.base.dao;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Blob;
import java.sql.Clob;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.hibernate.HibernateException;
import org.hibernate.ScrollableResults;
import org.hibernate.type.Type;

import alvin.bef.framework.base.exception.BackEndException;

/**
 * 
 * @author Alvin
 *
 */
class ScrollableResultsWrapper implements ScrollableResults {

	ScrollableResults results = null;
	int rowNumber = -1;

	public ScrollableResultsWrapper(ScrollableResults results) {
		this.results = results;
	}

	@Override
	public void afterLast() throws HibernateException {

		throw new BackEndException("don't support method");
	}

	@Override
	public void beforeFirst() throws HibernateException {

		throw new BackEndException("don't support method");
	}

	@Override
	public void close() throws HibernateException {

		results.close();
	}

	@Override
	public boolean first() throws HibernateException {

		throw new BackEndException("don't support method");
	}

	@Override
	public Object[] get() throws HibernateException {

		return results.get();
	}

	@Override
	public Object get(int index) throws HibernateException {

		return results.get(index);
	}

	@Override
	public BigDecimal getBigDecimal(int index) throws HibernateException {

		return results.getBigDecimal(index);
	}

	@Override
	public BigInteger getBigInteger(int index) throws HibernateException {

		return results.getBigInteger(index);
	}

	@Override
	public byte[] getBinary(int index) throws HibernateException {

		return results.getBinary(index);
	}

	@Override
	public Blob getBlob(int index) throws HibernateException {

		return results.getBlob(index);
	}

	@Override
	public Boolean getBoolean(int index) throws HibernateException {

		return results.getBoolean(index);
	}

	@Override
	public Byte getByte(int index) throws HibernateException {

		return results.getByte(index);
	}

	@Override
	public Calendar getCalendar(int index) throws HibernateException {

		return results.getCalendar(index);
	}

	@Override
	public Character getCharacter(int index) throws HibernateException {

		return results.getCharacter(index);
	}

	@Override
	public Clob getClob(int index) throws HibernateException {

		return results.getClob(index);
	}

	@Override
	public Date getDate(int index) throws HibernateException {

		return results.getDate(index);
	}

	@Override
	public Double getDouble(int index) throws HibernateException {

		return results.getDouble(index);
	}

	@Override
	public Float getFloat(int index) throws HibernateException {

		return results.getFloat(index);
	}

	@Override
	public Integer getInteger(int index) throws HibernateException {

		return results.getInteger(index);
	}

	@Override
	public Locale getLocale(int index) throws HibernateException {

		return results.getLocale(index);
	}

	@Override
	public Long getLong(int index) throws HibernateException {

		return results.getLong(index);
	}

	@Override
	public int getRowNumber() throws HibernateException {

		return rowNumber;
	}

	@Override
	public Short getShort(int index) throws HibernateException {

		return results.getShort(index);
	}

	@Override
	public String getString(int index) throws HibernateException {

		return results.getString(index);
	}

	@Override
	public String getText(int index) throws HibernateException {

		return results.getString(index);
	}

	@Override
	public TimeZone getTimeZone(int index) throws HibernateException {

		return results.getTimeZone(index);
	}

	@Override
	public Type getType(int index) {

		return results.getType(index);
	}

	@Override
	public boolean isFirst() throws HibernateException {

		throw new BackEndException("don't support method");
	}

	@Override
	public boolean isLast() throws HibernateException {

		throw new BackEndException("don't support method");
	}

	@Override
	public boolean last() throws HibernateException {

		throw new BackEndException("don't support method");
	}

	@Override
	public boolean next() throws HibernateException {

		boolean result = results.next();
		if (result) {
			rowNumber++;
		}
		return result;
	}

	@Override
	public boolean previous() throws HibernateException {
		throw new BackEndException("don't support method");
	}

	@Override
	public boolean scroll(int index) throws HibernateException {
		throw new BackEndException("don't support method");
	}

	@Override
	public boolean setRowNumber(int arg0) throws HibernateException {
		throw new BackEndException("don't support method");
	}

}
