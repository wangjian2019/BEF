package alvin.bef.framework.base.exception;

public class NonUniqueBeanException extends GenericException {

	public NonUniqueBeanException() {
		super();
	}

	public NonUniqueBeanException(String msg) {
		super(msg);
	}

	public NonUniqueBeanException(Exception ex) {
		super(ex);
	}

}
