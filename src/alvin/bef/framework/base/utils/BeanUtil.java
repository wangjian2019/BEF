package alvin.bef.framework.base.utils;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.DynaClass;
import org.apache.commons.beanutils.DynaProperty;
import org.apache.commons.beanutils.LazyDynaBean;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.upload.FormFile;
import org.springframework.cglib.proxy.Enhancer;

import alvin.bef.framework.base.abstractmanager.AbstractManager;
import alvin.bef.framework.base.annotation.BefMeta;
import alvin.bef.framework.base.enums.IEnum;
import alvin.bef.framework.base.exception.BackEndException;
import alvin.bef.framework.base.exception.ExceptionUtils;
import alvin.bef.framework.base.model.StandardObject;
import alvin.bef.framework.base.session.UserSession;
import alvin.bef.framework.base.spring.SpringHelper;

/**
 * 
 * @author Alvin
 *
 */
public final class BeanUtil {
	protected static final Log log = LogFactory.getLog(BeanUtil.class);
	public final static String PRIMARYKEY = "id";
	public final static String DELETEID = "deleteId";

	public final static String DELETEDSIGN = "_deleted_";
	private static List<String> filterList = Arrays.asList(new String[] {
			"createdOn", "updatedOn", "updatedBy", "createdBy", PRIMARYKEY });

	public static boolean objectsEqual(Object s, Object d) {
		if (s == null) {
			if (d == null) {
				return true;
			} else {
				return false;
			}
		} else {
			if (d == null) {
				return false;
			} else {
				return s.equals(d);
			}
		}
	}

	/**
	 * Copy all properties in source to dest, if the property also exists in
	 * dest.
	 * <p>
	 * Because browsers would not submit unchecked checkboxes, if you want to
	 * use checkbox for boolean types, you need play following tricks:
	 * 
	 * <pre>
	 * &lt;input type=&quot;checkbox&quot; name=&quot;myProperty&quot; value=&quot;true&quot;&gt;Check me
	 * &lt;input type=&quot;hidden&quot; name=&quot;myProperty&quot; value=&quot;false&quot;/&gt;
	 * </pre>
	 * 
	 * That is, put a hidden field with the same name and value 'false' behind
	 * each checkbox.
	 * 
	 * @param source
	 * @param dest
	 */
	public static void copyProperties(DynaBean source, Object dest)
			throws Exception {

		if (source == null)
			throw new IllegalArgumentException("source bean is null.");
		if (dest == null)
			throw new IllegalArgumentException("dest bean is null.");

		DynaClass dynaClass = source.getDynaClass();

		UserSession session = SpringHelper.getSessionManager().getUserSession();

		List<PropertyDescriptor> propertyDescriptors = getValidPropertyDescriptors(dest);
		PropertyDescriptor propertyDescriptor = null;
		for (Iterator<PropertyDescriptor> it = propertyDescriptors.iterator(); it
				.hasNext();) {
			propertyDescriptor = it.next();
			if (hasDynaProperty(dynaClass, propertyDescriptor.getName())) {
				IDataProcessor dataProcessor = ProcessorUtils
						.getProcessor(propertyDescriptor);
				if (dataProcessor == null)
					continue;
				dataProcessor.process(dest, propertyDescriptor, source,
						session, false);
			}
		}

	}

	private static boolean hasDynaProperty(DynaClass klass, String name) {
		DynaProperty[] dynaProperties = klass.getDynaProperties();

		for (DynaProperty dynaProperty : dynaProperties) {
			if (dynaProperty.getName().equals(name))
				return true;
		}

		return false;
	}

	public static void setProperties(Object owner, String fieldName,
			Object value, UserSession session) {
		try {
			PropertyDescriptor propertyDescriptor = PropertyUtils
					.getPropertyDescriptor(owner, fieldName);
			IDataProcessor dataProcessor = ProcessorUtils
					.getProcessor(propertyDescriptor);
			if (dataProcessor != null)
				dataProcessor.process(owner, propertyDescriptor, value,
						session, false);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			throw new BackEndException(e);
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			throw new BackEndException(e);
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			throw new BackEndException(e);
		}

	}

	public static void setProperties(DynaBean dynaBean, Object bean,
			UserSession session) {
		setProperties(dynaBean, bean, session, null);
	}

	/**
	 * mapping property value of POJO
	 * 
	 * @param dynaBean
	 * @param bean
	 * @param session
	 */
	public static void setProperties(DynaBean dynaBean, Object bean,
			UserSession session, List<String> excludeProps) {
		if (dynaBean == null || bean == null)
			throw new RuntimeException("DynaBean or Object is null");

		List<PropertyDescriptor> propertyDescriptors = getValidPropertyDescriptors(
				bean, session, excludeProps);
		PropertyDescriptor propertyDescriptor = null;
		for (Iterator<PropertyDescriptor> it = propertyDescriptors.iterator(); it
				.hasNext();) {
			propertyDescriptor = it.next();
			IDataProcessor dataProcessor = ProcessorUtils
					.getProcessor(propertyDescriptor);
			if (dataProcessor == null)
				continue;
			try {
				dataProcessor.process(bean, propertyDescriptor, dynaBean,
						session, false);
			} catch (Exception e) {
				String propertyName = propertyDescriptor.getName();
				Object value = dynaBean.get(propertyName);
				throw new BackEndException("propertyName:" + propertyName);
			}
		}
	}

	public static void setProperties(DynaBean dynaBean, Object bean,
			UserSession session, boolean isClientDelete) {
		if (dynaBean == null || bean == null)
			throw new RuntimeException("DynaBean or Object is null");

		List<PropertyDescriptor> propertyDescriptors = getValidPropertyDescriptors(bean);
		PropertyDescriptor propertyDescriptor = null;
		for (Iterator<PropertyDescriptor> it = propertyDescriptors.iterator(); it
				.hasNext();) {
			propertyDescriptor = it.next();
			IDataProcessor dataProcessor = ProcessorUtils
					.getProcessor(propertyDescriptor);
			if (dataProcessor == null)
				continue;
			dataProcessor.process(bean, propertyDescriptor, dynaBean, session,
					isClientDelete);

		}

	}

	public static void setSimpleProperties(DynaBean dynaBean, Object bean,
			UserSession session, boolean isClientDelete) {
		if (dynaBean == null || bean == null)
			throw new RuntimeException("DynaBean or Object is null");

		List<PropertyDescriptor> propertyDescriptors = getValidPropertyDescriptors(bean);
		PropertyDescriptor propertyDescriptor = null;
		for (Iterator<PropertyDescriptor> it = propertyDescriptors.iterator(); it
				.hasNext();) {
			propertyDescriptor = it.next();
			IDataProcessor dataProcessor = ProcessorUtils
					.getProcessor(propertyDescriptor);
			if (dataProcessor == null)
				continue;
			if (dataProcessor instanceof CollectionProcessor)
				continue;
			dataProcessor.process(bean, propertyDescriptor, dynaBean, session,
					isClientDelete);

		}

	}

	public static void setProperty(Object src, String propertyName, Object value) {
		try {
			PropertyUtils.setProperty(src, propertyName, value);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	private static List<PropertyDescriptor> getValidPropertyDescriptors(
			Object obj) {
		return getValidPropertyDescriptors(obj, null, null);
	}

	/**
	 * get valid PropertyDescriptors
	 * 
	 * @param obj
	 * @return
	 */
	private static List<PropertyDescriptor> getValidPropertyDescriptors(
			Object obj, UserSession userSession, List<String> excludeProps) {
		PropertyDescriptor pds[] = PropertyUtils.getPropertyDescriptors(obj);
		List<PropertyDescriptor> list = new ArrayList<PropertyDescriptor>();
		for (int i = 0; i < pds.length; i++) {
			if (pds[i].getReadMethod() == null
					|| pds[i].getWriteMethod() == null
					|| filterList.contains(pds[i].getName())
					|| (excludeProps != null && excludeProps.contains(pds[i]
							.getName())))
				continue;
			list.add(pds[i]);
		}
		return list;
	}

	private static String getMethodName(String propertyName) {
		return "get" + propertyName.substring(0, 1).toUpperCase()
				+ propertyName.substring(1);
	}

	private static String isMethodName(String propertyName) {
		return "is" + propertyName.substring(0, 1).toUpperCase()
				+ propertyName.substring(1);
	}

	public static Method getter(Class<?> c, String property) {
		try {
			return c.getMethod(getMethodName(property));
		} catch (SecurityException e) {
			// ignore
		} catch (NoSuchMethodException e) {
			// ignore
		}

		try {
			return c.getMethod(isMethodName(property));
		} catch (NoSuchMethodException e) {
			throw new RuntimeException("No getter in class " + c.getName()
					+ " for property " + property);
		}
	}

	// alternative to PropertyUtils.getProperty(); this attempts to call the
	// method,
	// regardless of whether a property by that name actually exists.
	public static Object getProperty(Object obj, String propertyName) {
		if (obj == null) {
			return null;
		}

		try {
			return getter(obj.getClass(), propertyName).invoke(obj);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Retrieves the value for the specified property. This is different with
	 * the <code>BeanUtil.getProperty()</code> by:
	 * <ol>
	 * <li>support "a.b";
	 * <li>support isXxx();
	 * <li>avoids Lazy initialization problem;
	 * </ol>
	 * 
	 * @param obj
	 * @param propertyName
	 * @return
	 */
	public static Object getPropertyValue(StandardObject obj,
			String propertyName) {

		Object value = obj;
		String[] keys = propertyName.split("\\.");
		for (int i = 0; i < keys.length; i++) {
			if (value == null) {
				break;
			}
			try {
				value = PropertyUtils.getProperty(value, keys[i]);
			} catch (Exception e) {
				throw ExceptionUtils.wrap(e);
			}
		}
		return value;
	}

	@SuppressWarnings("unchecked")
	public static <T> boolean isNewObject(T o) {
		if (o == null) {
			return true;
		}
		AbstractManager<? super T> mgr = SpringHelper.getManagerByObject(o);

		if (mgr == null)
			return true;

		return mgr.isNewObject(o);
	}

	public static boolean isBlank(Object o) {
		if (o == null)
			return true;
		Class<?> clazz = o.getClass();
		if (isSimpleType(clazz))
			return "".equals(o.toString().trim());
		else if (clazz.isArray())
			return Array.getLength(o) <= 0;
		else if (Collection.class.isAssignableFrom(clazz)) {
			return ((Collection<?>) o).size() <= 0;
		} else if (Map.class.isAssignableFrom(clazz)) {
			return ((Map<?, ?>) o).size() <= 0;
		}
		return false;
	}

	public static String trim(String val) {
		String str = val;
		if (str != null) {
			str = str.trim();
			if (str.length() == 0) {
				str = null;
			}
		}
		return str;
	}

	public static boolean isSimpleType(Class<?> clazz) {// exclude boolean and
														// Boolean
		return (clazz.isPrimitive() || String.class.isAssignableFrom(clazz)
				|| Boolean.class.isAssignableFrom(clazz)
				|| Integer.class.isAssignableFrom(clazz)
				|| Long.class.isAssignableFrom(clazz)
				|| Float.class.isAssignableFrom(clazz)
				|| Character.class.isAssignableFrom(clazz)
				|| Short.class.isAssignableFrom(clazz)
				|| Double.class.isAssignableFrom(clazz)
				|| Byte.class.isAssignableFrom(clazz)
				|| BigDecimal.class.isAssignableFrom(clazz)
				|| BigInteger.class.isAssignableFrom(clazz) || byte[].class
					.isAssignableFrom(clazz));
	}

	public static boolean isEnumType(Class<?> clazz) {
		clazz = getOriginalClass(clazz);
		return (IEnum.class.isAssignableFrom(clazz));
	}

	public static boolean isCollectionType(Class<?> clazz) {
		clazz = getOriginalClass(clazz);
		return (Collection.class.isAssignableFrom(clazz));
	}

	public static boolean isDateType(Class<?> clazz) {
		return (java.sql.Date.class.isAssignableFrom(clazz));
	}

	public static boolean isUtilDateType(Class<?> clazz) {
		return (java.util.Date.class.equals(clazz));
	}

	public static boolean isTimestampType(Class<?> clazz) {
		return (Timestamp.class.isAssignableFrom(clazz));
	}

	public static boolean isBitSet(Class<?> clazz) {
		return BitSet.class.isAssignableFrom(clazz);
	}

	public static boolean isEncryptedStringType(Class<?> clazz) {
		return (EncryptedString.class.isAssignableFrom(clazz));
	}

	public static Class<?> getOriginalClass(Class<?> clz) {
		Class<?> ret = clz;
		while (Enhancer.isEnhanced(ret)) {
			ret = ret.getSuperclass();
		}
		return ret;
	}

	public static PropertyDescriptor[] getPropertyDescriptors(Class<?> clz) {
		clz = getOriginalClass(clz);
		ArrayList<PropertyDescriptor> list = new ArrayList<PropertyDescriptor>();
		try {
			if (clz.isInterface()) {
				list.addAll(Arrays.asList(Introspector.getBeanInfo(clz)
						.getPropertyDescriptors()));
				Class<?>[] superClasses = clz.getInterfaces();
				if (superClasses != null) {
					for (int i = 0; i < superClasses.length; i++) {
						list.addAll(Arrays
								.asList(getPropertyDescriptors(superClasses[i])));
					}
				}

			} else {
				while (clz != null && !clz.equals(Object.class)) {
					list.addAll(Arrays.asList(Introspector.getBeanInfo(clz)
							.getPropertyDescriptors()));
					clz = clz.getSuperclass();
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		Set<String> propertyNames = new HashSet<String>();
		List<PropertyDescriptor> ret = new ArrayList<PropertyDescriptor>();
		for (int i = 0; i < list.size(); i++) {
			PropertyDescriptor pd = list.get(i);
			if (!propertyNames.contains(pd.getName())) {
				propertyNames.add(pd.getName());
				ret.add(pd);
			}
		}
		return ret.toArray(new PropertyDescriptor[ret.size()]);
	}

	public static Long longValue(String value) {
		if (value != null && !"".equals(value.trim()))
			return Long.valueOf(value.trim());
		return null;
	}

	public static String idValue(String value) {
		if (value == null || "".equals(value.trim()))
			return null;
		return value;
	}

}

class ProcessorUtils {
	private static Map<Class<? extends IDataProcessor>, IDataProcessor> map = new HashMap<Class<? extends IDataProcessor>, IDataProcessor>();

	public static IDataProcessor getProcessor(
			PropertyDescriptor propertyDescriptor) {
		Class<?> propertyType = propertyDescriptor.getPropertyType();
		if (Boolean.class.isAssignableFrom(propertyType)
				|| boolean.class.isAssignableFrom(propertyType)) {
			return getProcessor(BooleanProcessor.class);
		} else if (isBaseType(propertyType)) {
			if (isNumericType(propertyType)) {
				return getProcessor(NumericDataTypeProcessor.class);
			} else
				return getProcessor(BaseDataTypeProcessor.class);
		}
		// it's Java enum, also is IEnum. isEnumType() should be only DB Enum
		// (IEnum)
		else if (BeanUtil.isEnumType(propertyType)
				&& !Enum.class.isAssignableFrom(propertyType)) {
			return getProcessor(EnumProcessor.class);
		} else if (BeanUtil.isDateType(propertyType)) {
			return getProcessor(DateProcessor.class);
		} else if (BeanUtil.isUtilDateType(propertyType)) {
			return getProcessor(UtilDateProcessor.class);
		} else if (BeanUtil.isTimestampType(propertyType)) {
			return getProcessor(TimestampProcessor.class);
		} else if (BeanUtil.isCollectionType(propertyType)) {
			return getProcessor(CollectionProcessor.class);
		} else if (Enum.class.isAssignableFrom(propertyType)) {
			return getProcessor(JavaEnumProcessor.class);
		} else if (BeanUtil.isEncryptedStringType(propertyType)) {
			return getProcessor(EncryptedStringProcessor.class);
		} else if (BeanUtil.isBitSet(propertyType)) {
			return null;
		} else {
			return getProcessor(LookupProcessor.class);
		}
	}

	private static boolean isBaseType(Class<?> clazz) {
		return (BeanUtil.isSimpleType(clazz)
				|| Locale.class.isAssignableFrom(clazz) || TimeZone.class
					.isAssignableFrom(clazz));
	}

	private static boolean isNumericType(Class<?> clazz) {
		return (Integer.TYPE.equals(clazz)
				|| Integer.class.isAssignableFrom(clazz)
				|| Long.TYPE.equals(clazz)
				|| Long.class.isAssignableFrom(clazz)
				|| Float.TYPE.equals(clazz)
				|| Float.class.isAssignableFrom(clazz)
				|| Short.TYPE.equals(clazz)
				|| Short.class.isAssignableFrom(clazz)
				|| Double.TYPE.equals(clazz)
				|| Double.class.isAssignableFrom(clazz)

				|| BigDecimal.class.isAssignableFrom(clazz) || BigInteger.class
					.isAssignableFrom(clazz));
	}

	private static IDataProcessor getProcessor(
			Class<? extends IDataProcessor> clazz) {
		IDataProcessor processor = map.get(clazz);
		if (processor == null) {
			try {
				processor = clazz.newInstance();
			} catch (InstantiationException e) {
				throw new RuntimeException(e);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			}
			map.put(clazz, processor);
		}
		return processor;
	}

}

// ////////////////////Inner Interface DataProcessor///////////////////////////
interface IDataProcessor {
	public void process(Object owner, PropertyDescriptor propertyDescriptor,
			DynaBean dynaBean, UserSession session, boolean isClientDelete);

	public void process(Object owner, PropertyDescriptor propertyDescriptor,
			Object value, UserSession session, boolean isClientDelete);
}

// ///////////////////Inner Abstract Processor AbstractProcessor
// //////////////////////////////////
abstract class AbstractProcessor implements IDataProcessor {

	protected Object spaceStrHandler(String value, Class<?> type) {
		if ("".equalsIgnoreCase(value.trim())) {
			if (!char.class.isAssignableFrom(type)
					&& !Character.class.isAssignableFrom(type)
					&& !String.class.isAssignableFrom(type)) {
				return null;
			}
		}
		return ConvertUtils.convert(value, type);
	}

	protected void invokeSetMethod(Object owner,
			PropertyDescriptor propertyDescriptor, Object value) {
		try {
			propertyDescriptor.getWriteMethod().invoke(owner,
					new Object[] { value });
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	protected Object invokeGetMethod(Object owner,
			PropertyDescriptor propertyDescriptor) {
		try {
			return propertyDescriptor.getReadMethod().invoke(owner);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	protected BefMeta getClassFromAnnotation(Object owner,
			PropertyDescriptor propertyDescriptor) {
		return MetadataUtil.getContentMetaByAnnotationForCollectionFields(
				owner.getClass(), propertyDescriptor.getName());
	}

}

// ///////////////////Inner Class CollectionProcessor
// //////////////////////////////////
class CollectionProcessor extends AbstractProcessor {

	public void process(Object owner, PropertyDescriptor propertyDescriptor,
			DynaBean dynaBean, UserSession session, boolean isClientDelete) {
		Object value = dynaBean.get("_" + propertyDescriptor.getName() + "_");
		process(owner, propertyDescriptor, value, session, isClientDelete);
	}

	@SuppressWarnings("unchecked")
	public void process(Object owner, PropertyDescriptor propertyDescriptor,
			Object value, UserSession session, boolean isClientDelete) {
		if (value == null)
			return;
		Collection<DynaBean> coll = (Collection<DynaBean>) value;
		setCollectionProperty(coll, owner, propertyDescriptor, session,
				isClientDelete);
	}

	@SuppressWarnings("unchecked")
	private Object getChildById(Collection<?> beans, Serializable id) {
		if (id == null)
			return null;

		Object ret = null;
		for (Object bean : beans) {
			if (bean != null) {
				AbstractManager beanManager = SpringHelper
						.getManagerByObject(bean);
				if (id.equals(beanManager.getObjectId(bean))) {
					ret = bean;
					break;
				}
			}
		}

		return ret;
	}

	/**
	 * mapping property value of POJO in collection
	 * 
	 * @param forms
	 * @param owner
	 * @param collectionProperty
	 * @param session
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private void setCollectionProperty(Collection<DynaBean> forms,
			Object owner, PropertyDescriptor collectionProperty,
			UserSession session, boolean isClientDelete) {
		Class<?> entityClass = null;
		Collection<Object> childBeans = null;
		childBeans = (Collection<Object>) invokeGetMethod(owner,
				collectionProperty);

		Object childBean = null;
		List<Object> tempList = new ArrayList<Object>();
		for (DynaBean dynaBean : forms) {
			String id = BeanUtil.idValue((String) dynaBean
					.get(BeanUtil.PRIMARYKEY));
			boolean deleted = Boolean.valueOf(
					(String) dynaBean.get(BeanUtil.DELETEDSIGN)).booleanValue();
			if (id == null) {
				if (deleted)
					continue;
				AbstractManager cmgr = SpringHelper
						.getManagerByClass(entityClass);
				childBean = cmgr.createNewObject();
				// childBeans.add(childBean);
				tempList.add(childBean);
			} else {
				childBean = getChildById(childBeans, id);
				if (deleted) {
					childBean = null;
					continue;
					// childBeans.remove(childBean);
				}
				tempList.add(childBean);
			}
			BeanUtil.setProperties(dynaBean, childBean, session);
		}
		childBeans.clear();
		childBeans.addAll(tempList);
	}

}

// ///////////////////Inner Class BooleanProcessor
// //////////////////////////////////
class BooleanProcessor extends AbstractProcessor {
	public void process(Object owner, PropertyDescriptor propertyDescriptor,
			DynaBean dynaBean, UserSession session, boolean isClientDelete) {
		Object value = dynaBean.get(propertyDescriptor.getName());
		process(owner, propertyDescriptor, value, session, isClientDelete);

	}

	public void process(Object owner, PropertyDescriptor propertyDescriptor,
			Object value, UserSession session, boolean isClientDelete) {
		if (value != null
				&& "on".equalsIgnoreCase(((String) value).toLowerCase()))
			value = "true";
		value = Boolean.valueOf((String) value);
		invokeSetMethod(owner, propertyDescriptor, value);
	}

}

// ///////////////////Base data type processor for example
// int(Integer),long(long) etc. exclude boolean(Boolean)
// //////////////////////////////////
class BaseDataTypeProcessor extends AbstractProcessor {
	public void process(Object owner, PropertyDescriptor propertyDescriptor,
			DynaBean dynaBean, UserSession session, boolean isClientDelete) {
		Object value = dynaBean.get(propertyDescriptor.getName());
		process(owner, propertyDescriptor, value, session, isClientDelete);
	}

	public void process(Object owner, PropertyDescriptor propertyDescriptor,
			Object value, UserSession session, boolean isClientDelete) {
		if (value == null) {
			if (isClientDelete) {
				invokeSetMethod(owner, propertyDescriptor, null);
			}
			return;
		}
		invokeSetMethod(
				owner,
				propertyDescriptor,
				spaceStrHandler(String.valueOf(value),
						propertyDescriptor.getPropertyType()));
	}
}

class NumericDataTypeProcessor extends AbstractProcessor {
	public void process(Object owner, PropertyDescriptor propertyDescriptor,
			DynaBean dynaBean, UserSession session, boolean isClientDelete) {
		Object value = dynaBean.get(propertyDescriptor.getName());
		process(owner, propertyDescriptor, value, session, isClientDelete);
	}

	public void process(Object owner, PropertyDescriptor propertyDescriptor,
			Object value, UserSession session, boolean isClientDelete) {
		if (value == null) {
			if (isClientDelete) {
				invokeSetMethod(owner, propertyDescriptor, null);
			}
			return;
		}
		invokeSetMethod(owner, propertyDescriptor, null);
	}
}

// ////////////////Date processor
// ///////////////////////////////////////////////////
class DateProcessor extends AbstractProcessor {
	public void process(Object owner, PropertyDescriptor propertyDescriptor,
			DynaBean dynaBean, UserSession session, boolean isClientDelete) {
		Object value = dynaBean.get(propertyDescriptor.getName());
		process(owner, propertyDescriptor, value, session, isClientDelete);
	}

	public void process(Object owner, PropertyDescriptor propertyDescriptor,
			Object value, UserSession session, boolean isClientDelete) {
		if (value == null) {
			if (isClientDelete) {
				invokeSetMethod(owner, propertyDescriptor, null);
			}
			return;
		}
		invokeSetMethod(owner, propertyDescriptor, null);
	}
}

class UtilDateProcessor extends AbstractProcessor {
	public void process(Object owner, PropertyDescriptor propertyDescriptor,
			DynaBean dynaBean, UserSession session, boolean isClientDelete) {
		Object value = dynaBean.get(propertyDescriptor.getName());
		process(owner, propertyDescriptor, value, session, isClientDelete);
	}

	public void process(Object owner, PropertyDescriptor propertyDescriptor,
			Object value, UserSession session, boolean isClientDelete) {
		if (value == null) {
			if (isClientDelete) {
				invokeSetMethod(owner, propertyDescriptor, null);
			}
			return;
		}
		invokeSetMethod(owner, propertyDescriptor, null);
	}
}

class TimestampProcessor extends AbstractProcessor {
	public void process(Object owner, PropertyDescriptor propertyDescriptor,
			DynaBean dynaBean, UserSession session, boolean isClientDelete) {
		Object value = dynaBean.get(propertyDescriptor.getName());
		process(owner, propertyDescriptor, value, session, isClientDelete);
	}

	public void process(Object owner, PropertyDescriptor propertyDescriptor,
			Object value, UserSession session, boolean isClientDelete) {
		if (value == null) {
			if (isClientDelete) {
				invokeSetMethod(owner, propertyDescriptor, null);
			}
			return;
		}
		invokeSetMethod(owner, propertyDescriptor, null);
	}
}

class EnumProcessor extends AbstractProcessor {

	public void process(Object owner, PropertyDescriptor propertyDescriptor,
			DynaBean dynaBean, UserSession session, boolean isClientDelete) {
		String propertyName = propertyDescriptor.getName();
		Object value = dynaBean.get(propertyName);
		process(owner, propertyDescriptor, value, session, isClientDelete);
	}

	@SuppressWarnings("unchecked")
	public void process(Object owner, PropertyDescriptor propertyDescriptor,
			Object value, UserSession session, boolean isClientDelete) {
		if (value == null) {
			if (isClientDelete) {
				invokeSetMethod(owner, propertyDescriptor, null);
			}
			return;
		}

		String enumId = BeanUtil.idValue((String) value);
		if (enumId == null) {
			invokeSetMethod(owner, propertyDescriptor, null);
			return;
		}
		Class<?> entityClass = BeanUtil.getOriginalClass(propertyDescriptor
				.getPropertyType());
		AbstractManager enumManager = SpringHelper
				.getManagerByClass(entityClass);
		Object enumBean = enumManager.getObject(enumId);
		invokeSetMethod(owner, propertyDescriptor, enumBean);
	}
}

class LookupProcessor extends AbstractProcessor {

	public void process(Object owner, PropertyDescriptor propertyDescriptor,
			DynaBean dynaBean, UserSession session, boolean isClientDelete) {
		String propertyName = propertyDescriptor.getName();
		Object value = dynaBean.get(propertyName);
		process(owner, propertyDescriptor, value, session, isClientDelete);
	}

	@SuppressWarnings("unchecked")
	public void process(Object owner, PropertyDescriptor propertyDescriptor,
			Object value, UserSession session, boolean isClientDelete) {
		if (value == null) {
			if (isClientDelete) {
				invokeSetMethod(owner, propertyDescriptor, null);
			}
			return;
		}
		DynaBean lookupDynaBean = (DynaBean) value;
		String lookupBeanId = BeanUtil.idValue((String) lookupDynaBean
				.get(BeanUtil.PRIMARYKEY));
		if (lookupBeanId == null) {
			invokeSetMethod(owner, propertyDescriptor, null);
			return;
		}
		Class<?> clazz = BeanUtil.getOriginalClass(propertyDescriptor
				.getPropertyType());
		AbstractManager beanManager = SpringHelper.getManagerByClass(clazz);
		Object lookupBean = beanManager.getObject(lookupBeanId);
		invokeSetMethod(owner, propertyDescriptor, lookupBean);
	}
}

class ComponentProcessor extends AbstractProcessor {

	public void process(Object owner, PropertyDescriptor propertyDescriptor,
			DynaBean dynaBean, UserSession session, boolean isClientDelete) {
		String propertyName = propertyDescriptor.getName();
		Object value = dynaBean.get(propertyName);
		process(owner, propertyDescriptor, value, session, isClientDelete);
	}

	public void process(Object owner, PropertyDescriptor propertyDescriptor,
			Object value, UserSession session, boolean isClientDelete) {
		LazyDynaBean lookupDynaBean = (LazyDynaBean) value;
		if (lookupDynaBean != null && lookupDynaBean.getMap() != null
				&& lookupDynaBean.getMap().size() > 0) {
			Object componentClass = null;
			if (value == null) {
				if (isClientDelete) {
					invokeSetMethod(owner, propertyDescriptor, null);
				}
				return;
			}
			componentClass = invokeGetMethod(owner, propertyDescriptor);
			if (componentClass == null) {
				Class<?> clazz = null;
				try {

					clazz = owner
							.getClass()
							.getMethod(
									propertyDescriptor.getReadMethod()
											.getName()).getReturnType();
				} catch (Exception e1) {
					throw ExceptionUtils.wrap(e1);
				}
				try {
					componentClass = clazz.newInstance();
					invokeSetMethod(owner, propertyDescriptor, componentClass);
				} catch (Exception e) {
					BeanUtil.log.error(
							"Can not create instance of " + clazz.getName(), e);
					throw new RuntimeException(e);
				}
			}
			BeanUtil.setProperties(lookupDynaBean, componentClass, session);
		}

	}
}

class JavaEnumProcessor extends AbstractProcessor {

	public void process(Object owner, PropertyDescriptor propertyDescriptor,
			DynaBean dynaBean, UserSession session, boolean isClientDelete) {
		String propertyName = propertyDescriptor.getName();
		Object value = dynaBean.get(propertyName);
		process(owner, propertyDescriptor, value, session, isClientDelete);
	}

	@SuppressWarnings("unchecked")
	public void process(Object owner, PropertyDescriptor propertyDescriptor,
			Object value, UserSession session, boolean isClientDelete) {
		if (value == null) {
			if (isClientDelete) {
				invokeSetMethod(owner, propertyDescriptor, null);
			}
			return;
		}
		if ("".equalsIgnoreCase(((String) value).trim())) {
			invokeSetMethod(owner, propertyDescriptor, null);
			return;
		}

		Class<Enum> entityClass = (Class<Enum>) BeanUtil
				.getOriginalClass(propertyDescriptor.getPropertyType());

		invokeSetMethod(owner, propertyDescriptor,
				Enum.valueOf(entityClass, (String) value));
	}
}


class EncryptedStringProcessor extends AbstractProcessor {

	@Override
	public void process(Object owner, PropertyDescriptor propertyDescriptor,
			DynaBean dynaBean, UserSession session, boolean isClientDelete) {
		String propertyName = propertyDescriptor.getName();
		Object value = dynaBean.get(propertyName);
		if (!(value instanceof FormFile)) // don't parse file field
			process(owner, propertyDescriptor, value, session, isClientDelete);
	}

	public void process(Object owner, PropertyDescriptor propertyDescriptor,
			Object value, UserSession session, boolean isClientDelete) {
		if (value == null) {
			if (isClientDelete) {
				invokeSetMethod(owner, propertyDescriptor, null);
			}
			return;
		}
		if ("".equalsIgnoreCase(((String) value).trim())) {
			invokeSetMethod(owner, propertyDescriptor, null);
			return;
		}
		invokeSetMethod(owner, propertyDescriptor, new EncryptedString(
				(String) value));
	}
}

class DecimalProcessor extends AbstractProcessor {

	@Override
	public void process(Object owner, PropertyDescriptor propertyDescriptor,
			DynaBean dynaBean, UserSession session, boolean isClientDelete) {
		String propertyName = propertyDescriptor.getName();
		Object value = dynaBean.get(propertyName);
		process(owner, propertyDescriptor, value, session, isClientDelete);
	}

	public void process(Object owner, PropertyDescriptor propertyDescriptor,
			Object value, UserSession session, boolean isClientDelete) {
		if (value == null) {
			if (isClientDelete) {
				invokeSetMethod(owner, propertyDescriptor, null);
			}
			return;
		}
		if ("".equalsIgnoreCase(((String) value).trim())) {
			invokeSetMethod(owner, propertyDescriptor, null);
			return;
		}
		invokeSetMethod(owner, propertyDescriptor, null);

	}

}
