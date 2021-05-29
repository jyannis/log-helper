# Log-helper

## 项目介绍

Log-helper是一个轻量的接口级日志框架，便于节约用户自己设计日志处理器的工作成本。

<br>

### 项目源码

| Github                                | Gitee                                |
| ------------------------------------- | ------------------------------------ |
| https://github.com/jyannis/log-helper | https://gitee.com/jyannis/log-helper |

<br>

### 联系方式

|  QQ  |   123400197    |
| :--: | :------------: |
|  WX  |   jyannis123   |
| Mail | jyannis@qq.com |

<br>

<br>

## 快速开始

### 接入方式

`maven dependency`

```xml
		<dependency>
			<groupId>top.jyannis</groupId>
			<artifactId>log-helper</artifactId>
			<version>0.1.2</version>
		</dependency>
```

<br>

### 简单使用

直接在业务控制层方法上添加`@Log`注解即可。

```java
...
import top.jyannis.loghelper.annotation.Log;
...

	@Log
    @GetMapping("/")
    public Void get(String arg){
        if(StringUtils.isEmpty(arg)){
            throw new RuntimeException();
        }
    }

```



在本地访问接口，无异常时日志如下：

```shell
2021-05-21 21:40:52.224  INFO 27512 --- [nio-8080-exec-5] t.j.l.processor.DefaultLogProcessor      : call method: top.jyannis.loghelperdemo.MyController.get()
2021-05-21 21:40:52.224  INFO 27512 --- [nio-8080-exec-5] t.j.l.processor.DefaultLogProcessor      : request params: 'a message'
2021-05-21 21:40:52.224  INFO 27512 --- [nio-8080-exec-5] t.j.l.processor.DefaultLogProcessor      : request ip: 192.168.2.115
2021-05-21 21:40:52.224  INFO 27512 --- [nio-8080-exec-5] t.j.l.processor.DefaultLogProcessor      : request address:  局域网
2021-05-21 21:40:52.224  INFO 27512 --- [nio-8080-exec-5] t.j.l.processor.DefaultLogProcessor      : request browser: Firefox 88.0
2021-05-21 21:40:52.224  INFO 27512 --- [nio-8080-exec-5] t.j.l.processor.DefaultLogProcessor      : request time cost: 1 ms
```

包含以下内容：

```shell
# 访问的方法
top.jyannis.loghelperdemo.MyController.get()
# 请求参数
request params: 'a message'
# 来源ip
request ip: 192.168.2.115
# 来源地址
request address:  局域网
# 来源浏览器
request browser: Firefox 88.0
# 接口耗时
request time cost: 1 ms
```



有异常时，除了打印常规信息外，还会打印异常栈：

```shell
2021-05-21 21:46:44.008 ERROR 29152 --- [nio-8080-exec-4] t.j.l.processor.DefaultLogProcessor      : call method: top.jyannis.loghelperdemo.MyController.get()
2021-05-21 21:46:44.008 ERROR 29152 --- [nio-8080-exec-4] t.j.l.processor.DefaultLogProcessor      : request params: 
2021-05-21 21:46:44.008 ERROR 29152 --- [nio-8080-exec-4] t.j.l.processor.DefaultLogProcessor      : request ip: 192.168.2.115
2021-05-21 21:46:44.008 ERROR 29152 --- [nio-8080-exec-4] t.j.l.processor.DefaultLogProcessor      : request address:  局域网
2021-05-21 21:46:44.008 ERROR 29152 --- [nio-8080-exec-4] t.j.l.processor.DefaultLogProcessor      : request browser: Firefox 88.0
2021-05-21 21:46:44.009 ERROR 29152 --- [nio-8080-exec-4] t.j.l.processor.DefaultLogProcessor      : request time cost: 0 ms
2021-05-21 21:46:44.009 ERROR 29152 --- [nio-8080-exec-4] t.j.l.processor.DefaultLogProcessor      : java.lang.RuntimeException
	at top.jyannis.loghelperdemo.MyController.get(MyController.java:20)
	...
```

<br>

## 高级

### 定制化处理器

用户可能希望有自己的日志处理方式，例如以存库代替存档、补充其他日志信息、选择自己的打印方式等。

可以通过<font color='red'>重写`AbstractLogProcessor`并注册到Spring容器</font>来实现这一点：

```java
@Slf4j
@Service
public class DefaultLogProcessor extends AbstractLogProcessor {

    /**
     * 正常流程的日志处理方法
     * @param logInfo log data
     */
    @Override
    public void processAround(LogInfo logInfo) {
        log.info("call method: {}",logInfo.getMethod());
        log.info("request params: {}",logInfo.getParams());
        log.info("request ip: {}",logInfo.getRequestIp());
        log.info("request address: {}",logInfo.getAddress());
        log.info("request browser: {}",logInfo.getBrowser());
        log.info("request time cost: {} ms",logInfo.getTime());
    }

    /**
     * 异常流程的日志处理办法
     * @param logInfo log data
     */
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
```

<br>

