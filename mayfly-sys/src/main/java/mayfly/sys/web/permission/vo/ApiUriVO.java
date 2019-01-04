package mayfly.sys.web.permission.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2018-11-24 11:41 AM
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiUriVO {

    private Byte method;

    private String uriPattern;

    private byte status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
