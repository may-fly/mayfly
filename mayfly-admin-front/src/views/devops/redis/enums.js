import {Enum} from '~/common/Enum.js'

export default {
  // redis值类型枚举
  ValueTypeEnum: new Enum().add('string', 'string', 1).add('set', 'set', 2)
}