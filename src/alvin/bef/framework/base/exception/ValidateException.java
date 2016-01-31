/**
 * 
 */
package alvin.bef.framework.base.exception;


/**
 * @author Alvin
 * 
 */
public class ValidateException extends ApiException {

	private static final long serialVersionUID = 6935454279577552827L;

	private String javascriptCallback = null;
	
	private String relativeFieldDotPath=null;
	
	private Class<?> beanClass = null;

	public ValidateException(ApiError error, String message) {
		this(error, message, (String)null);
	}
	
   public ValidateException(ApiError error, String message,Class<?> beanClazz,String relativeFieldDotPath) {
      this(error, message, (String)null);
      this.beanClass = beanClazz;
      this.relativeFieldDotPath = relativeFieldDotPath;
   }

   public ValidateException(ApiError error, String message, String javascriptCallback) {
        super(error, message);
        this.javascriptCallback = javascriptCallback;
    }

    public ValidateException(ApiError error, String message, Throwable cause) {
        this(error, message, cause, null);
    }

	public ValidateException(ApiError error, String message, Throwable cause, String javascriptCallback) {
		super(error, message, cause);
		this.javascriptCallback = javascriptCallback;
	}
	
	public String getRelativeFieldDotPath(){
	   return this.relativeFieldDotPath;
	}
	
	public Class<?> getBeanClass(){
	   return this.beanClass;
	}

	public String getJavascriptCallback() {
		return javascriptCallback;
	}
}
