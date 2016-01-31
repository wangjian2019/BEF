package alvin.bef.framework.base.query.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;

import alvin.bef.framework.base.query.IFilter;
import alvin.bef.framework.base.query.InvalidFilterException;
/**
 * 
 * @author Alvin
 *
 */
public class ConstantFilter implements IFilter {

    /**
     * @serial
     */
    private static final long serialVersionUID = 8616855439430319115L;

    private Object value;

    public ConstantFilter(Object value) {
        this.value = value;
    }

    public IFilter.FILTER_OPERATOR getOperator() {
        return OPERATOR_NONE;
    }

    public int getFilterType() {
        return TYPE_CONSTANT;
    }

    /**
     * @param value
     *            The value to set.
     */
    public void setValue(Object value) {
        this.value = value;
    }

    /**
     * @return Returns the value.
     */
    public Object getValue() {
        return value;
    }

    public String getString() {
        return getString(null);
    }

    public String getString(Map aliasMap) {
        return "?";
    }

    public String toString() {
        return getString();
    }
    
    public List getValues() {
        List ret = new ArrayList();
        ret.add(value);
        return ret;
    }

    public List getFields() throws InvalidFilterException {
        return Collections.EMPTY_LIST;
    }

    public IFilter appendAnd(IFilter filter) throws InvalidFilterException {
        throw new InvalidFilterException();
    }

    public IFilter appendOr(IFilter filter) throws InvalidFilterException {
        throw new InvalidFilterException();
    }
    
    public boolean equals(Object o) {
        if (o == null || !o.getClass().equals(ConstantFilter.class)) {
            return false;
        }
        
        return ObjectUtils.equals(this.value, ((ConstantFilter)o).value);
    }
}
