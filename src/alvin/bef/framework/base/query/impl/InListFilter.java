package alvin.bef.framework.base.query.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import alvin.bef.framework.base.query.InvalidFilterException;
/**
 * 
 * @author Alvin
 *
 */
public class InListFilter extends BaseInFilter {
    /**
     * @serial
     */
    private static final long serialVersionUID = 2390499421040469559L;

    private List rightHand;

    public InListFilter(VariableFilter left, List right, boolean notFlag) {
        super(left, notFlag);
        this.rightHand = right;
    }

    public String getString() throws InvalidFilterException {
        StringBuffer sb = new StringBuffer();
        sb.append(getLeftHand().getString()).append(
                notFlag ? " not in " : " in ").append("(");
        boolean first = true;
        for (Iterator ite = rightHand.iterator(); ite.hasNext();) {
            if (first) {
                sb.append("?");
                first = false;
            } else {
                sb.append(", ?");
            }
            ite.next();
        }
        sb.append(")");
        return sb.toString();
    }

    public List getValues() throws InvalidFilterException {
        List ret = new ArrayList();
        List left = getLeftHand().getValues();
        List right = new ArrayList();
        for (Iterator ite = rightHand.iterator(); ite.hasNext();) {
            right.add(ite.next());
        }
        ret.addAll(left);
        ret.addAll(right);
        return ret;
    }

    public List getFields() throws InvalidFilterException {
        List ret = new ArrayList();
        ret.addAll(getLeftHand().getFields());
        return ret;
    }
}