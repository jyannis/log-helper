package top.jyannis.loghelper.processor;

import cn.hutool.json.JSONUtil;
import top.jyannis.loghelper.domain.LogInfo;
import top.jyannis.loghelper.util.RequestUtil;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Jyannis
 * @version 1.0 update on 2021/5/20
 */
public abstract class AbstractLogProcessor implements LogProcessor {

    @Override
    public void process(ProceedingJoinPoint joinPoint, LogInfo logInfo) {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        // method full name
        String methodName = joinPoint.getTarget().getClass().getName() + "." + signature.getName() + "()";

        assert logInfo != null;

        logInfo.setAddress(RequestUtil.getHttpCityInfo(logInfo.getRequestIp()));
        logInfo.setMethod(methodName);
        logInfo.setParams(getParameter(method, joinPoint.getArgs()));
        process(logInfo);
    }

    @Override
    public void process(LogInfo logInfo){
        String logType = logInfo.getLogType();
        if(StringUtils.equals(logType, "INFO")){
            processAround(logInfo);
            return;
        }
        if(StringUtils.equals(logType, "ERROR")){
            processAfterThrow(logInfo);
        }
    }

    private String getParameter(Method method, Object[] args) {
        List<Object> argList = new ArrayList<>();
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            //arguments decorated by @RequestBody
            RequestBody requestBody = parameters[i].getAnnotation(RequestBody.class);
            if (requestBody != null) {
                argList.add(args[i]);
            }
            //arguments decorated by @RequestParam
            RequestParam requestParam = parameters[i].getAnnotation(RequestParam.class);
            if (requestParam != null) {
                Map<String, Object> map = new HashMap<>();
                String key = parameters[i].getName();
                if (!StringUtils.isEmpty(requestParam.value())) {
                    key = requestParam.value();
                }
                map.put(key, args[i]);
                argList.add(map);
            }else {
                //arguments without @RequestBody or @RequestParam
                argList.add(args[i]);
            }
        }
        if (argList.size() == 0) {
            return "";
        }
        return argList.size() == 1 ? JSONUtil.toJsonStr(argList.get(0)) : JSONUtil.toJsonStr(argList);
    }

}
