package alvin.bef.framework.base.exception;

import org.hibernate.SessionException;

public class NoSessionException extends SessionException {

   public NoSessionException() {
      super("You are not logged in.");
   }

}
