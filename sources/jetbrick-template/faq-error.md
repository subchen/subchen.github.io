jetbrick-template 常见错误分析
====================================


The method getXXX() or isXXX() is undefined for the type Object
----------------------------------------------------------------
    
    ${obj.foo}

  1. 确定 obj 存在 getFoo() or isFoo() 方法，并且是 `public` 的
  2. obj 对象是否已经声明变量类型，否则请用 `#define(TYPE obj)` 声明变量类型。


The method foo(xxx, ...) is undefined for the type Object
--------------------------------------------------------------
    
    ${obj.foo(...)}

  1. 确定 obj 存在 foo(...) 方法，并且是 `public` 的，参数类型是否匹配。
  2. obj 对象是否已经声明变量类型，否则请用 `#define(TYPE obj)` 声明变量类型。
  3. 如果 foo 是扩展方法，那么请确认扩展函数 XXX 是否已经注册到 `JetEngine` 中，或者参数类型是否匹配。

Operator [] is not applicable for the object (Object)
----------------------------------------------------------

    ${obj[foo]}

  1. obj 对象是否已经声明变量类型，否则请用 `#define(TYPE obj)` 声明变量类型。
  2. 只有 `List`，`Map`，`JetContext` 对象支持 "[]" 操作


Duplicate local variable xxx
-----------------------------------

  变量 xxx 定义的两次（相同作用域只能定义一次），请查找 `#define` 和 `#set` 指令是否对 变量 xxx 进行多次定义 


Type mismatch: cannot convert from XXX to YYY
-------------------------------------------------------------

    #define(String str)
    #set(int a = str)

  变量类型部不兼容，比如 `String` 对象复制给 `int`。


Undefined function XXX
--------------------------------

  扩展函数 XXX 没有找到，请确认扩展函数 XXX 是否已经注册到 `JetEngine` 中，或者参数类型是否匹配。


Undefined tag definition: XXX(...)
----------------------------------------

  没有找到对应的 Tag 定义，请确认 Tag 是否已经注册到 `JetEngine` 中，或者参数类型是否匹配。


line xxx: Implicit definition for context variable: XXX
------------------------------------------------------------

  变量 XXX 没有声明变量类型，而直接使用。（这个仅仅是一个 Warning，不是 Error）











