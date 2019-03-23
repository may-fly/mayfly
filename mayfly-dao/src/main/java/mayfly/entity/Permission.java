package mayfly.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Description: permission
 * @author: hml
 * @date: 2018/6/25 下午5:24
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Permission implements Serializable {
    private Integer id;

    private String description;

    private String code;

    private String uriPattern;

    private Integer method;

    private Integer groupId;

    private Integer status;

    public LocalDateTime createTime;

    public LocalDateTime updateTime;
}
