package mayfly.sys.web.machine.vo;

import lombok.Data;

import java.util.Date;

/**
 * ls命令返回的结果对象
 *
 * @author meilin.huang
 * @version 1.0
 * @date 2019-11-11 10:39 上午
 */
@Data
public class LsVO {

    private String path;

    private String name;

    private Character type;

    private Long size;

    private Date updateTime;
}
