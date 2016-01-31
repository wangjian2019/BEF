package alvin.bef.framework.base.query;

import java.beans.PropertyDescriptor;
import java.io.Serializable;

/**
 * 
 * @author Alvin
 *
 */
public interface ICopyFilter extends Serializable{ 
	public boolean validate(PropertyDescriptor pd);
}
