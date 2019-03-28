package mayfly.common.permission.check;

import mayfly.common.permission.registry.DefaultUserPermissionCodeRegistry;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-03-28 3:32 PM
 */
public class DefaultUserPermissionCheck implements UserPermissionCheck {
    @Override
    public boolean has(Integer userId, String permissionCode) {
        return DefaultUserPermissionCodeRegistry.getInstance().has(userId, permissionCode);
    }
}
