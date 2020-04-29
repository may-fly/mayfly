import request from './request.js'

export default {
  login: (param) => request.request('POST', '/open/v1/login', param),
  captcha:  () => request.request('GET', '/open/captcha'),
  logout: (param) => request.request('POST', '/sys/accounts/logout/{token}', param),
}