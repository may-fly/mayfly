import SocketBuilder from './SocketBuilder'

/**
 * 工具类
 */
class Utils {
  /**
   * 属性拷贝，将一个对象的属性拷贝给另一个对象
   * @param {Object} source  源对象
   * @param {Object} target 目标对象
   */
  static copyProperties(source, target) {
    for (let k in target) {
      let value = source[k];
      if (value) {
        target[k] = value;
      }
    }
  }
  
  /**
   * 重置对象属性为null
   * @param {Object} target  对象
   */
  static resetProperties(target) {
    for (let k in target) {
      let value = target[k];
      if (value != null) {
        target[k] = null;
      }
    }
  }
  
  /**
   * websocket建造器
   * @param {Object} websocketUrl  socket url
   */
  static socketBuilder(websocketUrl) {
    return SocketBuilder.builder(websocketUrl);
  }
}

export default Utils

/**
 * @description 绑定事件 on(element, event, handler)
 */
export const on = (function () {
  if (document.addEventListener) {
    return function (element, event, handler) {
      if (element && event && handler) {
        element.addEventListener(event, handler, false);
      }
    };
  } else {
    return function (element, event, handler) {
      if (element && event && handler) {
        element.attachEvent('on' + event, handler);
      }
    };
  }
})();

/**
 * @description 解绑事件 off(element, event, handler)
 */
export const off = (function () {
  if (document.removeEventListener) {
    return function (element, event, handler) {
      if (element && event) {
        element.removeEventListener(event, handler, false);
      }
    };
  } else {
    return function (element, event, handler) {
      if (element && event) {
        element.detachEvent('on' + event, handler);
      }
    };
  }
})();