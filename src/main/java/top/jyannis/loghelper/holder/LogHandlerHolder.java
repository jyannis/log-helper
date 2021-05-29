package top.jyannis.loghelper.holder;

import lombok.extern.slf4j.Slf4j;
import top.jyannis.loghelper.domain.LogMode;
import top.jyannis.loghelper.handler.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Jyannis
 * @version 1.0 update on 2021/5/26
 */
@Slf4j
public class LogHandlerHolder {

    private Map<String,LogHandler> logHandlerMap;

    public LogHandlerHolder() {
        logHandlerMap = new HashMap<>();
        addLogHandler(LogMode.ALL,new LogAllHandler());
        addLogHandler(LogMode.INFO,new LogInfoHandler());
        addLogHandler(LogMode.ERROR,new LogErrorHandler());
        addLogHandler(LogMode.NONE,new LogNoneHandler());
    }

    public Map<String, LogHandler> getLogHandlerMap() {
        return logHandlerMap;
    }

    public void addLogHandler(String logMode, LogHandler logHandler) {
        logHandlerMap.put(logMode, logHandler);
    }

    public LogHandler getLogHandler(String logMode){
        LogHandler logHandler = logHandlerMap.get(logMode);
        if(logHandler == null){
            log.warn("logHandler in logMode:{} not exists",logMode);
            return logHandlerMap.get(LogMode.ALL);
        }
        return logHandler;
    }
}
