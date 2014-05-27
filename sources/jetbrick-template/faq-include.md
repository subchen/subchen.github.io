jetbrick-template 中如何嵌入子模板？（父子间参数传递）
===================================================

在 jetbrick-template 中我们有:

* `#include(file, ...)` 指令
* `include(file, ...)` 函数
* `read(file, ...)` 函数 - 用于嵌入纯文本文件

最常规的做法是
---------------------

### include 静态文件名

```
#include("/include/header.jetx")
${include("/include/header.jetx")}
```

### include 动态文件名

```
#define(String url)
#include(url)
${include(url)}
```

> **注意**： `#include` 指令和 `include()` 函数的区别是：
> 
> * `#include` 指令直接将内容输出到原始的 Stream/Writer 里面，效率高。
> * `include()` 函数将内容缓存到一个 String 中返回，可以对返回值进行进一步处理。
> * `#include` 指令如果包含的是静态文件名，那么会检查文件是否存在。`include()` 函数不会做任何检查。


对 include() 函数返回值处理的应用
---------------------------------

```
下面内容全部转为大写字母
${include("ascii.jetx").toUpperCase()}
```

输出内容

```
ABCDEFG...
```


父子模板间传递参数
---------------------------------

我们有以下几种方式可以在父子模板间传递参数

* 父模板的 `JetContext` 自动传递给子模板。
* `#set` 指令会修改当前模板的 `JetContext`，同时也会影响子模板。
* `#include` 指令和 `include()` 函数的第二个参数可以传递一个单独的 Map 对象给子模板。
* 子模板通过 `#put` 指令向父模板返回数据。

### include 父传子参数例子：

子模板 sub.inc.jetx

```
Hello from sub, parent name is ${parentName}.
```

父模板 parent.jetx

```
This is parent.
#include("sub.inc.jetx", {"parentName", "PARENT_NAME"})
```

显示结果如下：

```
This is parent.
Hello from sub, parent name is PARENT_NAME.
```

### \#put 子传父参数例子：


子模板 sub.inc.jetx

```
Hello from sub.
#put("age", 1234567890);
```

父模板 parent.jetx

```
This is parent.
#include("sub.inc.jetx")
sub.age = ${context.age}
```

显示结果如下：

```
This is parent.
Hello from sub.
sub.age = 1234567890
```



