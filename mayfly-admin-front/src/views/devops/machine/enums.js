import {Enum} from '~/common/Enum.js'

/**
 * 枚举类
 */
export default {
  // 文件类型枚举
  FileTypeEnum: new Enum().add('DIRECTORY', '目录', 1).add('FILE', '文件', 2)
}