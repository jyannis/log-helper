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
package top.jyannis.loghelper.handler;

import lombok.extern.slf4j.Slf4j;
import top.jyannis.loghelper.domain.LogInfo;
import top.jyannis.loghelper.util.ThrowableUtil;

/**
 * @author Jyannis
 * @version 1.0 update on 2021/5/26
 */
@Slf4j
public class LogErrorHandler extends AbstractLogHandler {

    @Override
    public void processAround(LogInfo logInfo) {
    }

    @Override
    public void processAfterThrow(LogInfo logInfo) {
        String stackTrace = ThrowableUtil.getStackTrace(logInfo.getThrowable());
        log.error("call method: {}",logInfo.getMethod());
        log.error("call url: {}",logInfo.getLookupPath());
        log.error("request params: {}",logInfo.getParams());
        log.error("request ip: {}",logInfo.getRequestIp());
        log.error("request address: {}",logInfo.getAddress());
        log.error("request browser: {}",logInfo.getBrowser());
        log.error("request time cost: {} ms",logInfo.getTime());
        log.error(stackTrace);
    }

}