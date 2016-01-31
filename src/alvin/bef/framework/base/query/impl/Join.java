package alvin.bef.framework.base.query.impl;

import alvin.bef.framework.base.query.IJoin;

/**
 * 
 * @author Alvin
 *
 */
public class Join implements IJoin {

    /**
     * @serial
     */
    private static final long serialVersionUID = 7419608174717689616L;

    private IJoin next;

    private IJoin.JoinType type;

    private String aliasName;

    private String property;

    private String byAliasName; // by which the class joined
    
    private boolean ignoreInSelect=false;


    public void setIgnoreInSelect(boolean ignoreInSelect) {
    	this.ignoreInSelect = ignoreInSelect;
    }

		public Join(String property,String aliasName,
            String byAliasName, IJoin.JoinType joinType) {
    	this.aliasName = aliasName;
    	this.byAliasName = byAliasName;
        this.property = property;
        this.type = joinType;
    }
    
    public Join(String property,String aliasName,
            String byAliasName, IJoin.JoinType joinType,boolean ignoreInSelect) {
    	this.aliasName = aliasName;
    	this.byAliasName = byAliasName;
        this.property = property;
        this.type = joinType;
        this.ignoreInSelect = ignoreInSelect;
    }    

    public IJoin getNextJoin() {
        return next;
    }

    public IJoin appendJoin(IJoin join) {
        if (this.next == null) {
            this.next = join;
        } else {
            this.next.appendJoin(join);
        }

        return this;
    }

   



    public String toString() {
    	StringBuffer buf = new StringBuffer();

        if (type == IJoin.JoinType.leftJoinFetch) {
            buf.append(" LEFT JOIN FETCH ");
        }else if(type == IJoin.JoinType.rightJoinFetch){
            buf.append(" RIGHT JOIN FETCH ");
        }else if (type == IJoin.JoinType.innerJoinFetch){
            buf.append(" INNER JOIN FETCH ");
        }else if(type == IJoin.JoinType.leftJoin) {
            buf.append(" LEFT JOIN ");
        }else if(type == IJoin.JoinType.rightJoin){
            buf.append(" RIGHT JOIN ");
        }else if (type == IJoin.JoinType.innerJoin){
            buf.append(" INNER JOIN ");
        }
        
        buf.append(" ").append(byAliasName).append(".").append(property);
        if(aliasName != null){
        	buf.append(" AS ").append(aliasName);
        }

        if (next != null) {
            buf.append(next.toString());
        }

        return buf.toString();
    }

    public IJoin.JoinType getJoinType() {
        return type;
    }

    public void setJoinType(IJoin.JoinType type) {
        this.type = type;
    }

	@Override
	public boolean isIgnoreInSelect() {
		return ignoreInSelect;
	}

    /**
     * @param logicName
     *            The logicName to set.
     */
}