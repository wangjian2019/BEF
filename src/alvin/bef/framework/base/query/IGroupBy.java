package alvin.bef.framework.base.query;

/**
 * 
 * @author Alvin
 *
 */
public interface IGroupBy {

	public void addField(String name);
	public String toString(String tableAliasName);
}
