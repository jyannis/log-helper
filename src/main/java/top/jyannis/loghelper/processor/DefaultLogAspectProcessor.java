package top.jyannis.loghelper.processor;

import org.aspectj.lang.ProceedingJoinPoint;
import top.jyannis.loghelper.domain.LogInfo;

/**
 * @author Jyannis
 * @version 1.0 update on 2021/5/26
 */
public class DefaultLogAspectProcessor extends AbstractLogAspectProcessor {

    @Override
    public void preLogAround(ProceedingJoinPoint joinPoint, LogInfo logInfo) {

    }

    @Override
    public void postLogAround(ProceedingJoinPoint joinPoint, LogInfo logInfo) {

    }

    @Override
    public void preLogAfterThrow(ProceedingJoinPoint joinPoint, LogInfo logInfo) {

    }

    @Override
    public void postLogAfterThrow(ProceedingJoinPoint joinPoint, LogInfo logInfo) {

    }
}
