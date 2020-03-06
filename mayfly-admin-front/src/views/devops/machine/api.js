import Api from '~/common/Api';

export const machineApi = {
    // 获取权限列表
    list: Api.url("/devops/machines").method('get'),
    // 保存按钮
    save: Api.url("/devops/machines").method('post'),
    update: Api.url("/devops/machines/{id}").method('put'),
    // 删除机器
    del: Api.url("/devops/machines/{id}").method('delete'),
    // 获取配置文件列表
    files: Api.url("/devops/machines/{id}/files").method('get'),
    lsFile: Api.url("/devops/machines/files/{fileId}/ls").method('get'),
    rmFile: Api.url("/devops/machines/files/{fileId}/rm").method('delete'),
    uploadFile: Api.url("/devops/machines/files/upload"),
    fileContent: Api.url("/devops/machines/files/{fileId}/cat").method('get'),
    // 修改文件内容
    updateFileContent: Api.url("/devops/machines/files/{id}").method('put'),
    // 添加文件or目录
    addConf: Api.url("/devops/machines/{machineId}/files").method('post'),
    // 删除配置的文件or目录
    delConf: Api.url("/devops/machines/files/{id}").method('delete'),
}