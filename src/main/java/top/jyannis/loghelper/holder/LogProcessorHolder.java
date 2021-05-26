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
public class LogProcessorHolder {

    private Map<String,LogHandler> logProcessorMap;

    public LogProcessorHolder() {
        logProcessorMap = new HashMap<>();
        addLogProcessor(LogMode.ALL,new LogAllHandler());
        addLogProcessor(LogMode.INFO,new LogInfoHandler());
        addLogProcessor(LogMode.ERROR,new LogErrorHandler());
        addLogProcessor(LogMode.NONE,new LogNoneHandler());
    }

    public Map<String, LogHandler> getLogProcessorMap() {
        return logProcessorMap;
    }

    public void addLogProcessor(String logMode, LogHandler logHandler) {
        logProcessorMap.put(logMode, logHandler);
    }

    public LogHandler getLogProcessor(String logMode){
        LogHandler logHandler = logProcessorMap.get(logMode);
        if(logHandler == null){
            log.warn("logHandler in logMode:{} not exists",logMode);
            return logProcessorMap.get(LogMode.ALL);
        }
        return logHandler;
    }
}
