# mayfly

### 介绍
mayfly前后端分离的后台系统(包含按钮级别的权限管理，以及权限禁用，触发按钮置灰禁用状态)。后期补充redis系统管理


### 系统环境
- 前端：node  vue  element-ui
- 后端：jdk8  SpringBoot  Mybatis
- 数据库： mysql  redis

### 模块介绍
> mayfly-admin-front
前端系统

> mayfly-common
后端公用模块，包括一些常用的uitls以及权限管理，参数校验器，日志处理等。

> mayfly-dao
后端DAO模块，主要包含通用BaseMapper的封装，系统需要的PO以及Mapper等

> mayfly-sys
后端系统主要模块，包含BaseService以及各功能对应的Service和Controller等

### 项目特点 
- #### 方法日志记录
日志记录采用AOP（mayfly.sys.aop.log.LogAspect）类进行拦截带有@MethodLog注解的所有方法，进行出入参以及运行时间的记录，也包含异常日志的记录。
使用方式大致如下：
```
/**
*如果有时候返回值太多，也可将注解中的result属性去掉（默认为false）。也可以使用@@NoNeedLogParam注解作用于参数上，让其不记录该参数值，*可以用来去除一些系统参数，如HttpServletRespose等
*/
@MethodLog(value = "获取权限列表", time = true)
@GetMapping("/v1/permissions")
public Result list(PermissionForm condition, @Valid PageForm pageForm)
```
打印结果如图：
![输出日志](https://images.gitee.com/uploads/images/2019/0329/125647_5fca5971_1240250.png "屏幕截图.png")

### 系统页面

1.权限页面列表
![权限页面列表](https://images.gitee.com/uploads/images/2019/0327/143455_07c3dc2d_1240250.png "屏幕截图.png")
