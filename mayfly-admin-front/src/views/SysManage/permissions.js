import Permission from "../../common/Permission"
import enums from '../../common/enums'


const method = enums.requestMethod
/**
 * 权限模块相关权限code
 */
const code = {
  // 菜单列表页面相关
  menu: {
    list: Permission.code("resource:list").uri("/sys/resources").method(method.GET),
    save: Permission.code("resource:save").uri("/sys/resources").method(method.POST),
    update: Permission.code("resource:update").uri("/sys/resources/{id}").method(method.PUT),
    del: Permission.code("resource:delete").uri("/sys/resources/{id}").method(method.DELETE),
    changeStatus: Permission.code("resource:changeStatus").uri("/sys/resources/{id}/{status}").method(method.PUT)
  },

  // 角色页面相关
  role: {
    list: Permission.code("role:list").uri("/sys/roles").method(method.GET),
    save: Permission.code("role:save").uri("/sys/roles").method(method.POST),
    update: Permission.code("role:update").uri("/sys/roles/{id}").method(method.PUT),
    del: Permission.code("role:delete").uri("/sys/roles/{id}").method(method.DELETE),
    // 获取指定角色拥有的资源
    rolePermissions: Permission.code("role:rolePermissions").uri("/sys/roles/{id}/resources").method(method.GET),
    saveResources: Permission.code("role:saveResources").uri("/sys/roles/{id}/resources").method(method.POST)
  },

  account: {
    list: Permission.code("account:list").uri("/sys/accounts").method(method.GET),
    save: Permission.code("account:save").uri("/sys/accounts").method(method.POST),
    del: Permission.code("account:delete").uri("/sys/accounts/{id}").method(method.DELETE),
    changeStatus: Permission.code("account:changeStatus").uri("/sys/accounts/{id}/{status}").method(method.PUT),
    roles: Permission.code("account:roles").uri("/sys/accounts/{id}/roles").method(method.GET),
    saveRoles: Permission.code("account:roles").uri("/sys/accounts/{id}/roles").method(method.POST)
  }
}


export default code;
