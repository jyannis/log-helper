package top.jyannis.loghelper.holder;

import top.jyannis.loghelper.interceptor.LogInterceptor;

import java.util.Map;

/**
 * @author Jyannis
 * @version 1.0 update on 2021/5/24
 */
public class LogInterceptorHolder {

    private Map<String,LogInterceptor> logInterceptorMap;

    public LogInterceptorHolder() {
    }

    public LogInterceptorHolder(Map<String, LogInterceptor> logInterceptorMap) {
        this.logInterceptorMap = logInterceptorMap;
    }

    public Map<String, LogInterceptor> getLogInterceptorMap() {
        return logInterceptorMap;
    }

    public void setLogInterceptorMap(Map<String, LogInterceptor> logInterceptorMap) {
        this.logInterceptorMap = logInterceptorMap;
    }
}
