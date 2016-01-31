package alvin.bef.framework.base.query.impl;

import alvin.bef.framework.base.query.IFilter;
import alvin.bef.framework.base.query.InvalidFilterException;

/**
 * 
 * @author Alvin
 *
 */
public abstract class BaseFilter implements IFilter {

    public IFilter appendAnd(IFilter filter) throws InvalidFilterException {
        IFilter ret = this;
        if (filter != null) {
            ret = new SimpleFilter(OPERATOR_AND, this, filter);
        }
        return ret;
    }

    public IFilter appendOr(IFilter filter) throws InvalidFilterException {
        IFilter ret = this;
        if (filter != null) {
            ret = new SimpleFilter(OPERATOR_OR, this, filter);
        }
        return ret;
    }
    
    public String toString() {
        try {
            return getString();
        }
        catch (InvalidFilterException e) {
            throw new RuntimeException(e);
        }
    }
}
