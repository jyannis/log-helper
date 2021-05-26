package top.jyannis.loghelper.autoconfigure;

import top.jyannis.loghelper.aspect.LogAspect;
import top.jyannis.loghelper.domain.LogMode;
import top.jyannis.loghelper.holder.LogFilterChainHolder;
import top.jyannis.loghelper.holder.LogProcessorHolder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.jyannis.loghelper.processor.DefaultLogAspectProcessor;
import top.jyannis.loghelper.processor.LogAspectProcessor;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;

/**
 * @author Jyannis
 * @version 1.0 update on 2021/5/20
 */
@Configuration
public class LogAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    LogFilterChainHolder logFilterChainHolder(){
        LinkedHashMap<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        filterChainDefinitionMap.put("/**", LogMode.ALL);
        return new LogFilterChainHolder(filterChainDefinitionMap);
    }

    @Bean
    @ConditionalOnMissingBean
    LogProcessorHolder logProcessorHolder(){
        return new LogProcessorHolder();
    }

    @Bean
    @ConditionalOnMissingBean
    LogAspectProcessor logAspectProcessor(){
        return new DefaultLogAspectProcessor();
    }

    @Bean
    LogAspect logAspect(HttpServletRequest httpServletRequest,
                        LogFilterChainHolder logProcessor,
                        LogProcessorHolder logProcessorHolder,
                        LogAspectProcessor logAspectProcessor){
        return new LogAspect(httpServletRequest,logProcessor,logProcessorHolder,logAspectProcessor);
    }
}
