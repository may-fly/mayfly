import allMenus from './menu';
import Permission from '../permission';

//根据权限做菜单过滤
function filterMenu(menus) {
	//登录者含有的菜单
	let hasMenus = [];
	allMenus.forEach(menu => {
		if (Permission.getMenuInfo(menu.code).show) {
			let childrens = menu.childrens;
			menu.childrens = [];
			hasMenus.push(menu);
			if (childrens) {
				childrens.forEach(children => {
					if (Permission.getMenuInfo(children.code).show) {
						menu.childrens.push(children);
					}
				})
			}
		}
	});
	
	return hasMenus;
}

let menus = filterMenu();


export default menus;