package alvin.bef.framework.base.query.hql;

import java.util.Map;

import alvin.bef.framework.base.session.UserSession;

/**
 * 
 * @author Alvin
 *
 */
public interface HQLJoinRefBuilder {
	/**
	 * 
	 * Build left join string  if needed base on the pattern we defined which driven by slow queries.
	 */
	// public String buildJoinRef(HqlQuery query,Map<String,ApiField>
	// refFieldPair,String alias,UserSession userSession);

	// add by Alvin
	public String buildJoinRef(HqlQuery query, String alias,
			UserSession userSession);
}
