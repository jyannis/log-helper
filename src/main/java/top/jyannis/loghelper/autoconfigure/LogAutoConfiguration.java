package top.jyannis.loghelper.autoconfigure;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.context.ApplicationContext;
import top.jyannis.loghelper.domain.LogMode;
import top.jyannis.loghelper.holder.LogFilterChainHolder;
import top.jyannis.loghelper.holder.LogInterceptorHolder;
import top.jyannis.loghelper.interceptor.*;
import top.jyannis.loghelper.processor.DefaultLogProcessor;
import top.jyannis.loghelper.processor.LogProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.jyannis.loghelper.register.MappedInterceptorRegister;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Jyannis
 * @version 1.0 update on 2021/5/20
 */
@Configuration
@AutoConfigureBefore(WebMvcAutoConfiguration.class)
public class LogAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    LogProcessor logProcessor(){
        return new DefaultLogProcessor();
    }

//    @Bean
//    LogAspect logAspect(LogProcessor logProcessor){
//        return new LogAspect(logProcessor);
//    }

    @Bean
    @ConditionalOnMissingBean
    LogAllInterceptor logAllInterceptor(LogProcessor logProcessor){
        return new LogAllInterceptor(logProcessor);
    }

    @Bean
    @ConditionalOnMissingBean
    LogErrorInterceptor logErrorInterceptor(LogProcessor logProcessor){
        return new LogErrorInterceptor(logProcessor);
    }

    @Bean
    @ConditionalOnMissingBean
    LogInfoInterceptor logInfoInterceptor(LogProcessor logProcessor){
        return new LogInfoInterceptor(logProcessor);
    }

    @Bean
    @ConditionalOnMissingBean
    LogAnnotationInterceptor logAnnotationInterceptor(LogProcessor logProcessor){
        return new LogAnnotationInterceptor(logProcessor);
    }

    @Bean
    @ConditionalOnMissingBean
    LogFilterChainHolder logFilterFactoryBean(){
        Map<String, String> filterChainDefinitionMap = new HashMap<>();
        filterChainDefinitionMap.put("/**", LogMode.ALL);
        return new LogFilterChainHolder(filterChainDefinitionMap);
    }

    @Bean
    LogInterceptorHolder logInterceptorHolder(LogAllInterceptor logAllInterceptor,
                                              LogInfoInterceptor logInfoInterceptor,
                                              LogErrorInterceptor logErrorInterceptor,
                                              LogAnnotationInterceptor logAnnotationInterceptor){
        Map<String,LogInterceptor> logInterceptorMap = new HashMap<>();
        logInterceptorMap.put(LogMode.ALL,logAllInterceptor);
        logInterceptorMap.put(LogMode.INFO,logInfoInterceptor);
        logInterceptorMap.put(LogMode.ERROR,logErrorInterceptor);
        logInterceptorMap.put(null,logAnnotationInterceptor);
        return new LogInterceptorHolder(logInterceptorMap);
    }

    @Bean
    MappedInterceptorRegister mappedInterceptorRegister(ApplicationContext applicationContext,
                                                        LogFilterChainHolder logFilterChainHolder,
                                                        LogInterceptorHolder logInterceptorHolder){
        MappedInterceptorRegister mappedInterceptorRegister = new MappedInterceptorRegister(applicationContext, logFilterChainHolder, logInterceptorHolder);
        mappedInterceptorRegister.registerMappedInterceptors();
        return mappedInterceptorRegister;
    }
}
