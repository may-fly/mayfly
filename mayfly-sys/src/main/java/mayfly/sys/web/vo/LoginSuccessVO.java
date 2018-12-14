package mayfly.sys.web.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mayfly.entity.Api;
import mayfly.entity.Menu;

import java.util.List;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2018-12-09 9:00 AM
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginSuccessVO {

    private String token;

    private List<Api> apis;

    private List<Menu> menus;
}
