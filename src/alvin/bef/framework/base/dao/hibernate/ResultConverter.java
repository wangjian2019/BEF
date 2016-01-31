package alvin.bef.framework.base.dao.hibernate;

import java.util.List;

import org.hibernate.ScrollableResults;

/**
 * 
 * @author Alvin
 *
 * @param <T>
 */
public interface ResultConverter<T> {
    
    /**
     * Convert the raw columns from the DB results into the columns that this converter will provide.
     * @param columns The raw column names from the DB results, if known. This can be null if the columns are unknown, such as when use HQL with no select clause.
     */
    List<String> convertColumns(List<String> columns);
    
    /**
     * Convert the data in the current row of results into whatever type this converter provides.
     * @param columns The raw column names from the DB results, if known. This can be null if the columns are unknown, such as when use HQL with no select clause.
     * @param results The results. You should only look at the current row.
     */
    T convertCurrentResult(List<String> columns, ScrollableResults results);
}
