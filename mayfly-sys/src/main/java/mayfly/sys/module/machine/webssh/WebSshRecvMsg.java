package mayfly.sys.module.machine.webssh;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author meilin.huang
 * @date 2021-01-06 4:28 下午
 */
@Getter
@Setter
@ToString
public class WebSshRecvMsg {

    public static final Integer CMD = 1;

    public static final Integer RESIZE = 2;

    private String cmd;

    private Integer type;

    private Long machineId;

    private Integer cols;

    private Integer rows;
}
