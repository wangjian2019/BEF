package alvin.bef.framework.base.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public class SpringHelperInit {
    
    @Autowired
    public void setApplicationContext(ApplicationContext context) {
       SpringHelper.init(context);
    }
 }