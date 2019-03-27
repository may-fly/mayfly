import {Enum} from './Enum.js'

export default {
  requestMethod: new Enum().add('get', 'GET', 1).add('post', 'POST', 2).add('put', 'PUT', 3).add('del', 'DELETE', 4)
}