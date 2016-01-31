package alvin.bef.framework.base.dao;

import java.util.List;
/**
 * 
 * @author Alvin
 *
 * @param <T>
 */
public interface ResultHandler<T> {
    void handle(List<String> columns, T result) throws Exception;
}
