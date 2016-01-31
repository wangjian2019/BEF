package alvin.bef.framework.base.abstractmanager.impl;


/**
 * 
 * @author Alvin
 *
 * @param <T>
 */
final class GenericManager<T> extends AbstractBaseManager<T>{
	
	public GenericManager(Class<T> clazz){
		super(clazz);
	}
}
