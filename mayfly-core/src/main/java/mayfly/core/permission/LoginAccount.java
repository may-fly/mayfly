package mayfly.core.permission;

import mayfly.core.util.ArrayUtils;
import mayfly.core.util.StringUtils;

import java.io.Serializable;
import java.util.Collection;

/**
 * @author meilin.huang
 * @date 2020-03-03 8:40 上午
 */
public class LoginAccount<I> implements Serializable {

    private static final long serialVersionUID = -8397728352145291302L;

    public static final String CODE_STATUS_SEPARATOR = ":";

    /**
     * 线程上下文，用于保存到登录账号信息
     */
    private static final ThreadLocal<LoginAccount<?>> CONTEXT = new ThreadLocal<>();


    /**
     * 账号id
     */
    private I id;

    /**
     * 账号用户名
     */
    private String username;

    /**
     * 账号权限列表，用','分割
     */
    private String[] permissions;


    /**
     * 判断该账号是否拥有该权限码
     *
     * @param permissionCode 权限code
     * @return true：有
     */
    public boolean hasPermission(String permissionCode) throws PermissionDisabledException {
        if (ArrayUtils.isEmpty(permissions)) {
            return true;
        }
        if (StringUtils.isEmpty(permissionCode)) {
            return false;
        }
        String[] codes = this.permissions;
        if (ArrayUtils.contains(codes, permissionCode)) {
            return true;
        }
        if (ArrayUtils.contains(codes, getDisablePermissionCode(permissionCode))) {
            throw new PermissionDisabledException();
        }
        return false;
    }

    /**
     * 设置登录账号上下文
     *
     * @param loginAccount  login account
     */
    public static void set(LoginAccount<?> loginAccount) {
        CONTEXT.set(loginAccount);
    }

    /**
     * 获取该线程上下文的登录账号
     */
    @SuppressWarnings("unchecked")
    public static <I> LoginAccount<I> get() {
        return (LoginAccount<I>) CONTEXT.get();
    }

    /**
     * 移除登录账号
     */
    public static void remove() {
        CONTEXT.remove();
    }

    /**
     * 获取权限code对应的禁用code,即code + ":" + 0
     *
     * @param code code
     * @return 禁用code
     */
    public static String getDisablePermissionCode(String code) {
        return code + CODE_STATUS_SEPARATOR + 0;
    }



    //---------  getter setter  --------//

    public I getId() {
        return id;
    }

    public LoginAccount<I> setId(I id) {
        this.id = id;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public LoginAccount<I> setUsername(String username) {
        this.username = username;
        return this;
    }

    public String[] getPermissions() {
        return permissions;
    }

    public LoginAccount<I> setPermissions(String[] permissions) {
        this.permissions = permissions;
        return this;
    }

    public LoginAccount<I> setPermissions(Collection<String> permissions) {
        this.permissions = permissions.toArray(new String[0]);
        return this;
    }
}
