import request from './request'

export default {
  login: (param: any) => request.request('POST', '/open/login', param),
  captcha: () => request.request('GET', '/open/captcha', null),
  logout: (param: any) => request.request('POST', '/sys/accounts/logout/{token}', param)
}