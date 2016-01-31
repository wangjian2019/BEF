package alvin.bef.framework.base.dao;
/**
 * 
 * @author Alvin
 *
 */
public class Limit {
    private final int firstResult;
    private final int maxResults;

    public Limit(int firstResult, int maxResults) {
        this.firstResult = firstResult;
        this.maxResults = maxResults;
    }
    
    public int getFirstResult() {
        return firstResult;
    }
    
    public int getMaxResults() {
        return maxResults;
    }
    
    public String toString() {
       return "limit " + firstResult + ", " + maxResults;
    }
}
