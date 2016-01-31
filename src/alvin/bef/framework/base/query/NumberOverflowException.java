package alvin.bef.framework.base.query;

/**
 * 
 * @author Alvin
 *
 */
public class NumberOverflowException extends RuntimeException {

   private static final long serialVersionUID = 1L;
   private Number number;
   private Number maxValue;
   private Number minValue;

   public NumberOverflowException() {
   }

   public NumberOverflowException(String message) {
      super(message);
   }

   public NumberOverflowException(Number number) {
      this(number, Integer.MAX_VALUE, Integer.MIN_VALUE);
   }

   public NumberOverflowException(Number number, Number maxValue, Number minValue) {
      this.number = number;
      this.maxValue = maxValue;
      this.minValue = minValue;
   }

   public Number getNumber() {
      return number;
   }

   public Number getMaxValue() {
      return maxValue;
   }

   public Number getMinValue() {
      return minValue;
   }

   @Override
   public String getMessage() {
      if (number == null) {
         return "Number overflow.";
      } else {
         return String.format("Number overflow, %s out of range [%s, %s].", number, minValue, maxValue);
      }
   }
}
