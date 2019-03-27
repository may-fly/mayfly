import ElementUI from 'element-ui';
import Axios from 'axios';
import Config from '../config';

function buildApiUrl(url) {
	return `${Config.apiUrl}${url}`;
}

function setToken() {
	Axios.defaults.headers['token'] = sessionStorage.getItem(Config.tokenKey);
	Axios.defaults.headers.post['Content-Type'] = 'application/json';
}


function isFunction(fn) {
	return Object.prototype.toString.call(fn) === '[object Function]';
}

// function buildServerApiRequest(params, url, type, callback) {
// 	setToken();
// 	if ('get' == type) {
// 		params = {
// 			params: params
// 		}
// 		//做一些加载的小动画挺好
// 	}
// 	let apiUrl = buildApiUrl(url);
// 	let result = Axios[type](apiUrl, params);
// 
// 	if (isFunction(callback)) { //没有回调则返回es6 promise
// 		result.then(r => {
// 			r = r.data;
// 			if (r.code = Config.result.success) {
// 				callback(r);
// 			} else {
// 				ElementUI.Notification.error({
// 					// title: '请求错',
// 					message: r.msg
// 				});
// 			}
// 		}).catch(e => {
// 			if (__DEV__)
// 				console.log(e);
// 			ElementUI.Notification.error({
// 				title: '请求错误',
// 				message: 'Network Error'
// 			});
// 		});
// 	}
// 	return result;
// }

/**
 * 解析请求错误
 */
function parseError(error) {
	if (__DEV__)
		console.log(error);
	ElementUI.Notification.error({
		title: '请求错误',
		message: '服务器异常'
	});
}

/**
 * 解析服务端返回结果
 */
function parseResponse(res, callback) {
	console.log("res:");
	console.log(res)
	if (res.code === Config.result.success) {
		if (isFunction(callback)) {
			callback(res.data);
		} else {
			throw new TypeError("callback必须为函数");
		}
	} else {
		ElementUI.Notification.error({
			title: '请求错误',
			message: res.msg
		});
	}
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

function get(uri, params, callback) {
	if (!uri) 
		return;
		
	setToken();
	Axios.get(buildApiUrl(uri), {params}).then(res => {
		parseResponse(res.data, callback);
	}).catch(err => {
		parseError(err);
	});
}

function post(uri, params, callback) {
	if (!uri) 
		return;
		
	setToken();
	Axios.post(buildApiUrl(uri), params).then(res => {
		parseResponse(res.data, callback);
	}).catch(err => {
		parseError(err);
	});
}

function put(uri, params, callback) {
	if (!uri) 
		return;

	setToken();
	Axios.put(buildApiUrl(uri), params).then(res => {
		parseResponse(res.data, callback);
	}).catch(err => {
		parseError(err);
	});
}


function del(uri, params, callback) {
	if (!uri) 
		return;
		
	setToken();
	Axios.delete(buildApiUrl(uri), params).then(res => {
		parseResponse(res.data, callback);
	}).catch(err => {
		parseError(err);
	});
}

/**
 * 根据api中对应的方法值，请求对应的方法
 */
function send(api, params, callback) {
	let uri = api.uri;
	//简单判断该uri是否是restful风格
	if (uri.indexOf("{") != -1) {
		uri = parseRestUrl(uri, params);
	}
	switch(api.method) {
		case 1: 
			get(uri, params, callback);
			break;
		case 2:
			post(uri, params, callback);
			break;
		case 3:
			put(uri, params, callback);
			break;
		case 4:
			del(uri, params, callback);
			break;
	}
}



// export function buildApiRequest(params, url, type, callback) {
// 	return buildServerApiRequest(params, url, type, callback);
// }
// 
export function getApiUrl(url) {
	//只是返回api地址而不做请求，用在上传组件之类的
	return buildApiUrl(url) + '?token=' + sessionStorage.getItem(Config.tokenKey);
}

export default {
	get,
	post,
	put,
	del,
	send,
	parseRestUrl
}