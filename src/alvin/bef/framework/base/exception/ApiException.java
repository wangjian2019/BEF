package alvin.bef.framework.base.exception;

/**
 * 
 * @author Alvin
 *
 */
public class ApiException extends BackEndException {
   private static final long serialVersionUID = -5937852666109148341L;

   private final ApiError apiError;

   public ApiException(ApiError error) {
      super();
      this.apiError = error;
   }

   public ApiException(ApiError error, Throwable cause) {
      super(cause);
      this.apiError = error;
   }

   public ApiException(ApiError error, String msg) {
      super(msg);
      this.apiError = error;
   }

   public ApiException(ApiError error, String msg, Throwable cause) {
      super(msg, cause);
      this.apiError = error;
   }

   public ApiError getApiError() {
      return this.apiError;
   }

}
