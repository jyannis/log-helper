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
package top.jyannis.loghelper.annotation;


import top.jyannis.loghelper.domain.LogMode;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Jyannis
 * @version 1.0 update on 2021/5/20
 */
@Deprecated
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Log {

    /**
     * 日志记录模式
     * {@link LogMode}
     * 默认有效值如下：
     * {@code ALL} 所有操作都会被记录
     * {@code INFO} 只有不发生异常的操作会被记录
     * {@code ERROR} 只有发生异常时记录
     * {@code NONE} 什么都不记录
     *
     * logging mode
     * Default valid values are listed below
     * {@code ALL} all operations will be logged
     * {@code INFO} operations will be logged only when there is no exceptions thrown
     * {@code ERROR} operations will be logged only when exception is thrown
     * {@code NONE} log nothing
     */
    String mode() default LogMode.ALL;

}
