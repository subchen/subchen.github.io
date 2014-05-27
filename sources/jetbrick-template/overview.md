概述 Overview
==================

jetbrick-template 是一个新一代 Java 模板引擎，具有高性能和高扩展性。 适合于动态 HTML 页面输出或者代码生成，可替代 JSP 页面或者 Velocity 等模板。 指令和 Velocity 相似，表达式和 Java 保持一致，易学易用。

* 支持类似于 Velocity 的多种指令
* 支持静态编译
* 支持编译缓存
* 支持热加载
* 支持类型推导
* 支持泛型
* 支持可变参数方法调用
* 支持方法重载
* 支持类似于 Groovy 的方法扩展
* 支持函数扩展
* 支持自定义标签 #tag
* 支持宏定义 #macro
* 支持布局 Layout

简单易用的指令
=============================

jetbrick-template 指令集和老牌的模板引擎 Velocity 非常相似，易学易用。

```html
#define(List<UserInfo> userlist)
<table>
  <tr>
    <td>序号</td>
    <td>姓名</td>
    <td>邮箱</td>
  </tr>
  #for (UserInfo user : userlist)
  <tr>
    <td>${for.index}</td>
    <td>${user.name}</td>
    <td>${user.email}</td>
  </tr>
  #end
</table>
```

详细指令语法，请参考：[语法指南](syntax.html)。或者[和 Velocity 的比较](syntax.html#velocity)。

卓越性能 Performance
=============================

jetbrick-template 将模板编译成 Java ByteCode 运行，并采用强类型推导，无需反射和减少类型转换。渲染速度等价于 Java 硬编码。比 Velocity 等模板快一个数量级。 比 JSP 也快，因为 JSP 只有 Scriptlet 是编译的，Tag 和 EL 都是解释执行的。 而 jetbrick-template 是全编译的。

![performance](../assets/images/perfermance.png)

在 Stream 模式中(Webapp 采用 OutputStream 将文本输出到浏览器)，由于 Java 硬编码输出字符串需要进行一次编码的转换。 而 jetbrick-template 却在第一次运行期间就缓存了编码转换结果，使得 jetbrick-template 的性能甚至优于 Java 硬编码。

具体测试用例，请参考：[Template Engine Benchmark Test](https://github.com/subchen/ebm) (platform: Window 7 x64, Intel i5, 16GB RAM, JDK 1.6.0_41 x64)

易于集成 Integrate
=============================

可以和市面上常见的 Web MVC framework 进行集成。

* [HttpServlet](integrate.html#HttpServlet)
* [Filter](integrate.html#Filter)
* [Struts 2.x](integrate.html#Struts)
* [Spring MVC](integrate.html#SpringMVC)
* [JFinal](integrate.html#JFinal)
* [Nutz](integrate.html#Nutz)
* [Jodd](integrate.html#Jodd)

具体集成方法，请参考： [Web 框架集成](integrate.html)

也可以和 Spring Ioc 进行集成，请参考：[如何在 Spring 中使用 JetEngine](faq-spring.html)

友好的错误提示
=============================

具有详细的模板解析和编译错误提示，出错提示可以定位到原始模板所在的行号。

```
22:14:51.271 [main] INFO  (JetTemplate.java:68) - Loading template source file: D:\workspace\github\jetbrick-schema-app\bin\config\report\schema.html.jetx
22:14:51.406 [main] ERROR (JetTemplateErrorListener.java:27) - Template parse failed:
D:\workspace\github\jetbrick-schema-app\bin\config\report\schema.html.jetx:37
message: The method getColumnNam() or isColumnNam() is undefined for the type jetbrick.schema.app.model.TableColumn.
33:         </tr>
34:     #for(TableColumn c: t.columns)
35:         <tr style="background-color:white;">
36:             <td>${c.displayName}</td>
37:             <td>${c.columnNam}</td>
                        ^^^^^^^^^
 
Exception in thread "main" jetbrick.commons.exception.SystemException: java.lang.RuntimeException: The method getColumnNam() or isColumnNam() is undefined for the type jetbrick.schema.app.model.TableColumn.
  at jetbrick.commons.exception.SystemException.unchecked(SystemException.java:23)
  at jetbrick.commons.exception.SystemException.unchecked(SystemException.java:12)
  at jetbrick.schema.app.TemplateEngine.apply(TemplateEngine.java:44)
  at jetbrick.schema.app.Task.writeFile(Task.java:83)
  at jetbrick.schema.app.task.SqlReportTask.execute(SqlReportTask.java:19)
  at jetbrick.schema.app.SchemaGenerateApp.taskgen(SchemaGenerateApp.java:56)
  at jetbrick.schema.app.SchemaGenerateApp.main(SchemaGenerateApp.java:74)
Caused by: java.lang.RuntimeException: The method getColumnNam() or isColumnNam() is undefined for the type jetbrick.schema.app.model.TableColumn.
  at jetbrick.template.parser.JetTemplateCodeVisitor.reportError(JetTemplateCodeVisitor.java:1388)
  at jetbrick.template.parser.JetTemplateCodeVisitor.visitExpr_field_access(JetTemplateCodeVisitor.java:665)
  at jetbrick.template.parser.JetTemplateCodeVisitor.visitExpr_field_access(JetTemplateCodeVisitor.java:1)
...
```

* 出错模板：D:\workspace\github\jetbrick-schema-app\bin\config\report\schema.html.jetx
* 出错行号：37
* 错误原因：The method getColumnNam() or isColumnNam() is undefined for the type jetbrick.schema.app.model.TableColumn.

点击这里查看：[jetbrick-template 常见错误分析](faq-error.html)
