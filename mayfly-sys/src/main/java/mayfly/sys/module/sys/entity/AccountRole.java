package mayfly.sys.module.sys.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 *  权限
 *
 * @author  hml
 * @date 2018/6/27 下午2:35
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountRole {

    private Integer id;

    private Integer accountId;

    private Integer roleId;

    private LocalDateTime createTime;
}
