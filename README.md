# Log-helper

这是一个基于interceptor的版本，由于不好解决“拦截器获取HTTP Body参数”的问题，没有合并到master。



## 项目结构

- interceptor包

  包含各种LodMode下的拦截器和为Log注解服务的拦截器，都继承于LogInterceptor。

  各种子拦截器仅实现LodMode方面的过滤，真正的打印日志逻辑写在父拦截器中。

- processor包

  日志处理类。

- register包

  只有一个MappedInterceptorRegister，是用来注册mappedInterceptor的注册机。

  利用mappedInterceptor的path match能力，可以很方便地实现URL过滤器链。

- holder包

  用来维护过滤器链和拦截器组。

  - LogFilterChainHolder持有过滤器链

    用户可以通过`filterChainDefinitionMap.add("/**",LogMode.ALL)`来指定：任意URL都使用LogAllInterceptor这个拦截器，来完成日志处理。

  - LogInterceptorHolder持有拦截器组

    这个holder存在的意义是可以通过这个holder bean便捷地获取所有的拦截器。



## 技术注意点

### mappedInterceptor注册

spring中对于mappedInterceptor的载入在`AbstractHandlerMapping#getHandlerExecutionChain`中实现，该方法会在`adaptedInterceptors`里取出mappedInterceptor并做相应处理。

为了在这个载入方法调用前把mappedInterceptor注册进容器，需要在自动配置类（即`LogAutoConfiguration`）上添加`@AutoConfigureBefore(WebMvcAutoConfiguration.class)`



### 拦截器获取HTTP Body参数

Body参数存储在`httpServletRequest.getInputStream`中，由于流只能读取一次，一旦读取会造成控制层方法出现错误。

如果为了解决这个问题让流能够重复读取（例如可以通过包装HttpServletRequest让流能够重复读取），可能造成很大的内存消耗，尤其在文件流读取时该问题更为显著。这不是一个好的解决方式。

此问题似乎解决不了，故放弃基于interceptor做日志框架的方案。