package alvin.bef.framework.base.model;

import javax.persistence.Basic;
import javax.persistence.MappedSuperclass;

/**
 * 
 * @author Alvin
 *
 */
@MappedSuperclass
public abstract class SoftDeleteStandardObject extends StandardObject implements
		SoftDelete {

	@Basic(optional = false)
	private boolean deleted;

	@Override
	public boolean isDeleted() {
		return deleted;
	}

	@Override
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
}
