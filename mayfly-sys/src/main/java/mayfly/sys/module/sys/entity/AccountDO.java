package mayfly.sys.module.sys.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import mayfly.core.base.mapper.annotation.Table;
import mayfly.core.base.model.BaseDO;

import java.io.Serializable;

@Accessors(chain = true)
@Getter
@Setter
@Table("tb_account")
public class AccountDO extends BaseDO implements Serializable {

    private static final long serialVersionUID = -5172351936029693334L;

    private String username;

    private String password;

    private Integer status;
}
