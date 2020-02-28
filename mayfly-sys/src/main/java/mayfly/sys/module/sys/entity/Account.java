package mayfly.sys.module.sys.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import mayfly.sys.common.base.model.BaseEntity;

import java.io.Serializable;

@Accessors(chain = true)
@Getter
@Setter
public class Account extends BaseEntity implements Serializable {

    private String username;

    private String password;

    private Integer status;
}
