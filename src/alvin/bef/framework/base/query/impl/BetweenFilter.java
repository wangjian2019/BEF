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
public class BetweenFilter extends BaseFilter {

    /**
     * @serial
     */
    private static final long serialVersionUID = -6587497347379438416L;

    private VariableFilter leftHand;

    private ConstantFilter rightHand1;

    private ConstantFilter rightHand2;

    public BetweenFilter(VariableFilter left, ConstantFilter right1,
            ConstantFilter right2) {
        this.leftHand = left;
        this.rightHand1 = right1;
        this.rightHand2 = right2;
    }

    public IFilter getLeftHand() {
        return leftHand;
    }

    public IFilter getRightHand() {
        // Not visible from out side.
        return null;
    }

    public IFilter.FILTER_OPERATOR getOperator() {
        return OPERATOR_BETWEEN;
    }

    public int getFilterType() {
        return TYPE_FILTER;
    }

    public String getString() throws InvalidFilterException {
        StringBuffer sb = new StringBuffer();
        sb.append(leftHand.getString()).append(" between ").append(
                rightHand1.getString()).append(" and ").append(
                rightHand2.getString());
        return sb.toString();
    }

    public List getFields() throws InvalidFilterException {
        List ret = new ArrayList();
        ret.addAll(leftHand.getFields());
        return ret;
    }

    public List getValues() throws InvalidFilterException {
        List ret = new ArrayList();
        List left = leftHand.getValues();
        List right1 = rightHand1.getValues();
        List right2 = rightHand2.getValues();
        ret.addAll(left);
        ret.addAll(right1);
        ret.addAll(right2);
        return ret;
    }
}
