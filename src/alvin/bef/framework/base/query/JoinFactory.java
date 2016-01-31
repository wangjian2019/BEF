package alvin.bef.framework.base.query;

import alvin.bef.framework.base.query.impl.Join;

/**
 * 
 * @author Alvin
 *
 */
public class JoinFactory {

    public static IJoin createJoin(String property,String aliasName, String byAliasName, IJoin.JoinType joinType) {
        return new Join(property,aliasName, byAliasName, joinType);
    }

    public static IJoin createJoin(String property,String aliasName, String byAliasName, IJoin.JoinType joinType, Boolean isIgnoreSelect) {
      return new Join(property,aliasName, byAliasName, joinType,isIgnoreSelect);
  }
}
