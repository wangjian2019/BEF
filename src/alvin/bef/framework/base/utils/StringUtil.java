package alvin.bef.framework.base.utils;

import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.util.StringUtils;

/**
 * 
 * @author Alvin
 *
 */
public class StringUtil {
   public static final Charset UTF8 = Charset.forName("UTF-8");
   private static final CharsetEncoder UTF8_ENCODER = UTF8.newEncoder(); // used only for escapeXml - no actual encoding

   public static String escapeXml(String string) {
      return escapeXml(string, UTF8);
   }
   
   public static boolean isChanged(String oldOne, String newOne){
	   if(oldOne == null && newOne == null)
		   return false;
	   if((oldOne != null && newOne == null) || (oldOne == null && newOne != null))
		   return true;
	   return !oldOne.equals(newOne);
   }

   public static String escapeXml(String string, Charset charset) {
      CharsetEncoder encoder = charset == UTF8 ? UTF8_ENCODER : charset.newEncoder();

      StringBuilder sb = new StringBuilder();
      for (char c : string.toCharArray()) {
         switch (c) {
            case '<':
               sb.append("&lt;");
               break;
            case '>':
               sb.append("&gt;");
               break;
            case '\'':
               sb.append("&#39;");
               break;
            case '"':
               sb.append("&quot;");
               break;
            case '&':
               sb.append("&amp;");
               break;
            default:
               if (encoder.canEncode(c)) {
                  sb.append(c);
               }
               else {
                  String replacement = "&#" + ((int) c) + ";";
                  sb.append(replacement);
               }

               break;
         }
      }
      return sb.toString();
   }

   public static String toUppercaseFirstCharacter(String s) {
      if (s == null || s.length() == 0) {
         return s;
      }

      return Character.toUpperCase(s.charAt(0)) + (s.length() > 1 ? s.substring(1) : "");
   }

   public static String toLowercaseFirstCharacter(String s) {
      if (s == null || s.length() == 0) {
         return s;
      }

      return Character.toLowerCase(s.charAt(0)) + (s.length() > 1 ? s.substring(1) : "");
   }

   public static <E> String toString(Collection<E> c, String delimiter) {
      return toString(c, delimiter, null, null, ToStringFormatter.getInstance());
   }

   public static <E> String toString(Collection<E> c, String delimiter, Formatter<? super E> formatter) {
      return toString(c, delimiter, null, null, formatter);
   }

   public static <E> String toString(Collection<E> c, String delimiter, String before, String after) {
      return toString(c, delimiter, before, after, ToStringFormatter.getInstance());
   }

   public static <E> String toString(Collection<E> c, String delimiter, String before, String after, Formatter<? super E> formatter) {
      if (c == null) {
         return null;
      }

      StringBuilder sb = new StringBuilder();
      append(sb, c, delimiter, before, after, formatter);
      return sb.toString();
   }

   public static <E> void append(StringBuilder sb, Collection<E> c, String delimiter) {
      append(sb, c, delimiter, null, null, ToStringFormatter.getInstance());
   }

   public static <E> void append(StringBuilder sb, Collection<E> c, String delimiter, Formatter<? super E> formatter) {
      append(sb, c, delimiter, null, null, formatter);
   }

   public static <E> void append(StringBuilder sb, Collection<E> c, String delimiter, String before, String after) {
      append(sb, c, delimiter, before, after, null);
   }

   public static <E> void append(StringBuilder sb, Collection<E> c, String delimiter, String before, String after, Formatter<? super E> formatter) {
      if (c.isEmpty()) {
         return;
      }

      if (before != null) {
         sb.append(before);
      }

      boolean first = true;
      for (E e : c) {
         if (first) {
            first = false;
         }
         else {
            sb.append(delimiter);
         }
         sb.append(formatter.format(e));
      }

      if (after != null) {
         sb.append(after);
      }
   }

   public static <K,V> String toString(Map<K,V> m) {
      return toString(m, ", ", "=", true, null, null, ToStringFormatter.getInstance(), ToStringFormatter.getInstance());
   }

   public static <K,V> String toString(Map<K,V> m, String delimiter) {
      return toString(m, delimiter, "=", true, null, null, ToStringFormatter.getInstance(), ToStringFormatter.getInstance());
   }

   public static <K,V> String toString(Map<K,V> m, String delimiter, String entryDelimiter, boolean includeEntryDelimiterOnEmptyValue) {
      return toString(m, delimiter, entryDelimiter, includeEntryDelimiterOnEmptyValue, null, null, ToStringFormatter.getInstance(), ToStringFormatter.getInstance());
   }

   public static <K,V> String toString(Map<K,V> m, String delimiter, String entryDelimiter, boolean includeEntryDelimiterOnEmptyValue, String before, String after) {
      return toString(m, delimiter, entryDelimiter, includeEntryDelimiterOnEmptyValue, before, after, ToStringFormatter.getInstance(), ToStringFormatter.getInstance());
   }

   public static <K,V> String toString(Map<K,V> m, String delimiter, String entryDelimiter, boolean includeEntryDelimiterOnEmptyValue, String before, String after, Formatter<? super K> keyFormatter, Formatter<? super V> valueFormatter) {
      if (m == null) {
         return null;
      }

      StringBuilder sb = new StringBuilder();
      append(sb, m, delimiter, entryDelimiter, includeEntryDelimiterOnEmptyValue, before, after, keyFormatter, valueFormatter);
      return sb.toString();
   }

   public static <K,V> void append(StringBuilder sb, Map<K,V> m, String delimiter) {
      append(sb, m, delimiter, "=", true, null, null, ToStringFormatter.getInstance(), ToStringFormatter.getInstance());
   }

   public static <K,V> void append(StringBuilder sb, Map<K,V> m, String delimiter, String entryDelimiter, boolean includeEntryDelimiterOnEmptyValue) {
      append(sb, m, delimiter, entryDelimiter, includeEntryDelimiterOnEmptyValue, null, null, ToStringFormatter.getInstance(), ToStringFormatter.getInstance());
   }

   public static <K,V> void append(StringBuilder sb, Map<K,V> m, String delimiter, String entryDelimiter, boolean includeEntryDelimiterOnEmptyValue, String before, String after) {
      append(sb, m, delimiter, entryDelimiter, includeEntryDelimiterOnEmptyValue, before, after, ToStringFormatter.getInstance(), ToStringFormatter.getInstance());
   }

   public static <K,V> void append(StringBuilder sb, Map<K,V> m, String delimiter, String entryDelimiter, boolean includeEntryDelimiterOnEmptyValue, String before, String after, Formatter<? super K> keyFormatter, Formatter<? super V> valueFormatter) {
      if (m.isEmpty()) {
         return;
      }

      if (before != null) {
         sb.append(before);
      }

      boolean first = true;
      for (Entry<K,V> entry : m.entrySet()) {
         if (first) {
            first = false;
         }
         else {
            sb.append(delimiter);
         }
         sb.append(keyFormatter.format(entry.getKey()));
         String valueString = valueFormatter.format(entry.getValue());
         if (includeEntryDelimiterOnEmptyValue || !valueString.equals("")) {
            sb.append(entryDelimiter);
            sb.append(valueString);
         }
      }

      if (after != null) {
         sb.append(after);
      }
   }

   /**
    * Simple split method that preserves empty-string tokens.
    * Java's split method strips out empty tokens.
    */
   public static List<String> split(String s, char delimiter) {
      List<String> tokens = new ArrayList<String>();
      for (int start = 0, index = 0;; start = index + 1) {
         index = s.indexOf(delimiter, start);
         if (index != -1) {
            tokens.add(s.substring(start, index));
         }
         else {
            tokens.add(s.substring(start));
            break;
         }
      }
      return tokens;
   }
	
   public static List<List<String>> splitAry(List<String> ary, int subSize) {
	   List<List<String>> subAryList = new ArrayList<List<String>>();
	   if(null == ary || subSize == 0) return subAryList;
	   int size = ary.size();  
	   int count = size % subSize == 0 ? size / subSize: size / subSize + 1;
	   for (int i = 0; i < count; i++) {   	   
		   int fromIndex = i*subSize;
		   int toIndex = (i+1)*subSize;
		   if(toIndex > size) toIndex = size;
		   subAryList.add(ary.subList(fromIndex, toIndex));
	   } 
	   return subAryList;
   }

   /**
    * Trims trailing spaces from string.
    * <p>
    * One reason to strip trailing spaces is to make java db model code work consistently
    * with mysql.  Mysql ignores trailing spaces in varchar field.  So if one has a model
    * where varchar is used for in a key for lookup, problems arise.  For example, if you attempt
    * to update the model and merely add a trailing space to this field, the lookup that 
    * precedes the update would fail, because the java code would think that the objects
    * are not the same, while mysql thinks they are. 
    * 
    * Strip trailing spaces from field to be consistent with mysql.
    * 
    * @param field to trim
    * @return Trimmed string
    */
   public static String trimTrailingSpaces(String field) {
	   return StringUtils.trimTrailingCharacter(field, ' ');	   
   }
   
   public static String earsePrefix(String value, String...prefixs){
	   if(null == value || null == prefixs || prefixs.length <=0){
		   return value;
	   }
	   
	   for(String prefix: prefixs){
		   if(value.startsWith(prefix)){
				return value.replace(prefix, "");
			}
	   }
	   
	   return value;
   }
}
