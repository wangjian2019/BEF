package alvin.bef.framework.base.dao.hibernate;

import java.util.List;

import org.hibernate.ScrollableResults;

/**
 * 
 * @author Alvin
 *
 * @param <T>
 */
public class FirstColumnResultConverter<T> implements ResultConverter<T> {

    @Override
    public List<String> convertColumns(List<String> columns) {
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T convertCurrentResult(List<String> columnNames, ScrollableResults results) {
        return (T) results.get(0);
    }
}
