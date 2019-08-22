package mayfly.sys.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 *
 * @author meilin.huang
 * @date 2018/6/21 下午3:34
 */
@Component
@ConfigurationProperties(prefix = "druid")
@PropertySource("classpath:druid.properties")
public class DruidProperties {

    private String loginUsername;

    private String loginPassword;

    private String allow;

    private String deny;

    private boolean resetEnable;

    public String getLoginUsername() {
        return loginUsername;
    }

    public void setLoginUsername(String loginUsername) {
        this.loginUsername = loginUsername;
    }

    public String getLoginPassword() {
        return loginPassword;
    }

    public void setLoginPassword(String loginPassword) {
        this.loginPassword = loginPassword;
    }

    public String getAllow() {
        return allow;
    }

    public void setAllow(String allow) {
        this.allow = allow;
    }

    public String getDeny() {
        return deny;
    }

    public void setDeny(String deny) {
        this.deny = deny;
    }

    public boolean isResetEnable() {
        return resetEnable;
    }

    public void setResetEnable(boolean resetEnable) {
        this.resetEnable = resetEnable;
    }
}
