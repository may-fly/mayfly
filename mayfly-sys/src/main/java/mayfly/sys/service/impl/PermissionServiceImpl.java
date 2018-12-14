package mayfly.sys.service.impl;

import mayfly.common.log.MethodLog;
import mayfly.common.utils.PlaceholderResolver;
import mayfly.common.utils.UUIDUtils;
import mayfly.common.web.RequestMethod;
import mayfly.common.web.UriPattern;
import mayfly.entity.Api;
import mayfly.entity.Menu;
import mayfly.sys.common.cache.UserCacheKey;
import mayfly.sys.service.ApiService;
import mayfly.sys.service.MenuService;
import mayfly.sys.service.PermissionService;
import mayfly.sys.web.vo.LoginSuccessVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 权限服务实现类
 * @author: hml
 * @date: 2018/6/26 上午9:49
 */
@Service
public class PermissionServiceImpl implements PermissionService {
    /**
     * 占位符解析器
     */
    private static PlaceholderResolver resolver = PlaceholderResolver.getDefaultResolver();

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private MenuService menuService;
    @Autowired
    private ApiService apiService;

    @MethodLog(value = "保存id以及权限列表", time = true)
    @Override
    public LoginSuccessVO saveIdAndPermission(Integer id) {
        String token = UUIDUtils.generateUUID();
        List<Menu> menus = menuService.getByUserId(id);
        List<Api> apis = apiService.listByUserId(id);
        //保存用户对应的api权限，用来后端校验uri
        List<UriPattern> uis = apis.stream().map(a -> new UriPattern(RequestMethod.getByType(a.getMethod()), a.getUriPattern()))
                .collect(Collectors.toList());
        //缓存用户id以及用户权限列表
        redisTemplate.opsForValue().set(resolver.resolveByObject(UserCacheKey.USER_ID_KEY, token), id, UserCacheKey.EXPIRE_TIME, TimeUnit.HOURS);
        redisTemplate.opsForValue().set(resolver.resolveByObject(UserCacheKey.USER_PERMISSION_KEY, token), uis,UserCacheKey.EXPIRE_TIME, TimeUnit.HOURS);
        return LoginSuccessVO.builder().token(token).menus(menus).apis(apis).build();
    }

    @Override
    public Integer getIdByToken(String token) {
        return (Integer)redisTemplate.opsForValue().get(resolver.resolveByObject(UserCacheKey.USER_ID_KEY, token));
    }

    @Override
    public List<UriPattern> getUriPermissionByToken(String token) {
        return (List<UriPattern>)redisTemplate.opsForValue().get(resolver.resolveByObject(UserCacheKey.USER_PERMISSION_KEY, token));
    }


}
