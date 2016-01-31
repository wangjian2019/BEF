package alvin.bef.framework.base.dao.hibernate;

import java.util.List;

import org.hibernate.ScrollableResults;

/**
 * 
 * @author Alvin
 *
 */
public class ToObjectArrayResultConverter implements ResultConverter<Object[]> {

    @Override
    public List<String> convertColumns(List<String> columns) {
        return columns;
    }

    @Override
    public Object[] convertCurrentResult(List<String> columnNames, ScrollableResults results) {
        return results.get();
    }

}
