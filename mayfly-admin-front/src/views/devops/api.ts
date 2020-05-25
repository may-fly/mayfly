import Api from '@/common/Api';

export const machineApi = {
    // 获取权限列表
    list: Api.create("/devops/machines", 'get'),
    // 保存按钮
    save: Api.create("/devops/machines", 'post'),
    update: Api.create("/devops/machines/{id}", 'put'),
    // 删除机器
    del: Api.create("/devops/machines/{id}", 'delete'),
    // 获取配置文件列表
    files: Api.create("/devops/machines/{id}/files", 'get'),
    lsFile: Api.create("/devops/machines/files/{fileId}/ls", 'get'),
    rmFile: Api.create("/devops/machines/files/{fileId}/rm", 'delete'),
    uploadFile: Api.create("/devops/machines/files/upload", 'post'),
    fileContent: Api.create("/devops/machines/files/{fileId}/cat", 'get'),
    // 修改文件内容
    updateFileContent: Api.create("/devops/machines/files/{id}", 'put'),
    // 添加文件or目录
    addConf: Api.create("/devops/machines/{machineId}/files", 'post'),
    // 删除配置的文件or目录
    delConf: Api.create("/devops/machines/files/{id}", 'delete'),
}

export const redisApi = {
    // 获取权限列表
    list: Api.create("/devops/redis", 'get'),
    info: Api.create("/devops/redis/{id}/info", 'get'),
    // 删除权限
    del: Api.create("/devops/redis/{id}", 'delete'),
    // 保存按钮
    save: Api.create("/devops/redis", 'post'),
    update: Api.create("/devops/redis/{id}", 'put')
}

export const redisKeyApi = {
    scan: Api.create("/devops/redis/{cluster}/{id}/scan", 'get'),
    value: Api.create("/devops/redis/{cluster}/{id}/value", 'get'),
    update: Api.create("/devops/redis/{cluster}/{id}", 'put'),
    del: Api.create("/devops/redis/{cluster}/{id}", 'delete')
}