package alvin.bef.framework.base.query.impl;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import alvin.bef.framework.base.query.IFilter;
import alvin.bef.framework.base.query.InvalidFilterException;

/**
 * 
 * @author Alvin
 *
 */
public class StringFilter extends BaseFilter {

    /**
     * @serial
     */
    private static final long serialVersionUID = -4622487684916342256L;

    private String strCondition;

    public StringFilter(String condition) {
        this.strCondition = condition;
    }

    public IFilter.FILTER_OPERATOR getOperator() {
        return OPERATOR_NONE;
    }

    public int getFilterType() {
        return TYPE_FILTER;
    }

    public String getString() throws InvalidFilterException {
        return getString(null);
    }

    public String getString(Map aliasMap) throws InvalidFilterException {
        return strCondition;
    }

    public List getValues() throws InvalidFilterException {
        return Collections.EMPTY_LIST;
    }

    public List getFields() throws InvalidFilterException {
        return Collections.EMPTY_LIST;
    }
}
