package alvin.bef.framework.base.query.impl;

import alvin.bef.framework.base.query.ISelector;
/**
 * 
 * @author Alvin
 *
 */
public class HQLMapSelectorImpl extends SelectorImpl implements ISelector {
	private static final long serialVersionUID = 8610645844985020397L;

	
	@Override
	public String toString(String tableAliasName) {
		if(fields.isEmpty())
			return null;
		StringBuilder sb = new StringBuilder();
		
		for(Field field : fields){
			if(sb.length() > 0)
				sb.append(",");
			else{
				sb.append("new Map(");
			}
			if(field.isFunc()){
				sb.append(field.getFuncName())
				  .append("(");
			}
			
			if(!"*".equalsIgnoreCase(field.getFieldName())){
			   if(field.getTableAlias()!=null ){
			      sb.append(field.getTableAlias());
			      sb.append(".");
			   }else if(tableAliasName != null){
			      sb.append(tableAliasName);
			      sb.append(".");
			   }
			}
			
			sb.append(field.getFieldName());
			if(field.getAliasName() != null){
				sb.append(" ")
				  .append(field.getAliasName());
			}
			if(field.isFunc()){
				sb.append(")");
			}
		}
		sb.append(") ");
		if(sb.length() > 0)
			sb.insert(0, "select ");
		return sb.toString();
	}

}
