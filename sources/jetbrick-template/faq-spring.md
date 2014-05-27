jetbrick-template 在 Spring 中的集成方法
====================================================

你可以按照以下几种方式之一来置 `JetEngine` 在 `Spring` 上下文的实例 (单例模式)。 

> **注意**：当同时指定 `configFile` 和 `configProperties` 时，`configProperties` 中的配置会覆盖 `configFile` 中的配置。


```xml
<!-- 使用 classpath 下面的默认配置文件 -->
<bean id="jetEngine" class="jetbrick.template.JetEngineFactoryBean" />

<!-- 指定配置文件 -->
<bean id="jetEngine" class="jetbrick.template.JetEngineFactoryBean">
  <property name="configFile" value="classpath:META-INF/jetbrick-template.properties" />
</bean>

<!-- 指定配置文件 -->
<bean id="jetEngine" class="jetbrick.template.JetEngineFactoryBean">
  <property name="configFile" value="file:/path/to/jetbrick-template.properties" />
</bean>

<!-- 直接配置属性 -->
<bean id="jetEngine" class="jetbrick.template.JetEngineFactoryBean">
  <property name="configProperties">
    <props>
      <prop key="compile.debug">true</prop>
      ...
    </props>
 </property>
</bean>
```

感谢 应卓 (yingzhor@gmail.com) 提供相关代码。

