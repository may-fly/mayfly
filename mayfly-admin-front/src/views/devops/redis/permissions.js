import Permission from '~/common/Permission'

/**
 * 权限code
 */
const permissions = {
  // redis相关
  redis: {
    redis: Permission.code("redis")
  },
  redisKey: {
    redisKey: Permission.code("redis:key"),
    del: Permission.code("redis:key:delete")
  }
}


export default permissions;
