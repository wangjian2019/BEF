package alvin.bef.framework.base.exception;

import javax.servlet.http.HttpServletResponse;

/**
 * 
 * @author Alvin
 *
 */
public enum ApiError {
   API_DISABLED(HttpServletResponse.SC_FORBIDDEN, SoapErrorCode.API_DISABLED),
   CANNOT_DELETE_NOT_FOUND(HttpServletResponse.SC_NOT_FOUND, SoapErrorCode.CANNOT_DELETE),
   CANNOT_DELETE_INTERNAL_ERROR(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, SoapErrorCode.CANNOT_DELETE),
   CANNOT_DELETE_CONSTRAINT_VIOLATION(HttpServletResponse.SC_PRECONDITION_FAILED, SoapErrorCode.CANNOT_DELETE),
   CANNOT_SCHEDULE(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, SoapErrorCode.CANNOT_SCHEDULE),
   CREDIT_CARD_PROCESSING_FAILURE(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, SoapErrorCode.CREDIT_CARD_PROCESSING_FAILURE),
   DUPLICATE_VALUE(HttpServletResponse.SC_PRECONDITION_FAILED, SoapErrorCode.DUPLICATE_VALUE),
   INVALID_FIELD(HttpServletResponse.SC_BAD_REQUEST, SoapErrorCode.INVALID_FIELD),
   INVALID_LOGIN_FORBIDDEN(HttpServletResponse.SC_FORBIDDEN, SoapErrorCode.INVALID_LOGIN),
   INVALID_LOGIN_CREDENTIALS(HttpServletResponse.SC_UNAUTHORIZED, SoapErrorCode.INVALID_LOGIN),
   INVALID_SESSION(HttpServletResponse.SC_UNAUTHORIZED, SoapErrorCode.INVALID_SESSION),
   INVALID_TYPE(HttpServletResponse.SC_BAD_REQUEST, SoapErrorCode.INVALID_TYPE),
   NOT_FOUND(HttpServletResponse.SC_NOT_FOUND, SoapErrorCode.INVALID_ID),
   INVALID_VALUE(HttpServletResponse.SC_BAD_REQUEST, SoapErrorCode.INVALID_VALUE),
   INVALID_VERSION(HttpServletResponse.SC_BAD_REQUEST, SoapErrorCode.INVALID_VERSION),
   
   LOCK_COMPETITION(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, SoapErrorCode.LOCK_COMPETITION,true),
   
   MALFORMED_QUERY(HttpServletResponse.SC_BAD_REQUEST, SoapErrorCode.MALFORMED_QUERY),
   MAX_RECORDS_EXCEEDED(HttpServletResponse.SC_BAD_REQUEST, SoapErrorCode.MAX_RECORDS_EXCEEDED),
   MISSING_REQUIRED_VALUE(HttpServletResponse.SC_BAD_REQUEST, SoapErrorCode.MISSING_REQUIRED_VALUE),
   NO_PERMISSION(HttpServletResponse.SC_FORBIDDEN, SoapErrorCode.NO_PERMISSION),
   UNKNOWN_ERROR(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, SoapErrorCode.UNKNOWN_ERROR),
   TRANSACTION_FAILED(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, SoapErrorCode.TRANSACTION_FAILED),
   INVALID_TEMPLATE(HttpServletResponse.SC_PRECONDITION_FAILED, SoapErrorCode.INVALID_TEMPLATE),
   ACCOUNTING_PERIOD_CLOSED(HttpServletResponse.SC_PRECONDITION_FAILED, SoapErrorCode.ACCOUNTING_PERIOD_CLOSED),
   BATCH_FAIL_ERROR(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, SoapErrorCode.BATCH_FAIL_ERROR),
   PDF_QUERY_ERROR(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, SoapErrorCode.PDF_QUERY_ERROR),
   REQUEST_EXCEEDED_LIMIT(HttpServletResponse.SC_OK,SoapErrorCode.REQUEST_EXCEEDED_LIMIT),
   REQUEST_REJECTED(HttpServletResponse.SC_OK,SoapErrorCode.REQUEST_REJECTED),
   
   TEMPORARY_ERROR(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,SoapErrorCode.TEMPORARY_ERROR,true),
   
   TRANSACTION_TERMINATED(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,SoapErrorCode.TRANSACTION_TERMINATED),
   
   TRANSACTION_TIMEOUT(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,SoapErrorCode.TRANSACTION_TIMEOUT),
   REQUEST_EXCEEDED_RATE(HttpServletResponse.SC_OK,SoapErrorCode.REQUEST_EXCEEDED_RATE)
   ;
   
   private final int httpResponseCode;
   private final SoapErrorCode soapErrorCode;
   private final boolean isRetryAble;
   
   private ApiError(int httpResponseCode, SoapErrorCode soapErrorCode) {
      this.httpResponseCode = httpResponseCode;
      this.soapErrorCode = soapErrorCode;
      this.isRetryAble = false;
   }

   private ApiError(int httpResponseCode, SoapErrorCode soapErrorCode, boolean retryAble) {
	   this.httpResponseCode = httpResponseCode;
	   this.soapErrorCode = soapErrorCode;
	   this.isRetryAble = retryAble;
   }
   
   public SoapErrorCode getSoapErrorCode() {
      return soapErrorCode;
   }
   
   public boolean isRetryAble(){
	   return this.isRetryAble;
   }
   
   public int getHttpResponseCode() {
      return httpResponseCode;
   }
}
