package mayfly.sys.module.sys.service.impl;

import mayfly.core.util.CollectionUtils;
import mayfly.core.permission.registry.PermissionCacheHandler;
import mayfly.core.permission.registry.UserPermissionCodeRegistry;
import mayfly.core.util.BracePlaceholder;
import mayfly.core.util.TreeUtils;
import mayfly.core.util.UUIDUtils;
import mayfly.sys.common.enums.EnableDisableEnum;
import mayfly.sys.module.sys.entity.Account;
import mayfly.sys.common.cache.UserCacheKey;
import mayfly.sys.module.sys.enums.ResourceTypeEnum;
import mayfly.sys.common.utils.BeanUtils;
import mayfly.sys.module.sys.service.PermissionService;
import mayfly.sys.module.sys.service.ResourceService;
import mayfly.sys.module.sys.controller.vo.AccountVO;
import mayfly.sys.module.sys.controller.vo.LoginSuccessVO;
import mayfly.sys.module.sys.controller.vo.ResourceListVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
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
public class PermissionServiceImpl implements PermissionService, UserPermissionCodeRegistry<Integer> {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private ResourceService resourceService;

    /**
     * 权限缓存处理器
     */
    private PermissionCacheHandler<Integer> permissionCacheHandler = PermissionCacheHandler.of(this);


    @Override
    public LoginSuccessVO saveIdAndPermission(Account account) {
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
                .map(p -> p.getStatus().equals(EnableDisableEnum.DISABLE.getValue()) ? PermissionCacheHandler.getDisablePermissionCode(p.getCode()) : p.getCode())
                .collect(Collectors.toList());
        // 缓存用户id
        redisTemplate.opsForValue().set(BracePlaceholder.resolveByObject(UserCacheKey.USER_ID_KEY, token), id, UserCacheKey.EXPIRE_TIME, TimeUnit.MINUTES);
        // 保存用户权限code
        if (!CollectionUtils.isEmpty(permissionCodes)) {
            permissionCacheHandler.savePermission(id, permissionCodes, UserCacheKey.EXPIRE_TIME, TimeUnit.MINUTES);
        }
        return LoginSuccessVO.builder().admin(BeanUtils.copyProperties(account, AccountVO.class))
                .token(token).resources(resources).build();
    }

    @Override
    public Integer getIdByToken(String token) {
        return (Integer) redisTemplate.opsForValue().get(BracePlaceholder.resolveByObject(UserCacheKey.USER_ID_KEY, token));
    }


    //------------------------------------------------------------
    //  UserPermissionCodeRegistry  接口实现类
    //------------------------------------------------------------

    @SuppressWarnings("unchecked")
    @Override
    public void save(Integer userId, Collection<String> permissionCodes, long time, TimeUnit timeUnit) {
        // 给权限code key添加用户id
        String permissionKey = BracePlaceholder.resolveByObject(UserCacheKey.USER_PERMISSION_KEY, userId);
        redisTemplate.delete(permissionKey);
        redisTemplate.boundSetOps(permissionKey).add(permissionCodes.toArray());
        redisTemplate.boundSetOps(permissionKey).expire(time, timeUnit);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void delete(Integer userId) {
        redisTemplate.delete(BracePlaceholder.resolveByObject(UserCacheKey.USER_PERMISSION_KEY, userId));
    }

    @SuppressWarnings("all")
    @Override
    public boolean has(Integer userId, String permissionCode) {
        return redisTemplate.opsForSet().isMember(BracePlaceholder.resolveByObject(UserCacheKey.USER_PERMISSION_KEY, userId), permissionCode);
    }
}
