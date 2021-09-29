# mayfly

### 介绍
mayfly前后端分离的后台系统(包含按钮级别的权限管理，以及权限禁用，触发按钮置灰禁用状态等)。机器管理[webssh, 文件管理，监控等]及Redis单机以及集群管理（已完成部分接口以及界面）

### 项目地址
github: <https://github.com/may-fly/mayfly>
gitee: <https://gitee.com/objs/mayfly>

### golang版
<https://gitee.com/objs/mayfly-go> （功能较为齐全，因golang支持交叉编译等特性比较适合开发该类系统 故重点开发）

### 系统环境及框架
- 前端：typescript，  vue3，  element-plus
- 后端：jdk8及以上（HttpUtils依赖jdk11），  SpringBoot，  Mybatis
- DB： mysql，  redis
- 部署：[Jenkins+Docker+Springboot持续集成部署](https://www.jianshu.com/p/1401e2fe4711)

### 模块介绍
> mayfly-web（前端系统）


> mayfly-core（后端核心模块）
- 常用的各种uitls
- BaseMapper（无需第三方插件包，详见博客：[Mybatis之通用Mapper（基于mybatis的Provider机制无需第三方插件包）](https://www.jianshu.com/p/5fcea00f439d)）
、BaseService、BaseDO、Result、PageResult
- 基于javax.validation自定义的枚举（`EnumValue`）、日期范围（`DateTimeRange DateRange`）等参数校验注解
- 权限检验相关
- 日志切面等

> mayfly-sys（后端系统模块）
包含各功能模块对应的Service和Controller等

### 演示环境
地址：<http://mayfly.1yue.net>
账号：test、test2、meilin.huang 密码：123456

### 项目特点 

- #### 方法日志记录(记录方法出入参以及执行时间或异常)
日志记录采用AOP（mayfly.core.log.LogAspect）类进行拦截带有@Log注解的所有方法或者带有@Log类下的所有方法，进行出入参以，对象实体字段变化记录及运行时间的记录，
也包含异常日志的记录，也可以设置按指定的日志级别打印日志.
使用方式大致如下：
```
/**
*如果有时候返回值太多，也可将@Log注解中的result属性去掉（默认为false）
*也可以使用@NoNeedLogParam注解作用于参数上，让其不记录该参数值,可以用来去除一些系统参数，如HttpServletResponse等
*/
@Log(value = "获取权限列表", time = true)
@GetMapping("/v1/permissions")
public Result list(PermissionQuery condition){}


/**
*也可以用于类上,如下，只有在DEBUG级别下才会打印返回值【在返回值为列表时可用，防止记录过多日志】
*/
@Log(resLevel = Log.Level.DEBUG)
public class PermissionServiceImpl{}

@Log(value = "'测试使用: ' + #res?.fieldName", el = true) // 使用spel表达式，res表示返回值
@Log(value = "'测试使用: ' + #tc?.fieldName", el = true)  // 使用spel表达式，tc表示请求参数名
@GetMapping("/v1/test")
public TestClass list(@RequestBody TestClass tc){}

/**
* 可对实体字段加该注解，用于字段变更时更精细化描述，如 type(类型): 1[类型1] -> 2[类型2]
*/
@LogChange(name = "类型", enumValue = "TypeEnum.class")
private Integer type;
```

打印结果如图：
正常日志：记录日志出入参，字段变更内容信息等
![日志](https://images.gitee.com/uploads/images/2021/0918/172725_b913b9c1_1240250.png "正常日志.png")

异常日志：业务异常只打印与本系统相关的堆栈信息，即只打印以本项目包开头的类调用堆栈信息
![异常日志](https://images.gitee.com/uploads/images/2021/0105/145526_e0a16cf0_1240250.png "异常日志.png")


- #### 自定义常用参数校验注解(如入参枚举值，时间范围校验等)

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
    @EnumValue(MethodEnum.class)
    @NotNull
    private Integer method;

    @NotBlank
    private String description;

    /**
     * status只能是StatusEnum中(声明如下)对应的枚举值value
     */
    @EnumValue(StatusEnum.class)
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


- #### 自定义权限校验注解(控制后端接口以及前端列表按钮权限)
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


- #### 基于断言对业务逻辑判断
系统中的业务逻辑判断都采用断言的方式进行判断，减少逻辑代码中大量的if代码。分布式系统可对不同业务模块定义不同错误码，方便问题溯源

```
    // 可全局设置系统不满足断言条件时的默认错误码
    BizAssert.setDefaultError(xxxxxx)

    public void update(ResourceDO resource) {
        ResourceDO old = getById(resource.getId());
        BizAssert.notNull(old, "资源不存在");
        BizAssert.equals(resource.getType(), old.getType(), "资源类型不可变更");
        // 禁止误传修改其父节点
        resource.setPid(null);

        // 如果是权限，还需校验权限码
        if (Objects.equals(old.getType(), ResourceTypeEnum.PERMISSION.getValue())) {
            // 权限类型需要校验code不能为空
            String code = resource.getCode();
            // 如果修改了权限code，则需要校验
            if (!Objects.equals(old.getCode(), code)) {
                checkPermissionCode(code);
            }
        }
        updateByIdSelective(resource);
    }
    
    /**
     *  检验权限code
     */
    private void checkPermissionCode(String code) {
        BizAssert.notEmpty(code, "权限code不能为空");
        BizAssert.state(!code.contains(","), "权限code不能包含','");
        BizAssert.equals(countByCondition(new ResourceDO().setCode(code)), 0L, "该权限code已存在");
    }

```

- #### 前端枚举值统一管理维护
具体细节可见前端模块：mayfly-front 或者博客：https://www.jianshu.com/p/75516ec4f366

- #### 前端断言方式校验参数并错误提示
具体细节可见前端模块：mayfly-front 或者博客：https://www.jianshu.com/p/e6fb858bfbf7


### 系统部分页面

#### 系统管理
菜单&权限管理页
![菜单&权限管理页](https://images.gitee.com/uploads/images/2021/0611/155833_662f2f18_1240250.png "屏幕截图.png")

角色分配菜单&权限页
![角色分配菜单&权限页](https://images.gitee.com/uploads/images/2021/0611/155919_c3aaa930_1240250.png "屏幕截图.png")

操作日志
![操作日志](https://images.gitee.com/uploads/images/2021/0929/162505_0f9f4770_1240250.png "屏幕截图.png")

#### 机器管理
机器ssh终端
![机器ssh终端](https://images.gitee.com/uploads/images/2021/0611/160010_4aac05c1_1240250.png "屏幕截图.png")

机器文件管理
![机器文件管理](https://images.gitee.com/uploads/images/2021/0611/160118_70c7ac58_1240250.png "屏幕截图.png")

文件树查看
![文件树查看](https://images.gitee.com/uploads/images/2021/0611/160141_15e3a4ae_1240250.png "屏幕截图.png")

文件内容查看与编辑
![文件内容查看](https://images.gitee.com/uploads/images/2021/0611/160222_61ad25a0_1240250.png "屏幕截图.png")

#### dbms
![数据查询](https://images.gitee.com/uploads/images/2021/0301/105401_4f56b45d_1240250.png "数据查询.png")

#### redis基本操作
![redis基本操作](https://images.gitee.com/uploads/images/2020/0311/110550_b18f0b76_1240250.png "redis基本操作.png")
![redis基本操作](https://images.gitee.com/uploads/images/2020/0311/105230_7281f9e2_1240250.png "redis基本操作.png")
