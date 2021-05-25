package top.jyannis.loghelper.register;

import org.springframework.beans.factory.support.*;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.web.servlet.handler.MappedInterceptor;
import top.jyannis.loghelper.holder.LogFilterChainHolder;
import top.jyannis.loghelper.holder.LogInterceptorHolder;
import top.jyannis.loghelper.interceptor.LogInterceptor;

import java.util.*;

/**
 * @author Jyannis
 * @version 1.0 update on 2021/5/244
 */
public class MappedInterceptorRegister {

    private final ApplicationContext applicationContext;
    private final LogFilterChainHolder logFilterChainHolder;
    private final LogInterceptorHolder logInterceptorHolder;
    private int index = 0;

    public MappedInterceptorRegister(ApplicationContext applicationContext, LogFilterChainHolder logFilterChainHolder, LogInterceptorHolder logInterceptorHolder) {
        this.applicationContext = applicationContext;
        this.logFilterChainHolder = logFilterChainHolder;
        this.logInterceptorHolder = logInterceptorHolder;
    }

    public void registerMappedInterceptors(){
        Map<String, String> filterChainDefinitionMap = logFilterChainHolder.getFilterChainDefinitionMap();
        Set<String> keySet = filterChainDefinitionMap.keySet();
        String[] patterns = keySet.toArray(new String[keySet.size()]);
        int length = patterns.length;
        for(int i = 0; i < length; i++){
            List<Object> mappedInterceptorFields = prepareMappedInterceptor(filterChainDefinitionMap, patterns, i, length);
            registerMappedInterceptor(mappedInterceptorFields);
        }
    }

    private void registerMappedInterceptor(List<Object> mappedInterceptorFields) {
        DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory)applicationContext.getAutowireCapableBeanFactory();
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(MappedInterceptor.class);
        mappedInterceptorFields
                .forEach(beanDefinitionBuilder::addConstructorArgValue);
        AbstractBeanDefinition beanDefinition = beanDefinitionBuilder.getBeanDefinition();
        defaultListableBeanFactory.registerBeanDefinition("mappedInterceptor#jyannis" + index, beanDefinition);
        index ++;
    }

    private List<Object> prepareMappedInterceptor(Map<String, String> filterChainDefinitionMap, String[] patterns, int i, int length) {
        String logMode = filterChainDefinitionMap.get(patterns[i]);
        String[] excludePatterns = i == length - 1 ?
                null : Arrays.copyOfRange(patterns, i + 1, length);
        List<Object> mappedInterceptorFields = new ArrayList<>();
        mappedInterceptorFields.add(new String[]{patterns[i]});
        mappedInterceptorFields.add(excludePatterns);
        mappedInterceptorFields.add(logInterceptorHolder.getLogInterceptorMap().get(logMode));
        return mappedInterceptorFields;
    }

}
