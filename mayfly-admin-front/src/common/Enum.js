/**
 * 枚举类
 * @author meilin.huang
 */
export class Enum {
  /**
   * 添加枚举字段
   * field: 枚举字段
   * label: 界面显示
   * value: 枚举值
   */
  add (field, label, value) {
    this[field] = {label, value}
    return this
  }

  /**
   * 根据枚举value获取其label
   */
  getLabelByValue (value) {
    // 字段不存在返回‘’
    if (value === undefined || value === null) {
      return ''
    }
    for (let i in this) {
      let e = this[i]
      if (e && e.value === value) {
        return e.label
      }
    }

    return ''
  }
}
