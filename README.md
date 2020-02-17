## 自学交流论坛社区
## 黄俊豪的毕业设计


## 资料
[Spring 文档](https://spring.io/guides)    
[Spring Web](https://spring.io/guides/gs/serving-web-content/)   
[es](https://elasticsearch.cn/explore)    
[Github deploy key](https://developer.github.com/v3/guides/managing-deploy-keys/#deploy-keys)    
[Bootstrap](https://v3.bootcss.com/getting-started/)    
[Github OAuth](https://developer.github.com/apps/building-oauth-apps/creating-an-oauth-app/)    
[Spring boot](https://docs.spring.io/spring-boot/docs/2.0.0.RC1/reference/htmlsingle/#boot-features-embedded-database-support)    
[菜鸟教程](https://www.runoob.com/mysql/mysql-insert-query.html)    
[Thymeleaf](https://www.thymeleaf.org/doc/tutorials/3.0/usingthymeleaf.html#setting-attribute-values)    
[Spring Dev Tool](https://docs.spring.io/spring-boot/docs/2.0.0.RC1/reference/htmlsingle/#using-boot-devtools)  
[chrome热部署插件](https://docs.spring.io/spring-boot/docs/2.0.0.RC1/reference/htmlsingle/#using-boot-devtools-livereload)
[Spring MVC](https://docs.spring.io/spring/docs/5.0.3.RELEASE/spring-framework-reference/web.html#mvc-handlermapping-interceptor)  
[Markdown 插件](http://editor.md.ipandao.com/)   
[UFfile SDK](https://github.com/ucloud/ufile-sdk-java)  
[Count(*) VS Count(1)](https://mp.weixin.qq.com/s/Rwpke4BHu7Fz7KOpE2d3Lw)
[maven厂库](https://mvnrepository.com/)
[mybatis-spring-boot-autoconfigure](http://mybatis.org/spring-boot-starter/mybatis-spring-boot-autoconfigure/index.html)
[Lombok消除Java类中的大量样板代码java库](https://projectlombok.org/features/all)
[Chrome WebDriver](http://npm.taobao.org/mirrors/chromedriver)
[前端日期处理函数库](http://momentjs.cn/)
[markdown开源文本html编辑器](http://editor.md.ipandao.com/)
[Ucloud云服务 文件上传](https://github.com/ucloud/ufile-sdk-java)
## 脚本
```sql
CREATE TABLE USER
(
    ID int AUTO_INCREMENT PRIMARY KEY NOT NULL,
    ACCOUNT_ID VARCHAR(100),
    NAME VARCHAR(50),
    TOKEN VARCHAR(36),
    GMT_CREATE BIGINT,
    GMT_MODIFIED BIGINT
);
```
``` bash
#mybatis generate MyBatis Generator可以根据数据库表生成相应的实体、sql映射文件、Dao等，能应付简单的CRUD（Create, Retrieve, Update, Delete），
#对于连接查询或存储过程等还是要手动编写sql和对象。
在terminal 输入 一下命令：
mvn -Dmybatis.generator.overwrite=true mybatis-generator:generate

```

##工具
[Git]https://git-scm.com/download/win
将git 的bash.exe 配置到 idea 的terminal
[Visual Paradigm](https://www.visual-paradigm.com)
##提交文件到github的命令：
echo "# community" >> README.md
git init
git add .
git commit -m "first commit"
git remote add origin https://github.com/AlvesAngle/community.git
git push -u origin master

…or push an existing repository from the command line

git remote add origin https://github.com/AlvesAngle/community.git
git push -u origin master

再次提交：
git add .
git st
git commit --amend --no-edit
git push -u origin master -f

多人协作： 
git push (当多个人修改了版本，你的版本和别人不对)
git pull (把远端的拉回来)
进入vim 编辑状态 按esc 输入:s 保存退出


解决

出现错误的主要原因是github中的README.md文件不在本地代码目录中,

命令行中输入:
git pull--rebase origin master

git push -u origin master
原因:自己分支版本低于主版本

git push -u origin master-f

#bootstap
官网： https：//v3.bootcss.com

##Vue.js
官网： https//cn.vuejs.org
##Vue.js 前端模板框架
# iview:
 http://v1.iviewui.com/
真心不错建议：
# Element:
 http://element-cn.eleme.io/#/zh-CN
##使用github.com 账号登录
https://developer.github.com/apps/building-oauth-apps/creating-an-oauth-app/

#Okhttp 
作用：一个处理网络请求的开源项目,
网址：https://square.github.io/okhttp/


