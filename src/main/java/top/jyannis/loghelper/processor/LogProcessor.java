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
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.scheduling.annotation.Async;

/**
 * @author Jyannis
 * @version 1.0 update on 2021/5/20
 */
public interface LogProcessor {

    /**
     * process log
     * @param joinPoint @log joinPoint
     * @param logInfo log data
     */
    @Async
    void process(ProceedingJoinPoint joinPoint, LogInfo logInfo);

    /**
     * process log
     * @param logInfo log data
     */
    @Async
    void process(LogInfo logInfo);

    /**
     * process log when there is no exceptions thrown
     * @param logInfo log data
     */
    @Async
    void processAround(LogInfo logInfo);

    /**
     * process log when there exception is thrown
     * @param logInfo log data
     */
    @Async
    void processAfterThrow(LogInfo logInfo);

}