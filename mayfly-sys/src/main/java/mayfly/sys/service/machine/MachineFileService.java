package mayfly.sys.service.machine;

import mayfly.entity.MachineFile;
import mayfly.sys.service.base.BaseService;
import mayfly.sys.web.machine.form.MachineFileForm;
import mayfly.sys.web.machine.vo.LsVO;

import java.util.List;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-11-04 3:04 下午
 */
public interface MachineFileService extends BaseService<MachineFile> {

    /**
     * 获取指定机器上已保存的配置文件信息
     * @param machineId  机器id
     * @return  配置文件信息
     */
    List<MachineFile> listByMachineId(Integer machineId);

    /**
     * 获取配置文件内容
     * @param fileId  id
     * @param path   路径
     * @return          文件内容
     */
    String getFileContent(Integer fileId, String path);

    /**
     * 更新文件内容
     * @param confId   配置信息id
     * @param path     文件全路径
     * @param content  文件内容
     */
    void updateFileContent(Integer confId, String path, String content);

    /**
     * 新增配置文件
     * @param machineId  机器id
     * @param form       表单
     */
    MachineFile addFile(Integer machineId, MachineFileForm form);

    /**
     * 获取目录下的内容
     * @param fileId  目录id
     * @param path   路径
     * @return       内容
     */
    List<LsVO> ls(Integer fileId, String path);
}
