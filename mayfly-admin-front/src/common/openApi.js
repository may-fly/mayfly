import request from './request.js'

export default {
  login: (param) => request.request('POST', '/open/v1/login', param)
}