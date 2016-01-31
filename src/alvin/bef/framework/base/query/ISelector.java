package alvin.bef.framework.base.query;

import java.io.Serializable;
/**
 * 
 * @author Alvin
 *
 */
public interface ISelector extends Serializable{
	
	public void addField(String fieldName,String alias);
	public void addField(String fieldName, String alias,String tableAlias);
	public void addField(String fieldName);
	public void addFunc(String funcName,String fieldName);
	public void addFunc(String funcName,String fieldName,String alias);
	public String toString(String tableAliasName);

}
