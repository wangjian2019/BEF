package alvin.bef.framework.base.query;

import alvin.bef.framework.base.query.impl.Sort;

public class SortFactory {
    public static ISort createSort(String varName) {
        ISort ret = new Sort(varName);
        return ret;
    }

    public static ISort createSort(String varName, int direction) {
        ISort ret = new Sort(varName, direction);
        return ret;
    }

    public static ISort createSort(String logicName, String varName,
            int direction) {
        ISort ret = new Sort(logicName, varName, direction);
        return ret;
    }
}
