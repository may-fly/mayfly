import Permission from "../../common/Permission"
import enums from '../../common/enums'


const method = enums.requestMethod
/**
 * 权限模块相关权限code
 */
const code = {
	// 权限列表页面相关
	permission: {
		// 获取权限列表
		list: Permission.code("permission:list").uri("/sys/v1/permissions").method(method.GET),
		// 启用禁用按钮
		changeStatus: Permission.code("permission:changeStatus").uri("/sys/v1/permissions/{id}/{status}").method(method.PUT),
		// 删除权限
		del: Permission.code("permission:delete").uri("/sys/v1/permissions/{id}").method(method.DELETE),
		// 保存按钮
		save: Permission.code("permission:save").uri("/sys/v1/permissions").method(method.POST),
    update: Permission.code('permission:update').uri("/sys/v1/permissions/{id}").method(method.PUT)
	},
  
  permissionGroup: {
    list: Permission.code("permission:group:list").uri("/sys/v1/permissionGroups").method(method.GET),
    all: Permission.code("permission:group:all").uri("/sys/v1/permissionGroups/all").method(method.GET)
  },
	
	// 菜单列表页面相关
	menu: {
		list: Permission.code("menu:list").uri("/sys/v1/resources").method(method.GET),
		save: Permission.code("menu:save").uri("/sys/v1/resources").method(method.POST),
		update: Permission.code("menu:update").uri("/sys/v1/resources/{id}").method(method.PUT),
		del: Permission.code("menu:delete").uri("/sys/v1/resources/{id}").method(method.DELETE),
    changeStatus: Permission.code("menu:changeStatus").uri("/sys/v1/resources/{id}/{status}").method(method.PUT)
	},
	
	// 角色页面相关
	role: {
		list: Permission.code("role:list").uri("/sys/v1/roles").method(method.GET),
		save: Permission.code("role:save").uri("/sys/v1/roles").method(method.POST),
		// 获取指定角色拥有的资源
		rolePermissions: Permission.code("role:rolePermissions").uri("/sys/v1/roles/{id}/resources").method(method.GET),
		// 获取指定角色拥有的菜单
		// roleMenus: Permission.code("role:roleMenus").uri("/sys/v1/roles/{id}/resources").method(method.GET),
		// savePermission: Permission.code("role:savePermission").uri("/sys/v1/roles/{id}/permissions").method(method.POST),
		saveResources: Permission.code("role:saveResources").uri("/sys/v1/roles/{id}/resources").method(method.POST)
	},
  
  account: {
    list: Permission.code("admin:list").uri("/sys/v1/admins").method(method.GET)
  }
}


export default code;
