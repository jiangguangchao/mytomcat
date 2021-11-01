# mytomcat
这是一个tomcat 源码项目，目前包含2个子模块，myweb 和 tomcat-source.
myweb是一个简单的web项目，目前包含一个简单的servlet.
tomcat-source 是一个tomcat8.5.69的源码模块。通过 conf/server.xml配置外部项目，可以将myweb放入tomcat-source中启动。
将两个模块构建在一个项目下的目的是debug方便。比如在web项目的servlet的 doGet方法有一行代码 
response.setContentType("text/html; charset=UTF-8");
在这一行代码大断电，如果是两个独立的项目，由这行代码深入，无法直接debug进tomcat源码，或者是可以进入源码，但是进入的是无法修改
的源码。像这个项目，集合了这两个模块，debug就可以联合两个模块的代码。
