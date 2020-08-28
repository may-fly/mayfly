package mayfly.sys.config;

import lombok.Getter;
import lombok.Setter;
import mayfly.core.mail.MailAccount;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author meilin.huang
 * @date 2020-07-23 5:05 下午
 */
@Component
@ConfigurationProperties(prefix = "mail")
@PropertySource("classpath:mail.properties")
@Getter
@Setter
public class MailProperties {

    private String host;

    private Integer port;

    private String user;

    private String pass;

    private String tos;

    private MailAccount mailAccount;

    public MailAccount mailAccount() {
        return Optional.ofNullable(mailAccount).orElseGet(() -> MailAccount.builder().host(host).user(user).pass(pass).build());
    }
}
