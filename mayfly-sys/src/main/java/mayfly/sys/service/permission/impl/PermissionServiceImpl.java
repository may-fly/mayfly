package mayfly.sys.service.permission.impl;

import mayfly.common.enums.BoolEnum;
import mayfly.common.permission.registry.PermissionCacheHandler;
import mayfly.common.permission.registry.SysPermissionCodeRegistry;
import mayfly.common.permission.registry.UserPermissionCodeRegistry;
import mayfly.common.util.BracePlaceholder;
import mayfly.common.util.TreeUtils;
import mayfly.common.util.UUIDUtils;
import mayfly.entity.Admin;
import mayfly.entity.Resource;
import mayfly.sys.common.cache.UserCacheKey;
import mayfly.sys.common.enums.ResourceTypeEnum;
import mayfly.sys.common.utils.BeanUtils;
import mayfly.sys.service.permission.PermissionService;
import mayfly.sys.service.permission.ResourceService;
import mayfly.sys.web.permission.vo.AdminVO;
import mayfly.sys.web.permission.vo.LoginSuccessVO;
import mayfly.sys.web.permission.vo.ResourceListVO;
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
 * @author hml
 * @date 2018/6/26 上午9:49
 */
@Service
public class PermissionServiceImpl implements PermissionService, UserPermissionCodeRegistry, SysPermissionCodeRegistry {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private ResourceService resourceService;

    /**
     * 权限缓存处理器
     */
    private PermissionCacheHandler permissionCacheHandler = PermissionCacheHandler.of(this, this);


    @Override
    public LoginSuccessVO saveIdAndPermission(Admin admin) {
        Integer id = admin.getId();
        String token = UUIDUtils.generateUUID();
        List<ResourceListVO> resources = resourceService.listByUserId(id);
        // 获取所有叶子节点
        List<ResourceListVO> permissions = new ArrayList<>();
        for (ResourceListVO root : resources) {
            TreeUtils.fillLeaf(root, permissions);
        }
        // 如果权限被禁用，将会在code后加上:0标志
        List<String> permissionCodes = permissions.stream().filter(p -> Objects.equals(p.getType(), ResourceTypeEnum.PERMISSION.getValue()))
                .map(p -> p.getStatus().equals(BoolEnum.FALSE.getValue()) ? PermissionCacheHandler.getDisablePermissionCode(p.getCode()) : p.getCode())
                .collect(Collectors.toList());
        // 缓存用户id
        redisTemplate.opsForValue().set(BracePlaceholder.resolveByObject(UserCacheKey.USER_ID_KEY, token), id, UserCacheKey.EXPIRE_TIME, TimeUnit.MINUTES);
        // 保存用户权限code
        permissionCacheHandler.savePermission(id, permissionCodes, UserCacheKey.EXPIRE_TIME, TimeUnit.MINUTES);
        return LoginSuccessVO.builder().admin(BeanUtils.copyProperties(admin, AdminVO.class))
                .token(token).resources(resources).build();
    }

    @Override
    public Integer getIdByToken(String token) {
        return (Integer)redisTemplate.opsForValue().get(BracePlaceholder.resolveByObject(UserCacheKey.USER_ID_KEY, token));
    }

    @Override
    public void reloadPermission() {
        permissionCacheHandler.reloadSysPermission();
    }

    @Override
    public void addPermission(String permissionCode) {
        permissionCacheHandler.addSysPermission(permissionCode);
    }

//    @Override
//    public void disablePermission(String code) {
//        permissionCacheHandler.disabledPermission(code);
//    }
//
//    @Override
//    public void enablePermission(String code) {
//        permissionCacheHandler.enablePermission(code);
//    }
//
//    @Override
//    public void delPermission(String permissionCode) {
//        permissionCacheHandler.deletePermission(permissionCode);
//    }




    //------------------------------------------------------------
    //  UserPermissionCodeRegistry  SysPermissionCodeRegistry  接口实现类
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

    @SuppressWarnings("unchecked")
    @Override
    public boolean has(Integer userId, String permissionCode) {
        return redisTemplate.opsForSet().isMember(BracePlaceholder.resolveByObject(UserCacheKey.USER_PERMISSION_KEY, userId), permissionCode);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void save() {
        List<Resource> permissions = resourceService.listByCondition(Resource.builder().type(ResourceTypeEnum.PERMISSION.getValue()).build());
        String[] permissionCodes = permissions.stream()
                .map(p -> p.getStatus().equals(BoolEnum.FALSE.getValue()) ? PermissionCacheHandler.getDisablePermissionCode(p.getCode()) : p.getCode())
                .toArray(String[]::new);
        redisTemplate.boundSetOps(UserCacheKey.ALL_PERMISSION_KEY).add(permissionCodes);
    }

    @Override
    public void reload() {
        // 删除所有权限，并重新保存
        redisTemplate.delete(UserCacheKey.ALL_PERMISSION_KEY);
        this.save();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void add(String code) {
        redisTemplate.boundSetOps(UserCacheKey.ALL_PERMISSION_KEY).add(code);
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean has(String permissionCode) {
        return redisTemplate.boundSetOps(UserCacheKey.ALL_PERMISSION_KEY).isMember(permissionCode);
    }

//    @SuppressWarnings("unchecked")
//    @Override
//    public void rename(String oldCode, String newCode) {
//        redisTemplate.boundSetOps(UserCacheKey.ALL_PERMISSION_KEY).remove(oldCode);
//        redisTemplate.boundSetOps(UserCacheKey.ALL_PERMISSION_KEY).add(newCode);
//    }
//
//    @SuppressWarnings("unchecked")
//    @Override
//    public void delete(String code) {
//        redisTemplate.boundSetOps(UserCacheKey.ALL_PERMISSION_KEY).remove(code);
//    }
}
