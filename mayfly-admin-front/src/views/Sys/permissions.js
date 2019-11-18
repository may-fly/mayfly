import Permission from "~/common/Permission"
import enums from '~/common/enums'


const method = enums.requestMethod
/**
 * 权限code
 */
const code = {
  machine: {
    // 获取权限列表
    list: Permission.code("machines:list").uri("/sys/machines").method(method.GET),
    // 保存按钮
    save: Permission.code("machine:save").uri("/sys/machines").method(method.POST),
    // 删除机器
    del: Permission.code("machine:delete").uri("/sys/machines/{id}").method(method.DELETE),
    // 服务管理按钮
    serviceManage: Permission.code("machine:serviceManage"),
    // 获取配置文件列表
    files: Permission.code("machine:files").uri("/sys/machines/{id}/files").method(method.GET),
    // 设置配置文件内容
    updateFileContent: Permission.code("machine:updateFileContent").uri("/sys/machines/files/{id}").method(method.PUT),
    // 添加文件or目录
    addFile: Permission.code("machine:addFile").uri("/sys/machines/{machineId}/files").method(method.POST),
    // 删除配置的文件or目录
    delFile: Permission.code("machine:delFile").uri("/sys/machines/files/{id}").method(method.DELETE),
  },
  
  // redis相关
  redis: {
    // 获取权限列表
    list: Permission.code("redis:list").uri("/sys/redis").method(method.GET),
    info: Permission.code("redis:info").uri("/sys/redis/{id}/info").method(method.GET),
    // 删除权限
    del: Permission.code("redis:delete").uri("/sys/v1/permissions/{id}").method(method.DELETE),
    // 保存按钮
    save: Permission.code("permission:save").uri("/sys/v1/permissions").method(method.POST),
    update: Permission.code('redis:key:update').uri("/sys/v1/permissions/{id}").method(method.PUT)
  },
  redisKey: {
    scan: Permission.code("redis:key:scan").uri("/sys/redis/{cluster}/{id}/scan").method(method.GET),
    value: Permission.code("redis:key:value").uri("/sys/redis/{cluster}/{id}/scan").method(method.GET),
    update: Permission.code("redis:key:update").uri("/sys/redis/{cluster}/{id}").method(method.PUT),
    del: Permission.code("redis:key:delete").uri("/sys/redis/{cluster}/{id}").method(method.DELETE)
  }
}


export default code;
