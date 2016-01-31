package alvin.bef.framework.base.query.impl;

import alvin.bef.framework.base.query.IFilter;
import alvin.bef.framework.base.query.ISort;

/**
 * 
 * @author Alvin
 *
 */
public class Sort implements ISort {
    private static final long serialVersionUID = 5800038389048270341L;

    private String variableName;

    private int direction;

    private ISort next;

    private String logicName;

    public Sort(String varName) {
        init(null, varName, ASC);
    }

    // public Sort(Class clazz, String varName) {
    // init(clazz, varName, ASC);
    // }

    public Sort(String varName, int direction) {
        init(null, varName, direction);
    }

    // public Sort(Class clazz, String varName, int direction) {
    // init(clazz, varName, direction);
    // }

    public Sort(String logicName, String varName, int direction) {
        init(logicName, varName, direction);
    }

    private void init(String logicName, String varName, int direction) {
        this.logicName = logicName;
        this.variableName = varName;
        this.direction = direction;
    }

    // private void init(Class clazz, String varName, int direction) {
    // if (clazz != null) {
    // this.className = clazz.getName();
    // }
    // else {
    // this.className = null;
    // }
    // this.variableName = varName;
    // this.direction = direction;
    // this.next = null;
    // }

    // private String toSortString(Map aliasMap) {
    // String alias = null;
    // if (className != null && aliasMap != null) {
    // alias = (String)aliasMap.get(className);
    // }
    // StringBuffer sb = new StringBuffer();
    // if (alias != null && alias.length() > 0) {
    // sb.append(alias).append(".");
    // }
    // sb.append(variableName);
    // if (direction == ASC) {
    // sb.append(" asc");
    // }
    // else {
    // sb.append(" desc");
    // }
    // return sb.toString();
    // }

    private String toSortString() {
        StringBuilder sb = new StringBuilder();
        if (logicName != null && logicName.length() > 0) {
            sb.append(logicName).append(".");
        } else {
            sb.append(IFilter.DEFAULT_ALIAS).append(".");
        }
        sb.append(variableName);
        if (direction == ASC) {
            sb.append(" asc");
        } else {
            sb.append(" desc");
        }
        return sb.toString();
    }

    @Override
    public String getSortString() {
        StringBuilder str = new StringBuilder(toSortString());
        if (next != null) {
            str.append(", ");
            str.append(next.getSortString());
        }
        return str.toString();
    }

    // public String getSortString() {
    // return getSortString(null);
    // }
    //    
    // public String getSortString(Map aliasMap) {
    // StringBuffer str = new StringBuffer(toSortString(aliasMap));
    // if (next != null) {
    // str.append(", ");
    // str.append(next.getSortString(aliasMap));
    // }
    // return str.toString();
    // }

    public ISort appendSort(ISort next) {
        if (this.next == null) {
            this.next = next;
        } else {
            this.next.appendSort(next);
        }
        return this;
    }

    /**
     * @param className
     *            The className to set.
     */
    // public void setClassName(String className) {
    // this.className = className;
    // }
    //
    // /**
    // * @return Returns the className.
    // */
    // public String getClassName() {
    // return className;
    // }
    /**
     * @param variableName
     *            The variableName to set.
     */
    public void setVariableName(String variableName) {
        this.variableName = variableName;
    }

    /**
     * @return Returns the variableName.
     */
    public String getVariableName() {
        return variableName;
    }

    /**
     * @param direction
     *            The direction to set.
     */
    public void setDirection(int direction) {
        this.direction = direction;
    }

    /**
     * @return Returns the direction.
     */
    public int getDirection() {
        return direction;
    }

    /**
     * @param next
     *            The next to set.
     */
    public void setNext(ISort next) {
        this.next = next;
    }

    /**
     * @return Returns the next.
     */
    public ISort getNext() {
        return next;
    }

    public void setLogicName(String logicName) {
        this.logicName = logicName;
    }

    public String getLogicName() {
        return logicName;
    }

    @Override
    public String toString() {
        return this.getSortString();
    }
}
