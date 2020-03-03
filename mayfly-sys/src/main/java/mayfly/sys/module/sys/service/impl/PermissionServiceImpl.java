package mayfly.sys.module.sys.service.impl;

import mayfly.core.permission.LoginAccount;
import mayfly.core.permission.registry.LoginAccountRegistryHandler;
import mayfly.core.permission.registry.PermissionCheckHandler;
import mayfly.core.util.BracePlaceholder;
import mayfly.core.util.TreeUtils;
import mayfly.core.util.UUIDUtils;
import mayfly.core.util.bean.BeanUtils;
import mayfly.sys.common.cache.UserCacheKey;
import mayfly.sys.common.enums.EnableDisableEnum;
import mayfly.sys.module.sys.controller.vo.AccountVO;
import mayfly.sys.module.sys.controller.vo.LoginSuccessVO;
import mayfly.sys.module.sys.controller.vo.ResourceListVO;
import mayfly.sys.module.sys.entity.AccountDO;
import mayfly.sys.module.sys.enums.ResourceTypeEnum;
import mayfly.sys.module.sys.service.PermissionService;
import mayfly.sys.module.sys.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 权限服务实现类
 *
 * @author hml
 * @date 2018/6/26 上午9:49
 */
@Service
public class PermissionServiceImpl implements PermissionService  {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private ResourceService resourceService;

    /**
     * 权限缓存处理器
     */
    private LoginAccountRegistryHandler<Integer> loginAccountRegistryHandler = LoginAccountRegistryHandler.of(this);


    @Override
    public LoginSuccessVO saveIdAndPermission(AccountDO account) {
        Integer id = account.getId();
        String token = UUIDUtils.generateUUID();
        List<ResourceListVO> resources = resourceService.listByUserId(id);
        // 获取所有叶子节点
        List<ResourceListVO> permissions = new ArrayList<>();
        for (ResourceListVO root : resources) {
            TreeUtils.fillLeaf(root, permissions);
        }
        // 如果权限被禁用，将会在code后加上:0标志
        List<String> permissionCodes = permissions.stream().filter(p -> Objects.equals(p.getType(), ResourceTypeEnum.PERMISSION.getValue()))
                .map(p -> p.getStatus().equals(EnableDisableEnum.DISABLE.getValue()) ? PermissionCheckHandler.getDisablePermissionCode(p.getCode()) : p.getCode())
                .collect(Collectors.toList());
        // 保存登录账号信息
        LoginAccount<Integer> loginAccount = new LoginAccount<Integer>().setId(account.getId()).setUsername(account.getUsername())
                .setPermissions(permissionCodes);
        loginAccountRegistryHandler.saveLoginAccount(token, loginAccount, UserCacheKey.EXPIRE_TIME, TimeUnit.MINUTES);

        return LoginSuccessVO.builder().admin(BeanUtils.copyProperties(account, AccountVO.class))
                .token(token).resources(resources).build();
    }

    @Override
    public void removeToken(String token) {
        loginAccountRegistryHandler.removeLoginAccount(token);
    }


    //------------------------------------------------------------
    //  LoginAccountRegistry  接口实现类
    //------------------------------------------------------------

    @SuppressWarnings("all")
    @Override
    public void save(String token, LoginAccount loginAccount, long time, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(BracePlaceholder.resolveByObject(UserCacheKey.ACCOUNT_TOKEN_KEY, token), loginAccount, time, timeUnit);
    }

    @SuppressWarnings("all")
    @Override
    public LoginAccount<Integer> getLoginAccount(String token) {
        return (LoginAccount<Integer>) redisTemplate.opsForValue().get(BracePlaceholder.resolveByObject(UserCacheKey.ACCOUNT_TOKEN_KEY, token));
    }

    @SuppressWarnings("all")
    @Override
    public void delete(String token) {
        redisTemplate.delete(BracePlaceholder.resolveByObject(UserCacheKey.ACCOUNT_TOKEN_KEY, token));
    }
}
