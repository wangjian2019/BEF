package alvin.bef.framework.base.query.hql.query;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import alvin.bef.framework.base.exception.BeanNotExistException;
import alvin.bef.framework.base.exception.NonUniqueBeanException;

/**
 * Run HQL and get results.
 * 
 * @author Alvin
 */
class HRunner {

   final HQueryInfo query;

   HRunner(HQueryInfo query) {
      this.query = query;
   }

   <T> T unique() {
      query.maxResults = 2;
      List<T> list = list();
      if (list.isEmpty()) {
         throw new BeanNotExistException();
      }
      if (list.size() > 1) {
         throw new NonUniqueBeanException("None-unique result fetched.");
      }
      return list.get(0);
   }

   <T> T first() {
      query.maxResults = 1;
      List<T> list = list();
      if (list.isEmpty()) {
         return null;
      }
      return list.get(0);
   }

   <T> List<T> list() {
      final String hql = toHql();
      final int offset = query.offset;
      final int maxResults = query.maxResults;
      return query.hibernateTemplate.execute(new HibernateCallback<List<T>>() {
         @SuppressWarnings("unchecked")
         public List<T> doInHibernate(Session session) throws HibernateException, SQLException {
            Query q = session.createQuery(hql);
            bindParameters(query, q, offset, maxResults);
            return q.list();
         }
      });
   }

   <T> List<T> list(Page page) {
      int total = count();
      page.setTotalRecords(total);
      if (total == 0) {
         return Collections.emptyList();
      }
      final String hql = toHql();
      final int offset = (page.getCurrentPage() - 1) * page.getRecordsPerPage();
      final int maxResults = page.getRecordsPerPage();
      return query.hibernateTemplate.execute(new HibernateCallback<List<T>>() {
         @SuppressWarnings("unchecked")
         public List<T> doInHibernate(Session session) throws HibernateException, SQLException {
            Query q = session.createQuery(hql);
            bindParameters(query, q, offset, maxResults);
            return q.list();
         }
      });
   }

   int count() {
      final String hql = toHql(false, true);
      return query.hibernateTemplate.execute(new HibernateCallback<Integer>() {
         public Integer doInHibernate(Session session) throws HibernateException, SQLException {
            Query q = session.createQuery(hql);
            bindParameters(query, q, 0, 0);
            Number num = (Number) q.uniqueResult();
            return num.intValue();
         }
      });
   }

   String toHql() {
      return toHql(false, false);
   }

   String toHql(boolean pretty) {
      return toHql(pretty, false);
   }

   String toHql(boolean pretty, boolean overrideSelectAsCount) {
      StringBuilder sb = new StringBuilder(128);
      // select ...
      if (overrideSelectAsCount) {
         sb.append("select count(*) ");
         if (pretty) {
            sb.append("\n");
         }
      }
      else {
         if (query.select != null) {
            sb.append("select ");
            appendStrings(sb, query.select, ", ");
            sb.append(" ");
            if (pretty) {
               sb.append("\n");
            }
         }
      }
      // from ...
      sb.append("from ").append(query.from).append(" ");
      if (pretty) {
         sb.append("\n");
      }
      // join ...
      if (query.joins != null) {
         for (HJoinInfo join : query.joins) {
            sb.append(join.joinType)
              .append(" ")
              .append(join.join)
              .append(" ");
            if (pretty) {
               sb.append("\n");
            }
         }
      }
      // where ...
      if (query.where != null) {
         sb.append("where ")
           .append(query.where)
           .append(" ");
         if (pretty) {
            sb.append("\n");
         }
      }
      // order by ...
      if (!overrideSelectAsCount && query.orders != null) {
         sb.append("order by ");
         appendStrings(sb, query.orders, ", ");
         sb.append(" ");
         if (pretty) {
            sb.append("\n");
         }
      }
      return sb.toString().trim();
   }

   void bindParameters(HQueryInfo query, Query q, int offset, int maxResults) {
      if (query.whereParams != null) {
         int n = 0;
         for (Object param : query.whereParams) {
            q.setParameter(n, param);
            n ++;
         }
      }
      if (offset > 0) {
         q.setFirstResult(offset);
      }
      if (maxResults > 0) {
         q.setMaxResults(maxResults);
      }
   }

   void appendStrings(StringBuilder sb, Object[] arr, String spliter) {
      if (arr.length == 0) {
         return;
      }
      for (Object s : arr) {
         sb.append(s.toString()).append(spliter);
      }
      // delete last spliter:
      int end = sb.length();
      int start = end - spliter.length();
      sb.delete(start, end);
   }
}
