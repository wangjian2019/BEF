package alvin.bef.framework.base.exception;

public class BeanNotExistException extends GenericException {

	public BeanNotExistException() {
		super();
	}

	public BeanNotExistException(String msg) {
		super(msg);
	}

	public BeanNotExistException(Exception ex) {
		super(ex);
	}

}
