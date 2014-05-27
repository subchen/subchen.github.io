简述
===============

jetbrick-template 除了作为普通的模板引擎嵌入在 Application 中外，大部分情况下还会和各种 WebMVC 框架整合作为前端的 View，来代替过时的 JSP 或者 Velocity。

目前已近集成了几种流行的 Web 框架：

* [HttpServlet](#HttpServlet)
* [Filter](#Filter)
* [Struts 2.x](#Struts)
* [Spring MVC](#SpringMVC)
* [Jodd 3.5.1+](#Jodd)
* [JFinal](#JFinal)
* [Nutz](#Nutz)

[点击这里下载各种集成方式的演示 demo](download.html#samples)


Web 中的默认隐含对象
=========================

当 jetbrick-template 被用作 Web 应用中时候，会自动引入下面的对象，这些对象在所有的模板中全局可访问。

|    隐含对象      |      类 型           |                说 明                       |
-------------------|----------------------|---------------------------------------------
| context          | JetConext            |                                            |
| servletContext   | ServletContext       |                                            |
| session          | HttpSession          |                                            |
| request          | HttpServletRequest   |                                            |
| response         | HttpServletResponse  |                                            |
| applicationScope | Map<String,Object>   | 快捷访问 servletContext.getAttribute(name) |
| sessionScope     | Map<String,Object>   | 快捷访问 session.getAttribute(name)        |
| requestScope     | Map<String,Object>   | 快捷访问 request.getAttribute(name)        |
| parameter        | Map<String,String>   | 快捷访问 request.getParameter(name)        |
| parameterValues  | Map<String,String[]> | 快捷访问 request.getParameterValues(name)  |
| ctxpath 或者 CONTEXT_PATH  | String               | 快捷访问 request.getContextPath()          |
| webroot 或者 WEBROOT_PATH  | String               | 返回完整的webapp路径: http://127.0.0.1:8080/myapp |
| BASE_PATH        | String               | 专门用于 &lt;base href="${BASE_PATH}"> |



**下面的例子演示了如何使用这些隐含变量：**

模板如下：

```
request.requestURI == ${request.requestURI}
request.getParameter("name") == ${parameter.name}
request.getAttribute("items") == ${requestScope.items}
session.getAttribute("user") == ${sessionScope.user}
```

特别需要说明的一点是：模板中使用或者声明的全局变量不光会从 `JetConext context` 中获取，在 Web 应用中，还会从 `requestScope`，`sessionScope`，`applicationScope` 中查找对应的内容。

默认的查顺序如下：

1. context
2. requestScope
3. sessionScope
4. applicationScope

也就是说，如果存在 `request.getAttribute("user")` 的情况下 `${user}` 等价于 `${requestScope.user}`。

<a name="JetEngine"></a>
JetEngine 自动加载方式
========================

需要在 web.xml 中进行配置，下面两个配置项都是可选项。

```xml
<context-param>
  <param-name>jetbrick-template-config-location</param-name>
  <param-value>/WEB-INF/jetbrick-template.properties</param-value>
</context-param>

<listener>
  <listener-class>jetbrick.template.web.JetWebEngineLoader</listener-class>
</listener>
```

如果没有配置 `jetbrick-template-config-location` 参数，那么配置文件默认从 classpath 根目录下面查找 jetbrick-template.properties。

如果没有配置 JetWebEngineLoader 启动监听器，那么 JetEngine 也会在模板第一次请求的时候自动初始化。配置成 Listener，可以在 Webapp 启动的时候马上进行初始化。

**注意：**

1. 在 Web 集成模式中，采用以下的默认值：

    ```
    template.loader = jetbrick.template.web.WebResourceLoader
    template.path = /
    ```

2. 对于 `WebResourceLoader` 的来说，`template.path` 的路径相对于 webapp 的根目录。

    ```
    / == servletContext.getRealPath("/")
    /WEB-INF/jetx == servletContext.getRealPath("/WEB-INF/jetx")
    ```
 
3. jetbrick-template 内置和其他 Web 框架的集成方式都可以用这两个配置想进行全局初始化。

4. 在 web 集成模式中，`JetEngine` 是单例的，可以通过 `JetWebEngineLoader.getJetEngine()` 获取。


各种集成方式介绍
====================

<a name="HttpServlet"></a>
直接使用 HttpServlet
----------------------------

jetbrick-template 可以直接作为 HttpServlet 使用。需要在 web.xml 中作如下配置。

```xml
<servlet>
  <servlet-name>jetbrick-template</servlet-name>
  <servlet-class>jetbrick.template.web.servlet.JetTemplateServlet</servlet-class>
  <load-on-startup>1</load-on-startup>
</servlet>
<servlet-mapping>
  <servlet-name>jetbrick-template</servlet-name>
  <url-pattern>*.jetx</url-pattern>
</servlet-mapping>
```

最简单，也是最直接的方式。打开浏览器访问 http://127.0.0.1:8080/index.jetx 看看效果吧。

具体例子代码参考： https://github.com/subchen/jetbrick-template-webmvc-samples/

<a name="Filter"></a>
直接使用 Filter 模式
----------------------------

jetbrick-template 可以直接作为 Filter 使用。需要在 web.xml 中作如下配置。

```xml
<filter>
  <filter-name>jetbrick-template</filter-name>
  <filter-class>jetbrick.template.web.servlet.JetTemplateFilter</filter-class>
</filter>
<filter-mapping>
  <filter-name>jetbrick-template</filter-name>
  <url-pattern>*.jetx</url-pattern>
</filter-mapping>
```

<a name="Struts"></a>
Struts 2.x
----------------------------

jetbrick-template 可以和 Struts 2.x 进行集成。

首先需要对 Struts 进行如下配置（`struts.xml`），这个配置是告诉 Struts 使用 `jetbrick.template.web.struts.JetTemplateResult` 这个类来处理采用 jetbrick-template 格式的模板：

```xml
<result-types>
  <result-type name="jetx" class="jetbrick.template.web.struts.JetTemplateResult" />
</result-types>
```

接下来配置你的 action 如下：

```xml
<action name="index" class="jetbrick.template.samples.struts.action.IndexAction">
  <result type="jetx">/index.jetx</result>
</action>
```

打开浏览器访问 http://127.0.0.1:8080/index.do 看看效果吧。

具体例子代码参考： https://github.com/subchen/jetbrick-template-webmvc-samples/

<a name="SpringMVC"></a>
Spring MVC
----------------------------

jetbrick-template 可以和 Spring MVC 进行集成。

配置方式如下：

```xml
<bean id="viewResolver" class="jetbrick.template.web.springmvc.JetTemplateViewResolver">
  <property name="suffix" value=".jetx" />
  <property name="contentType" value="text/html; charset=utf-8" />
  <property name="order" value="9" />
</bean>
```

具体例子代码参考： https://github.com/subchen/jetbrick-template-webmvc-samples/

<a name="JFinal"></a>
JFinal
----------------------------

jetbrick-template 可以和 JFinal 进行集成。

1. 修改 JFinal 主配置文件

    ```java
    public class JetxConfig extends JFinalConfig {
        @Override
        public void configConstant(Constants me) {
            me.setMainRenderFactory(new JetTemplateRenderFactory());
            ...
        }
        ...
    }
    ```

2. 新建一个 Controller

    ```java
    public class UsersController extends Controller {
        public void index() {
            setAttr("userlist", DaoUtils.getUserList());
            render("/users.jetx");
        }
    }
    ```

可以了，就这么简单!

具体例子代码参考： https://github.com/subchen/jetbrick-template-webmvc-samples/


<a name="Jodd"></a>
Jodd 3.5.1+
----------------------------

注意，由于 Jodd 3.5 更改了 Result 的集成接口，所以 `jetbrick-template` 从 `1.2.5` 版本开始，只支持 `Jodd 3.5.1+`，如果需要 `Jodd 3.5` 之前的版本，请使用 `jetbrick-template 1.2.4`。 

1. 首先需要配置 Jodd 的配置文件：madvoc.props

        [jetbrick.template.web.jodd.JetTemplateResult]
        contentType=text/html; charset=UTF-8
        
        [automagicMadvocConfigurator]
        includedEntries=jodd.*,jetbrick.template.web.jodd.*,yourapp.jodd.action.*

2. Action 例子

        @MadvocAction
        public class UsersAction {
            @Out
            Collection<UserInfo> userlist;
        
            @Action(extension = Action.NONE)
            public Object view() {
                userlist = DaoUtils.getUserList();
                return "jetx:/users.jetx";
            }
        }

3. jetx 例子

    ```html
    <table border="1" width="600">
      <tr>
        <td>ID</td>
        <td>姓名</td>
        <td>邮箱</td>
        <td>书籍</td>
      </tr>
      #for(UserInfo user: userlist)
      <tr>
        <td>${user.id}</td>
        <td>${user.name}</td>
        <td>${user.email}</td>
        <td><a href="books?author=${user.id}">书籍列表</a></td>
      </tr>
      #end
    </table>
    ```

具体例子代码参考： https://github.com/subchen/jetbrick-template-webmvc-samples/


<a name="Nutz"></a>
Nutz
----------------------------

感谢 wendal (wendal1985@gmail.com) 提供相关代码。

View： `jetbrick.template.web.nutz.JetTemplateView`
ViewMaker：`jetbrick.template.web.nutz.JetTemplateViewMaker`


1. 将视图工厂整合进应用中：

    在主模块上，加：`@Views({JetTemplateViewMaker.class})` 注解

    ```java
    @Views({JetTemplateViewMaker.class})
    @...
    public class MainModule {
    }
    ```

2. 使用 jetx 视图：

    ```java
    @At
    @Ok(".jetx:/WEB-INF/html/user_info.jetx")
    public String list(@Param("name") String name, HttpServletRequest request){
      if (name != null) return name;
      return "测试";
    }
    ```

3. 模板中使用:

    ```
    #define(String obj)
    ${obj}
    ```

4. 获得输出：

    ```
    测试
    ```

具体例子代码参考： https://github.com/subchen/jetbrick-template-webmvc-samples/
