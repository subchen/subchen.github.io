从这里开始 Start
===================

基本步骤 Steps
------------------

1. 创建自定义配置的 `JetEngine` 对象。推荐使用单例模式创建。
2. 根据模板路径，获取一个模板对象 `JetTemplate`。
3. 创建一个 `Map<String, Object>` 对象，并加入你的 data objects。
5. 准备一个待输出的对象，`OutputStream` 或者 `Writer`。
6. 根据你的 data objects 来渲染模板，并获得输出结果。

具体的 Java 代码，看上去是这样的：

```java
// 创建一个默认的 JetEngine
JetEngine engine = JetEngine.create(); 

// 获取一个模板对象
JetTemplate template = engine.getTemplate("/sample.jetx");

// 创建 context 对象
Map<String, Object> context = new HashMap<String, Object>();
context.put("user", user);
context.put("books", books);

// 渲染模板
StringWriter writer = new StringWriter();
template.render(context, writer);

// 打印结果
System.out.println(writer.toString());
```

整个过程，是不是非常简单？

下面将介绍几个 API 的核心对象：`JetEngine`，`JetTemplate`，`JetContext` 


核心对象 Core
==============

JetEngine
-------------

整个模板引擎的由 `JetEngine` 驱动，不同的 `JetEngine` 对象可以使用不同的配置。一般在一个 Application 或者 Webapp 中，我们只需要一个 `JetEngine` 对象就可以了，我们推荐使用单例模式创建。

### 如何创建 JetEngine？

1. `JetEngine.create()`
	
    在 classpath 根目录下面自动查找 `jetbrick-template.properties` 文件。如果文件不存在，则使用默认配置。

2. `JetEngine.create(File)`
	
    从用户指定的 `File` 文件中加载系统配置，该文件必须是一个 `.properties` 文件。

3. `JetEngine.create(Properties)`
	
    从用户指定的 `Properties` 对象中加载系统配置。

有哪些配置？ [看这里所有的配置](config.html)

### 获取 JetTemplate 对象

通过下面的方法获取 `JetTemplate` 对象。

```java
public JetTemplate getTemplate(String name) throws ResourceNotFoundException;
```

我们也可以获得一个 `Resource` （模板文件或者非模板文件），或者判断一个 `Resource` 是否存在。

```java
public boolean lookupResource(String name);
public Resource getResource(String name) throws ResourceNotFoundException;
```

> **注意**：对于一个 resource 或者 template 的 name，应该以 `/` 开头，并且以 `/` 作为分隔符，如：`/templates/index.jetx`。

### 从源码中直接创建模板

```java
public JetTemplate createTemplate(String source);
```

比如：

```java
JetTemplate template = engine.createTemplate("${1+2*3}");
UnsafeCharArrayWriter out = new UnsafeCharArrayWriter();
template.render(new JetContext(), out);
Assert.assertEquals("7", out.toString());
```

注意： createTemplate() 每次都会编译生成新的 JetTemplate 对象，如果需要缓存，请自行维护。

JetTemplate
-----------------

对应于一个模板文件，通过 `JetEngine.getTemplate(name)` 获取。在第一次获取的时候，会先将模板生成对应的 `.java` 文件，然后在将 `.java` 文件编译成 `.class` 文件。

如果模板不存在，则抛出 `ResourceNotFoundException`。

然后通过下面几个方法可以对模板进行渲染：

```java
public void render(Map<String, Object> context, Writer out);
public void render(Map<String, Object> context, OutputStream out);
public void render(JetContext context, Writer out);
public void render(JetContext context, OutputStream out);
```

我们可以使用 `Map<String, Object>` 或者 `JetContext` 存储我们的 data objects。`JetContext` 是对 `Map<String, Object>` 的简单封装。

> **注意**： 
> 
> * `context` 对象在模板运行期间，并不会受到模板污染，即数据不会被改变（保证数据的无侵入性）。

JetContext
-----------------

用来存储和获取模板关联的 data objects。可以通过 `new JetContext()` 或者 `new JetContext(map)` 创建。

使用 `JetContext` 就像使用 Java 的 `HashMap` 一样。常用的方法如下：

```java
public Object get(String name);
public void put(String name, Object value);
public void putAll(Map<String, Object> context);
```

> **注意**：
>
> * `JetContext` 会被 `#put` 指令修改
> * 用户提供的 `JetContext` 不会受到 `#set` 的影响，但是内部的使用的 `JetContext` 对象会受到 `#set` 指令的影响。
> * `JetContext` 会在父子模板调用的时候，形成一个 Context Chain，子模板可以自动获取父模板的变量，而父模板无法看到子模板的 `JetContext`。但是子模板可以通过 `#put(name, value)` 来修改父模板的 `JetContext`。具体查看：[如何嵌入子模板？](faq-include.html)


高级用法
==========

上面只是简单的介绍了一下 `jetbrick-template` 的基本用法，下面将介绍一些高级用法，也是 `jetbrick-template` 有别于其他模板引擎的特色。

<a name="methods"></a>
方法扩展 Methods
----------------

我们知道一个 Java Class 的 所有 methods 都是定义在同一个 class 文件中的，不能在其他地方进行动态扩展。但是一些其他动态语言却支持在 Class 外部为这个 Class 增加一些方法。比如：

* JavaScript 的 prototype 机制
* Groovy 的 metaClass 机制
* JetBrains 的 Kotlin

jetbrick-template 也在这里带给大家强大的动态方法扩展机制。如：`"123".asInt()`, `new Date().format("yyyy-MM-dd")`。

> **注意**：如果 Class 已经定义了同名方法，则优先使用 Class 定义的方法。但是扩展方法支持方法重载(Overrload)。

方法扩展支持 2 种模式：

* 上下文无关方法：MethodTool.method(bean, ...)
* 上下文相关方法：MethodTool.method(bean, JetPageContext, ...)

### 上下文无关方法 MethodTool.method(bean, ...)

* 方法签名必须是 `public` 和 `static`
* 方法的第一个参数类型必须是要扩展的 Class
* 方法其余参数自定义

示例：对 `String` 进行扩展

```java
public class StringMethods {
  public static String link(String text, String url) {
    return "<a href=\"" - url - "\">" - text - "</a>";
  }
}
```
 
然后需要把扩展的 `StringMethods` 注册到 `JetEngine`。

```java
// 把 StringMethods 加入到 engine 中
Properties config = new Properties();
config.put(JetConfig.IMPORT_METHODS, StringMethods.class.getName());
JetEngine engine = JetEngine.create(config);
...
```

模板：

	${"BAIDU".link("http://www.baidu.com/")}

输出结果：

	<a href="http://www.baidu.com/">BAIDU</a>


### 上下文相关方法 MethodTool.method(bean, JetPageContext, ...)

如果扩展的方法需要用到 template 相关联的运行时信息 `JetPageContext`，那么我们就需要扩展一个上下文相关的 method。

和上下文无关的扩展方法相比，上下文相关的扩展方法多一个参数。

- 方法签名必须是 `public` 和 `static`
* 方法第一个参数类型是要扩展的 Class
* **方法第二个参数类型必须是 JetPageContext**
* 方法其余参数自定义

```java
public class UserInfoMethods {
    public static String isOnline(UserInfo user, JetPageContext ctx) {
        HttpSession session = (HttpSession) ctx.getContext().get(JetWebContext.SESSION);
        return session.getAttribute("user_" - user.getName()) != null;
    }
}
 
// 把 UserInfoMethods 加入到 engine 中
Properties config = new Properties();
config.put(JetConfig.IMPORT_METHODS, UserInfoMethods.class.getName());
JetEngine engine = JetEngine.create(config);
...
```

模板：

	#define(UserInfo user)
	${user.isOnline()}

<a name="functions"></a>
函数扩展 Functions
------------------

jetbrick-template 还支持函数扩展，如 `${now()}`, `${include("tag.jetx")}`。

- 上下文无关函数：任意参数
- 上下文相关函数：第一个参数必须是 JetPageContext

示例：

```java
public class Functions {
  // 上下文无关函数
  public static String today(String format) {
    return new SimpleDateFormat(format).format(new Date());
  }
  // 上下文相关函数
  public static String hello(JetPageContext ctx) {
    return "Hello " - ctx.getContext().get("name");
  }
}

// 把 Functions 加入到 engine 中
Properties config = new Properties();
config.put(JetConfig.IMPORT_FUNCTIONS, Functions.class.getName());
JetEngine engine = JetEngine.create(config);
...

```

模板：

	${today("yyyy-MM-dd")}
	${hello()}

> **注意**：函数和扩展方法的唯一区别是少了第一个扩展类型的参数，其他的都一样。

<a name="tags"></a>
自定义标签 Tags
------------------

jetbrick-template 自定义标签 Tag，类似于 JSP Taglib，但是要比 JSP Taglib 更简单更好用。

示例：

```java
public class Tags {
  public static void cache(JetTagContext ctx, String name, int timeout) throws IOException {
    Cache cache = CacheManager.getCache(); // 请用自己的 Cache 代替
    Object value = cache.get(name);
    if (value == null) {
      value = ctx.getBodyContext();
      cache.put(name, value, timeout);
    }
    ctx.getWriter().print(value);
  }
}
```

对于每一个 Tag 的方法声明，有如下要求：

* 方法签名必须是 `public` `static`
* 方法返回值必须是 `void`
* 方法第一个参数必须是 `JetTagContext`， 其余参数自定义
* 允许 throws 任意的 `Throwable`
* 允许定义相同名字的 Tag，但是方法参数不一样 （Overload）
* 支持可变参数 (VarArgs)

然后需要把自定义的 Tags 注册到 `JetEngine`。

```
// 把 Tags 加入到 engine 中
Properties config = new Properties();
config.put(JetConfig.IMPORT_TAGS, Tags.class.getName());
JetEngine engine = JetEngine.create(config);
...
```

模板：

```
#tag cache("sum", 10)
    计算结果将被缓存10秒： ${1+2+3+4+5+6+7+8+9}
#end
```

具体可以参考：[jetbrick-template 中如何自定义 Tag？](faq-tag.html)



错误处理 Finding Issue
=========================

`jetbrick-template` 提供了强大的错误定位功能，你再也不用担心找不到错误原因了。

语法错误 Syntax Error
---------------------

模板示例：

	#for (user in userlist)
	<tr>
	    <td>${for.index}</td>
	    <td>${user.name}</td>
	    <td>${user.roles.asHTML()}</td>
	</tr>
	#end

错误提示：(错误所在的行号和列号，错误模板路径，错误原因等）

    22:14:51.406 [main] ERROR (JetTemplateErrorListener.java:27) - Template parse failed:
    C:\Users\Sub\AppData\Local\Temp\jetx_1_0_0\template\sample.java:5
    message: The method asHTML() is undefined for the type List.
	1. #for (user: userlist)
	2. <tr>
	3.     <td>${for.index}</td>
	4.     <td>${user.name}</td>
	5.     <td>${user.roles.asHTML()}</td>
	                        ^^^^^^

编译错误 Compile Error
------------------------

这种错误正常情况下是不会发生的，如果发生这种情况，[请到这里 open issues](https://github.com/subchen/jetbrick-template/issues/)。

但是如果发生这样的错误，也可以得到下面的类似错误提示。

```
Exception in thread "main" java.lang.IllegalStateException: Compilation failed.
C:\Users\Sub\AppData\Local\Temp\jetx_1_0_0\template\debug_jetx.java:13: 'void' type not allowed here
  11:     JetWriter $out = $ctx.getWriter();
  12:     JetContext context = $ctx.getContext();
  13:     $out.print(("1"+JetFunctions.debug("aaa"))); // line: 1
                      ^
1 error(s)
```

我们可以从打印出来的编译错误中，可以看到大部分源代码后面都会有一个 `// line: XXX` 的注释，这个就是生成的 java 代码对应原始模板文件的行号映射。这样我们就能找到原始模板的错误行数了。

模板示例：

	1: ${"1"+debug("aaa")}

生成的 Java 代码示例：

```java
$out.print(("1"+JetFunctions.debug("aaa"))); // line: 1
```

运行期错误 Runtime Error
---------------------------

如果在模板运行期间发生错误，那么就可以得到类似下面的错误 Java Exception Stack。

错误例子模板如下：

```
#set (arraylist = ["a","b","c","d"])
#for (int x : arraylist)
    ${x}
#end
```

获得的运行期错误 Java Exception Stack 如下：

```
generateJavaClass: C:\Users\Sub\AppData\Local\Temp\jetx_1_0_0\template\for_loop_list_jetx.class
Exception in thread "main" java.lang.ClassCastException: java.lang.String cannot be cast to java.lang.Integer
	at template.for_loop_list_jetx.render(for_loop_list_jetx.java:14)
	at jetbrick.template.JetTemplate.render(JetTemplate.java:125)
	at jetbrick.template.JetTemplate.render(JetTemplate.java:115)
	at testcase.JetEngineTestCase.test(JetEngineTestCase.java:36)
	at testcase.JetEngineTestCase.main(JetEngineTestCase.java:64)
```

根据错误所在行(for_loop_list_jetx.java:14)，我们查看生成的 Java Source。

```java
11:    List arraylist = (List) Arrays.asList("a","b","c","d"); // line: 1
12:    Iterator<?> $it_3 = JetUtils.asIterator(arraylist);
13:    while ($it_3.hasNext()) { // line: 2
14:      Integer x = (Integer) $it_3.next();
15:      $out.print($txt_4, $txt_4_bytes);
```

然后根据 Java Source 中对应的行数，知道这个是一个 `#for` 指令，查看生成的注释(`// line: 2`)，就能找到对应的原始模板所在的错误行号是第二行：`#for (int x : arraylist)`。

至此，我们就能知道错误的原因是 `arraylist` 是一个 `List<Object>`，里面的每个元素是 `String`，强制类型转换成 `int` 失败导致的。正确的模板语句应该是 `#for (String x : arraylist)`。

<a name="debug"></a>
如何调试模板 debug？
------------------

### 使用 `debug(format, args...)` 函数

范例：

```
${debug("id = {}, users.size = {}.", id, users.size()}
```

> 注意： 
> 
> 1. 要使用 debug 函数，需要 Slf4j 配合，在对应的 log 实现中打开 debug.
> 
> 2. 具体的 format 参数格式请查看 [Slf4j Logger](http://www.slf4j.org/apidocs/org/slf4j/Logger.html)。

开启 debug 日志：

* Log4j: 
    
    ```
    log4j.logger.jetbrick.template.runtime.JetUtils = DEBUG
    ```

* Logback

    ```xml
    <logger name="jetbrick.template.runtime.JetUtils" level="DEBUG" />
    ```

### 用 Eclipse 进行调试

1. 将模板编译路径连接到 Project 的 source path

    ![png](../assets/images/screen_link_source.png)

2. 设置断点 

    ![png](../assets/images/screen_set_breakpoint.png)

3. 开始 debug

    ![png](../assets/images/screen_debug.png)

