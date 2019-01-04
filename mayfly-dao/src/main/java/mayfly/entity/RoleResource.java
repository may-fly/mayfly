package mayfly.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2018-12-07 4:14 PM
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleResource {

    private Integer id;

    private Integer roleId;

    private Integer resourceId;

    /**
     * 资源类型，1：菜单；2：权限
     */
    private Byte type;

    private LocalDateTime createTime;

    /**
     * 资源类型枚举
     */
    public enum TypeEnum{
        /**
         * 按钮类型
         */
        MENU(Byte.valueOf("1"), "菜单"),
        /**
         * 权限类型
         */
        PERMISSION(Byte.valueOf("2"), "权限");

        Byte type;
        String description;

        TypeEnum(Byte type, String description) {
            this.type = type;
            this.description = description;
        }

        public Byte type() {
            return this.type;
        }
    }
}
