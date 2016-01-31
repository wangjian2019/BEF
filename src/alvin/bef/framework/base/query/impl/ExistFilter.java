package alvin.bef.framework.base.query.impl;

import java.util.ArrayList;
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
public class ExistFilter extends BaseFilter {
    /**
     * @serial
     */
    private static final long serialVersionUID = 5396926142280221620L;

    protected StringFilter selectQuery;

    protected boolean notFlag;

    public ExistFilter(StringFilter selectQuery, boolean notFlag) {
        this.selectQuery = selectQuery;
        this.notFlag = notFlag;
    }

    public IFilter getLeftHand() {
        return null;
    }

    public IFilter getRightHand() {
        return null;
    }

    public IFilter.FILTER_OPERATOR getOperator() {
        if (notFlag) {
            return OPERATOR_NOT_EXISTS;
        } else {
            return OPERATOR_EXISTS;
        }
    }

    public int getFilterType() {
        return TYPE_FILTER;
    }

    public String getString() throws InvalidFilterException {
        return getString(null);
    }

    public String getString(Map aliasMap) throws InvalidFilterException {
        StringBuffer sb = new StringBuffer();
        sb.append((!notFlag) ? " if not " : " if ").append(" exists ").append(
                selectQuery.getString(aliasMap));
        fieldName = selectQuery.getString(aliasMap);
        return sb.toString();
    }

    public List getValues() throws InvalidFilterException {
        return Collections.EMPTY_LIST;
    }

    private String fieldName = null;

    public List getFields() throws InvalidFilterException {
        List ret = new ArrayList();
        getString();
        if (fieldName != null)
            ret.add(fieldName);
        return ret;
    }
}
