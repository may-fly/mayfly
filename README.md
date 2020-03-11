# mayfly

## 介绍
mayfly前后端分离的后台系统(包含按钮级别的权限管理，以及权限禁用，触发按钮置灰禁用状态等)。后续计划补上机器管理及Redis单机以及集群管理（已完成部分接口以及界面）


## 系统环境
- 前端：node，  vue，  element-ui
- 后端：jdk11，  SpringBoot，  Mybatis
- DB： mysql，  redis

## 模块介绍
> mayfly-account-front
前端系统

> mayfly-core
后端核心模块，包括一些常用的uitls，BaseMapper（无需第三方插件包，详见博客：https://www.jianshu.com/p/5fcea00f439d），BaseService以及权限管理，参数校验器，日志处理等。

> mayfly-sys
后端系统主要模块，包含各功能模块对应的Service和Controller等

## 项目特点 

- ### 方法日志记录(记录方法出入参以及执行时间或异常)
日志记录采用AOP（mayfly.sys.aop.log.LogAspect）类进行拦截带有@MethodLog注解的所有方法或者带有@MethodLog类下的所有方法，进行出入参以及运行时间的记录，
也包含异常日志的记录，也可以设置按指定的日志级别打印日志.
使用方式大致如下：
```
/**
*如果有时候返回值太多，也可将@MethodLog注解中的result属性去掉（默认为false）
*也可以使用@NoNeedLogParam注解作用于参数上，让其不记录该参数值,可以用来去除一些系统参数，如HttpServletResponse等
*/
@MethodLog(value = "获取权限列表", time = true)
@GetMapping("/v1/permissions")
public Result list(PermissionForm condition, @Valid PageForm pageQuery){}


/**
*也可以用于类上,如下，在DEBUG级别下打印日志
*/
@MethodLog(level = MethodLog.LogLevel.DEBUG)
public class PermissionServiceImpl{}
```
打印结果如图：
![日志输出](https://images.gitee.com/uploads/images/2020/0311/104645_3955cb50_1240250.png "日志输出.png")


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

    /**
     * status只能是StatusEnum中(声明如下)对应的枚举值value
     */
    @EnumValue(clazz = StatusEnum.class)
    private Integer status;   
}

/**
 * 状态枚举类
 *
 * @author meilin.huang
 * @version 1.0
 * @date 2019-12-25 10:28 上午
 */
public enum StatusEnum implements NameValueEnum<Integer> {
    /**
     * 启用状态
     */
    ENABLE(1, "启用"),

    /**
     * 禁用状态
     */
    DISABLE(0, "禁用");

    private Integer value;
    private String name;
    EnableDisableEnum(Integer value, String name) {
        this.value = value;
        this.name = name;
    }


    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Integer getValue() {
        return this.value;
    }
}
```
更多可使用的参数字段校验规则，详见：mayfly.core.validation.annotation包

Controller方法参数校验用法：
![参数前加@Valid注解](https://images.gitee.com/uploads/images/2019/0329/131943_438c4935_1240250.png "屏幕截图.png")


- ### 自定义权限校验注解(控制后端接口以及前端列表按钮权限)
系统使用自定义权限注解@mayfly.core.permission.Permission来控制用户的操作权限（即后端接口调用的权限，可实时禁用以及删除权限），和通过权限code控制前端页面的列表权限以及按钮权限（显示与否，以及是否为禁用状态。前端通过VUE的自定义指令v-permission进行按钮的控制，详情可见前端模块：mayfly-account-front ）。
```
/**
*每个方法都有丢应的权限code(用来进行权限校验以及前端页面按钮控制)  
*如果该注解只作用于类上，则类中所有方法对应的权限code为类权限code
*/
@mayfly.core.permission.Permission(code = "permission:")
@RestController
@RequestMapping("/sys")
public class PermissionController 
```
具体如何拦截以及实时启用禁用可见对应拦截器:mayfly.sys.interceptor.PermissionInterceptor

- ### 前端枚举值统一管理维护
具体细节可见前端模块：mayfly-account-front 或者博客：https://www.jianshu.com/p/75516ec4f366

## 系统部分页面

1.菜单&权限管理页
![菜单&权限管理页](https://images.gitee.com/uploads/images/2020/0311/104924_bb08cd6d_1240250.png "菜单&权限管理页.png")

2.角色分配菜单&权限页
![角色分配菜单&权限页](https://images.gitee.com/uploads/images/2020/0311/104949_3c61e72f_1240250.png "屏幕截图.png")

3.操作日志
![操作日志](https://images.gitee.com/uploads/images/2020/0311/105025_2d59ed81_1240250.png "操作日志.png")

4.机器文件管理
![机器文件管理](https://images.gitee.com/uploads/images/2020/0311/110243_fdd2ff21_1240250.png "机器文件管理.png")
![机器文件管理](https://images.gitee.com/uploads/images/2020/0311/105100_a1b58e35_1240250.png "机器文件管理.png")

5.redis基本操作
![redis基本操作](https://images.gitee.com/uploads/images/2020/0311/110550_b18f0b76_1240250.png "redis基本操作.png")
![redis基本操作](https://images.gitee.com/uploads/images/2020/0311/105230_7281f9e2_1240250.png "redis基本操作.png")
