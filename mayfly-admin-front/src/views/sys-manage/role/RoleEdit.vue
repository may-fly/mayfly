<template>
  <div class="role-dialog">
    <el-dialog :title="title" :visible="visible" :show-close="false" width="500px">
      <el-form :model="form" size="small" label-width="90px">
        <el-form-item label="角色名称:" required>
          <el-input v-model="form.name" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="角色描述:">
          <el-input v-model="form.remark" type="textarea" :rows="3" placeholder="请输入角色描述"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" :loading="btnLoading" @click="btnOk" size="small">确 定</el-button>
        <el-button @click="cancel()" size="small">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script lang="ts">
import { Component, Vue, Prop, Watch } from 'vue-property-decorator'
import { roleApi } from '../api'
@Component({
  name: 'RoleEdit'
})
export default class RoleEdit extends Vue {
  @Prop({ default: '新增角色' })
  private title!: String

  @Prop({ default: false })
  private visible!: Boolean

  @Prop()
  private data!: [Object, Boolean]

  form = {
    id: null,
    name: '',
    remark: ''
  }
  btnLoading = false

  @Watch('data', { deep: true })
  onRoleChange() {
    if (this.data) {
      for (let k in this.form) {
        this.form[k] = this.data[k]
      }
    } else {
      for (let k in this.form) {
        this.form[k] = ''
      }
    }
  }

  handleChange() {}

  cancel() {
    // 更新父组件visible prop对应的值为false
    this.$emit("update:visible", false);
    // 若父组件有取消事件，则调用
    this.$emit('cancel');
  }

  async btnOk() {
    let formData = this.form
    let p = this.form.id ? roleApi.update : roleApi.save
    await p.request(this.form)
    this.$emit('val-change', this.form)
    this.cancel()
    this.btnLoading = true
    setTimeout(() => {
      this.btnLoading = false
    }, 1000)
  }
}
</script>
<style lang="less">
</style>
