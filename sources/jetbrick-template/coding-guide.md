JAVA 编码规范 1.0 (jetbrick 版)
==================================

Java 文件格式
----------------------

1. 文件格式必须是 `UTF-8`，无 `BOM` 格式
2. 文件回车换行符必须是 `Unix` 风格
3. 每个文件结尾必须有一个空白行
4. 行尾空白内容应该被 trim 掉
5. 每个文件开头必须写上项目的标准 LICENSE 注释，如下：

    ```java
    /**
     * jetbrick-template
     * http://subchen.github.io/jetbrick-template/
     *
     * Copyright 2010-2014 Guoqiang Chen. All rights reserved.
     * Email: subchen@gmail.com
     *
     * Licensed under the Apache License, Version 2.0 (the "License");
     * you may not use this file except in compliance with the License.
     * You may obtain a copy of the License at
     *
     *   http://www.apache.org/licenses/LICENSE-2.0
     *
     * Unless required by applicable law or agreed to in writing, software
     * distributed under the License is distributed on an "AS IS" BASIS,
     * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
     * See the License for the specific language governing permissions and
     * limitations under the License.
     */
    ```

6. 代码必须是格式化的，请使用统一的 Eclipse 的代码格式文件：[eclipse-jetbrick-style-formatter.xml](download/eclipse-jetbrick-style-formatter.xml)
7. 不想被自动格式化的代码请用 `@formatter` 包裹，如：

    ```java
    //@formatter:off
    private static final String[] DATE_PATTERNS = new String[] { 
        "yyyy-MM-dd HH:mm:ss.SSS", 
        "yyyy-MM-dd HH:mm:ss", 
        "yyyy-MM-dd", 
        "HH:mm:ss"
    };
    //@formatter:on
    ```
8. Java 文件必须是可编译的，不应该有任何的 `warning` 存在

包名
---------------------

1. 包名必须是全部小写的，最好用一个单词表示
2. 包名必须以 `jetbrick` 开头
3. 接口或者抽象类的多种实现，推荐以 `spi`, `support` 包命名

类名
---------------------

1. 类名必须首字母大写，驼峰命名法： 如 `UserInfo`，`ClassUtils`
2. 类名尽量不要缩写，如果缩写，必须为特别常用的缩写
3. 接口的命名不要以 `I` 开头
4. 抽象类推荐以 `Abstract` 开头
5. 接口的默认实现推荐以 `Default` 开头或者 `Impl` 结尾
6. 每个 Class 都需要标注 `@auther`, `@since`
7. 每个 Class 都应该有简短的注释

Imports
---------------------

1. Imports 间不要有空行
2. 超过 3 个相同包下面的 Class 需要使用 `.*` 代替
3. 不要使用 `import static`, 除了 `JUnit` / `TestNG` 的 `assertXXX` 方法

方法
---------------------

1. 方法名称应该采用首字母小写，驼峰命名法： 如 `getUser`，`lookupClass`
2. 对于一个 `public` 的方法，都应该对参数进行基本的校验，比如 `null` 检测
3. 对外开放 API 的 `public` 方法都需要标注 `@since`
4. 每个 `public` 方法都应该有简短的注释

常量
---------------------

1. 常量必须是全大写，并用 `_` 分隔，如 `MAX_INTEGER`
2. 常量必须是 `static` `final`

变量
---------------------

1. 变量名称必须首字母小写，驼峰命名法
2. 变量名尽量使用缩写，以简短为主
3. 不要用拼音，要用英文表示
4. 如果是集合或数组，用复数名词，或者添加 `List`, `Map` 等后缀

注释
---------------------

1. 注释必须和代码保持一致，中文/英文均可
2. 注释中的第一个句子要以（英文）句号、问号或者感叹号结束。 `javadoc` 工具会将注释中的第一个句子放在方法汇总表和索引中。
3. 如果注释中有超过一个段落，用 `<p>` 标签分隔
4. 如果注释中有多个章节，用 `<h2>` 标签声明每个章节的标题
5. 示例代码以 `<pre>` 包裹

异常
---------------------

1. 异常类名必须以 `Exception` 结尾
2. 所有自定义异常都必须继承自 `RuntimeException`
3. 方法尽量不要抛出非 `RuntimeException` 异常
4. 异常应该和主要的 Class 放在一起，而不是所有的异常类放在一个包下面
5. 异常描述应该使用英文句子，尽量不要用中文。
6. 被 `catch` 住的 `Exception`，必须要处理，或者重新抛出

日志
---------------------

1. 日志框架使用 `slf4j`
2. 实例不多的对象类，不要使用 `static` 声明 `log`
3. 尽量使用 `debug` 而不是 `info` 级别
4. 启动时候需要输出的重要日志，请用 `info` 级别
4. 被 `catch` 住的 `Exception`，应该被打印出来 `log.error(e)`


单元测试
---------------------

1. 单元测试框架用 `TestNG`
2. 单元测试覆盖率工具用 `EclEmma`
3. Mock 框架使用 `Mockito`
4. 尽可能为每个方法提供单元测试
5. 覆盖率应该不低于 70%

