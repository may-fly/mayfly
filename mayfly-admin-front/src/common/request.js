import ElementUI from 'element-ui';
import router from "../router";
import Axios from 'axios';
import Config from './config';
import enums from './enums'

// 全局设置
function setToken() {
  Axios.defaults.headers['token'] = sessionStorage.getItem(Config.name.tokenKey);
}


function buildApiUrl(url) {
  return `${Config.apiUrl}${url}`;
}

/**
 * 解析服务端返回结果
 */
function parseResponse(res) {
  return new Promise((resolve, reject) => {
    if (res.status !== 200) {
      reject('请求异常');
      return;
    }
    // 获取请求返回结果
    let data = res.data;
    // 如果提示没有权限，则移除token，使其重新登录
    if (data.code === enums.ResultEnum.NO_PERMISSION.value) {
      sessionStorage.removeItem(Config.name.tokenKey);
      ElementUI.Notification.error({
        title: '请求错误',
        message: '登录超时'
      });
      setTimeout(() => {
        router.push({
          path: '/login',
        });
      }, 1000)
      return;
    }
    if (data.code === enums.ResultEnum.SUCCESS.value) {
      resolve(data.data);
    } else {
      reject(data.msg);
    }
  });
}


/**
 * @author: hml
 *
 * 将带有{id}的url替换为真实值；
 * 若restUrl:/category/{categoryId}/product/{productId}  param:{categoryId:1, productId:2}
 * 则返回 /category/1/product/2 的url
 */
function parseRestUrl(restUrl, param) {
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
function request(method, url, params) {
  if (!url)
    return;
  // 简单判断该url是否是restful风格
  if (url.indexOf("{") != -1) {
    url = parseRestUrl(url, params);
  }
  setToken();
  let query = {
    method,
    url: buildApiUrl(url),
  };
  let lowMethod = method.toLowerCase();
  // post和put使用json格式传参
  if (lowMethod === 'post' || lowMethod === 'put') {
    query.headers = {
      'Content-Type': 'application/json;charset=UTF-8'
    },
      query.data = params;
  } else {
    query.params = params;
  }
  return Axios.request(query).then(res => parseResponse(res))
    .catch(e => {
      if (typeof e == 'object') {
        ElementUI.Message.error(e.message);
      } else {
        ElementUI.Message.error(e);
      }

      return Promise.reject(e);
    });
}

/**
 * 根据Permission中对应的方法值，请求对应的方法
 * 
 * @param {Object} permission       Permission对象(~/common/Permission.js)，包含url和请求方法
 * @param {Object} params    api请求参数
 */
function send(permission, params) {
  return request(permission.method, permission.url, params);
}

export function getApiUrl(url) {
  // 只是返回api地址而不做请求，用在上传组件之类的
  return buildApiUrl(url) + '?token=' + sessionStorage.getItem(Config.name.tokenKey);
}

export default {
  request,
  send,
  parseRestUrl,
  getApiUrl
}
