package mayfly.core.permission.checker;

import mayfly.core.permission.registry.DefaultUserPermissionCodeRegistry;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-03-28 3:32 PM
 */
public class DefaultUserPermissionChecker<I> implements UserPermissionChecker<I> {
    @Override
    public boolean has(I userId, String permissionCode) {
        return DefaultUserPermissionCodeRegistry.getInstance().has(userId, permissionCode);
    }
}
