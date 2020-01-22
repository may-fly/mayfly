import Permission from '~/common/Permission'

/**
 * 权限code
 */
const permissions = {
  // redis相关
  redis: {
    // 获取权限列表
    list: Permission.url("/devops/redis").method('get').code("redis:list"),
    info: Permission.url("/devops/redis/{id}/info").method('get').code("redis:info"),
    // 删除权限
    del: Permission.url("/devops/redis/{id}").method('delete').code("redis:delete"),
    // 保存按钮
    save: Permission.url("/devops/redis").method('post').code("permission:save"),
    update: Permission.url("/devops/redis/{id}").method('put').code('redis:key:update')
  },
  redisKey: {
    scan: Permission.url("/devops/redis/{cluster}/{id}/scan").method('get').code("redis:key:scan"),
    value: Permission.url("/devops/redis/{cluster}/{id}/value").method('get').code("redis:key:value"),
    update: Permission.url("/devops/redis/{cluster}/{id}").method('put').code("redis:key:update"),
    del: Permission.url("/devops/redis/{cluster}/{id}").method('delete').code("redis:key:delete")
  }
}


export default permissions;
