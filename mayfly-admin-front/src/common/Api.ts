import request from './request'

/**
 * 可用于各模块定义各自api请求
 */
class Api {
  /**
   * 请求url
   */
  url: string;

  /**
   * 请求方法
   */
  method: string;

  constructor(url: string, method: string) {
    this.url = url;
    this.method = method;
  }

  /**
   * 权限对应的url
   * @param {String} uri 请求url
   */
  setUrl(url: string) {
    this.url = url;
    return this;
  }

  /**
   * url的请求方法
   * @param {String} method 请求方法
   */
  setMethod(method: string) {
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
  request(param: any): Promise<any> {
    return request.send(this, param);
  }


  /**    静态方法     **/

  /**
   * 静态工厂，返回Api对象，并设置url与method属性
   * @param {String} code 权限code（权限标识符）Permission对象必有的属性
   */
  static create(url: string, method: string) {
    return new Api(url, method);
  }
}


export default Api