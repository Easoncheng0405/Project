# 吉林大学2015级软件工程课程设计


### 关于Project

> 吉林大学2015级软件工程课程设计——银行储蓄系统



项目采用`Spring-boot`+`Spring-data-jpa`+`mysql`+`thymeleaf`+`adminLte`框架,在运行代码前可能需要了解
它们与它们依赖的基础知识


### 环境配置

你需要:

* `Maven`
* `mysql`
* `java8`

>  确保你的电脑里正确安装了以上软件


首先从[这儿](https://github.com/Easoncheng0405/Project)下载源码或者使用git克隆到你的本地仓库


编辑 **project\src\main\resources** 下的 **application.properties** 将里面的数据库配置改为你自己的数据库配置


如：用户名 密码 所使用的数据库 (默认数据库为bank,用户名为root,密码0405)

### 运行项目



在 **project** 目录下运行 `mvn spring-boot:run` (需要配置maven环境变量)

待控制台显示 JVM started in ... seconds ,打开浏览器访问 localhost:8080 即可

系统有一个默认的超级管理员，工号 40000 密码 1234abcd* 无法删除。


### 使用文档

具体见软件需求分析文档，设计文档和代码注释


我的博客: www.chengjie-jlu.com

