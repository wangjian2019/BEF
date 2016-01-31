package alvin.bef.framework.base.query.util;

import alvin.bef.framework.base.exception.GenericException;
import alvin.bef.framework.base.query.IFilter;
import alvin.bef.framework.base.query.InvalidFilterException;

/**
 * 
 * @author Alvin
 *
 */
public class FilterUtil {
	public static IFilter and(IFilter a, IFilter b) {
		try {
			return a != null ? b != null ? a.appendAnd(b) : a : b != null ? b
					: null;
		} catch (InvalidFilterException e) {
			throw new GenericException(e);
		}
	}

	public static IFilter or(IFilter a, IFilter b) {
		try {
			return a != null ? b != null ? a.appendOr(b) : a : b != null ? b
					: null;
		} catch (InvalidFilterException e) {
			throw new GenericException(e);
		}
	}
}
