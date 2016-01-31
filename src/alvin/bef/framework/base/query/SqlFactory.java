package alvin.bef.framework.base.query;

import alvin.bef.framework.base.query.impl.GroupByImpl;
import alvin.bef.framework.base.query.impl.SelectorImpl;

public class SqlFactory {
	
	public static ISelector createSelector(){
		return new SelectorImpl();
	}
	
	public static IGroupBy createGroupBy(String groupByName){
		IGroupBy group = new GroupByImpl();
		group.addField(groupByName);
		return group;
	}
	public static IGroupBy createGroupBy(){
		IGroupBy group = new GroupByImpl();
		return group;
	}

}
