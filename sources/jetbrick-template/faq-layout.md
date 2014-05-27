在 jetbrick-template 中如何实现 layout 功能
===================================================

通常页面都有一个布局，大体上有页面头部，尾部，以及正文三部分。头部和尾部内容基本上固定，只有正文是变化的。

jetbrick-template 可以使用 3 种方法实现模板的 layout 功能。

1. `#include(file, ...)` 指令
2. `#tag layout(file, ...)` 标签
3. `#tag block(name)` + `#inlcude(file, ...)` 组合

常规模式
-------------------

我们将页面公共部分放在 include 文件中，然后在每个页面中应用 include 文件来实现内容共享。

main.jetx

```html
#include("/include/header.jetx")
<table>  
  这是正文内容  
</table>  
#include("/include/footer.jetx")
```

这个不算 layout?
呵呵，继续放下看。

变种模式
-------------------

常规模式中，只是共享了一些公共内容，但是并没有实现真正的 layout，因为 layout 一变，include 的方式可能就会发生变化，这样所有文件都要重新 include，不是很方便。

下面就是 #include 模式的变种（动态 include），来实现真正的 layout。

layout.jetx

```html
<html>
  <head>
    <title>${title}</title>
  </head>
  <body>
    <div class="header">This is a logo image.</div>

    #include(parameter.pageUrl)

    <div class="footer">Copyright @2000-2010, All Rights Reserved.</div>
  </body>
</html>
```

main.jetx

```html
<table>  
  这是正文内容  
</table>
```

Action.java

```java
String url = "/layout.jetx?pageUrl=" + URLEncoder.encode("/main.jetx", "utf-8);
RequestDispatcher rd = getServletContext().getRequestDispatcher(url);
rd.forward(request, response);
```

怎么样？通过一个 pageUrl 参数来达到动态 layout 的目的，这样我们以后只要修改layout.jetx 文件就能达到修改所有页面布局了。

URL 访问方法： http://127.0.0.1:8080/layout.jetx?pageUrl=main.jetx

\#tag layout(file, ...)
----------------------------

这里再介绍一种采用 layout 自定义标签来实现。

layout.jetx

```html
<html>
  <head>
    <title>${title}</title>
  </head>
  <body>
    <div class="header">This is a logo image.</div>

    ${bodyContent}

    <div class="footer">Copyright @2000-2010, All Rights Reserved.</div>
  </body>
</html>
```

main.jetx

```html
#tag layout("layout.jetx") 
<table>  
  这是正文内容  
</table>
#end
```

layout 标签允许指定一个 layout 模板文件，在渲染页面的时候，会将layout标签体的渲染内容作为一个 bodyContent 变量插入到 layout 指定模板文件里。

这样，我们就可以通过直接访问 http://127.0.0.1:8080/main.jetx



组合 \#tag block() \+ \#inlcude
-----------------------------------

上面的 #tag layout() 标签只能嵌入一块自定义的内容 `${bodyContent}`，那么如果想要嵌入多个自定义的变量块呢？

main.jetx

```html
#tag block("bodyContent1")
    BODY 1111
#end
#tag block("bodyContent2")
    BODY 2222
#end
#include ("layout.jetx")
```

layout.jetx

```html
This is a header.
<div>
    ${bodyContent1}
</div>
<div>
    ${bodyContent2}
</div>
This is a footer.
```

输出结果：

```html
This is a header.
<div>
    BODY 1111
</div>
<div>
    BODY 2222
</div>
This is a footer.
```

怎么样？现在就可以随意的进行组合了。


block 的默认值和重载
----------------------

在 layout.jetx 中，我们也可以定义默认内容，在 main.jetx 中对默认内容进行重载，如下：

layout.jetx

```html
This is a header.
<div>
#tag default_block("bodyContent1")
    This is a default BODY 1111
#end
</div>
<div>
#tag default_block("bodyContent2")
    This is a default BODY 2222
#end
</div>
This is a footer.
```

main.jetx

```html
#tag block("bodyContent1")
    Override block 111
#end
#include ("layout.jetx")
```

输出结果：

```html
This is a header.
<div>
    Override block 111
</div>
<div>
    This is a default BODY 2222
</div>
This is a footer.
```


总结
---------------

不管采用哪种方式，jetbrick-template 都提供很大的灵活性来实现页面布局功能。

其中自定义 #tag 标签机制提供了非常强大功能，很容易实现其他模板引擎难以实现的功能，比如像 JSP Taglib 一样实现自定义标签，如 Cache Tag 将内容缓存到 memecached/redis 的标签等等。
