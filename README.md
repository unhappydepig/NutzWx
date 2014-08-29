1)create database platform
2)mvn clean
3)mvn eclipse:eclipse -Dwtpversion=1.0
4)mvn package -Dmaven.test.skip=true
5)mvn dependency:tree 可以查看依赖树

NutzWx
======

基于Nutz、Nutzwx 开源微信后台开发框架。

前台采用 jQuery、EasyUI-Tab、zDialog；

后端采用 Velocity 模板语言、Druid 连接池、Quartz 定时任务等。


0.1 beta：实现微信帐号的接入；
