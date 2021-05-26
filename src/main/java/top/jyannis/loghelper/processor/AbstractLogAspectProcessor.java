package top.jyannis.loghelper.processor;

import org.aspectj.lang.ProceedingJoinPoint;
import top.jyannis.loghelper.domain.LogInfo;

/**
 * @author Jyannis
 * @version 1.0 update on 2021/5/26
 */
public abstract class AbstractLogAspectProcessor implements LogAspectProcessor {

    @Override
    public LogInfo buildLogInfo() {
        return new LogInfo();
    }

}
