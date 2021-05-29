/*
 *  Copyright 2019-2020 Zheng Jie
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package top.jyannis.loghelper.aspect;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.annotation.*;
import top.jyannis.loghelper.domain.LogInfo;
import top.jyannis.loghelper.holder.LogFilterChainHolder;
import top.jyannis.loghelper.holder.LogHandlerHolder;
import top.jyannis.loghelper.handler.LogHandler;
import top.jyannis.loghelper.processor.LogAspectProcessor;
import top.jyannis.loghelper.util.RequestUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Jyannis
 * @version 1.0 update on 2021/5/26
 */
@Aspect
public class LogAspect {

    private final HttpServletRequest request;
    private final LogFilterChainHolder logFilterChainHolder;
    private final LogHandlerHolder logHandlerHolder;
    private final LogAspectProcessor logAspectProcessor;
    private LogHandler logHandler;

    private ThreadLocal<Long> currentTimeThreadLocal = new ThreadLocal<>();

    public LogAspect(HttpServletRequest request, LogFilterChainHolder logFilterChainHolder, LogHandlerHolder logHandlerHolder, LogAspectProcessor logAspectProcessor) {
        this.request = request;
        this.logFilterChainHolder = logFilterChainHolder;
        this.logHandlerHolder = logHandlerHolder;
        this.logAspectProcessor = logAspectProcessor;
    }

    private void prepareLogProcessor(String lookupPath){
        /**
         * 用户访问不存在的url时会跳转到/error，此时没必要记录日志
         * When a user accesses a URL that does not exist, it will redirect to /error, no logs will be recorded.
         */
        if(StringUtils.equals("/error",lookupPath)){
            this.logHandler = null;
            return;
        }
        String logMode = logFilterChainHolder.matches(lookupPath);
        this.logHandler = logHandlerHolder.getLogHandler(logMode);
    }

    @Pointcut("@within(org.springframework.stereotype.Controller)")
    public void controllerPointcut() {
    }

    @Pointcut("@within(org.springframework.web.bind.annotation.RestController)")
    public void restControllerPointcut() {
    }

    @Pointcut("controllerPointcut() || restControllerPointcut()")
    public void logPointcut(){

    }

    @Around("logPointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {

        LogInfo logInfo = logAspectProcessor.buildLogInfo();
        if(logInfo == null)throw new RuntimeException("buildLogInfo() ERROR. logInfo can't be null.");
        logAspectProcessor.preLogAround(joinPoint,logInfo);

        String lookupPath = RequestUtil.getLookupPath(request);
        prepareLogProcessor(lookupPath);
        Object result;
        currentTimeThreadLocal.set(System.currentTimeMillis());
        if(logHandler == null){
            result = joinPoint.proceed();
            return result;
        }
        result = joinPoint.proceed();

        assembleLogInfo(logInfo,"INFO");
        logHandler.preHandle(joinPoint, logInfo);
        logHandler.processAround(logInfo);

        logAspectProcessor.postLogAround(joinPoint,logInfo);
        return result;
    }

    @AfterThrowing(pointcut = "logPointcut()", throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {

        LogInfo logInfo = logAspectProcessor.buildLogInfo();
        if(logInfo == null)throw new RuntimeException("buildLogInfo() ERROR. logInfo can't be null.");
        logAspectProcessor.preLogAfterThrow((ProceedingJoinPoint)joinPoint,logInfo);
        assembleLogInfo(logInfo,"ERROR");
        logInfo.setThrowable(e);
        logHandler.preHandle((ProceedingJoinPoint) joinPoint, logInfo);
        logHandler.processAfterThrow(logInfo);
        logAspectProcessor.postLogAfterThrow((ProceedingJoinPoint)joinPoint,logInfo);

    }

    private void assembleLogInfo(LogInfo logInfo, String logType){
        logInfo.setTime(System.currentTimeMillis() - currentTimeThreadLocal.get());
        currentTimeThreadLocal.remove();
        logInfo.setLogType(logType);
        logInfo.setLookupPath(RequestUtil.getLookupPath(request));
        logInfo.setRequestIp(RequestUtil.getIp(request));
        logInfo.setBrowser(RequestUtil.getBrowser(request));
        logInfo.setMethod(RequestUtil.getMethodName(request));
    }

}
