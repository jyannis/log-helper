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
package top.jyannis.loghelper.domain;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author Jyannis
 * @version 1.0 update on 2021/5/26
 */
@Setter
@Getter
@NoArgsConstructor
public class LogInfo implements Serializable {

    /**
     * 文件名
     * method full name
     */
    private String method;

    /** url(lookupPath) */
    private String lookupPath;

    /**
     * 参数
     * params
     */
    private String params;

    /**
     * 日志类型（info,warn,error等）
     * log type (info,warn,error etc.)
     */
    private String logType;

    /**
     * 请求ip
     * request ip
     */
    private String requestIp;

    /**
     * 请求来源地址
     * request source address
     */
    private String address;

    /**
     * 请求来源浏览器
     * request source browser
     */
    private String browser;

    /**
     * 请求耗时
     * request time consuming
     */
    private Long time;

    /**
     * 异常
     * exception
     */
    private Throwable throwable;

}
