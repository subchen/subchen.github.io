jetbrick-template 中为什么需要 #define 声明变量类型？
====================================================

由于 jetbrick-template 将模板编译成 Java Class 来提高模板的运行性能。所以模板在运行之前需要像 JSP 一样，先进行编译。

jetbrick-template 编译的时候，需要确定变量的类型来消除反射和类型转换。(注意： JSP 的 EL 表达式是通过反射解释执行的，不需要在编译期间确定变量的类型)

jetbrick-template 支持类型推导，但是如果无法进行类型推导的时候，就会默认对象的类型是 Object，那么如果需要调用非 Object 对象的属性或者方法的时候，就需要借助 #define 或者 #set 指令来进行变量类型的声明。

如何获取变量的类型：

1. 全局默认导入的变量类型，如 `context`， `request`， `session` 等
2. 全局 `import.variables` 中定义的变量类型
3. `obj.foo` 对应的属性类型
4. `obj.method()` 对应的方法返回值
5. 扩展函数、扩展方法的返回值
6. 其他运算结果

注意： 由于 Java 泛型采用的是类型消除（伪泛型），所以对于 Java 泛型的类型推导在一些情况下是不工作的，这时就需要用 `#set(type name = expression)` 将中间结果定义为一个类型。也可以用强制类型转换：`((String)(obj.items.get(0)).toUpperCase()`。

一般在下列情况需要 #define 类型声明

* `${bar.foo}`        读取 foo 属性
* `${bar.foo()}`      调用 foo() 方法
* `${fnCall(bar)}`    调用 fnCall() 函数扩展

下列情况不需要 `#define` 类型声明

* `${bar}`    直接输出对象
* `#for (type var : items)`    #for 指令的循环对象 items

我们建议每个模板中所有的 `#define` 语句都统一定义在文件开头位置，这样方便我们知道模板依赖的各种变量。

`#define` 指令是声明哪些全局 `contex`t 的变量类型，如果需要指定中间运算结果的变量类型，请用 `#set` 指令。

