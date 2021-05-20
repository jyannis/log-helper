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
package com.jyannis.loghelper.aspect;

import com.jyannis.loghelper.annotation.Log;
import com.jyannis.loghelper.domain.LogInfo;
import com.jyannis.loghelper.processor.LogProcessor;
import com.jyannis.loghelper.util.RequestHolder;
import com.jyannis.loghelper.util.RequestUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @author Jyannis
 * @version 1.0 update on 2021/5/20
 */
@Aspect
public class LogAspect {

    private final LogProcessor logProcessor;

    private ThreadLocal<Long> currentTime = new ThreadLocal<>();

    public LogAspect(LogProcessor logProcessor) {
        this.logProcessor = logProcessor;
    }

    /**
     * 配置切入点
     */
    @Pointcut("@annotation(com.jyannis.loghelper.annotation.Log)")
    public void logPointcut() {
    }

    @Around("logPointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result;
        currentTime.set(System.currentTimeMillis());
        if(!Arrays.asList("ALL", "INFO").contains(getMode(joinPoint))){
            result = joinPoint.proceed();
            currentTime.remove();
            return result;
        }
        result = joinPoint.proceed();
        LogInfo logInfo = new LogInfo("INFO",System.currentTimeMillis() - currentTime.get());
        currentTime.remove();
        HttpServletRequest request = RequestHolder.getHttpServletRequest();
        logInfo.setRequestIp(RequestUtil.getIp(request));
        logInfo.setBrowser(RequestUtil.getBrowser(request));
        logProcessor.process(joinPoint, logInfo);
        return result;
    }

    @AfterThrowing(pointcut = "logPointcut()", throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
        if(!Arrays.asList("ALL", "ERROR").contains(getMode(joinPoint))){
            currentTime.remove();
            return ;
        }
        LogInfo logInfo = new LogInfo("ERROR",System.currentTimeMillis() - currentTime.get());
        currentTime.remove();
        logInfo.setThrowable(e);
        HttpServletRequest request = RequestHolder.getHttpServletRequest();
        logInfo.setRequestIp(RequestUtil.getIp(request));
        logInfo.setBrowser(RequestUtil.getBrowser(request));
        logProcessor.process((ProceedingJoinPoint) joinPoint, logInfo);
    }

    private String getMode(JoinPoint joinPoint){
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Log log = method.getAnnotation(Log.class);
        return log.mode();
    }
}
