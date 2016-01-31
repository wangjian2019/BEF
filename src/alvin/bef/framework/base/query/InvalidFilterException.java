package alvin.bef.framework.base.query;
/**
 * 
 * @author Alvin
 *
 */
public class InvalidFilterException extends RuntimeException {
    /**
     * @serial
     */
    private static final long serialVersionUID = 4960950868380388312L;

    public InvalidFilterException() {
        super();
    }

    public InvalidFilterException(String msg) {
        super(msg);
    }

    public InvalidFilterException(Exception cause) {
        super(cause);
    }

    public InvalidFilterException(String msg, Exception cause) {
        super(msg, cause);
    }
}
