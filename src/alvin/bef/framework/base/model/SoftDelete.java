package alvin.bef.framework.base.model;

/**
 * 
 * @author Alvin
 *
 */
public interface SoftDelete {
	static final String FIELD_DELETED = "deleted";

	boolean isDeleted();

	void setDeleted(boolean deleted);
}
