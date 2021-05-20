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
package top.jyannis.loghelper.processor;

import top.jyannis.loghelper.domain.LogInfo;
import top.jyannis.loghelper.util.ThrowableUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Jyannis
 * @version 1.0 update on 2021/5/20
 */
@Slf4j
public class DefaultLogProcessor extends AbstractLogProcessor {

    @Override
    public void processAround(LogInfo logInfo) {
        log.info("call method: {}",logInfo.getMethod());
        log.info("request params: {}",logInfo.getParams());
        log.info("request ip: {}",logInfo.getRequestIp());
        log.info("request address: {}",logInfo.getAddress());
        log.info("request browser: {}",logInfo.getBrowser());
        log.info("request time cost: {} ms",logInfo.getTime());
    }

    @Override
    public void processAfterThrow(LogInfo logInfo) {
        String stackTrace = ThrowableUtil.getStackTrace(logInfo.getThrowable());
        log.error("call method: {}",logInfo.getMethod());
        log.error("request params: {}",logInfo.getParams());
        log.error("request ip: {}",logInfo.getRequestIp());
        log.error("request address: {}",logInfo.getAddress());
        log.error("request browser: {}",logInfo.getBrowser());
        log.error("request time cost: {} ms",logInfo.getTime());
        log.error(stackTrace);
    }

}
