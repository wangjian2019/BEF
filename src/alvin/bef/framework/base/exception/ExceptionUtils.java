package alvin.bef.framework.base.exception;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hibernate.StaleObjectStateException;
import org.hibernate.exception.LockAcquisitionException;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.dao.DataIntegrityViolationException;

import alvin.bef.framework.base.utils.HexUtil;
import alvin.bef.framework.base.utils.StringUtil;

/**
 * @author Alvin
 */
public class ExceptionUtils {

	static {
		org.apache.commons.lang.exception.ExceptionUtils
				.addCauseMethodName("getOriginalCause");
	}

	private static final String DATAINTEGRITYVIOLATIONEXCEPTION_PATTERN = "[^']*'(.+)'[^']*";
	private static final Pattern PATTERN = Pattern
			.compile(DATAINTEGRITYVIOLATIONEXCEPTION_PATTERN);

	public static final String getErrorFieldValue(
			DataIntegrityViolationException ee) {
		String rootMessage = org.apache.commons.lang.exception.ExceptionUtils
				.getRootCauseMessage(ee);
		Matcher m = PATTERN.matcher(rootMessage);
		if (m.matches()) {
			return m.group(1);
		}
		return null;
	}

	public static RuntimeException wrap(Throwable t) {
		if (t instanceof RuntimeException) {
			return (RuntimeException) t;
		} else {
			return new BackEndException(t);
		}
	}

	public static String getDescriptionWithRootCause(Throwable t) {
		Throwable rootCause = org.apache.commons.lang.exception.ExceptionUtils
				.getRootCause(t);
		if (rootCause != null && rootCause != t) {
			return t + " (caused by " + rootCause + ")";
		} else {
			return t.toString();
		}
	}

	private static final String STANDARD_FRAME_PREFIX = "       at ";

	/**
	 * Return a human-readable stack trace for the given thread.
	 */
	public static String getThreadStackTrace(Thread thread) {
		return StringUtil.toString(Arrays.asList(thread.getStackTrace()), "\n"
				+ STANDARD_FRAME_PREFIX, thread + "\n" + STANDARD_FRAME_PREFIX,
				"\n");
	}

	/**
	 * Return a human-readable stack trace for the given Throwable. This stack
	 * trace does not include causes.
	 */
	public static String getStackTrace(Throwable t) {
		return StringUtil
				.toString(Arrays.asList(t.getStackTrace()), "\n"
						+ STANDARD_FRAME_PREFIX, t + "\n"
						+ STANDARD_FRAME_PREFIX, "\n");
	}

	/**
	 * Return a human-readable stack trace for the given Throwable. This stack
	 * trace does not include causes.
	 */
	public static String getRawStackTrace(Throwable t) {
		return StringUtil.toString(Arrays.asList(t.getStackTrace()), "\n", t
				.getClass().getCanonicalName() + "\n", "\n");
	}

	/**
	 * Get a hex string "unique" to this throwable and its stack trace. This is
	 * useful because if two throwables have the same fingerprint, this means
	 * they are the same type, thrown from the same location, with the same call
	 * stack.
	 */
	public static String getFingerprint(Throwable t) {
		return md5(getRawStackTrace(t));
	}

	public static String md5(String s) {
		final byte[] toHashBytes = s.getBytes(Charset.forName("UTF8"));

		MessageDigest md5;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
		byte[] hash = md5.digest(toHashBytes);
		String hashString = HexUtil.toHexString(hash);
		return hashString;
	}

	public static String getRootCauseStackTrace(Throwable t) {
		return StringUtil.toString(Arrays
				.asList(org.apache.commons.lang.exception.ExceptionUtils
						.getRootCauseStackTrace(t)), "\n", "", "\n");
	}

	public static boolean isCausedBy(Throwable t, Class<? extends Throwable> c) {
		return org.apache.commons.lang.exception.ExceptionUtils.indexOfType(t,
				c) != -1;
	}

	/**
	 * check the given throwable instance t is caused by the exact throwable
	 * class c (not check its super or sub class) and its error message contains
	 * the given keywords
	 * 
	 * @param t
	 * @param c
	 * @param keywords
	 * @return
	 */
	public static boolean isCausedByExactlyMatch(Throwable t,
			Class<? extends Throwable> c, String keywords) {
		int index = org.apache.commons.lang.exception.ExceptionUtils
				.indexOfThrowable(t, c);
		if (index == -1)
			return false;
		if (keywords == null)
			return true;

		Throwable cause = (Throwable) org.apache.commons.lang.exception.ExceptionUtils
				.getThrowableList(t).get(index);
		String msg = cause.getMessage();
		if (msg == null)
			return false;

		return msg.toLowerCase().contains(keywords.toLowerCase());
	}

	@SuppressWarnings("unchecked")
	public static <C extends Throwable, CE extends C> CE getCause(Throwable t,
			Class<C> c) {
		int index = org.apache.commons.lang.exception.ExceptionUtils
				.indexOfType(t, c);
		return (CE) (index == -1 ? null
				: org.apache.commons.lang.exception.ExceptionUtils
						.getThrowableList(t).get(index));
	}

	public static String toString(Throwable t) {
		if (t == null) {
			return "[no exception]";
		}

		Throwable cause = org.apache.commons.lang.exception.ExceptionUtils
				.getRootCause(t);
		return t.getClass().getSimpleName()
				+ (cause != null ? " (caused by "
						+ cause.getClass().getSimpleName() + ")" : "") + ": "
				+ t.getMessage();
	}

	/**
	 * Judge it whether or not concurrency exception. such as
	 * StaleObjectStateException
	 * ,LockAcquisitionException,ConcurrencyFailureException
	 * 
	 * @param t
	 * @return
	 */
	public static boolean isCausedByConcurrency(Throwable t) {
		if (isCausedBy(t, StaleObjectStateException.class)
				|| isCausedBy(t, LockAcquisitionException.class)
				|| isCausedBy(t, ConcurrencyFailureException.class))
			return true;
		return false;
	}

}
