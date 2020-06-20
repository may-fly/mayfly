import ElementUI from 'element-ui';
import router from "../router";
import Axios from 'axios';
import Permission from './Permission';
import config from './config';

export interface Result {
  /**
   * 响应码
   */
  code: number;
  /**
   * 响应消息
   */
  msg: string;
  /**
   * 数据
   */
  data?: any;
}

export enum ResultCodeEnum {
  SUCCESS = 200,
  ERROR = 400,
  PARAM_ERROR = 405,
  SERVER_ERROR = 500,
  NO_PERMISSION = 501
}

const baseUrl = config.apiUrl

/**
 * 通知错误消息
 * @param msg 错误消息
 */
function notifyErrorMsg(msg: string) {
  // 错误通知
  ElementUI.Message.error(msg);
}

// create an axios instance
const service = Axios.create({
  baseURL: baseUrl, // url = base url + request url
  timeout: 5000 // request timeout
})

// request interceptor
service.interceptors.request.use(
  config => {
    // do something before request is sent
    const token = Permission.getToken()
    if (token) {
      // 设置token
      config.headers['token'] = token
    }
    return config
  },
  error => {
    console.log(error) // for debug
    return Promise.reject(error)
  }
)

// response interceptor
service.interceptors.response.use(
  response => {
    // 获取请求返回结果
    const data: Result = response.data;
    // 如果提示没有权限，则移除token，使其重新登录
    if (data.code === ResultCodeEnum.NO_PERMISSION) {
      Permission.removeToken()
      notifyErrorMsg('登录超时')
      setTimeout(() => {
        router.push({
          path: '/login',
        });
      }, 1000)
      return;
    }
    if (data.code === ResultCodeEnum.SUCCESS) {
      return data.data;
    } else {
      return Promise.reject(data);
    }
  },
  error => {
    return Promise.reject(error)
  }
)

/**
 * @author: hml
 *
 * 将带有{id}的url替换为真实值；
 * 若restUrl:/category/{categoryId}/product/{productId}  param:{categoryId:1, productId:2}
 * 则返回 /category/1/product/2 的url
 */
function parseRestUrl(restUrl: string, param: any) {
  return restUrl.replace(/\{\w+\}/g, (word) => {
    let key = word.substring(1, word.length - 1);
    let value = param[key];
    if (value != null || value != undefined) {
      // delete param[key]
      return value;
    }
    return "";
  });
}

/**
 * 请求uri
 * 该方法已处理请求结果中code != 200的message提示,如需其他错误处理(取消加载状态,重置对象状态等等),可catch继续处理
 * 
 * @param {Object} method 请求方法(GET,POST,PUT,DELTE等)
 * @param {Object} uri    uri
 * @param {Object} params 参数
 */
function request(method: string, url: string, params: any): Promise<any> {
  if (!url)
    throw new Error('请求url不能为空');
  // 简单判断该url是否是restful风格
  if (url.indexOf("{") != -1) {
    url = parseRestUrl(url, params);
  }
  const query: any = {
    method,
    url: url,
  };
  const lowMethod = method.toLowerCase();
  // post和put使用json格式传参
  if (lowMethod === 'post' || lowMethod === 'put') {
    // query.headers = {
    //   'Content-Type': 'application/json;charset=UTF-8'
    // }
    query.data = params;
  } else {
    query.params = params;
  }
  return service.request(query).then(res => res)
    .catch(e => {
      notifyErrorMsg(e.msg || e.message)
      return Promise.reject(e);
    });
}

/**
 * 根据Permission中对应的方法值，请求对应的方法
 * 
 * @param {Object} permission       Permission对象(~/common/Permission.js)，包含url和请求方法
 * @param {Object} params    api请求参数
 */
function send(api: any, params: any): Promise<any> {
  return request(api.method, api.url, params);
}

function getApiUrl(url: string) {
  // 只是返回api地址而不做请求，用在上传组件之类的
  return baseUrl + '?token=' + Permission.getToken();
}

export default {
  request,
  send,
  parseRestUrl,
  getApiUrl
}
