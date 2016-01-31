package alvin.bef.framework.base.utils;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Currency;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

import alvin.bef.framework.base.session.UserSession;

/**
 * 
 * @author Alvin
 *
 */
public class FormatUtil {

	public static final String TIME_PATTERN = "HH:mm:ss";
	private static final String LONG_TIME_PATTERN = "HH:mm:ss z";
	private static final String SHORT_TIME_PATTERN = "h:mm:ss a";
	private static final String TIME_24_ZONE = "H:mm:ss z";

	public static final String DEFAULT_DATE_PATTERN = "MM/dd/yyyy";

	public static final String CURRENCY_SYMBOL = "\u00A4";
	private static Map<String, String[]> DATE_TIME_PATTERNS_CACHE = new ConcurrentHashMap<String, String[]>();
	private static Map<String, String> LONG_DATE_FORMAT_CACHE = new ConcurrentHashMap<String, String>();

	public static String formatNumberToString(int number, int length,
			String prefix) {
		String value = String.valueOf(number);
		StringBuilder sb = new StringBuilder();
		if (value.length() >= length)
			return sb.append(prefix).append(value).toString();
		sb.append(prefix);
		for (int i = 0; i < (length - value.length()); i++) {
			sb.append("0");
		}
		return sb.append(value).toString();
	}

	public static int getNumberFromFormatedString(String value, int length,
			String prefix) {
		String regstr = new StringBuilder(prefix).append("[0-9]+").toString();
		if (value == null || !value.matches(regstr))
			return -1;
		return Integer.valueOf(value.substring(prefix.length()));
	}

	/**
	 * Get default DecimalFormat according to Locale.
	 * 
	 * @param locale
	 * @return DecimalFormat to handle number.
	 */
	public static NumberFormat getDefaultDecimalFormat(Locale locale) {
		return NumberFormat.getNumberInstance(locale);
	}

	public static NumberFormat getCurrencyFormat(Locale locale,
			String currencyCode) {
		NumberFormat formatter = NumberFormat.getCurrencyInstance(locale);
		if (formatter instanceof DecimalFormat) {
			DecimalFormat df = (DecimalFormat) formatter;
			String pattern = df.toPattern();
			StringBuilder patter_buf = new StringBuilder();
			String[] patterns = pattern.split(";");
			int length = patterns.length;
			for (int i = 0; i < length; i++) {
				patter_buf.append(patterns[i].replace(CURRENCY_SYMBOL, "")
						.trim());
				if (i == 0 && length > 1)
					patter_buf.append(";");
			}
			df.applyPattern(patter_buf.toString());// remove the SYMBOL from
													// currency patterns
		}
		if (!org.apache.commons.lang.StringUtils.isBlank(currencyCode)) {// is
																			// Amount,
																			// need
																			// to
																			// apply
																			// currency's
																			// decimal
																			// places
			Currency currency = Currency.getInstance(currencyCode);
			formatter.setCurrency(currency);
			formatter.setMaximumFractionDigits(currency
					.getDefaultFractionDigits());
			formatter.setMinimumFractionDigits(currency
					.getDefaultFractionDigits());
		}
		return formatter;
	}

	/**
	 * Get default NumberFormat according to <b>locale</b> for percent format.
	 * 
	 * @param locale
	 * @return
	 */
	public static NumberFormat getDefaultPercentFormat(Locale locale) {
		NumberFormat percentFormat = NumberFormat.getPercentInstance(locale);
		// TODO: a default max fraction digits ??
		return percentFormat;
	}

	private static String toLongDateFormat(Locale locale, String shortPattern) {
		String longDatePattern = LONG_DATE_FORMAT_CACHE.get(locale.toString());
		if (longDatePattern == null || longDatePattern.equals("")) {

			longDatePattern = new String(shortPattern);

			if (longDatePattern.indexOf("M") == longDatePattern
					.lastIndexOf("M"))
				longDatePattern = longDatePattern.replace("M", "MM");

			if (longDatePattern.indexOf("d") == longDatePattern
					.lastIndexOf("d"))
				longDatePattern = longDatePattern.replace("d", "dd");

			if (longDatePattern.indexOf("yy") == longDatePattern
					.lastIndexOf("yy"))
				longDatePattern = longDatePattern.replace("yy", "yyyy");

			// for locale like sl_SI, default Short pattern is dd.MM.y
			if (longDatePattern.indexOf("y") == longDatePattern
					.lastIndexOf("y"))
				longDatePattern = longDatePattern.replace("y", "yyyy");

			// for zh_HK, separator is not one of . / -
			// if (locale.toString().equals("zh_HK"))
			// longDatePattern = "yyyy-MM-dd";

			LONG_DATE_FORMAT_CACHE.put(locale.toString(), longDatePattern);
		}
		return longDatePattern;
	}

	private static String removeDayPart(String datePattern) {
		String tokens = datePattern.replaceAll("M", "").replaceAll("d", "")
				.replaceAll("y", "");
		String token = tokens.substring(0, 1);
		String[] items = datePattern.split(token.equals(".") ? "\\." : token);

		if (items.length != 3)
			return datePattern;

		int pos = 0;
		for (String item : items) {
			if (item.equals("dd"))
				break;
			pos++;
		}
		switch (pos) {
		case 0:
			return items[1].concat(token).concat(items[2]);
		case 1:
			return items[0].concat(token).concat(items[2]);
		default:
			return items[0].concat(token).concat(items[1]);
		}
	}

	/**
	 * 
	 * @param phoneNumber
	 * @return US format
	 */
	public static String formattedPhoneNumber(String phoneNumber) {
		if (null != phoneNumber && phoneNumber.length() == 10
				&& Pattern.matches("\\d{10}", phoneNumber)) {
			return "(" + phoneNumber.substring(0, 3) + ") "
					+ phoneNumber.substring(3, 6) + "-"
					+ phoneNumber.substring(6);
		}
		return phoneNumber;
	}
}
