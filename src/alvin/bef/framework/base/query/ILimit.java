package alvin.bef.framework.base.query;

/**
 * 
 * @author Alvin
 *
 */
public interface ILimit {

    /**
     * Get first result.
     * 
     * @return
     */
    int getFirstResult();

    /**
     * Get max results.
     */
    int getMaxResults();
}
