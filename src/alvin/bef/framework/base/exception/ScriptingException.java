package alvin.bef.framework.base.exception;

/**
 * 
 * @author Alvin
 *
 */
public class ScriptingException extends Exception {
    /**
     * @serial
     */
    private static final long serialVersionUID = 2173364655885261980L;

    private int errorLineNumber = -1;

    private String errorText = null;

    public int getErrorLineNumber() {
        return errorLineNumber;
    }

    public String getErrorText() {
        return errorText;
    }

    public ScriptingException() {
        super();
    }

    public ScriptingException(String message) {
        super(message);
    }

    public ScriptingException(String message, int errorLineNumber,
            String errorText) {
        super(message);
        this.errorLineNumber = errorLineNumber;
        this.errorText = errorText;
    }
}