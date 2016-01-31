package alvin.bef.framework.base.utils;

public class ToStringFormatter implements Formatter<Object> {

   private static final ToStringFormatter INSTANCE = new ToStringFormatter();
   
   public static ToStringFormatter getInstance() {
      return INSTANCE;
   }

   public String format(Object value) {
      return value != null ? String.valueOf(value) : null;
   }

   public String format(Object value, Object param) {
   	  return format(value);
   }
   public String toString() {
      return "toString()";
   }
}
