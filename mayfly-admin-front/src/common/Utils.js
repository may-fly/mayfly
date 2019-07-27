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
      if (value) {
        target[k] = null;
      }
    }
  }
}

export default Utils