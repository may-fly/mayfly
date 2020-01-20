package mayfly.sys.devops.module.machine.enums;

import mayfly.core.util.enums.ValueEnum;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-11-15 3:24 下午
 */
public enum MachineFileTypeEnum implements ValueEnum<Integer> {
    /**
     * 文件
     */
    FILE(2),

    /**
     * 目录
     */
    DIRECTORY(1);

    private Integer type;

    private MachineFileTypeEnum(Integer type) {
        this.type = type;
    }

    @Override
    public Integer getValue() {
        return this.type;
    }
}
