package alvin.bef.framework.base.exception;

/**
 * 
 * @author Alvin
 *
 */
public class GenericException extends RuntimeException {

	private static final long serialVersionUID = 6035495955844828311L;

	public GenericException() {
		super();
	}

	public GenericException(String message) {
		super(message);
	}

	public GenericException(Throwable cause) {
		super(cause);
	}

	public GenericException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
