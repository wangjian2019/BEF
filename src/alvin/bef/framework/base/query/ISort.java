package alvin.bef.framework.base.query;

import java.io.Serializable;

/**
 * 
 * @author Alvin
 *
 */
public interface ISort extends Serializable {
    public static final int ASC = 1;

    public static final int DESC = -1;

    public String getSortString();

    public ISort appendSort(ISort next);
}
