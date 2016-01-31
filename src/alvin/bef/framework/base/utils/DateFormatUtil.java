package alvin.bef.framework.base.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
/**
 * 
 * @author Alvin
 *
 */
public class DateFormatUtil {
	private static ThreadLocal<SimpleDateFormat> dateTimeFormat = new ThreadLocal<SimpleDateFormat>() {
		protected SimpleDateFormat initialValue(){
			return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
		}
	};
	private static ThreadLocal<SimpleDateFormat> dateFormat = new ThreadLocal<SimpleDateFormat>(){
		protected SimpleDateFormat initialValue(){
			return new SimpleDateFormat("yyyy-MM-dd");
		}
	};

}
