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
     * 唯一标识
     */
    private Long id;

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
    private Long createAccountId;

    /**
     * 创建账号名
     */
    private String createAccount;

    /**
     * 最后更新账号id
     */
    private Long updateAccountId;

    /**
     * 最后更新账号名
     */
    private String updateAccount;

    /**
     * 自动设置基本信息，默认id为空时设置新建需要的基本信息
     */
    public void autoSetBaseInfo() {
        this.autoSetBaseInfo(this.getId() == null);
    }

    /**
     * 自动设置基本信息
     *
     * @param isCreate 是否为创建操作
     */
    public void autoSetBaseInfo(boolean isCreate) {
        autoSetBaseInfo(isCreate, LoginAccount.get());
    }

    /**
     * 自动设置基本信息
     *
     * @param isCreate     是否为创建操作
     * @param loginAccount 登录账号
     */
    public void autoSetBaseInfo(boolean isCreate, LoginAccount<Long> loginAccount) {
        LocalDateTime now = LocalDateTime.now();
        if (isCreate) {
            this.createTime = now;
        }
        this.updateTime = now;

        // 设置操作账号相关信息
        if (loginAccount == null) {
            return;
        }
        String account = loginAccount.getUsername();
        Long accountId = loginAccount.getId();
        // 赋值
        this.updateAccount = account;
        this.updateAccountId = accountId;
        if (isCreate) {
            this.createAccount = account;
            this.createAccountId = accountId;
        }
    }

    //-------  getter setter ------- //

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public Long getCreateAccountId() {
        return createAccountId;
    }

    public void setCreateAccountId(Long createAccountId) {
        this.createAccountId = createAccountId;
    }

    public String getCreateAccount() {
        return createAccount;
    }

    public void setCreateAccount(String createAccount) {
        this.createAccount = createAccount;
    }

    public Long getUpdateAccountId() {
        return updateAccountId;
    }

    public void setUpdateAccountId(Long updateAccountId) {
        this.updateAccountId = updateAccountId;
    }

    public String getUpdateAccount() {
        return updateAccount;
    }

    public void setUpdateAccount(String updateAccount) {
        this.updateAccount = updateAccount;
    }

    @Override
    public String toString() {
        return "BaseDO{" +
                "id=" + id +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", createAccountId=" + createAccountId +
                ", createAccount='" + createAccount + '\'' +
                ", updateAccountId=" + updateAccountId +
                ", updateAccount='" + updateAccount + '\'' +
                '}';
    }
}
