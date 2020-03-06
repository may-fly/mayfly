import Permission from "../../common/Permission";

/**
 * 权限模块相关权限code
 */
const code = {
  // 菜单列表页面相关
  resource: {
    resource: Permission.code("resource"),
    save: Permission.code("resource:save"),
    update: Permission.code("resource:update"),
    del: Permission.code("resource:delete"),
  },

  // 角色页面相关
  role: {
    role: Permission.code("role"),
    del: Permission.code("role:delete"),
    saveResources: Permission.code("role:saveResources")
  },

  account: {
    account: Permission.code("account"),
    del: Permission.code("account:delete"),
    saveRoles: Permission.code("account:saveRoles")
  }
}


export default code;
