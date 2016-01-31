package alvin.bef.framework.base.query.impl;

import java.util.ArrayList;
import java.util.List;

import alvin.bef.framework.base.query.IFilter;
import alvin.bef.framework.base.query.InvalidFilterException;
/**
 * 
 * @author Alvin
 *
 */
public class NullFilter extends BaseFilter {

    /**
     * @serial
     */
    private static final long serialVersionUID = -6734339205615344210L;

    private VariableFilter property;

    private boolean notFlag;

    public NullFilter(String name, boolean notFlag) {
        this.setProperty(new VariableFilter(name));
        this.notFlag = notFlag;
    }

    public NullFilter(String logicName, String name, boolean notFlag) {
        this.setProperty(new VariableFilter(logicName, name));
        this.notFlag = notFlag;
    }

    public IFilter.FILTER_OPERATOR getOperator() {
        if (notFlag) {
            return OPERATOR_NOT_NULL;
        } else {
            return OPERATOR_NULL;
        }
    }

    public int getFilterType() {
        return TYPE_FILTER;
    }

    public String getString() throws InvalidFilterException {
        return getProperty().getString() + (notFlag ? " is not null" : " is null");
    }

    public List getFields() throws InvalidFilterException {
        List ret = new ArrayList();
        ret.add(getProperty().getString());
        return ret;
    }

    public List getValues() throws InvalidFilterException {
        return getProperty().getValues();
    }

    /**
     * @param property
     *            The property to set.
     */
    public void setProperty(VariableFilter property) {
        this.property = property;
    }

    /**
     * @return Returns the property.
     */
    public VariableFilter getProperty() {
        return property;
    }
}
