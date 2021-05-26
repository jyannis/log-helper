package top.jyannis.loghelper.processor;

import org.aspectj.lang.ProceedingJoinPoint;
import top.jyannis.loghelper.domain.LogInfo;

/**
 * @author Jyannis
 * @version 1.0 update on 2021/5/26
 */
public interface LogAspectProcessor {

    /**
     * 返回一个自定义LogInfo。可以返回LogInfo子类，便于开发者存储一些自己想要的信息
     * Return a custom LogInfo.
     * Developers can return a subClass of LogInfo, in order to store some data they need
     */
    LogInfo buildLogInfo();

    /**
     * preLogAround()会在环绕通知方法中首先被执行（在buildLogInfo之后）
     * preLogAround() will proceed firstly in around advice (after buildLogInfo())
     * @param joinPoint
     * @param logInfo the return data from buildLogInfo()
     */
    void preLogAround(ProceedingJoinPoint joinPoint, LogInfo logInfo);

    /**
     * postLogAround()会在环绕通知方法结束前执行
     * postLogAround() will proceed before the end of around advice
     * @param joinPoint
     * @param logInfo the return data from buildLogInfo()
     */
    void postLogAround(ProceedingJoinPoint joinPoint, LogInfo logInfo);

    /**
     * preLogAfterThrow()会在afterThrow通知方法中首先被执行（在buildLogInfo之后）
     * preLogAfterThrow() will proceed firstly in afterThrow advice (after buildLogInfo())
     * @param joinPoint
     * @param logInfo the return data from buildLogInfo()
     */
    void preLogAfterThrow(ProceedingJoinPoint joinPoint, LogInfo logInfo);

    /**
     * postLogAfterThrow()会在afterThrow通知方法结束前执行
     * postLogAfterThrow() will proceed before the end of afterThrow advice
     * @param joinPoint
     * @param logInfo the return data from buildLogInfo()
     */
    void postLogAfterThrow(ProceedingJoinPoint joinPoint, LogInfo logInfo);

}
