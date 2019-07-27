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
class Permission {
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
    sessionStorage.setItem(Config.name.resourcesKey, JSON.stringify(tokenMenuAndPermission.resources));
    //
    sessionStorage.setItem(Config.name.adminKey, JSON.stringify(tokenMenuAndPermission.admin))
  }

  /**
   * 从sessionStorage所有permissions获取指定permission对象的PermissionInfo
   */
  static getPermission(code) {
    // 超级管理员通行
    // if (JSON.parse(sessionStorage.getItem(Config.name.adminKey)).username == 'admin') {
    //   return new PermissionInfo(true, false);
    // }
    let menus = JSON.parse(sessionStorage.getItem(Config.name.resourcesKey));
    for (let menu of menus) {
      let leafs = Permission.getLeafs(menu);
      // 获取菜单的所有叶子节点
      for (let p of leafs) {
        // 如果是菜单类型，则跳过
        if (p.type === 1) {
          continue;
        }

        if (p.code === code) {
          // 如果是禁用状态，则禁止按钮点击
          if (p.status === 0) {
            return new PermissionInfo(true, true);
          }
          return new PermissionInfo(true, false);
        }
        // // 不可用状态权限code
        // let disableCode = code + ":" + 0;
        // // 如果是不可用状态，则标识disable为true
        // if (p.code === disableCode) {
        //   return new PermissionInfo(true, true);
        // }
      }
    }

    return new PermissionInfo(false, true);
  }

  /**
   * 获取菜单的所有叶子节点
   * @param {Object} menu 根菜单
   */
  static getLeafs(menu) {
    let leafs = [];
    Permission.fillLeafs(menu, leafs);
    return leafs;
  }

  /**
   * 将所有叶子节点填充
   * @param {Object} meun  根菜单
   * @param {Object} leafs 需要填充的叶子节点
   */
  static fillLeafs(menu, leafs) {
    let children = menu.children;
    if (!children) {
      leafs.push(menu);
      return;
    }
    children.forEach(m => {
      Permission.fillLeafs(m, leafs);
    })
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

export default Permission
