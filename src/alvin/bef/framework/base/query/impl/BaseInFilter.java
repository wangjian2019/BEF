package alvin.bef.framework.base.query.impl;

import alvin.bef.framework.base.query.IFilter;
/**
 * 
 * @author Alvin
 *
 */
public abstract class BaseInFilter extends BaseFilter {
    private VariableFilter leftHand;

    protected boolean notFlag;

    /**
     * @param leftHand
     *            The leftHand to set.
     */
    public void setLeftHand(VariableFilter leftHand) {
        this.leftHand = leftHand;
    }

    /**
     * @return Returns the leftHand.
     */
    public VariableFilter getLeftHand() {
        return leftHand;
    }

    public BaseInFilter(VariableFilter left, boolean notFlag) {
        this.setLeftHand(left);
        this.notFlag = notFlag;
    }

    public IFilter.FILTER_OPERATOR getOperator() {
        if (notFlag) {
            return OPERATOR_NOT_IN;
        } else {
            return OPERATOR_IN;
        }
    }

    public int getFilterType() {
        return TYPE_FILTER;
    }
}
