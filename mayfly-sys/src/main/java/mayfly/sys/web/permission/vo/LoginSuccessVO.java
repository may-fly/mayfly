package mayfly.sys.web.permission.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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

    private AdminVO admin;

    private List<String> permissions;

    private List<Menu> menus;
}
