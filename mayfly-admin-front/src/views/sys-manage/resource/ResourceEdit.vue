<template>
  <div class="menu-dialog">
    <el-dialog :title="title" :visible="visible" :show-close="false" width="28%">
      <el-form :model="form" ref="menuForm" :rules="rules" label-width="85px" size="small">
        <el-form-item prop="name" label="名称" required>
          <el-input v-model.trim="form.name" placeholder="请输入资源名" auto-complete="off"></el-input>
        </el-form-item>

        <el-form-item prop="type" label="类型" required>
          <el-radio
            :disabled="typeDisabled"
            v-for="item in enums.ResourceTypeEnum"
            :key="item.value"
            v-model="form.type"
            :label="item.value"
          >{{item.label}}</el-radio>
        </el-form-item>

        <el-form-item v-if="form.type === enums.ResourceTypeEnum.MENU.value" label="样式">
          <el-input v-model.trim="form.icon" placeholder="请输入菜单图标样式"></el-input>
        </el-form-item>
        <el-form-item v-if="form.type === enums.ResourceTypeEnum.MENU.value" prop="code" label="路由">
          <el-input v-model.trim="form.url" placeholder="请输入路由code"></el-input>
        </el-form-item>

        <el-form-item prop="code" label="权限code">
          <el-input v-model.trim="form.code" placeholder="请输入权限code"></el-input>
        </el-form-item>

        <el-form-item label="序号" prop="weight" required>
          <el-input v-model.trim="form.weight" type="number" placeholder="请输入序号"></el-input>
        </el-form-item>
      </el-form>

      <div style="text-align: center" class="dialog-footer">
        <el-button type="primary" :loading="btnLoading" @click="btnOk" size="mini">确 定</el-button>
        <el-button @click="cancel()" size="mini">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script lang="ts">
import { Component, Vue, Prop, Watch } from 'vue-property-decorator'
import { resourceApi } from '../api'
import enums from '../enums'
@Component({
  name: 'ResourceEdit'
})
export default class ResourceEdit extends Vue {
  @Prop()
  visible: Boolean
  @Prop()
  data: [Object, Boolean]
  @Prop()
  title: String
  @Prop()
  type: Number
  @Prop()
  typeDisabled: Boolean

  enums = enums
  //弹出框对象
  dialogForm = {
    title: '',
    visible: false,
    data: {}
  }
  props = {
    value: 'id',
    label: 'name',
    children: 'children'
  }
  form = {
    id: null,
    name: null,
    pid: null,
    code: null,
    url: null,
    icon: null,
    type: null,
    weight: null
  }
  // 资源类型选择是否禁用
  // typeDisabled: false,
  btnLoading = false
  rules = {
    name: [
      {
        required: true,
        message: '请输入资源名称',
        trigger: ['change', 'blur']
      }
    ],
    weight: [
      {
        required: true,
        message: '请输入序号',
        trigger: ['change', 'blur']
      }
    ]
  }

  @Watch('data', { deep: true })
  onDataChange() {
    if (this.data) {
      this.$Utils.copyProperties(this.data, this.form)
    }
  }

  btnOk() {
    let p = this.form.id ? resourceApi.update : resourceApi.save
    const menuForm: any = this.$refs['menuForm']
    menuForm.validate((valid: any) => {
      if (valid) {
        p.request(this.form).then(res => {
          this.$emit('val-change', this.form)
          this.btnLoading = true
          this.$message.success('保存成功')
          setTimeout(() => {
            this.btnLoading = false
          }, 1000)

          this.cancel()
        })
      } else {
        return false
      }
    })
  }
  cancel() {
    this.$emit('update:visible', false)
    this.$emit('cancel')
    setTimeout(() => {
      const menuForm: any = this.$refs['menuForm']
      menuForm.resetFields()
      //  重置对象属性为null
      this.$Utils.resetProperties(this.form)
    }, 200)
  }
}
</script>
<style lang="less">
// 	.m-dialog {
// 		.el-cascader {
// 			width: 100%;
// 		}
// 	}
</style>
