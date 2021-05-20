package top.jyannis.loghelper.autoconfigure;

import top.jyannis.loghelper.aspect.LogAspect;
import top.jyannis.loghelper.processor.DefaultLogProcessor;
import top.jyannis.loghelper.processor.LogProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Jyannis
 * @version 1.0 update on 2021/5/20
 */
@Configuration
public class LogAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    LogProcessor logProcessor(){
        return new DefaultLogProcessor();
    }

    @Bean
    LogAspect logAspect(LogProcessor logProcessor){
        return new LogAspect(logProcessor);
    }

}
