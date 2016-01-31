package alvin.bef.framework.base.dao.hibernate;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import alvin.bef.framework.base.query.IFilter;
import alvin.bef.framework.base.query.IGroupBy;
import alvin.bef.framework.base.query.IJoin;
import alvin.bef.framework.base.query.ILimit;
import alvin.bef.framework.base.query.IPage;
import alvin.bef.framework.base.query.IPageExt;
import alvin.bef.framework.base.query.ISelector;
import alvin.bef.framework.base.query.ISort;
import alvin.bef.framework.base.query.InvalidFilterException;
import alvin.bef.framework.base.query.NumberOverflowException;
import alvin.bef.framework.base.query.PerformanceContext;
import alvin.bef.framework.base.query.SqlFactory;

/**
 * 
 * @author Alvin
 *
 */
public class DAOUtil {

   protected final static Log log = LogFactory.getLog(DAOUtil.class);

   static <T> StringBuffer getMainQueryString(Class<T> clazz, String alias, ISelector selector, IJoin join, IFilter filter, ISort sort) throws InvalidFilterException {
      String filterString = null;
      String aliasName = alias;
      if (aliasName == null || aliasName.trim().length() == 0) {
         aliasName = IFilter.DEFAULT_ALIAS;
      }

      if (filter != null) {
         filterString = filter.getString();
      }

      StringBuffer mainSb = new StringBuffer();

      if (selector != null) {
         String sqlSelect = selector.toString(aliasName);
         if (sqlSelect != null) {
            mainSb.append(selector.toString(aliasName));
            mainSb.append(" ");
         }
      } else if (join != null && join.isIgnoreInSelect()) {
         mainSb.append("select ").append(aliasName).append(" ");
      }
      mainSb.append("from ").append(clazz.getName()).append(" ").append(aliasName);

      // construct join-clause
      if (join != null) {
         mainSb.append(" ")
               .append(join.toString());
      }

      if (filterString != null && filterString.trim().length() > 0) {
         mainSb.append(" where ").append(filterString);
      }
      return mainSb;
   }

   static <T> Query getExistsQuery(Session session, Class<T> clazz, String alias, IJoin join, IFilter filter, boolean cacheable) {
      ISelector selector = SqlFactory.createSelector();
      selector.addField("id");
      String queryString;
      try {
         // See comment on getCountQuery()
         //
         queryString = getMainQueryString(clazz, alias, selector, join != null && join.isIgnoreInSelect() ? join : null, filter, null).toString();
      } catch (InvalidFilterException e) {
         // TODO Auto-generated catch block
         throw new RuntimeException(e);
      }
      return session.createQuery(queryString).setCacheable(cacheable).setMaxResults(1);
   }

   static <T> Query getCountQuery(Session session, Class<T> clazz, String alias, IJoin join, IFilter filter, boolean cacheable) {
      ISelector selector = SqlFactory.createSelector();
      selector.addFunc("count", "*");
      String queryString;
      try {
         //for count query, don't support join

         //Ao Kang: I think the reason for no join for counting before is because that hibernate will have exception 
         //"query specified join fetching, but the owner of the fetched association was not present in the select list" if the join is a fetch join,
         // because we don't select the main object rather than a count(*) 
         // But if we don't include join for counting here, and there are some item like " where alias.name=XX" in the where clause, 
         // we will get exception also because the "alias" is a join alias.
         queryString = getMainQueryString(clazz, alias, selector, join != null && join.isIgnoreInSelect() ? join : null, filter, null).toString();
      } catch (InvalidFilterException e) {
         // TODO Auto-generated catch block
         throw new RuntimeException(e);
      }
      return session.createQuery(queryString).setCacheable(cacheable);
   }

   public static <T> Query[] setupQuery(Session session, Class<T> clazz, String alias, ISelector selector, IJoin join, IFilter filter, ISort sort, IGroupBy groupBy, IPage page) throws HibernateException {
      try {
         String aliasName = alias;
         if (aliasName == null || aliasName.trim().isEmpty()) {
            aliasName = IFilter.DEFAULT_ALIAS;
         }

         String sortString = null;

         if (sort != null) {
            sortString = sort.getSortString();
         }

         StringBuffer mainSb = getMainQueryString(clazz, alias, selector, join, filter, sort);

         Query countQuery = null;
         if (page != null && ! (page instanceof ILimit) && needCountForLastPage(page)) {
            countQuery = getCountQuery(session, clazz, alias, join, filter, true);
         }
         if (sortString != null && sortString.length() > 0) {
            mainSb.append(" order by ").append(sortString);
         }
         if (groupBy != null && groupBy.toString() != null) {
            mainSb.append(groupBy.toString(aliasName));
         }
         Query query = session.createQuery(mainSb.toString()).setCacheable(true);
         processFilterParameter(countQuery, query, filter);
         Query[] ret = {query, countQuery};
         return ret;
      } catch (InvalidFilterException e) {
         throw new HibernateException(e);
      }
   }
   
   /*
    * Is the page request potentially need count(*)?
    * For IPage not IPageExt, always return true.
    * For IPageExt, when pageNo is -1 (IPageExt.LastPage means last page) or isGotoLastPageWhenOutOfPages
    * is true then return true, otherwise false.
    */
   private static boolean needCountForLastPage(IPage page) {
      if (page instanceof IPageExt) {
         IPageExt pageExt = (IPageExt) page;
         return pageExt.getCurrentPage() == IPageExt.LAST_PAGE || pageExt.isGotoLastPageWhenOutOfPages();
      } else {
         return true;
      }
   }

   public static <T> Query[] setupQuery(Session session, Class<T> clazz, String alias, IJoin join, IFilter filter, ISort sort, IPage page) throws HibernateException {
      return setupQuery(session, clazz, alias, null, join, filter, sort, null, page);
   }

   static void processFilterParameter(Query countQuery, Query query, IFilter filter) throws InvalidFilterException {
      List<?> filterValues = null;
      if (filter != null) {
         filterValues = filter.getValues();
      }
      if (filterValues != null && !filterValues.isEmpty()) {
         int index = 0;
         for (Iterator<?> ite = filterValues.iterator(); ite.hasNext();) {
            Object val = ite.next();
            if (countQuery != null) {
               countQuery = setParameter(countQuery, index, val);
            }
            if (query != null) {
               query = setParameter(query, index, val);
            }
            index++;
         }
      }
   }

   static Query setParameter(Query query, int index, Object value) {
      Query ret;
      if (value instanceof Boolean) {
         ret = query.setBoolean(index, ((Boolean) value).booleanValue());
      } else if (value instanceof Byte) {
         ret = query.setByte(index, ((Byte) value).byteValue());
      } else if (value instanceof Character) {
         ret = query.setCharacter(index, ((Character) value).charValue());
      } else if (value instanceof Double) {
         ret = query.setDouble(index, ((Double) value).doubleValue());
      } else if (value instanceof Float) {
         ret = query.setFloat(index, ((Float) value).floatValue());
      } else if (value instanceof Integer) {
         ret = query.setInteger(index, ((Integer) value).intValue());
      } else if (value instanceof Long) {
         ret = query.setLong(index, ((Long) value).longValue());
      } else if (value instanceof Short) {
         ret = query.setShort(index, ((Short) value).shortValue());
      } else if (value instanceof String) {
         ret = query.setString(index, (String) value);
      } else if (value instanceof byte[]) {
         ret = query.setBinary(index, (byte[]) value);
      } else if (value instanceof BigDecimal) {
         ret = query.setBigDecimal(index, (BigDecimal) value);
      } else if (value instanceof BigInteger) {
         ret = query.setBigInteger(index, (BigInteger) value);
      } else if (value instanceof Date) {
         ret = query.setDate(index, (Date) value);
      } else if (value instanceof Time) {
         ret = query.setTime(index, (Time) value);
      } else if (value instanceof Timestamp) {
         ret = query.setTimestamp(index, (Timestamp) value);
      } else if (value instanceof java.util.Date) {
         ret = query.setDate(index, (java.util.Date) value);
      } else if (value instanceof Locale) {
         ret = query.setLocale(index, (Locale) value);
      } else {
         ret = query.setParameter(index, value);
      }
      return ret;
   }

   @SuppressWarnings("unchecked")
   static <T> List<?> processJoinFind(Session session, Class<T> clazz, String alias, IJoin join, IFilter filter, ISort sort, IPage page) {
      Query[] queries = setupQuery(session, clazz, alias, join, filter, sort, page);
      return processFind(queries, page);
   }

   private static <T> List<?> processFind(Query[] queries, IPage page) {
      if (page instanceof IPageExt) {
         IPageExt pageExt = (IPageExt) page;
         if (pageExt.getCurrentPage() > 0 && pageExt.isGotoLastPageWhenOutOfPages()) {
            try {
               pageExt.getStartRowPosition();
            } catch (NumberOverflowException ex) {
               pageExt.setCurrentPage(IPageExt.LAST_PAGE);
            }
         }
         List<?> results = innerProcessFind(queries, pageExt);
         if (results.isEmpty() && pageExt.isGotoLastPageWhenOutOfPages()
               && pageExt.getTotalRecords() == IPageExt.UNKNOWN_TOTAL) {
            pageExt.setCurrentPage(IPageExt.LAST_PAGE); // retrieve last page.
            results = innerProcessFind(queries, pageExt);
         }
         return results;
      } else {
         return innerProcessFind(queries[0], queries[1], page);
      }
   }

   private static <T> List<?> innerProcessFind(Query query, Query countQuery, IPage page) {
      if (page != null && countQuery != null && ! (page instanceof ILimit)) {
         long begin = System.currentTimeMillis();
         Long size = (Long) countQuery.uniqueResult();
         long time = System.currentTimeMillis() - begin;
         PerformanceContext.logSqlTime(time);
         if (log.isTraceEnabled()) {
            log.trace("Query count string: " + countQuery.getQueryString() + "\n\tUse: " + (time) + "ms");
         }
         if (size != null) {
            page.setTotalRecords(longToInt(size));
         }
         query.setFirstResult(page.getStartRowPosition());
         if (page.getRecordsPerPage() > 0) {
            query.setMaxResults(page.getRecordsPerPage());
         }
      }
      if (page instanceof ILimit) {
          ILimit limit = (ILimit) page;
          query.setFirstResult(limit.getFirstResult());
          query.setMaxResults(limit.getMaxResults());
      }
      long begin = System.currentTimeMillis();
      List<T> ret = query.list();
      long time = System.currentTimeMillis() - begin;
      PerformanceContext.logSqlTime(time);
      if (log.isTraceEnabled()) {
         log.trace("Query string: " + query.getQueryString() + "\n\tUse: " + (time) + "ms");
      }

      return ret;
   }

   private static <T> List<?> innerProcessFind(Query[] queries, IPageExt page) {
      Query query = queries[0];
      if (page.getCurrentPage() == IPageExt.LAST_PAGE) {
         Query countQuery = queries[1];
         long begin = System.currentTimeMillis();
         Long size = (Long) countQuery.uniqueResult();
         long time = System.currentTimeMillis() - begin;
         PerformanceContext.logSqlTime(time);
         if (log.isTraceEnabled()) {
            log.trace("Query count string: " + countQuery.getQueryString() + "\n\tUse: " + (time) + "ms");
         }
         page.setTotalRecords(longToInt(size));
         int totalPages = page.getTotalPages();
         if (totalPages == 0) {
            page.setCurrentPage(1);
            page.setLastPage(true);
            return Collections.<T>emptyList();
         }
         page.setCurrentPage(totalPages);
      }
      query.setFirstResult(page.getStartRowPosition());
      query.setMaxResults(page.getRecordsPerPage() + 1); // select one more record, so we know has next page.

      long begin = System.currentTimeMillis();
      List<T> ret = query.list();
      long time = System.currentTimeMillis() - begin;
      PerformanceContext.logSqlTime(time);
      if (log.isTraceEnabled()) {
         log.trace("Query string: " + query.getQueryString() + "\n\tUse: " + (time) + "ms");
      }

      if (ret.size() > page.getRecordsPerPage()) {
         page.setLastPage(false);
         page.setTotalRecords(IPageExt.UNKNOWN_TOTAL);
         ret.remove(ret.size() - 1);
      } else {
         page.setLastPage(true);
         if (ret.size() > 0) {
            long total = (page.getCurrentPage() - 1) * page.getRecordsPerPage() + ret.size();
            page.setTotalRecords(longToInt(total));
         } else {
            page.setTotalRecords(IPageExt.UNKNOWN_TOTAL);
         }
      }

      return ret;
   }

   private static int longToInt(long number) {
      if (number > Integer.MAX_VALUE || number < Integer.MIN_VALUE) {
         throw new NumberOverflowException(number);
      }
      return (int) number;
   }

   @SuppressWarnings("unchecked")
   static <T> List<T> processFind(Session session, Class<T> clazz, IJoin join, IFilter filter, ISort sort, IPage page) {
      // This will always return a List<T> because the selector is null.
      return (List<T>) processFind(session, clazz, null, join, filter, sort, null, page);
   }

   static <T> List<?> processFind(Session session, Class<T> clazz, ISelector selector, IJoin join, IFilter filter, ISort sort, IGroupBy groupBy, IPage page) {
      Query[] queries = setupQuery(session, clazz, null, selector, join, filter, sort, groupBy, page);
      return processFind(queries, page);
   }
}
