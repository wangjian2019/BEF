package alvin.bef.framework.base.dao;

import java.util.Arrays;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.internal.SQLQueryImpl;

import alvin.bef.framework.base.dao.hibernate.ResultConverter;

/**
 * 
 * @author Alvin
 *
 */
public class ScrollUtil {
	public static <T> void scroll(Query query, ResultConverter<T> converter,
			ResultHandler<T> handler) {

		List<String> rawColumns, convertedColumns;
		String[] returnAliases = query.getReturnAliases();
		if (returnAliases != null) {
			rawColumns = Arrays.asList(returnAliases);
			convertedColumns = converter.convertColumns(rawColumns);
		} else {
			rawColumns = null;
			convertedColumns = null;
		}

		ScrollableResults results = query.scroll(ScrollMode.FORWARD_ONLY);

		try {
			// Iterator<T> i = new ScrollableResultsIterator<T>(rawColumns,
			// results, converter);
			while (results.next()) {
				T result = converter.convertCurrentResult(rawColumns, results);
				handler.handle(convertedColumns, result);
			}
		} catch (Exception e) {
			if (e instanceof RuntimeException) {
				throw (RuntimeException) e;
			} else {
				throw new RuntimeException(e);
			}
		} finally {
			results.close();
		}
	}

	public static <T> void scrollWithStreamResults(Query query,
			ResultConverter<T> converter, ResultHandler<T> handler) {

		List<String> rawColumns, convertedColumns;
		String[] returnAliases = null;

		/*
		 * Since SQLQueryImpl do not support getReturnAliases, there will be
		 * UnsupportedOperationException, returnAliases will be null, we should
		 * deal with null values in ResultConverter. Then we are able to scroll
		 * with native sql.
		 */
		if (!(query instanceof SQLQueryImpl)) {
			returnAliases = query.getReturnAliases();
		}
		if (returnAliases != null) {
			rawColumns = Arrays.asList(returnAliases);
			convertedColumns = converter.convertColumns(rawColumns);
		} else {
			rawColumns = null;
			convertedColumns = null;
		}
		query.setFetchSize(Integer.MIN_VALUE);
		ScrollableResults results = new ScrollableResultsWrapper(
				query.scroll(ScrollMode.FORWARD_ONLY));

		try {
			// Iterator<T> i = new ScrollableResultsIterator<T>(rawColumns,
			// results, converter);
			while (results.next()) {
				T result = converter.convertCurrentResult(rawColumns, results);
				handler.handle(convertedColumns, result);
			}
		} catch (Exception e) {
			if (e instanceof RuntimeException) {
				throw (RuntimeException) e;
			} else {
				throw new RuntimeException(e);
			}
		} finally {
			results.close();
		}
	}
}
