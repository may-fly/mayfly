import request from './request'

/**
 * 可用于各模块定义各自api请求
 */
class Api {

  constructor(url) {
    this.url = url;
  }

  /**
   * 权限对应的url
   * @param {String} uri 请求url
   */
  url(url) {
    this.url = url;
    return this;
  }

  /**
   * url的请求方法
   * @param {String} method 请求方法
   */
  method(method) {
    this.method = method;
    return this;
  }
  
  /**
   * 获取权限的完整url
   */
  getUrl() {
    return request.getApiUrl(this.url);
  }

  /**
   * 操作该权限，即请求对应的url
   * @param {Object} param 请求该权限的参数
   */
  request(param) {
    return request.send(this, param);
  }


  /**    静态方法     **/

  /**
   * 静态工厂，设置code，并返回Permission对象
   * @param {String} code 权限code（权限标识符）Permission对象必有的属性
   */
  static url(url) {
    return new Api(url);
  }
}


export default Api
