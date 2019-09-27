import Permission from "../../common/Permission"
import enums from '../../common/enums'


const method = enums.requestMethod
/**
 * 权限code
 */
const code = {
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
