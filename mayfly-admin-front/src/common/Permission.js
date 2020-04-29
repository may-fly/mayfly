import Config from './config'

/**
 * 可用于各模块定义各自权限对象
 */
class Permission {

  constructor(code) {
    this.code = code;
  }

  /**    静态方法     **/

  /**
   * 静态工厂，设置code，并返回Permission对象
   * @param {String} code 权限code（权限标识符）Permission对象必有的属性
   */
  static code(code) {
    return new Permission(code);
  }


  /**
   * 登录成功保存对应的token以及菜单按钮列表
   */
  static savePermission(tokenMenuAndPermission) {
    //保存token
    Permission.saveToken(tokenMenuAndPermission.token)
    //保存resources
    sessionStorage.setItem(Config.name.menusKey, JSON.stringify(tokenMenuAndPermission.menus))
    // 保存登录用户基本信息
    sessionStorage.setItem(Config.name.adminKey, JSON.stringify(tokenMenuAndPermission.admin))

    let codeObj = {}
    for (let r of tokenMenuAndPermission.codes) {
      codeObj[r.code] = {status: r.status, type: r.type}
    }
    sessionStorage.setItem(Config.name.codesKey, JSON.stringify(codeObj))
  }

  /**
   * 获取token
   */
  static getToken() {
    return sessionStorage.getItem(Config.name.tokenKey);
  }

  /**
   * 保存token
   * @param {Object} token token
   */
  static saveToken(token) {
    sessionStorage.setItem(Config.name.tokenKey, token);
  }

  /**
   * 从sessionStorage所有permissions获取指定permission对象的PermissionInfo
   */
  static getPermission(code) {
    let resource = JSON.parse(sessionStorage.getItem(Config.name.codesKey))[code]
    if (resource) {
      // 如果是禁用状态，则禁止按钮点击
      if (resource.status == 0) {
        return new PermissionInfo(true, true);
      }
      return new PermissionInfo(true, false);
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
      elDom.style.display = 'none';
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

export default Permission
