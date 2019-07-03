import request from './request.js'

export default {
  login: (param) => request.request('post', '/open/v1/login', param)
}