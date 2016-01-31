package alvin.bef.framework.base.utils;

/**
 * 
 * @author Alvin
 *
 */
public class HexUtil {
   private static final char[] HEX_ARRAY = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};

   public static String toHexString(byte[] bytes) {
      char[] hexChars = new char[bytes.length * 2];
      for ( int j = 0; j < bytes.length; j++ ) {
          int v = bytes[j] & 0xFF;
          hexChars[j*2]     = HEX_ARRAY[v/16];
          hexChars[j*2 + 1] = HEX_ARRAY[v%16];
      }
      return new String(hexChars);
  }

}
