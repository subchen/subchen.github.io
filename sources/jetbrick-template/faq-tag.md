如何自定义 Tag
====================

jetbrick-template 支持自定义 Tag，类似于 JSP Taglib，但是要比 JSP Taglib 更简单更好用。

这里以建立一个支持 Cache 的 Tag 为例。

Cache Tag 代码范例：
-------------------------

### 首先定义一个 Tag 实现方法

每一个自定义的 Tag 对应一个 Java 的 `public` `static` 的方法。比如我们这里定义的 cache tag，如下:

Tag 方法定义：

```java
public class MyTags {
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

### 必须要注册到全局的 JetEngine 中，我们使用如下的配置：

```
import.tags = MyTags, ...
```

### 然后写对应的例子模板：

```
#tag cache("sum", 10)
    计算结果将被缓存10秒： ${1+2+3+4+5+6+7+8+9}
#end
```


Cache Tag 代码说明
---------------------

对于每一个 Tag 的方法声明，有如下要求：

* 方法签名必须是 `public` `static`
* 方法返回值必须是 `void`
* 方法第一个参数必须是 `JetTagContext`， 其余参数自定义
* 允许 throws 任意的 Throwable
* 允许定义相同名字的 Tag，但是方法参数不一样 （Overload）
* 支持可变参数 (VarArgs)

在 Tag 方法中，我们可以通过 `JetTagContext` 来获取相关内容。主要 API 如下：

* String JetTagContext.getBodyContext()
  获取 `#tag ... #end` 之间的内容
* void JetTagContext.writeBodyContext()
  将内容原封不动的在原地进行输出
* void JetTagContext.getWriter().print(...)
  自定义输出内容
* JetContext JetTagContext.getContext()
  获取模板管理的 `JetContext` 对象，在 Web 环境中，可以通过 `JetContext` 对象进一步获取 `request`, `response` 等对象。
* JetEngine JetTagContext.getEngine()
  获取模板全局 Engine。

详细了解 `JetTagContext` 具体 API，请看 apidocs。

关于模板的使用：

* Tag 的参数必须和定义的一致。
  如果定义为: `cache(JetTagContext ctx, String name, int timeout)`，那么在调用的时候，必须传2个参数，一个 String， 一个 int，比如： `#tag cache("abc", 123) ... #end`

