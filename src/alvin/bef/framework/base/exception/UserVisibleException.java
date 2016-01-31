package alvin.bef.framework.base.exception;

/**
 * 
 * @author Alvin
 *
 */
public class UserVisibleException extends BackEndException {

	private static final long serialVersionUID = 6389260430674033777L;

	private final boolean tracked;
	private final boolean localized;
	private final Object[] args;
	private boolean sendGack = true;

	public UserVisibleException(String message, Throwable cause,
			boolean tracked, boolean localized, Object... args) {
		super(message, cause);
		this.tracked = tracked;
		this.localized = localized;
		this.args = localized ? args : null;
	}

	public UserVisibleException(String message, Throwable cause,
			boolean localized, Object... args) {
		this(message, cause, false, localized, args);
	}

	public UserVisibleException(String message, Throwable cause) {
		this(message, cause, false, false, (Object[]) null);
	}

	public UserVisibleException(String message, boolean tracked,
			boolean localized, Object... args) {
		super(message);
		this.tracked = tracked;
		this.localized = localized;
		this.args = localized ? args : null;
	}

	public UserVisibleException(String message, boolean localized,
			Object... args) {
		this(message, false, localized, args);
	}

	public UserVisibleException(String message) {
		this(message, false, false, (Object[]) null);
	}

	public UserVisibleException(String message, boolean sendGack) {
		this(message, false, false, (Object[]) null);
		this.sendGack = sendGack;
	}

	public boolean isTracked() {
		return tracked;
	}

	public boolean isLocalized() {
		return localized;
	}

	public boolean isSendGack() {
		return sendGack;
	}

	public Object[] getArgs() {
		return args;
	}
}
