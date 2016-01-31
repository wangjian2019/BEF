package alvin.bef.framework.base.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.beanutils.PropertyUtils;

import alvin.bef.framework.base.annotation.BefMeta;
import alvin.bef.framework.base.exception.BackEndException;
import alvin.bef.framework.base.model.StandardObject;

/**
 * 
 * @author Alvin
 * 
 */
public final class MetadataUtil {

	public static void setProperty(Object o, String property, Object value) {
		try {
			PropertyUtils.setProperty(o, property, value);
		} catch (IllegalAccessException e) {
			throw new BackEndException(e);
		} catch (InvocationTargetException e) {
			throw new BackEndException(e);
		} catch (NoSuchMethodException e) {
			throw new BackEndException(e);
		}
	}

	public static Object getProperty(Object o, String property) {
		try {
			return PropertyUtils.getProperty(o, property);
		} catch (IllegalAccessException e) {
			throw new BackEndException(e);
		} catch (InvocationTargetException e) {
			throw new BackEndException(e);
		} catch (NoSuchMethodException e) {
			throw new BackEndException(e);
		}
	}

	/**
	 * judge the creator flag
	 */
	public static boolean hasCreatorFlag(Class<?> clazz) {
		return hasField(clazz, StandardObject.FIELD_CREATED_BY);
	}

	/**
	 * get content type by Class and fieldName
	 */
	public static BefMeta getContentMetaByAnnotationForCollectionFields(
			Class<?> clazz, String fieldName) {
		if (fieldName == null || clazz == null)
			return null;
		Field field = getField(clazz, fieldName);
		Annotation an = field.getAnnotation(BefMeta.class);
		if (an != null) {
			return (BefMeta) an;
		}
		return null;
	}

	/**
	 * search the field with value @param fieldName in the inherit tree
	 * 
	 * @deprecated
	 * @param clazz
	 * @param fieldName
	 * @return
	 */
	@Deprecated
	public static boolean hasField(Class<?> clazz, String fieldName) {
		return getField(clazz, fieldName) != null;
	}

	/**
	 * get filed by fieldName
	 * 
	 * @deprecated this method will move to BeanUtil
	 * @param clazz
	 * @param fieldName
	 * @return
	 */
	@Deprecated
	public static Field getField(Class<?> clazz, String fieldName) {
		if (clazz == null)
			return null;
		clazz = getOriginalClass(clazz);
		Field temp = null;
		try {
			Class<?> clz = clazz;
			while (clz != null && clz != Object.class) {
				try {
					temp = clz.getDeclaredField(fieldName);
				} catch (Exception e) {
				}
				if (temp != null) {
					break;
				} else {
					clz = clz.getSuperclass();
				}
			}
		} catch (SecurityException e) {
		}
		return temp;
	}

	public static boolean isEnum(Class<?> clazz, String fieldName) {
		Field field = getField(clazz, fieldName);
		if (field == null)
			return false;
		return BeanUtil.isEnumType(field.getType());
	}

	/**
	 * get the declared fields identified by field type but excludes inherited
	 * fields.
	 * 
	 * @param clazz
	 * @param type
	 * @return
	 */
	private static List<String> getDeclaredFieldNamesByType(Class<?> clazz,
			Class<?> type) {
		Field[] fields = clazz.getDeclaredFields();
		List<String> fieldNames = new ArrayList<String>(5);
		for (Field f : fields) {
			if (type.isAssignableFrom(f.getType())) {
				fieldNames.add(f.getName());
			}
		}
		return fieldNames;
	}

	private static List<Field> getDeclaredFieldsByType(Class<?> clazz,
			Class<?> type) {
		Field[] fields = clazz.getDeclaredFields();
		List<Field> fieldNames = new ArrayList<Field>(5);
		for (Field f : fields) {
			if (type.isAssignableFrom(f.getType())) {
				fieldNames.add(f);
			}
		}
		return fieldNames;
	}

	public static List<String> getSubBeanNamesByAnnotation(Class<?> clazz) {
		clazz = getOriginalClass(clazz);
		Field[] fields = clazz.getDeclaredFields();
		List<String> fieldNames = new ArrayList<String>(5);
		for (Field f : fields) {
			if (Collection.class.isAssignableFrom(f.getType())
					&& f.getAnnotation(BefMeta.class) != null) {
				fieldNames.add(f.getName());
			}
		}
		return fieldNames;
	}

	/**
	 * get the declared fields identified by field type include inherited fields
	 * 
	 * @deprecated
	 * @param clazz
	 * @param type
	 * @return
	 */
	@Deprecated
	public static Set<String> getQualifiedFieldsByType(Class<?> clazz,
			Class<?> type) {
		clazz = getOriginalClass(clazz);
		type = getOriginalClass(type);
		Class<?> clz = clazz;
		Set<String> fieldNames = new HashSet<String>(5);
		while (clz != null && clz != Object.class) {
			fieldNames.addAll(getDeclaredFieldNamesByType(clz, type));
			clz = clz.getSuperclass();
		}
		return fieldNames;
	}

	/**
	 * @deprecated
	 */
	@Deprecated
	public static Set<Field> getQualifiedFieldObjsByType(Class<?> clazz,
			Class<?> type) {
		clazz = getOriginalClass(clazz);
		type = getOriginalClass(type);
		Class<?> clz = clazz;
		Set<Field> fieldNames = new HashSet<Field>(5);
		while (clz != null && clz != Object.class) {
			fieldNames.addAll(getDeclaredFieldsByType(clz, type));
			clz = clz.getSuperclass();
		}
		return fieldNames;
	}

	private static Class<?> getOriginalClass(Class<?> clazz) {
		return BeanUtil.getOriginalClass(clazz);
	}
}
