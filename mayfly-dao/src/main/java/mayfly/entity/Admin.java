package mayfly.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Admin implements Serializable {

    private Integer id;

    private String username;

    private String password;

    private Integer status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
