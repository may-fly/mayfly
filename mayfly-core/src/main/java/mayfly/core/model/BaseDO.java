package mayfly.core.model;

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
    public static final String CREATOR = "creator";
    /**
     * 创建账号id字段名
     */
    public static final String CREATOR_ID = "creatorId";
    /**
     * 最后更新时间字段名
     */
    public static final String UPDATE_TIME = "updateTime";
    /**
     * 最后更新账号名字段名
     */
    public static final String MODIFIER = "modifier";
    /**
     * 最后更新账号id字段名
     */
    public static final String MODIFIER_ID = "modifierId";

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
    private Long creatorId;

    /**
     * 创建账号名
     */
    private String creator;

    /**
     * 最后更新账号id
     */
    private Long modifierId;

    /**
     * 最后更新账号名
     */
    private String modifier;

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
        autoSetBaseInfo(isCreate, LoginAccount.getFromContext());
    }

    /**
     * 自动设置基本信息
     *
     * @param isCreate     是否为创建操作
     * @param loginAccount 登录账号
     */
    public void autoSetBaseInfo(boolean isCreate, LoginAccount loginAccount) {
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
        this.modifier = account;
        this.modifierId = accountId;
        if (isCreate) {
            this.creator = account;
            this.creatorId = accountId;
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

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Long getModifierId() {
        return modifierId;
    }

    public void setModifierId(Long modifierId) {
        this.modifierId = modifierId;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    @Override
    public String toString() {
        return "BaseDO{" +
                "id=" + id +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", creatorId=" + creatorId +
                ", creator='" + creator + '\'' +
                ", modifierId=" + modifierId +
                ", modifier='" + modifier + '\'' +
                '}';
    }
}
