package mayfly.sys.module.sys.controller.vo;

import lombok.Data;

import java.util.List;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-07-27 10:52
 */
@Data
public class ResourceDetailVO {
    private List<ResourceDetailVO> children;
}
