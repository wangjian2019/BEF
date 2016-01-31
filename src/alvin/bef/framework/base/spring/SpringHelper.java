package alvin.bef.framework.base.spring;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.springframework.beans.BeansException;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.context.ApplicationContext;

import alvin.bef.framework.base.abstractmanager.AbstractManager;
import alvin.bef.framework.base.exception.GenericException;
import alvin.bef.framework.base.session.manager.SessionManager;

/**
 * @author Alvin
 *
 */
public class SpringHelper {
	private static final Log log = LogFactory.getLog(SpringHelper.class);

	private static ApplicationContext context;

	public static void init(ApplicationContext context) {
		SpringHelper.context = context;
	}

	public static boolean isInit() {
		return SpringHelper.context != null;
	}

	public static ApplicationContext getApplicationContext() {
		return context;
	}

	public static Object getBean(String beanName) {
		ApplicationContext ctx = getApplicationContext();
		return ctx.getBean(beanName);
	}

	public static <B> B getBean(Class<B> beanClass) {
		return getBean(getApplicationContext(), beanClass);
	}

	@SuppressWarnings("unchecked")
	public static <B> B getBean(ApplicationContext context, Class<B> beanClass) {
		B bean = null;
		Map<String, B> beansByName = context.getBeansOfType(beanClass, false,
				true);
		switch (beansByName.size()) {
		case 0:
			throw new GenericException("No bean found with class " + beanClass);

		case 1:
			return beansByName.entrySet().iterator().next().getValue();

		default:
			bean = beansByName.get(beanClass.getName());

			if (bean == null) {
				throw new GenericException("Found " + beansByName.size()
						+ " beans of class " + beanClass
						+ " but no exact match to class name.");
			}

			return bean;
		}
	}

	/**
	 * @param beanClassName
	 * @return
	 */
	public static AbstractManager<?> getManagerByClass(String beanClassName) {
		ApplicationContext ctx = getApplicationContext();
		String managerBeanName = beanClassName + ".Manager";
		try {
			return (AbstractManager<?>) ctx.getBean(managerBeanName);
		} catch (BeansException e) {
			if (log.isDebugEnabled())
				log.debug("Cannot get manager for class " + beanClassName, e);
		}
		return null;
	}

	public static AbstractManager getManagerByClass(Class clazz) {
		String className = clazz.getName();
		AbstractManager<?> rtn = getManagerByClass(className);
		if (rtn != null) {
			return rtn;
		}

		// if can not find by className, it might be cglib enhanced or
		// dynamically proxied.
		className = getOriginalClass(clazz).getName();
		return getManagerByClass(className);
	}

	public static Class getOriginalClass(Class clazz) {
		Class ret = clazz;
		while (Enhancer.isEnhanced(ret)) {
			ret = ret.getSuperclass();
		}
		return ret;
	}

	public static AbstractManager getManagerByObject(Object o) {
		if (o != null) {
			return getManagerByClass(o.getClass());
		}
		return null;
	}

	public static SessionManager getSessionManager() {
		// return getBean(SessionManager.class);
		return (SessionManager) context
				.getBean("alvin.bef.framework.base.session.manager.SessionManager");
	}

	public static SessionFactory getSessionFactory() {
		SessionFactory factory = (SessionFactory) SpringHelper
				.getBean("sessionFactory");
		return factory;
	}
}
