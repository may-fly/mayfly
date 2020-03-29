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

    /**
     * 权限码分隔符
     */
    public static final String PERMISSION_CODE_SPLIT = ",";

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
    private String permissions;


    /**
     * 判断该账号是否拥有该权限码
     *
     * @param permissionCode 权限code
     * @return true：有
     */
    public boolean hasPermission(String permissionCode) {
        return !StringUtils.isEmpty(permissions)
                && ArrayUtils.contains(this.permissions.split(PERMISSION_CODE_SPLIT), permissionCode);
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

    public String getPermissions() {
        return permissions;
    }

    public LoginAccount<I> setPermissions(String permissions) {
        this.permissions = permissions;
        return this;
    }

    public LoginAccount<I> setPermissions(Collection<String> permissions) {
        this.permissions = String.join(PERMISSION_CODE_SPLIT, permissions);
        return this;
    }
}
