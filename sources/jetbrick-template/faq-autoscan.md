如何自动发现用户自定义的扩展方法/函数/标签 Class
========================================================

jetbrick-template 主要的扩展点有下列几个：

1. 扩展方法
2. 扩展函数
3. 自定义标签 #tag

常规的配置方法如下：

```
# 扩展方法
import.methods = app.methods.StringMethods, app.methods.DateMethods

# 扩展函数
import.functions = app.functions.UserFunctions

# 自定义标签
import.tags = app.tags.UserTags, app.tags.CacheTags
```

如果需要增加或者调整 Class，需要同时维护这个配置文件，比较麻烦。

从 1.1.2 开始，增加 annotation 自动扫描查找 Methods / Functions / Tags Class 的功能

具体的 annotation 如下：

```java
@JetAnnotations.Methods
@JetAnnotations.Functions
@JetAnnotations.Tags
```

只要在对应的 Class 中，增加对应的 annotation 即可。

例如：

```java

@JetAnnotations.Methods
public class StringMethods {
    ...
}
```

然后开启 `import.autoscan = true` 就可以自动发现了。

当然，为了加快发现的速度，建议同时配置 `import.autoscan.packages`。

Good luck.
