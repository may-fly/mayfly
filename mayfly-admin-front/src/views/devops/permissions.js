import Permission from '~/common/Permission'
import enums from '~/common/enums'


const method = enums.requestMethod
/**
 * 权限code
 */
const code = {
  machine: {
    // 获取权限列表
    list: Permission.code("machines:list").uri("/devops/machines").method(method.GET),
    // 保存按钮
    save: Permission.code("machine:save").uri("/devops/machines").method(method.POST),
    // 删除机器
    del: Permission.code("machine:delete").uri("/devops/machines/{id}").method(method.DELETE),
    // 服务管理按钮
    serviceManage: Permission.code("machine:serviceManage"),
    // 获取配置文件列表
    files: Permission.code("machineFile:files").uri("/devops/machines/{id}/files").method(method.GET),
    lsFile: Permission.code("machineFile:ls").uri("/devops/machines/files/{fileId}/ls").method(method.GET),
    rmFile: Permission.code("machineFile:rm").uri("/devops/machines/files/{fileId}/rm").method(method.DELETE),
    uploadFile: Permission.code("machineFile:upload").uri("/devops/machines/files/upload"),
    fileContent: Permission.code("machineFile:cat").uri("/devops/machines/files/{fileId}/cat").method(method.GET),
    // 修改文件内容
    updateFileContent: Permission.code("machineFile:updateFileContent").uri("/devops/machines/files/{id}").method(method.PUT),
    // 添加文件or目录
    addConf: Permission.code("machineFile:addConf").uri("/devops/machines/{machineId}/files").method(method.POST),
    // 删除配置的文件or目录
    delConf: Permission.code("machineFile:delConf").uri("/devops/machines/files/{id}").method(method.DELETE),
  },

  // redis相关
  redis: {
    // 获取权限列表
    list: Permission.code("redis:list").uri("/devops/redis").method(method.GET),
    info: Permission.code("redis:info").uri("/devops/redis/{id}/info").method(method.GET),
    // 删除权限
    del: Permission.code("redis:delete").uri("/devops/redis/{id}").method(method.DELETE),
    // 保存按钮
    save: Permission.code("permission:save").uri("/devops/redis").method(method.POST),
    update: Permission.code('redis:key:update').uri("/devops/redis/{id}").method(method.PUT)
  },
  redisKey: {
    scan: Permission.code("redis:key:scan").uri("/devops/redis/{cluster}/{id}/scan").method(method.GET),
    value: Permission.code("redis:key:value").uri("/devops/redis/{cluster}/{id}/value").method(method.GET),
    update: Permission.code("redis:key:update").uri("/devops/redis/{cluster}/{id}").method(method.PUT),
    del: Permission.code("redis:key:delete").uri("/devops/redis/{cluster}/{id}").method(method.DELETE)
  }
}


export default code;
