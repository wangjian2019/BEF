package alvin.bef.framework.base.query;

import java.io.Serializable;

/**
 * 
 * @author Alvin
 *
 */
public interface IJoin extends Serializable {

	static enum JoinType {
		leftJoin, leftJoinFetch, rightJoin, rightJoinFetch, innerJoin, innerJoinFetch, cartesianJoin
	}

	IJoin appendJoin(IJoin join);

	IJoin getNextJoin();

	JoinType getJoinType();

	void setJoinType(JoinType type);

	boolean isIgnoreInSelect();

	void setIgnoreInSelect(boolean ignoreInSelect);
}
