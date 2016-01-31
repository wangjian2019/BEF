package alvin.bef.framework.base.query.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.ObjectUtils;

import alvin.bef.framework.base.query.IFilter;
import alvin.bef.framework.base.query.InvalidFilterException;

/**
 * 
 * @author Alvin
 *
 */
public class VariableFilter implements IFilter {

    /**
     * @serial
     */
    private static final long serialVersionUID = -7475757513391451953L;

    private String propertyName;

    private String logicName;

    public VariableFilter(String propertyName) {
        this.propertyName = propertyName;
    }

    /**
     * @param clazz
     * @param logicName2
     * @param propertyName2
     */
    public VariableFilter(String logicName, String propertyName) {
        this.logicName = logicName;
        this.propertyName = propertyName;
    }

    public IFilter.FILTER_OPERATOR getOperator() {
        return OPERATOR_NONE;
    }

    public int getFilterType() {
        return TYPE_CONSTANT;
    }

    /**
     * @param variableName
     *            The variableName to set.
     */
    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    /**
     * @return Returns the variableName.
     */
    public String getPropertyName() {
        return propertyName;
    }

    public String getString() {
        if (logicName != null && logicName.length() > 0) {
            return logicName + "." + propertyName;
        } else {
            return IFilter.DEFAULT_ALIAS + "." + propertyName;
        }
    }

    public String toString() {
        return getString();
    }
    
    public List getValues() {
        return Collections.EMPTY_LIST;
    }

    public List getFields() throws InvalidFilterException {
        List ret = new ArrayList();
        ret.add(propertyName);
        return ret;
    }

    public IFilter appendAnd(IFilter filter) throws InvalidFilterException {
        throw new InvalidFilterException();
    }

    public IFilter appendOr(IFilter filter) throws InvalidFilterException {
        throw new InvalidFilterException();
    }

    /**
     * @return Returns the logicName.
     */
    public String getLogicName() {
        return logicName;
    }

    /**
     * @param logicName
     *            The logicName to set.
     */
    public void setLogicName(String logicName) {
        this.logicName = logicName;
    }
    
    public boolean equals(Object o) {
        if (o == null || !o.getClass().equals(VariableFilter.class)) {
            return false;
        }
        
        return ObjectUtils.equals(getString(), ((VariableFilter)o).getString()); 
    }
}
