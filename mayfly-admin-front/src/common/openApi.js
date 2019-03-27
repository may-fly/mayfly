import request from './request.js'

export default {
  login: (param, callback) => request.post('/open/v1/login', param, callback)
}