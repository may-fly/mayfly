import Permission from '../permission'

/**
 * 菜单建造者
 */
class MenuBuilder{
	constructor() {}
	
	withCode(code) {
		this.code = code;
		return this;
	}
	
	withName(name) {
		this.name = name;
		return this;
	}
	
	withPath(path) {
		this.path = path;
		return this;
	}
	
	withIcon(icon) {
		this.icon = icon;
		return this;
	}
	
	withChildrens(childrens) {
		this.childrens = childrens;
		return this;
	}
	
	addChildren(children) {
		if (this.childrens) {
			this.childrens.push(children);
		} else {
			this.childrens = [];
			this.childrens.push(children);
		}
		
		return this;
	}
	
	build() {
		return {
			code: this.code,
			name: this.name,
			path: this.path,
			icon: this.icon,
			childrens: this.childrens
		}
	}
}

let menus = [];
let MenuCode = Permission.MenuCode;

let home = new MenuBuilder().withCode("HOME").withName("首页").withPath("/").withIcon("fa fa-tachometer").build();

/**
 * 权限管理
 */
let permissionManager = new MenuBuilder().withCode(MenuCode.permission.code).withName("权限管理").withIcon("fa fa-qrcode");
//api列表
let permissionApi = new MenuBuilder().withCode(MenuCode.permission.code).withName("API列表").withPath("/api_list");
//权限列表
let permission = new MenuBuilder().withCode("PERMISSION_LIST").withName("权限列表").withPath("/permission_list");
let role = new MenuBuilder().withCode("PERMISSION_ROLE").withName("角色管理").withPath("/role_manage")

permissionManager.addChildren(permission).addChildren(role).addChildren(permissionApi);




menus.push(home);
menus.push(permissionManager);
// /**
//  * 首页
//  * @type {{name: string, path: string, icon: string}}
//  */
// menu.home = {
//   name: '首页',
//   path: '/',
//   icon: 'fa fa-tachometer',
// };
// 
// let home = Menu.instance().code("HOME").name("首页");
// 
// 
// /**
//  * 字体图标
//  * @type {{name: string, icon: string, children: {}}}
//  */
// menu.font_icon = {
//   name: '字体图标',
//   icon: 'fa fa-th',
//   children: {}
// };
// let icon = menu.font_icon.children;
// 
// icon.font_awesome = {
//   name: 'FontAwesome 4.7',
//   path: '/font_awesome',
// 
// };
// icon.element_icon = {
//   name: 'ElementIcon',
//   path: '/element_icon',
// };
// 
// /**
//  * 内容管理
//  * @type {{name: string, icon: string, children: {}}}
//  */
// menu.content_manage = {
//   name: '内容管理',
//   icon: 'fa fa-file-text-o',
//   children: {}
// };
// 
// let ContentManage = menu.content_manage.children;
// 
// ContentManage.post = {
//   name: '文章管理',
//   path: '/post_manage',
// };
// 
// /**
//  * 用户管理
//  * @type {{name: string, icon: string, children: {}}}
//  */
// menu.user_manage = {
//   name: '用户管理',
//   icon: 'fa fa-user-circle-o',
//   children: {}
// };
// let UserManage = menu.user_manage.children;
// 
// UserManage.user = {
//   name: '用户列表',
//   path: '/user_manage',
// };
// 
// 
// /**
//  * 分类管理
//  * @type {{name: string, icon: string, children: {}}}
//  */
// menu.category_manage = {
//   name: '分类管理',
//   icon: 'fa fa-sitemap',
//   children: {}
// };
// let CategoryManage = menu.category_manage.children;
// 
// CategoryManage.category = {
//   name: '分类列表',
//   path: '/category_manage',
// };
// 
// 
// 
// menu.permission_manage = {
// 	code: "PERMISSION",
//   name: '权限管理',
//   icon: 'fa fa-qrcode',
//   children: {
// 		role: {
// 			name: '角色管理',
// 			path: '/role_manage',
// 		},
// 		
// 		permission: {
// 			name: '权限列表',
// 			path: '/permission_list',
// 		},
// 		
// 		apiList: {
// 			code: "PERMISSION_API",
// 			name: 'API列表',
// 			path: '/api_list'
// 		}
// 	}
// };

export default menus;

if(__DEV__){

//   menu.development_tools = {
//     name: '开发工具',
//     icon: 'fa fa-wrench',
//     children: {}
//   };
// 
//   let DevelopmentTools = menu.development_tools.children;
// 
//   DevelopmentTools.code = {
//     name: '构建代码',
//     path: '/build_code',
//   };

}
