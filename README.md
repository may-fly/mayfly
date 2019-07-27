# mayfly

## 介绍
mayfly前后端分离的后台系统(包含按钮级别的权限管理，以及权限禁用，触发按钮置灰禁用状态等)。后续计划补上Redis单机以及集群管理（已完成部分接口以及界面）


## 系统环境
- 前端：node，  vue，  element-ui
- 后端：jdk8，  SpringBoot，  Mybatis
- DB： mysql，  redis

## 模块介绍
> mayfly-admin-front
前端系统

> mayfly-common
后端公用模块，包括一些常用的uitls以及权限管理，参数校验器，日志处理等。

> mayfly-dao
后端DAO模块，主要包含通用BaseMapper的封装，系统需要的PO以及Mapper等

> mayfly-sys
后端系统主要模块，包含BaseService以及各功能对应的Service和Controller等

## 项目特点 

- ### 方法日志记录(记录方法出入参以及执行时间或异常)
日志记录采用AOP（mayfly.sys.aop.log.LogAspect）类进行拦截带有@MethodLog注解的所有方法或者带有@MethodLog类下的所有方法，进行出入参以及运行时间的记录，
也包含异常日志的记录，也可以设置按指定日志级别打印日志.
使用方式大致如下：
```
/**
*如果有时候返回值太多，也可将@MethodLog注解中的result属性去掉（默认为false）
*也可以使用@NoNeedLogParam注解作用于参数上，让其不记录该参数值,可以用来去除一些系统参数，如HttpServletResponse等
*/
@MethodLog(value = "获取权限列表", time = true)
@GetMapping("/v1/permissions")
public Result list(PermissionForm condition, @Valid PageForm pageForm){}


/**
*也可以用于类上,如下，以DEBUG方式打印日志
*/
@MethodLog(level = MethodLog.LogLevel.DEBUG)
public class PermissionServiceImpl{}
```
打印结果如图：
![日志输出](https://gitee.com/uploads/images/2019/0425/094929_de188281_1240250.png "屏幕截图.png")


- ### 自定义参数校验器(支持入参枚举值自动校验等)
参数校验采用AOP拦截所有Controller中的方法带有mayfly.common.validation.annotation.Valid注解的参数，并校验对应的参数字段注解以及值。

给需要校验的参数字段添加相应的校验规则：
```
@Data
public class PermissionForm {
    private Integer groupId;
    /**
     * 字符串非空检验
     */
    @NotBlank
    private String uriPattern; 

    @NotBlank
    private String code;

    /**
     * method不能为空且只能是MethodEnum中对应的枚举值value
     */
    @EnumValue(clazz = MethodEnum.class)
    @NotNull
    private Integer method;

    @NotBlank
    private String description;

    @EnumValue(clazz = StatusEnum.class)
    private Integer status;
}
```
更多可使用的参数字段校验规则，详见：mayfly.common.validation.annotation包

Controller方法参数校验用法：
![参数前加@Valid注解](https://images.gitee.com/uploads/images/2019/0329/131943_438c4935_1240250.png "屏幕截图.png")


- ### 自定义权限校验注解(控制后端接口以及前端列表按钮权限)
系统使用自定义权限注解@mayfly.common.permission.Permission来控制用户的操作权限（即后端接口调用的权限，可实时禁用以及删除权限），和通过权限code控制前端页面的列表权限以及按钮权限（显示与否，以及是否为禁用状态。前端通过VUE的自定义指令v-permission进行按钮的控制，详情可见前端模块：mayfly-admin-front ）。
```
/**
*每个方法都有丢应的权限code(用来进行权限校验以及前端页面按钮控制)  
*如果该注解只作用于类上，则类中方法对应的权限code为类权限code + 方法名
*/
@mayfly.common.permission.Permission(code = "permission:")
@RestController
@RequestMapping("/sys")
public class PermissionController 
```
具体如何拦截以及实时启用禁用可见对应拦截器:mayfly.sys.interceptor.PermissionInterceptor

- ### 前端枚举值统一管理维护
具体细节可见前端模块：mayfly-admin-front 或者博客：https://www.jianshu.com/p/75516ec4f366

## 系统部分页面

1.菜单&权限管理页
![菜单&权限管理](https://images.gitee.com/uploads/images/2019/0727/160648_88d6bc70_1240250.png "屏幕截图.png")

2.角色分配菜单&权限页
![分配角色权限](https://images.gitee.com/uploads/images/2019/0727/160803_36bf030d_1240250.png "屏幕截图.png")
