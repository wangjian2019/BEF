package alvin.bef.framework.base.exception;


/**
 * 
 * @author Alvin
 *
 */
public class BackEndException extends GenericException {

    private static final long serialVersionUID = 944712778984681916L;

    public BackEndException() {
        super();
    }

    public BackEndException(Throwable cause) {
        super(cause);
    }

    public BackEndException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public BackEndException(String message) {
        super(message);
    }
	
}
