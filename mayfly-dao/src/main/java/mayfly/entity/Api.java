package mayfly.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Description: api
 * @author: hml
 * @date: 2018/6/25 下午5:24
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Api implements Serializable {
    private Integer id;

    private String description;

    private Byte method;

    private String uriPattern;

    private String code;

    private Integer groupId;

    private Byte status;

    public LocalDateTime createTime;

    public LocalDateTime updateTime;
}
