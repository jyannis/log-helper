package top.jyannis.loghelper.interceptor;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.web.method.HandlerMethod;
import top.jyannis.loghelper.annotation.Log;
import top.jyannis.loghelper.domain.LogMode;
import top.jyannis.loghelper.processor.LogProcessor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @author Jyannis
 * @version 1.0 update on 2021/5/23
 */
public class LogAnnotationInterceptor extends LogInterceptor {

    public LogAnnotationInterceptor(LogProcessor logProcessor) {
        super(logProcessor);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if(getLogMode(handler) != null){
            super.preHandle(request,response,handler);
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        String logMode = getLogMode(handler);
        if(ex == null){
            if(StringUtils.equals(logMode, LogMode.ALL) || StringUtils.equals(logMode, LogMode.INFO)){
                super.afterCompletion(request,response,handler,null);
            }
            return;
        }
        if(StringUtils.equals(logMode, LogMode.ALL) || StringUtils.equals(logMode, LogMode.ERROR)){
            super.afterCompletion(request,response,handler,ex);
        }
    }

    private String getLogMode(Object handler){
        if (handler instanceof HandlerMethod) {
            Method method = ((HandlerMethod) handler).getMethod();
            if (AnnotatedElementUtils.isAnnotated(method, Log.class)) {
                return method.getAnnotation(Log.class).mode();
            }
        }
        return null;
    }

}
