开源许可 License
=============================

Copyright 2010-2014 Guoqiang Chen. All rights reserved. 
Email: subchen@gmail.com

Licensed under the Apache License, Version 2.0 (the "License"); 
you may not use this file except in compliance with the License. 
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software 
distributed under the License is distributed on an "AS IS" BASIS, 
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
See the License for the specific language governing permissions and 
limitations under the License.


第三方依赖包 Dependences
=============================

* [JDK 1.6+](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
* [ANTLR runtime 4.x](http://www.antlr.org/download.html)
* [Slf4j 1.7.x](http://www.slf4j.org/)
* [Eclipse Java Compiler 4.3.x](http://www.eclipse.org/jdt/) (可选, Tomcat 等服务器已自带)
* 
> **注意**： 
> 
> 从 1.2.6 开始，默认使用 JDT Compiler 进行编译，如果没有遭到对应的 JDT 运行库，那么将切换到 JDK Compiler。 (如果是 JDK 编译，那么请确保是 tools.jar 存在。)

Maven 依赖 POM.xml
=============================

已发布到 Maven 中央库： http://central.maven.org/maven2/com/github/subchen/

```xml
<dependency>
    <groupId>com.github.subchen</groupId>
    <artifactId>jetbrick-template</artifactId>
    <version>1.2.6</version>
</dependency>
```

Eclipse Java Compiler.

```xml
<dependency>
    <groupId>org.eclipse.jdt.core.compiler</groupId>
    <artifactId>ecj</artifactId>
    <version>4.3.1</version>
</dependency>
```

从源码安装 Sources
=============================

github: https://github.com/subchen/jetbrick-template

编译方法：

1. 先安装 apache-ant 1.9.x

    ```
    wget http://mirrors.cnnic.cn/apache//ant/binaries/apache-ant-1.9.2-bin.zip
    ```

2. 设置好 JDK, ANT 环境变量

    ```
    set JAVA_HOME=/path/jdk_1.6.x
    set ANT_HOME=/path/apache-ant_1.9.x
    set PATH=%JAVA_HOME%/bin;%ANT_HOME%/bin;%PATH%
    ```

3. 编译

    ```
    git clone https://github.com/subchen/jetbrick-template.git
    cd jetbrick-template
    ant dist
    ```

4. 编译后的文件存放在 build 目录中

    ```
    jetbrick-template-x.x.x.jar
    jetbrick-template-x.x.x.zip
    jetbrick-template-x.x.x-all.zip
    ```


<a name="samples"></a>
范例下载 Samples
=============================

### 官方范例：

* [jetx-samples-servlet.zip](demo/jetx-samples-servlet.zip)
* [jetx-samples-jfinal.zip](demo/jetx-samples-jfinal.zip)
* [jetx-samples-springmvc.zip](demo/jetx-samples-springmvc.zip)
* [jetx-samples-struts.zip](demo/jetx-samples-struts.zip)
* [jetx-samples-jodd.zip](demo/jetx-samples-jodd.zip)

下载的 zip 包中包含完整的源代码和可直接运行的 war 包。
更多代码可以前往： https://github.com/subchen/jetbrick-template-webmvc-samples/

### 网友提供的范例：

* [jetx-samples-nutz-by-howe.zip](demo/jetx-samples-nutz-by-howe.zip)
* [jetx-samples-springmvc-by-yingzhuo.zip](demo/jetx-samples-springmvc-by-yingzhuo.zip)

### 自定义标签 Tags/Methods/Functions：

* [jetbrick-template-extend-1.0.8](demo/jetbrick-template-extend-1.0.8.zip)

更多信息，前往： https://github.com/yingzhuo/jetbrick-template-extend

感谢网友 应卓 提供相关的实现。

<a name="documents"></a>
离线文档下载 Documents
=============================

[jetbrick-template-1.2.2-documents.pdf](download/jetbrick-template-1.2.2-documents.pdf)

感谢网友 laughing 提供离线 PDF 文档。


<a name="version"></a>
最新版本 Latest Version
=============================

* [jetbrick-template-1.2.6.jar](http://search.maven.org/remotecontent?filepath=com/github/subchen/jetbrick-template/1.2.6/jetbrick-template-1.2.6.jar)
* [jetbrick-template-1.2.6-sources.jar](http://search.maven.org/remotecontent?filepath=com/github/subchen/jetbrick-template/1.2.6/jetbrick-template-1.2.6-sources.jar)
* [jetbrick-template-1.2.6-javadoc.jar](http://search.maven.org/remotecontent?filepath=com/github/subchen/jetbrick-template/1.2.6/jetbrick-template-1.2.6-javadoc.jar)


**第三方依赖 jar 下载**

* [antlr4-runtime-4.1.jar](http://search.maven.org/remotecontent?filepath=org/antlr/antlr4-runtime/4.1/antlr4-runtime-4.1.jar)
* [slf4j-api-1.7.7.jar](http://search.maven.org/remotecontent?filepath=org/slf4j/slf4j-api/1.7.7/slf4j-api-1.7.7.jar)

更多历史版本，请看[这里](history.html)


更新历史 Release Notes
=============================

<a name="release_notes_1_2_6"></a>
**Version 1.2.6 (2014-05-18)**

* \[新增] [#94 增加 BASE_PATH 全局变量][issue_94]
* \[变更] [#93 默认编译器更改为 JDT（如果存在）][issue_93]
* \[变更] [#95 change buildin function iterator()->loop()][issue_95]
* \[修复] [#88 安全调用对扩展方法不起作用][issue_88]
* \[修复] [#92 JDK8 对 (a==null?null:a.toString()) 编译存在问题][issue_92]

[issue_88]: https://github.com/subchen/jetbrick-template/issues/88
[issue_92]: https://github.com/subchen/jetbrick-template/issues/92
[issue_93]: https://github.com/subchen/jetbrick-template/issues/93
[issue_94]: https://github.com/subchen/jetbrick-template/issues/94
[issue_95]: https://github.com/subchen/jetbrick-template/issues/95

