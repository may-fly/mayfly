package mayfly.sys.module.sys.controller.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    private AccountVO admin;

    private List<ResourceListVO> menus;

    private List<ResourceListVO> codes;
}
