package alvin.bef.framework.base.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import alvin.bef.framework.base.session.SessionSynchronizationType;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface SessionSynchronizationInterest {
	SessionSynchronizationType[] type() default { SessionSynchronizationType.SESSION_EVENT };
}
