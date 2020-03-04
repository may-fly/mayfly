package mayfly.core.base.model;

import mayfly.core.permission.LoginAccount;

import java.time.LocalDateTime;

/**
 * @author meilin.huang
 * @date 2020-02-21 7:07 下午
 */
public class BaseDO {

    /**
     * 创建账号字段名
     */
    public static final String CREATE_ACCOUNT = "createAccount";
    /**
     * 创建账号id字段名
     */
    public static final String CREATE_ACCOUNT_ID = "createAccountId";
    /**
     * 最后更新时间字段名
     */
    public static final String UPDATE_TIME = "updateTime";
    /**
     * 最后更新账号名字段名
     */
    public static final String UPDATE_ACCOUNT = "updateAccount";
    /**
     * 最后更新账号id字段名
     */
    public static final String UPDATE_ACCOUNT_ID = "updateAccountId";

    /**
     * id
     */
    private Integer id;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 最后更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 创建账号id
     */
    private Integer createAccountId;

    /**
     * 创建账号名
     */
    private String createAccount;

    /**
     * 最后更新账号id
     */
    private Integer updateAccountId;

    /**
     * 最后更新账号名
     */
    private String updateAccount;

    /**
     * 自动设置基本信息，默认id为空时设置新建需要的基本信息
     */
    public void autoSetBaseInfo() {
        this.autoSetBaseInfo(this.id == null);
    }

    /**
     * 自动设置基本信息
     */
    public void autoSetBaseInfo(boolean isCreate) {
        LocalDateTime now = LocalDateTime.now();
        if (isCreate) {
            this.createTime = now;
        }
        this.updateTime = now;

        // 设置操作账号相关信息
        LoginAccount<Integer> loginAccount = LoginAccount.get();
        if (loginAccount == null) {
            return;
        }
        String account = loginAccount.getUsername();
        Integer accountId = loginAccount.getId();
        // 赋值
        this.updateAccount = account;
        this.updateAccountId = accountId;
        if (isCreate) {
            this.createAccount = account;
            this.createAccountId = accountId;
        }
    }

    //-------  getter setter ------- //

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getCreateAccountId() {
        return createAccountId;
    }

    public void setCreateAccountId(Integer createAccountId) {
        this.createAccountId = createAccountId;
    }

    public String getCreateAccount() {
        return createAccount;
    }

    public void setCreateAccount(String createAccount) {
        this.createAccount = createAccount;
    }

    public Integer getUpdateAccountId() {
        return updateAccountId;
    }

    public void setUpdateAccountId(Integer updateAccountId) {
        this.updateAccountId = updateAccountId;
    }

    public String getUpdateAccount() {
        return updateAccount;
    }

    public void setUpdateAccount(String updateAccount) {
        this.updateAccount = updateAccount;
    }
}
