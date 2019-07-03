import request from './request'
import Config from './config'

/**
 * show: 菜单按钮是否显示
 * disabled: 菜单功能是否被禁用
 */
class PermissionInfo {
	constructor(show, disabled) {
		this.show = show;
		this.disabled = disabled;
	}
}

/**
 * 可用于各模块定义各自权限对象
 */
class Permission{
	constructor(code) {
    this.code = code;
  }
  
  /**
   * 权限对应的uri
   */
	uri(uri) {
		this.uri = uri;
		return this;
	}

  /**
   * uri的请求方法(方法枚举)
   */
	method(method) {
		this.method = method;
		return this;
	}
 
  /**
   * 操作该权限，即请求对应的uri
   */
  request(param) {
    return request.send(this, param);
  }

  /**
   * 静态工厂，设置code，并返回Permission对象
   */
  static code(code) {
    return new Permission(code);
  }

  /**
   * 登录成功保存对应的token以及菜单按钮列表
   */
  static savePermission(tokenMenuAndPermission) {
  	//保存token
  	sessionStorage.setItem(Config.name.tokenKey, tokenMenuAndPermission.token);
  	//保存menus
  	sessionStorage.setItem(Config.name.menusKey, JSON.stringify(tokenMenuAndPermission.menus));
  	//保存权限
  	sessionStorage.setItem(Config.name.permissionsKey, JSON.stringify(tokenMenuAndPermission.permissions))
  }

  /**
   * 从sessionStorage所有permissions获取指定permission对象的PermissionInfo
   */
  static getPermission(code) {
  	let permissions = JSON.parse(sessionStorage.getItem(Config.name.permissionsKey));
  	for (let p of permissions) {
  		if (p == code) {
  			return new PermissionInfo(true, false);
  		} 
  		// 不可用状态权限code
  		let disableCode = code + ":" + 0;
  		// 如果是不可用状态，则标识disable为true
  		if (p == disableCode) {
  			return new PermissionInfo(true, true);
  		}
  	}

  	return new PermissionInfo(false, true);
  }
  
  /**
   * 检查权限code并设定对应dom的属性
   * @param code 权限码
   * @param elDom  dom元素
   */
  static checkCodeAndSetDom(code, elDom) {
    // 根据权限code获取对应权限信息
    let permission = Permission.getPermission(code);
    // 如果没有显示权限，则隐藏该元素
    if (!permission.show) {
      elDom.style.display  = 'none';
    }
    // 如果该权限被暂用，则禁止该btn
    if (permission.disabled) {
      // 将按钮置为禁用
      elDom.setAttribute('disabled', 'disabled');
      // element-ui需要添加该类样式
      elDom.className = elDom.className + ' ' + 'is-disabled';
    }
  }
}

export default Permission
