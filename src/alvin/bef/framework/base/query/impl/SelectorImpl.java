package alvin.bef.framework.base.query.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.Assert;

import alvin.bef.framework.base.query.ISelector;
/**
 * 
 * @author Alvin
 *
 */
public class SelectorImpl implements ISelector{

    private static final long serialVersionUID = 6377018352522412077L;

    protected List<Field> fields = new ArrayList<Field>();

	public SelectorImpl() {
	    
	}
	
	public SelectorImpl(String... fieldNames) {
	    for (String fieldName : fieldNames) {
	        addField(fieldName);
	    }
	}
	
	@Override
	public void addField(String fieldName, String alias) {
	   // TODO Auto-generated method stub
		Assert.notNull(fieldName, "field name must not be null");
		Field field = new Field(fieldName,alias);
		fields.add(field);
	}

	public void addField(String fieldName, String alias,String tableAlias) {
      // TODO Auto-generated method stub
      Assert.notNull(fieldName, "field name must not be null");
      Field field = new Field(null,fieldName,alias,tableAlias);
      fields.add(field);
   }
	
	@Override
	public void addField(String fieldName) {
		// TODO Auto-generated method stub
		addField(fieldName,null);
	}
	public void addFunc(String funcName,String fieldName){
		addFunc(funcName,fieldName,null);
	}
	
	public void addFunc(String funcName,String fieldName,String alias){
		Assert.notNull(funcName, "function name must not be null");
		Assert.notNull(fieldName, "field name must not be null");
		Field field = new Field(funcName,fieldName,alias);
		fields.add(field);
	}
	
	public String toString(String tableAliasName){
		if(fields.isEmpty())
			return null;
		StringBuilder sb = new StringBuilder();
		
		for(Field field : fields){
			if(sb.length() > 0)
				sb.append(",");
			if(field.isFunc()){
				sb.append(field.getFuncName())
				  .append("(");
			}
			if(tableAliasName != null && !"*".equalsIgnoreCase(field.getFieldName())){
				sb.append(tableAliasName)
				  .append(".");
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
		if(sb.length() > 0)
			sb.insert(0, "select ");
		return sb.toString();
	}
	
	public String toString(){
		return toString(null);
	}
	
	protected class Field {
		private String fieldName;
		private String funcName;
		private String aliasName;
		private boolean isFunc = false;
		private String tableAlias;
		public Field(String fieldName,String aliasName){
			this.fieldName = fieldName;
			this.aliasName = aliasName;
			
		}
		public Field(String funcName,String fieldName,String aliasName){
		   this.funcName = funcName;
			this.fieldName = fieldName;
			this.aliasName = aliasName;
			isFunc = true;
		}
		public Field(String funcName,String fieldName,String aliasName,String tableAlias){
         this.funcName = funcName;
         this.fieldName = fieldName;
         this.aliasName = aliasName;
         isFunc = funcName!=null;
         this.setTableAlias(tableAlias);
      }
		public boolean isFunc() {
			return isFunc;
		}
		public void setFunc(boolean isFunc) {
			this.isFunc = isFunc;
		}
		public String getFieldName() {
			return fieldName;
		}
		public void setFieldName(String fieldName) {
			this.fieldName = fieldName;
		}
		public String getFuncName() {
			return funcName;
		}
		public void setFuncName(String funcName) {
			this.funcName = funcName;
		}
		public String getAliasName() {
			return aliasName;
		}
		public void setAliasName(String aliasName) {
			this.aliasName = aliasName;
		}
      public void setTableAlias(String tableAlias) {
         this.tableAlias = tableAlias;
      }
      public String getTableAlias() {
         return tableAlias;
      }
	}
	

}
