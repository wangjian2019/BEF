package alvin.bef.framework.base.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * @author Alvin
 *
 */
public class LocaleUtil {

	private static final Map<String, String> map = new LinkedHashMap<String, String>();

	/**
	 * String "en_US"
	 */
	public static final String DEFAULT_LOCALE = "en_US";

	public static Map<String, String> getDisplayLocale() {
		if (map.size() > 0)
			return map;

		Locale[] locale = getSupportedLocales();
		for (Locale l : locale) {
			map.put(l.getDisplayName(), l.toString());
		}

		List<Map.Entry<String, String>> temp = new ArrayList<Map.Entry<String, String>>(
				map.entrySet());
		Collections.sort(temp, new Comparator<Map.Entry<String, String>>() {
			@Override
			public int compare(Entry<String, String> o1,
					Entry<String, String> o2) {
				return o1.getKey().compareTo(o2.getKey());
			}
		});
		map.clear();
		for (int p = 0; p < temp.size(); p++) {
			map.put(temp.get(p).getKey(), temp.get(p).getValue());
		}
		return map;
	}

	/**
	 * 
	 * @param localeCode
	 *            localeCode should be like "en_US" or "th_TH_TH" and not null.
	 * @return Locale instance.
	 * @throws RuntimeException
	 */
	public static Locale encodeLocale(String localeCode)
			throws RuntimeException {
		if (localeCode == null)
			throw new RuntimeException("Locale string can not null.");
		String[] items = localeCode.split("_");
		if (items.length == 2) {
			return new Locale(items[0], items[1], "");
		} else if (items.length == 3) {
			return new Locale(items[0], items[1], items[2]);
		} else {
			throw new RuntimeException("Locale string should be like 'en_US'.");
		}
	}

	public static Locale[] getSupportedLocales() {
		Locale[] locales = Locale.getAvailableLocales();

		List<Locale> localeList = new ArrayList<Locale>();

		for (Locale l : locales) {
			// Java8 added new locale which has script, e.g., sr_BA_#Latn.
			if (StringUtils.isNotBlank(l.getCountry())
					&& StringUtils.isBlank(l.getVariant())
					&& StringUtils.isBlank(l.getScript())) {
				localeList.add(l);
			}
		}

		// Remove conflicting locales in different platform, and problemed
		// locales.
		localeList.remove(encodeLocale("hr_HR"));
		localeList.remove(encodeLocale("lt_LT"));
		localeList.remove(encodeLocale("zh_HK"));
		localeList.remove(encodeLocale("hi_IN"));

		return localeList.toArray(new Locale[localeList.size()]);
	}

	public static String getDisplayName(String str) {
		return encodeLocale(str).getDisplayName();
	}

	public static void setLocale(HttpServletRequest request, Locale locale) {
		if (locale != null) {
			// setting FMT_LOCALE means all resource bundles for this session
			// use this locale for messages
			javax.servlet.jsp.jstl.core.Config.set(request.getSession(),
					javax.servlet.jsp.jstl.core.Config.FMT_LOCALE, locale);
		}
	}

}