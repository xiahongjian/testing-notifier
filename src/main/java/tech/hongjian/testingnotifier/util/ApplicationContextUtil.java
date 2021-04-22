package tech.hongjian.testingnotifier.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Created by xiahongjian on 2021/4/22.
 */
@Component
public class ApplicationContextUtil implements ApplicationContextAware {
    private static ApplicationContext appCtx;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        appCtx = applicationContext;
    }

    public static <T> T getBean(Class<T> clazz) {
        return appCtx.getBean(clazz);
    }

    public static Object getBean(String beanName) {
        return appCtx.containsBean(beanName) ? appCtx.getBean(beanName) : null;
    }

    public static ApplicationContext getAppCtx() {
        return appCtx;
    }
}
