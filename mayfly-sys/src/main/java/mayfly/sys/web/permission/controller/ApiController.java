package mayfly.sys.web.permission.controller;

import mayfly.common.exception.BusinessException;
import mayfly.common.log.MethodLog;
import mayfly.common.result.Result;
import mayfly.common.validation.annotation.Valid;
import mayfly.entity.Permission;
import mayfly.sys.common.BeanUtils;
import mayfly.sys.service.permission.ApiService;
import mayfly.sys.web.permission.form.PermissionForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * uri控制器
 * @author: hml
 * @date: 2018/6/14 下午2:04
 */
@RestController
@RequestMapping("/sys")
public class ApiController {

    @Autowired
    private ApiService apiService;

    @Autowired
    private RedisTemplate redisTemplate;

//    @MethodLog(value = "获取所有需要角色校验的uri", time = true, result = false)
////    @Cacheable("uris")
//    @GetMapping("/v1/apis")
//    public Result getAllUris(ApiQueryForm queryForm, @NoNeedLogParam HttpServletRequest request) {
//        if (!StringUtils.isEmpty(queryForm.getDescription())) {
//            return Result.success().withData(apiService.listByCondition(BeanUtils.copyProperties(queryForm, Permission.class)));
//        }
//
//        List<Permission> allPermissions = new ArrayList<>();
//        WebApplicationContext wc = (WebApplicationContext) request.getAttribute(DispatcherServlet.WEB_APPLICATION_CONTEXT_ATTRIBUTE);
//        RequestMappingHandlerMapping bean = wc.getBean(RequestMappingHandlerMapping.class);
//        Map<RequestMappingInfo, HandlerMethod> handlerMethods = bean.getHandlerMethods();
//        for (RequestMappingInfo rmi : handlerMethods.keySet()) {
//            HandlerMethod hm = handlerMethods.get(rmi);
//            String methodName = null;
//            Iterator<org.springframework.web.bind.annotation.RequestMethod> methods = rmi.getMethodsCondition().getMethods().iterator();
//            while (methods.hasNext()) {
//                methodName = methods.next().name();
//            }
//            String uriPattern = null;
//            Iterator uris = rmi.getPatternsCondition().getPatterns().iterator();
//            while (uris.hasNext()) {
//                uriPattern = uris.next().toString();
//            }
//            if (uriPattern.startsWith("/open/") || uriPattern.startsWith("/error")) {
//                continue;
//            }
//            Permission permission = new Permission();
//            //如有该方法有记录日志，则调取起日志功能描述
//            Optional.ofNullable(hm.getMethod().getAnnotation(MethodLog.class)).ifPresent(log -> permission.setDescription(log.value()));
//            permission.setMethod(RequestMethod.getByMethodName(methodName).getType());
//            permission.setUriPattern(uriPattern);
//            allPermissions.add(permission);
//        }
//
//        //查询入库的api列表
//        List<Permission> addPermissions = apiService.listAll("create_time DESC");
//        //跟所有需要拦截的的uri进行比较
//        CollectionUtils.CompareResult<Permission> compareResult = CollectionUtils.compare(allPermissions, addPermissions, (Permission a1, Permission a2) ->
//                a1.getMethod().equals(a2.getMethod()) && a1.getUriPattern().equals(a2.getUriPattern()) ? 0 : 1);
//
//        //获取还没有入库的api
//        List<Permission> needAdd = compareResult.getAddValue();
//        needAdd.forEach(n -> n.setStatus((byte)3));
//        //需要删除的api，即uri不存在了
//        List<Permission> del = compareResult.getDelValue();
//        del.forEach(d -> d.setStatus((byte)2));
//        List<Permission> unmodified = compareResult.getUnmodifiedValue();
//
//        List<Permission> uris = new ArrayList<>();
//        uris.addAll(del);
//        uris.addAll(needAdd);
//        uris.addAll(unmodified);
//        return Result.success().withData(uris);
//    }

    @MethodLog(value = "api入库")
    @PostMapping("/v1/apis")
    public Result saveApi(@RequestBody @Valid PermissionForm permissionForm) {
        try {
            return Result.success().withData(apiService.saveApi(BeanUtils.copyProperties(permissionForm, Permission.class)));
        } catch (BusinessException e) {
            return Result.paramError(e.getMessage());
        }
    }

    @MethodLog(value = "更新api状态")
    @PutMapping("/v1/apis/{id}")
    public Result updateApiStatus(@PathVariable Integer id, @RequestBody PermissionForm permissionForm) {
        Permission permission = new Permission();
        permission.setId(id);
        permission.setStatus(permissionForm.getStatus());
        permission.setUpdateTime(LocalDateTime.now());
        return  Result.success().withData(apiService.updateById(permission));
    }

    @MethodLog(value = "删除api")
    @DeleteMapping("/v1/apis/{id}")
    public Result deleteApi(@PathVariable Integer id) {
        return apiService.deleteApi(id) ? Result.success() : Result.serverError();
    }

//    public static void main(String[] args) {
//        ApiUriVO a1 = ApiUriVO.builder().method((byte)1).uriPattern("/v1/test1").build();
//        ApiUriVO a2 = ApiUriVO.builder().method((byte)1).uriPattern("/v1/test2").build();
//        ApiUriVO a3 = ApiUriVO.builder().method((byte)1).uriPattern("/v1/test3").build();
//        ApiUriVO a4 = ApiUriVO.builder().method((byte)1).uriPattern("/v1/test4").build();
//        ApiUriVO a5 = ApiUriVO.builder().method((byte)1).uriPattern("/v1/test5").build();
//        ApiUriVO a6 = ApiUriVO.builder().method((byte)1).uriPattern("/v1/test6").createTime(LocalDateTime.now()).build();
//
//        List<ApiUriVO> newValue = new ArrayList<>();
//        newValue.add(a2);newValue.add(a4);newValue.add(a3);newValue.add(a1);
//        List<ApiUriVO> oldValue = new ArrayList<>();
//        oldValue.add(a2); oldValue.add(a3); oldValue.add(a4);
//        oldValue.add(a5);
//        oldValue.add(a6);
//
//        CollectionUtils.CompareResult<ApiUriVO> result = CollectionUtils.compare(oldValue, newValue, (ApiUriVO o1, ApiUriVO o2) ->
//                o1.getMethod().equals(o2.getMethod()) && o1.getUriPattern().equals(o2.getUriPattern()) ? 0 : 1
//        );
//
//        for (ApiUriVO apiUriVO : result.getUnmodifiedValue()) {
//            System.out.println(apiUriVO.getUriPattern());
//        }
//    }
}
