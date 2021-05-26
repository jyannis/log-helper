package top.jyannis.loghelper.handler;

import cn.hutool.json.JSONUtil;
import org.springframework.web.multipart.MultipartFile;
import top.jyannis.loghelper.domain.LogFileInfo;
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
public abstract class AbstractLogHandler implements LogHandler {

    @Override
    public void preHandle(ProceedingJoinPoint joinPoint, LogInfo logInfo) {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        /**
         * method full name has been gotten by log aspect
         * 获取方法名的逻辑放在了log aspect中
         */
//        String methodName = joinPoint.getTarget().getClass().getName() + "." + signature.getName() + "()";

        assert logInfo != null;

        logInfo.setAddress(RequestUtil.getHttpCityInfo(logInfo.getRequestIp()));
//        logInfo.setMethod(methodName);
        logInfo.setParams(getParameter(method, joinPoint.getArgs()));

    }

    private String getParameter(Method method, Object[] args) {
        List<Object> argList = new ArrayList<>();
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            //arguments decorated by @RequestBody
            RequestBody requestBody = parameters[i].getAnnotation(RequestBody.class);
            RequestParam requestParam = parameters[i].getAnnotation(RequestParam.class);
            if (requestBody != null) {
                argList.add(args[i]);
            } else if (requestParam != null) { //arguments decorated by @RequestParam
                Map<String, Object> map = new HashMap<>();
                String key = parameters[i].getName();
                if (!StringUtils.isEmpty(requestParam.value())) {
                    key = requestParam.value();
                }
                /**
                 * 过滤文件参数
                 * filter file arg
                 */
                args[i] = filterFileArg(args[i]);
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

    private Object filterFileArg(Object arg) {
        if(arg instanceof MultipartFile){
            MultipartFile file = (MultipartFile)arg;
            return new LogFileInfo(file.getSize(),file.getOriginalFilename());
        }
        return arg;
    }

}
