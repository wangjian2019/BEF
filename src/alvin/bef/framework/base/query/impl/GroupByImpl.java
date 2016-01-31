package alvin.bef.framework.base.query.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.Assert;

import alvin.bef.framework.base.query.IGroupBy;
/**
 * 
 * @author Alvin
 *
 */
public class GroupByImpl implements IGroupBy{

	List<String> list = new ArrayList<String>();
	@Override
	public void addField(String name) {
		Assert.notNull(name, "name must not be null");
		list.add(name);
	}
	
	public String toString(String tableAliasName){
		if(list.isEmpty())
		   return null;
		StringBuilder sb = new StringBuilder();
		sb.append(" group by ");
		if(tableAliasName != null)
			tableAliasName += ".";
		int count = 1;
		for(String name : list){
			if(count > 1)
				sb.append(",");
			if(tableAliasName != null){
				sb.append(tableAliasName)
				  .append(name);
			}
			count++;
		}
		return sb.toString();
	}

}
