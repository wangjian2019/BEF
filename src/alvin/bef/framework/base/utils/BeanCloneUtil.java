package alvin.bef.framework.base.utils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;

import alvin.bef.framework.base.query.ICopyFilter;

/**
 * @author Alvin
 * 
 */
public class BeanCloneUtil {
	/**
	 * simple filter
	 */
	public static final ICopyFilter _SimpleFilter = new ICopyFilter() {
		private static final long serialVersionUID = 1566163984692334683L;

		public boolean validate(PropertyDescriptor pd) {
			return !pd.getName().equals("class") && !pd.getName().equals("id")
					&& !pd.getName().equals("versionOptimizedLock");
		}
	};

	public static final ICopyFilter _StandardFilter = new ICopyFilter() {
		private static final long serialVersionUID = 5325323022862331178L;

		public boolean validate(PropertyDescriptor pd) {
			return BeanCloneUtil._SimpleFilter.validate(pd)
					&& !pd.getName().equals("createdBy")
					&& !pd.getName().equals("createdOn")
					&& !pd.getName().equals("updatedBy")
					&& !pd.getName().equals("updatedOn");
		}
	};
	public static final ICopyFilter _SpecialFilter = new ICopyFilter() {
		private static final long serialVersionUID = 5325323022862331178L;

		public boolean validate(PropertyDescriptor pd) {
			return BeanCloneUtil._StandardFilter.validate(pd);
		}
	};

	private static void shallowCopyBean(Object srcObj, Object dstObj,
			ICopyFilter filter) {
		PropertyDescriptor[] pds = PropertyUtils
				.getPropertyDescriptors(BeanUtil.getOriginalClass(srcObj
						.getClass()));
		for (int i = 0; i < pds.length; i++) {
			if (filter == null || filter.validate(pds[i])) {
				if (pds[i].getReadMethod() != null
						&& pds[i].getWriteMethod() != null) {
					try {
						Object value = getValue(pds[i], srcObj);
						Class clz = pds[i].getPropertyType();
						if (clz.isArray()) {
							Object newValue = Array.newInstance(clz,
									Array.getLength(value));
							for (int k = 0; k < Array.getLength(value); k++) {
								Array.set(newValue, k, Array.get(value, k));
							}
							value = newValue;
						} else if (Map.class.isAssignableFrom(clz)) {
							Map newValue = new HashMap();
							if (value != null)
								for (Iterator ite = ((Map) value).keySet()
										.iterator(); ite.hasNext();) {
									Object key = ite.next();
									Object o = ((Map) value).get(key);
									newValue.put(key, o);
								}
							value = newValue;
						} else if (Collection.class.isAssignableFrom(clz)) {
							Collection newValue = null;
							if (List.class.isAssignableFrom(clz)) {
								newValue = new ArrayList();
							} else {
								newValue = new HashSet();
							}
							if (value != null)
								for (Iterator ite = ((Collection) value)
										.iterator(); ite.hasNext();) {
									newValue.add(ite.next());
								}
							value = newValue;
						}
						pds[i].getWriteMethod().invoke(dstObj,
								new Object[] { value });
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				}
			}
		}
	}

	private static Object getValue(PropertyDescriptor pd, Object srcObj)
			throws IllegalArgumentException, IllegalAccessException,
			InvocationTargetException, InstantiationException {
		Object value = pd.getReadMethod().invoke(srcObj, new Object[0]);
		if (value != null) {
			Class cla = BeanUtil.getOriginalClass(value.getClass());
			Object o = cla.newInstance();
			shallowCopyBean(value, o, _SimpleFilter);
			return o;
		}
		return value;
	}

	public static void simpleCopyBean(Object srcObj, Object dstObj,
			ICopyFilter filter) {
		PropertyDescriptor[] pds = PropertyUtils
				.getPropertyDescriptors(BeanUtil.getOriginalClass(srcObj
						.getClass()));
		for (int i = 0; i < pds.length; i++) {
			if (filter == null || filter.validate(pds[i])) {
				if (pds[i].getReadMethod() != null
						&& pds[i].getWriteMethod() != null) {
					try {
						Object value = getValue(pds[i], srcObj);
						Class clz = pds[i].getPropertyType();
						if (clz.isArray()) {
							value = null;
						} else if (Map.class.isAssignableFrom(clz)) {
							value = null;
						} else if (Collection.class.isAssignableFrom(clz)) {
							value = null;
						}
						pds[i].getWriteMethod().invoke(dstObj,
								new Object[] { value });
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				}
			}
		}
	}

	public static Object getFieldValue(Object obj, String field) {
		PropertyDescriptor[] pds = PropertyUtils
				.getPropertyDescriptors(BeanUtil.getOriginalClass(obj
						.getClass()));
		Object value = null;
		for (int i = 0; i < pds.length; i++) {
			if (pds[i].getReadMethod() != null) {
				try {
					String fname = pds[i].getName();
					if (fname.equalsIgnoreCase(field)) {
						value = pds[i].getReadMethod().invoke(obj,
								new Object[0]);
					}
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		}
		return value;
	}

	public static void setFieldValue(Object obj, String field, Object value) {
		PropertyDescriptor[] pds = PropertyUtils
				.getPropertyDescriptors(BeanUtil.getOriginalClass(obj
						.getClass()));
		for (int i = 0; i < pds.length; i++) {
			if (pds[i].getReadMethod() != null
					&& pds[i].getWriteMethod() != null) {
				try {
					String fname = pds[i].getName();
					if (fname.equalsIgnoreCase(field)) {
						pds[i].getWriteMethod().invoke(obj,
								new Object[] { value });
					}
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		}
	}

	public static Object clone(final Object obj)
			throws CloneNotSupportedException {
		if (obj == null) {
			return null;
		}
		if (obj instanceof Cloneable) {
			Class<?> clazz = obj.getClass();
			Method m;
			try {
				m = clazz.getMethod("clone", (Class[]) null);
			} catch (NoSuchMethodException ex) {
				throw new NoSuchMethodError(ex.getMessage());
			}
			try {
				return m.invoke(obj, (Object[]) null);
			} catch (InvocationTargetException ex) {
				Throwable cause = ex.getCause();
				if (cause instanceof CloneNotSupportedException) {
					throw ((CloneNotSupportedException) cause);
				} else {
					throw new Error("Unexpected exception", cause);
				}
			} catch (IllegalAccessException ex) {
				throw new IllegalAccessError(ex.getMessage());
			}
		} else {
			throw new CloneNotSupportedException();
		}
	}

}
