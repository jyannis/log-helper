package top.jyannis.loghelper.interceptor;

import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import top.jyannis.loghelper.annotation.Log;
import top.jyannis.loghelper.domain.LogInfo;
import top.jyannis.loghelper.processor.LogProcessor;
import top.jyannis.loghelper.util.RequestUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.nio.charset.Charset;
import java.util.*;

/**
 * @author Jyannis
 * @version 1.0 update on 2021/5/23
 */
public abstract class LogInterceptor implements HandlerInterceptor {

    private final LogProcessor logProcessor;

    private ThreadLocal<Long> currentTime = new ThreadLocal<>();

    public LogInterceptor(LogProcessor logProcessor) {
        this.logProcessor = logProcessor;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        currentTime.set(System.currentTimeMillis());
//        无法读取stream，否则会导致controller异常
//        InputStream inputStream = request.getInputStream();
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        LogInfo logInfo = new LogInfo(System.currentTimeMillis() - currentTime.get());
        currentTime.remove();
        logInfo.setRequestIp(RequestUtil.getIp(request));
        logInfo.setAddress(RequestUtil.getHttpCityInfo(logInfo.getRequestIp()));
        logInfo.setBrowser(RequestUtil.getBrowser(request));
        if (handler instanceof HandlerMethod) {


//             获取方法名 方式一
//            Class<?> clazz = handler.getClass();
//            try {
//                Field field = clazz.getDeclaredField("description");
//                field.setAccessible(true);
//                System.out.println("--"+field.get(handler));
//            } catch (NoSuchFieldException | IllegalAccessException e) {
//                e.printStackTrace();
//            }

            logInfo.setMethod(request.getAttribute("org.springframework.web.servlet.HandlerMapping.bestMatchingHandler").toString());
//            获取方法名 方式二
//            logInfo.setMethod(method.getDeclaringClass().getName() + "#" + method.getName());
        }
        if(ex == null){
            logInfo.setLogType("INFO");
            logProcessor.processAround(logInfo);
        }else{
            logInfo.setLogType("ERROR");
            logProcessor.processAfterThrow(logInfo);
        }
    }

}
