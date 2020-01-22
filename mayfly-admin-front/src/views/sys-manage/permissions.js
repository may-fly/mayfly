import Permission from "../../common/Permission";

/**
 * 权限模块相关权限code
 */
const code = {
  // 菜单列表页面相关
  menu: {
    list: Permission.url("/sys/resources").method('get').code("resource:list"),
    detail: Permission.url("/sys/resources/{id}").method('get').code("resource:detail"),
    save: Permission.url("/sys/resources").method('post').code("resource:save"),
    update: Permission.url("/sys/resources/{id}").method('put').code("resource:update"),
    del: Permission.url("/sys/resources/{id}").method('delete').code("resource:delete"),
    changeStatus: Permission.url("/sys/resources/{id}/{status}").method('put').code("resource:changeStatus")
  },

  // 角色页面相关
  role: {
    list: Permission.url("/sys/roles").method('get').code("role:list"),
    save: Permission.url("/sys/roles").method('post').code("role:save"),
    update: Permission.url("/sys/roles/{id}").method('put').code("role:update"),
    del: Permission.url("/sys/roles/{id}").method('delete').code("role:delete"),
    // 获取指定角色拥有的资源
    rolePermissions: Permission.url("/sys/roles/{id}/resources").method('get').code("role:rolePermissions"),
    saveResources: Permission.url("/sys/roles/{id}/resources").method('post').code("role:saveResources")
  },

  account: {
    list: Permission.url("/sys/accounts").method('get').code("account:list"),
    save: Permission.url("/sys/accounts").method('post').code("account:save"),
    del: Permission.url("/sys/accounts/{id}").method('delete').code("account:delete"),
    changeStatus: Permission.url("/sys/accounts/{id}/{status}").method('put').code("account:changeStatus"),
    roles: Permission.url("/sys/accounts/{id}/roles").method('get').code("account:roles"),
    saveRoles: Permission.url("/sys/accounts/{id}/roles").method('post').code("account:roles")
  }
}


export default code;
