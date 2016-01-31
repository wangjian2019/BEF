package alvin.bef.framework.base.query.impl;

import java.util.ArrayList;
import java.util.List;

import alvin.bef.framework.base.query.InvalidFilterException;
/**
 * 
 * @author Alvin
 *
 */
public class InStringFilter extends BaseInFilter {
    /**
     * @serial
     */
    private static final long serialVersionUID = -2857571771346338343L;

    private StringFilter rightHand;

    public InStringFilter(VariableFilter left, StringFilter right,
            boolean notFlag) {
        super(left, notFlag);
        this.rightHand = right;
    }

    public String getString() throws InvalidFilterException {
        StringBuffer sb = new StringBuffer();
        sb.append(getLeftHand().getString()).append(
                notFlag ? " not in " : " in ").append("(").append(
                rightHand.getString()).append(")");
        return sb.toString();
    }

    public List getValues() throws InvalidFilterException {
        List ret = new ArrayList();
        List left = getLeftHand().getValues();
        List right = rightHand.getValues();
        ret.addAll(left);
        ret.addAll(right);
        return ret;
    }

    public List getFields() throws InvalidFilterException {
        List ret = new ArrayList();
        List left = getLeftHand().getFields();
        List right = rightHand.getFields();
        ret.addAll(left);
        ret.addAll(right);
        return ret;
    }
}
