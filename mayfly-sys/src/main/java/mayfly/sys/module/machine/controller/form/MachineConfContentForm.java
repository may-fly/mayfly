package mayfly.sys.module.machine.controller.form;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-11-05 4:39 下午
 */
@Data
public class MachineConfContentForm {
    /**
     * 文件路径
     */
    @NotBlank
    private String path;

    @NotBlank
    private String content;
}
