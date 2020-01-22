package mayfly.core.permission.checker;

import mayfly.core.permission.registry.DefaultUserPermissionRegistry;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-03-28 3:32 PM
 */
public class DefaultUserPermissionChecker<I> implements UserPermissionChecker<I> {
    @Override
    public boolean has(I userId, String permissionCode) {
        return DefaultUserPermissionRegistry.getInstance().has(userId, permissionCode);
    }

    @Override
    public I getUserIdByToken(String token) {
        throw new UnsupportedOperationException();
    }
}
