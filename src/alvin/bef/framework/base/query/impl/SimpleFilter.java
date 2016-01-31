package alvin.bef.framework.base.query.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ObjectUtils;

import alvin.bef.framework.base.query.IFilter;
import alvin.bef.framework.base.query.InvalidFilterException;

/**
 * 
 * @author Alvin
 *
 */
public class SimpleFilter extends BaseFilter {

    /**
     * @serial
     */
    private static final long serialVersionUID = 584865795425680104L;

    protected IFilter leftHand;

    protected IFilter rightHand;

    public IFilter.FILTER_OPERATOR operator;

    public SimpleFilter(IFilter.FILTER_OPERATOR operator, IFilter leftHand,
            IFilter rightHand) {
        this.operator = operator;
        this.leftHand = leftHand;
        this.rightHand = rightHand;
    }

    public IFilter getLeftHand() {
        return leftHand;
    }

    public IFilter getRightHand() {
        return rightHand;
    }

    public IFilter.FILTER_OPERATOR getOperator() {
        return operator;
    }

    public int getFilterType() {
        return TYPE_FILTER;
    }

    public String getString() throws InvalidFilterException {
        StringBuffer sb = new StringBuffer();
        String left = leftHand != null ? leftHand.getString() : null;
        String right = rightHand != null ? rightHand.getString() : null;
        if (left != null) {
            if (operator == OPERATOR_AND || operator == OPERATOR_OR) {
                sb.append("(");
            }
            sb.append(left);
            if (operator == OPERATOR_AND || operator == OPERATOR_OR) {
                sb.append(")");
            }
        }
        if (left != null && right != null)
            sb.append(getOperatorString());
        if (right != null) {
            if (operator == OPERATOR_AND || operator == OPERATOR_OR) {
                sb.append("(");
            }
            sb.append(right);
            if (operator == OPERATOR_AND || operator == OPERATOR_OR) {
                sb.append(")");
            }
        }
        return sb.toString();
    }
    
    protected String getOperatorString() throws InvalidFilterException {
        if (operator.equals(OPERATOR_LIKE)) {
            return " like ";
        } else if (operator.equals(OPERATOR_EQ)) {
            return " = ";
        } else if (operator.equals(OPERATOR_NOT_EQ)) {
            return " <> ";
        } else if (operator.equals(OPERATOR_GREATER_THAN)) {
            return " > ";
        } else if (operator.equals(OPERATOR_LESS_THEN)) {
            return " < ";
        } else if (operator.equals(OPERATOR_GREATER_EQ)) {
            return " >= ";
        } else if (operator.equals(OPERATOR_LESS_EQ)) {
            return " <= ";
        } else if (operator.equals(OPERATOR_AND)) {
            if (leftHand.getFilterType() != TYPE_FILTER
                    || rightHand.getFilterType() != TYPE_FILTER) {
                throw new InvalidFilterException();
            }
            return " and ";
        } else if (operator.equals(OPERATOR_OR)) {
            return " or ";
        } else if (operator.equals(OPERATOR_BETWEEN)) {
            return " between ";
        } else {
            throw new InvalidFilterException();
        }
    }

    public List getValues() throws InvalidFilterException {
        List ret = new ArrayList();
        if (leftHand != null) {
            List left = leftHand.getValues();
            if (left != null)
                ret.addAll(left);
        }
        if (rightHand != null) {
            List right = rightHand.getValues();
            if (right != null)
                ret.addAll(right);
        }
        return ret;
    }

    public List getFields() throws InvalidFilterException {
        List ret = new ArrayList();
        if (leftHand != null) {
            List left = leftHand.getFields();
            if (left != null)
                ret.addAll(left);
        }
        if (rightHand != null) {
            List right = rightHand.getFields();
            if (right != null)
                ret.addAll(right);
        }
        return ret;
    }
    
    public boolean equals(Object o) {
        if (o == null || !o.getClass().equals(SimpleFilter.class)) {
            return false;
        }
        
        SimpleFilter other = (SimpleFilter)o;
        return ObjectUtils.equals(this.leftHand, other.leftHand) &&
               ObjectUtils.equals(this.operator, other.operator) &&
               ObjectUtils.equals(this.rightHand, other.rightHand);
    }
}
