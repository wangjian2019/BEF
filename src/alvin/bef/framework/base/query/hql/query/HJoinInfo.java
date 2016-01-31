package alvin.bef.framework.base.query.hql.query;

/**
 * Hold join info to generate as:
 * joinType = "inner join"
 * join = "Cat"
 * joinAs = "c"
 * 
 * @author Alvin
 */
public class HJoinInfo {

   final String joinType;
   final String join;
   String joinAs;

   HJoinInfo(String joinType, String join, String joinAs) {
      this.joinType = joinType;
      this.join = join;
      this.joinAs = joinAs;
   }

}
