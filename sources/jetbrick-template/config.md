全局配置选项
===================

|         名称                         |           说明             |   默认值     |
---------------------------------------|----------------------------|----------------
| [import.packages][1]                 | 默认导入的 java 包         |              |
| [import.classes][2]                  | 默认导入的 java 类         |              |
| [import.variables][3]                | 默认定义的 java 变量类型    |              |
| [global.variables][71]               | 全局变量工厂类             |              |
| [import.methods][4]                  | 默认导入的扩展方法         |              |
| [import.functions][5]                | 默认导入的扩展函数         |              |
| [import.tags][6]                     | 默认导入的自定义标签 tags  |              |
| [import.autoscan][7]                 | 是否自动扫描用户自定义扩展 Class | false  |
| [import.autoscan.packages][8]        | 在指定的包中进行自动扫描   |              |
| [input.encoding][11]                 | 模板源文件的编码格式       | utf-8        |
| [output.encoding][12]                | 模板输出编码格式           | utf-8        |
| [syntax.safecall][61]                | 是否默认启用全局的安全调用  | false        |
| [template.loader][21]                | 模板资源载入Class          | jetbrick.template.resource.loader.FileSystemResourceLoader |
| [template.path][22]                  | 模板资源的根目录           | 当前目录     |
| [template.suffix][23]                | 默认模板文件扩展名         | .jetx        |
| [template.reloadable][24]            | 是否允许热加载             | false        |
| [compile.tool][35]                   | 编译器                    | jetbrick.template.compiler.JdtCompiler |
| [compile.strategy][31]               | 编译策略                   | always       |
| [compile.debug][33]                  | 是否允许输出 debug 信息    | false        |
| [compile.path][34]                   | 默认编译输出路径           | 系统TEMP目录下面的 jetx 目录 |
| [security.manager][41]               | 安全管理器实现类           |              |
| [security.manager.file][42]          | 安全管理器黑白名单文件     |              |
| [security.manager.namelist][43]      | 安全管理器黑白名单列表     |              |
| [trim.directive.line][51]            | 是否要删除指令行两边的空白 | true         |
| [trim.directive.comments][52]        | 是否支持指令两边增加注释对 | false        |
| [trim.directive.comments.prefix][53] | 指令注释的开始部分         | &lt;!--      |
| [trim.directive.comments.suffix][54] | 指令注释的结束部分         | --&gt;       |


[1]: #import.packages
[2]: #import.classes
[3]: #import.variables
[4]: #import.methods
[5]: #import.functions
[6]: #import.tags
[7]: #import.autoscan
[8]: #import.autoscan.packages
[11]: #input.encoding
[12]: #output.encoding
[21]: #template.loader
[22]: #template.path
[23]: #template.suffix
[24]: #template.reloadable
[31]: #compile.strategy
[33]: #compile.debug
[34]: #compile.path
[35]: #compile.tool
[41]: #security.manager
[42]: #security.manager.file
[43]: #security.manager.namelist
[51]: #trim.directive.line
[52]: #trim.directive.comments
[53]: #trim.directive.comments.prefix
[54]: #trim.directive.comments.suffix
[61]: #syntax.safecall
[71]: #global.variables

> **注意**：
>
> 1. 所有配置选项都必须在 `JetEngine` 初始化的时候指定，不允许动态修改。
> 2. 所有配置选项都支持变量啦，具体参考 [`template.path`][12] 或者 [`compile.path`][34] 中的例子。

全局定义(包/类/变量)
-----------------------

在模板中，如果要用到一些其他的 Class, 那么可以先 import 进来，这样就可以在模板中使用短名字，比如 `Date` 而不是 `java.util.Date`。

<a name="import.packages"></a>
### **import.packages**

用来配置包名，会自动导入包下面的所有类。允许配置多个包名，用逗号分隔。

支持三种方式，如下：
1. `jetbrick.schema.app.model`
2. `jetbrick.schema.app.methods.*`
3. `jetbrick.schema.**`

其中 `1` 和 `2` 是一样的，会自动导入包下面的所有 Class 文件，但是不包含子包。
而方式 `3` 会自动将子包下面的 Class 也一起导入进来。


示例如下：

```
import.packages = jetbrick.schema.app.model, jetbrick.schema.app.methods.*, jetbrick.schema.**
```

> **注意**：`jetbrick-template` 会自动引入 `java.lang.*` 和 `java.util.*`。


<a name="import.classes"></a>
### **import.classes**

用来配置单个类名，优先级比 `import.packages` 高。允许配置多个类名，用逗号分隔。

示例如下：

```
import.classes = java.io.File, java.util.List
```


<a name="import.variables"></a>
### import.variables

在一个 webapp 中，我们希望每个模板都自动引入一些变量，比如 `HttpServletRequest request`，那么我们就可以在这里定义。

允许配置多个变量定义，用逗号分隔。示例如下：

```
import.variables = HttpServletRequest request
import.variables = jetbrick.orm.Pagelist pagelist, List<Entity> entites
```

变量的类型可以使用泛型定义，并且会自动在 `import.packages` 和 `import.classes` 里面查找 Class。

> **注意**：全局定义的变量如果在模板中被重新定义成其他类型(`#define`, `#set`)，则以模板定义优先。


<a name="global.variables"></a>
### global.variables

该配置主要用来设置用户自定义的全局变量。如下：

```
global.variables = webapp.GlobalVariables
```

然后用户的实现 `webapp.GlobalVariables` 如下：

```java
public class GlobalVariables implements JetGlobalVariables  {
    @Override
    public Object get(JetContext context, String name) {
        if ("copyright".equals(name)) {
            return "copyright@2000-2010";
        } else if ("today".equals(name)) {
            return new Date();           
        } else if ("contextPath".equals(name)) {
            HttpServletRequest request = (HttpServletRequest) context.get(JetWebContext.REQUEST);
            return request.getContextPath();
        }
        return null;
    }
}
```

注意： 用户提供的全局变量，还需要在 `import.variables` 中声明变量的类型。

```
import.variables = String copyright, Date today, String contextPath
```

然后就可以在模板中使用了

```
${copyright}
${today.format("yyyy-MM-dd")}
${contextPath}
```


扩展方法/函数/标签
-----------------------

<a name="import.methods"></a>
### import.methods

我们知道一个 Java Class 的 所有 methods 都是定义在同一个 class 文件中的，不能在其他地方进行动态扩展。但是一些其他动态语言却支持在 Class 外部为这个 Class 增加一些方法。比如：

* JavaScript 的 prototype 机制
* Groovy 的 metaClass 机制
* Kotlin

jetbrick-template 也在这里带给大家强大的动态方法扩展机制。
具体参考： [jetbrick-template 动态方法扩展](userguide.html#methods)

这里就是把实现了动态扩展的 Method Class 注册到 JetEngine 中。允许配置多个 Class 定义，用逗号分隔。示例如下：

	import.methods = StringMethods, app.project.methods.UserAuthMethods

定义的类名会自动在 `import.packages` 里面查找 Class。

`jetbrick-template` 默认会注册 `jetbrick.template.runtime.JetMethods`，
具体参考：[默认的方法扩展 Methods](syntax.html#default_methods)。

<a name="import.functions"></a>
### import.functions

和 `import.methods` 类似，我们还支持在模板中使用函数。

允许配置多个 Function Class 定义，用逗号分隔。示例如下：

	import.functions = app.project.methods.UserAuthFunctions

`jetbrick-template` 默认会注册 `jetbrick.template.runtime.JetFunctions`，
具体参考：[默认的函数扩展 Functions](syntax.html#default_functions)。


<a name="import.tags"></a>
### import.tags

我们支持在模板中自定义标签 #tag。

允许配置多个 Tag Class 定义，用逗号分隔。示例如下：

	import.tags = app.project.tags.UserTags

`jetbrick-template` 默认会注册 `jetbrick.template.runtime.JetTags`，
具体参考：[默认的自定义标签 Tags](syntax.html#default_tags)。


<a name="import.autoscan"></a>
### import.autoscan

是否自动扫描用户自定义的扩展 Class，扫描的内容是： **扩展方法，扩展函数，自定义标签**

默认 `false`，不启用。

<a name="import.autoscan.packages"></a>
### import.autoscan.packages

在指定的包下面进行自动扫描，如果为空，那么扫描整个 classpath。支持定义多个包。

```
import.autoscan = true
import.autoscan.packages = app.methods, app.functions, app.tags
```

> **注意**：
> 
> 1. 扫描整个 classpath 需要花费一定的时间（大约每秒10000个类），建议配置 `import.autoscan.packages` 以加快速度。
> 2. 由于不会对扫描的 class 加载到 jvm 中，所以不会产生 OOM。

更多详细内容请参考： [如何让自动扫描发现用户自定义的扩展方法/函数/标签 Class](faq-autoscan.html)


模板路径和编码格式
------------------------

<a name="input.encoding"></a>
### input.encoding

模板源文件的编码格式，默认为 `utf-8`。

<a name="output.encoding"></a>
### output.encoding

模板输出内容的编码格式，默认为 `utf-8`。

> **注意**：一般在 web 中，`output.encoding` 应该和 html 页面的 `contentType` 中的编码，以及 `response` 的 `characterEncoding` 完全一致。


语法选项
------------------------

<a name="syntax.safecall"></a>
### syntax.safecall

jetbrick-template 支持 4 种方法的安全调用（类似于 Groovy），以避免出现 `NullPointerException`

1. 属性调用 `bean?.property`
2. 方法调用 `bean?.method(...)`
3. 数组访问 `array?[index]`
4. Map访问 `map?[key]`

如果 `syntax.safecall = true`，那么将会把全局默认的语法变成安全调用语法。
如 `bean.property` 将等价于 `bean?.property`。这样我就可以省略 `?` 拉。 

默认为 `false`，不启用。


<a name="template.loader"></a>
### template.loader

如何找到我们自己的模板文件呢？这里就是定义我们要使用的查找类。我们支持下面几种 Class

```
template.loader = jetbrick.template.resource.loader.FileSystemResourceLoader
template.loader = jetbrick.template.resource.loader.ClasspathResourceLoader
template.loader = jetbrick.template.resource.loader.JarResourceLoader
template.loader = jetbrick.template.web.WebResourceLoader
template.loader = jetbrick.template.resource.loader.MultipathResourceLoader
```

默认为 `jetbrick.template.resource.loader.FileSystemResourceLoader`。

注意：如果是 Web 集成模式，默认值为 `jetbrick.template.web.WebResourceLoader`。

<a name="template.path"></a>
### template.path

除了要定义 `template.loader`，我们还需要定义模板存放的根目录。

默认为系统当前目录：`System.getProperty("user.dir")`。

注意：如果是 web 集成模式，默认为 webapp 的根目录。具体请参考：[JetEngine 自动加载方式](integrate.html#JetEngine) 中注意事项。

* 从文件系统加载

```
template.loader = jetbrick.template.resource.loader.FileSystemResourceLoader
template.path = /opt/app/templates/
```

* 从Classpath下加载

```
template.loader = jetbrick.template.resource.loader.ClasspathResourceLoader
template.path = /META-INF/templates/
```

* 从jar包中加载

```
template.loader = jetbrick.template.resource.loader.JarResourceLoader
template.path = /opt/app/templates.jar
```

* 从webapp目录中加载(仅在Web框架集成中有效，并且已经被设置为默认项)

```
template.loader = jetbrick.template.web.WebResourceLoader
template.path = /WEB-INF/templates
```

* 从多个目录中加载

```
template.loader = jetbrick.template.resource.loader.MultipathResourceLoader
template.path = file:/path/to, classpath:/, jar:/path/to/sample.jar, webapp:/WEB-INF/templates
```

注意：`template.path` 支持多种路径，由逗号分隔。每个路径由一个前缀开头，代表相应的 ResouceLoader。具体如下：

|  前缀                 | 代表的 ResourceLoader       |
|-----------------------|-----------------------------|
| file:                 | FileSystemResourceLoader    |
| classpath:            | ClasspathResourceLoader     |
| jar:                  | JarResourceLoader           |
| webapp:               | WebResourceLoader           |
| &lt;MyClassLoader&gt;:| 用户自定义的 ResourceLoader (完整类名) |

<font color="red">
现在 `template.path` 支持变量了，如：
</font>

```
template.path = ${user.dir}/templates
template.path = ${webapp.dir}/WEB-INF/templates
```

那么我们支持哪些变量呢？其实这些变量都来自于 `System.getProperty(name)`，只要 `System` 里有的，都支持。
其中 `webapp.dir` 是个特殊变量，由 Web 集成框架在系统启动的时候，通过 `System.setProperty("webapp.dir", servletContext.getRealPath("/"))` 设置的。


<a name="template.suffix"></a>
### template.suffix

默认的模板文件扩展名 `.jetx`，主要用于 Web 框架集成中，用于查找和过滤模板用。

<a name="template.reloadable"></a>
### template.reloadable

在开发模式下面，我们一般需要频繁的修改模板内容来进行调试。那么我们需要打开这个功能来支持模板的热部署。（类似于 `JSP`）

是否需要重新编译和加载模板，取决于模板源文件的最后修改时间。

默认为 `false`，建议只在开发模式中启用。

编译选项
----------

jetbrick-template 采用编译成 Java ByteCode 来提高性能。

<a name="compile.tool"></a>
### compile.tool

模板编译器的配置，默认使用 Eclipse Java Compiler, 如果不存在，那么将切换到 JDK 自带的编译器 （需要使用 JDK，而不是 JRE）。

目前可选的配置如下：

```
# 使用 Eclipse Java Compiler (默认值)
compile.tool = jetbrick.template.compiler.JdtCompiler

# 使用 JDK Compiler
compile.tool = jetbrick.template.compiler.JdkCompiler
```

Eclipse Java Compiler 需要引入第三方 jar (Tomcat 等 WebServer 一般都自带该 jar)

```xml
<dependency>
    <groupId>org.eclipse.jdt.core.compiler</groupId>
    <artifactId>ecj</artifactId>
    <version>4.3.1</version>
</dependency>
```

1. 对于不支持 `javax.tools.JavaCompiler` 接口的 BAE (Baidu App Engine), 应该使用该编译器。
2. 对于出现未知的编译错误的时候，可以尝试切换编译器。

<a name="compile.strategy"></a>
### compile.strategy

模板从 1.2.0 开始，提供更加灵活的编译策略。由下面 4 中情况

```
compile.strategy = precompile
compile.strategy = always
compile.strategy = auto
compile.strategy = none
```

* `precompile`
    在 JetEngine 初始化的时候，自动获取所有的模板(根据 `template.suffix` 过滤)，然后启动一个独立的线程进行编译。
    这样虽然启动时间会增加，但是后面的模板访问将会非常的快。并且在预编译没有完成期间，应用可以正常访问，不冲突。        

* `always` （默认值）
    就是在模板被首次访问的时候，进行编译。

* `auto`
    就是在模板被首次访问的时候，如果磁盘中已经存在编译好的 Class 文件（并且源文件没有改变），那么直接加载该 Class 文件，否则进行编译。

* `none`
    该模式下，将不在对模板进行编译。（发布的时候，用户无需发布任何模板源文件）
    用户必须通过 `JetxGenerateApp` 预编译工具，事先将模板全部编译成 class 文件，并将所有的 class 文件放在 classpath 下面。
    注意： class 文件放在 classpath 下面，而不是 `compile.path` 对应的目录。

> 注意：
> 不管采用什么模式，对于使用 `JetEngine.createTemplate(source)` 直接由源码创建的模板，仍然需要进行编译。 

<a name="compile.debug"></a>
### compile.debug
  
是否在日志中打印输出模板生成的 Java Source 源代码。

默认 `false`，建议在开发模式中启用。

> **注意**：同时需要 slf4j 的配合才能输出日志。默认已经开启了 `INFO` 级别的日志。

<a name="compile.path"></a>
### compile.path

在模板编译的时候，会先生成对应的 `.java` 文件，然后在把 `.java` 文件编译成 `.class` 文件。我们生成的这 2 种文件就放在这个目录下面。

在用 Eclipse 进行 debug 的时候，可以 link 这个目录为 sourcepath 来进行 debug。
具体参考：[如何调试模板 debug？](userguide.html#debug)

默认会在系统TEMP目录 `System.getProperty("java.io.tmpdir")` 下面新建一个 `jetx` 目录。如果这个目录非法或者没有写的权限，那么就会抛出 Exception。

> **注意**：
> 
> * 如果一个应用中使用多个 `JetEngine` 实例，请配置不同的 `compile.path` 防止出现冲突。我们建议用户每次都重定义这个路径。

<font color="red">
现在 `compile.path` 支持变量了，如：
</font>

```
compile.path = ${java.io.tmp}/jetx
compile.path = ${webapp.dir}/WEB-INF/jetx_classes
```

那么我们支持哪些变量呢？其实这些变量都来自于 `System.getProperty(name)`，只要 `System` 里有的，都支持。
其中 `webapp.dir` 是个特殊变量，由 Web 集成框架在系统启动的时候，通过 `System.setProperty("webapp.dir", servletContext.getRealPath("/"))` 设置的。


安全管理器
-------------

从 1.2.0 开始，模块新增了安全管理器，特别适合于 CMS 软件，允许用户自定义模板的场景。


<a name="security.manager"></a>
### security.manager

配置安全管理器的实现类，默认为空，表示禁用安全管理器。

启用方式(使用默认的安全管理器)：

```
security.manager = jetbrick.template.parser.JetSecurityManagerImpl
```

用户也可以实现自己的安全管理器，只要实现接口： `jetbrick.template.JetSecurityManager` 即可。

安全管理器只在对模板进行解析编译的时候进行，运行期不会影响任何性能。

<a name="security.manager.file"></a>
### security.manager.file

给默认的安全管理器，配置黑白名单，将该名单放在独立的外部文件中。（每行一个名单）

```
security.manager.file = ${webapp.dir}/WEB-INF/jetx-white-black-list.txt
```

<a name="security.manager.namelist"></a>
### security.manager.namelist

给默认的安全管理器，配置黑白名单，多个名单以逗号分隔。

```
security.manager.namelist = -java.lang.System.exit \
                            -java.lang.reflect \
                            -java.sql \
                            -javax.tools \
                            -java.io \
                            +java.io.File.getName \
                            +java.io.File.getPath \
                            -sun \
```

`security.manager.file` 和 `security.manager.namelist` 二选一配置即可。

黑白名单的格式如下：

1. 前缀符号：
 
    * `+` 开头代表白名单
    * `-` 开头代表黑名单
    * 没有开始符号，则默认为白名单

2. 名单格式：

    * 包名： `pkg`
    * 类名名： `pkg.class`
    * 方法名： `pkg.class.method`
    * 字段名： `pkg.class.field`

实例：

```
-java.sql                           // 禁止访问 java.sql 下面的任何 Class，包括所有孙子包下面的 Class
-java.lang.System.exit              // 禁止调用 System.exit() 方法
+java.util.Collections.EMPTY_LIST   // 允许访问 Collections.EMPTY_LIST 字段
```


注释指令
------------

由于目前的指令一般直接嵌入在 HTML，对于一些使用可视化编辑器的用户来说，可能会造成一些干扰。
模板从 1.0.1 开始增加对指令注释支持，如：`<!-- #if (...) -->`; 增强对可视化编辑器的友好度。

<a name="trim.directive.comments"></a>
### trim.directive.comments

是否开启对注释指令的支持，默认为 `false`，表示不启用。

<a name="trim.directive.comments.prefix"></a>
### trim.directive.comments.prefix

设置注释开始格式，默认为 `<!--`

<a name="trim.directive.comments.suffix"></a>
### trim.directive.comments.suffix

设置注释开始格式，默认为 `-->`

> **注意**： 如果开启注释指令的支持，系统并没有强制要求 `trim.directive.comments.prefix` 和 `trim.directive.comments.suffix` 必须配对出现。也就是说如果使用 `<!-- #end` 也是可以的。当然我们还是建议你配对使用。

范例：

```html
<table>
<!-- #for (User user: userlist) -->
  <tr>
	<td>${user.name}</td>
	<td>${user.email}</td>
  </tr>
<!-- #end -->
</table>
```


其他选项
------------

<a name="trim.directive.line"></a>
### trim.directive.line

由于指令之间存在很多的空白内容，而空白内容也会被作为原始文本原封不动的输出，这样会造成很多输出的内容参差不齐。这个就是用来优化输出格式的，对于用模板来进行代码生成时候特别有用。不建议关闭。

模板示例：

````
#for (int n: [1,2,3])
${n}
#end
````

禁用后效果：`false`

````

1

2

3

````

启用后的效果：`true` (默认启用)

```
1
2
3
```


推荐配置
------------

### 开发环境

```
import.packages = pkg1, pkg2
import.autoscan = true
import.autoscan.packages = pkg1, pgk2

template.path = /path/to/templates/
template.reloadable = true

compile.strategy = always
compile.path = /path/to/temp/
compile.debug = true
```

### 生产环境

```
import.packages = pkg1, pkg2
import.autoscan = true
import.autoscan.packages = pkg1, pgk2

template.path = /path/to/templates/
template.reloadable = false

compile.strategy = precompile
compile.path = /path/to/temp/
compile.debug = false
```

