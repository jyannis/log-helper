package top.jyannis.loghelper.interceptor;

import top.jyannis.loghelper.processor.LogProcessor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Jyannis
 * @version 1.0 update on 2021/5/23
 */
public class LogAllInterceptor extends LogInterceptor {

    public LogAllInterceptor(LogProcessor logProcessor) {
        super(logProcessor);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        super.preHandle(request,response,handler);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        super.afterCompletion(request,response,handler,ex);
    }

}
