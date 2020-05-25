<template>
  <div class="account-dialog">
    <el-dialog :title="title" :visible="visible" :show-close="false" width="35%">
      <el-form :model="form" ref="accountForm" :rules="rules" label-width="85px" size="small">
        <el-form-item prop="username" label="用户名:" required>
          <el-input
            :disabled="edit"
            v-model.trim="form.username"
            placeholder="请输入用户名"
            auto-complete="off"
          ></el-input>
        </el-form-item>
        <el-form-item prop="password" label="密码:" required>
          <el-input
            type="password"
            v-model.trim="form.password"
            placeholder="请输入密码"
            autocomplete="new-password"
          ></el-input>
        </el-form-item>
        <el-form-item v-if="!edit" label="确认密码:" required>
          <el-input
            type="password"
            v-model.trim="form.repassword"
            placeholder="请输入确认密码"
            autocomplete="new-password"
          ></el-input>
        </el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="cancel" size="mini">取 消</el-button>
        <el-button type="primary" :loading="btnLoading" @click="btnOk" size="small">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { accountApi } from '../api'
import enums from '../enums'
export default {
  name: 'AccountEdit',
  props: {
    visible: Boolean,
    account: [Object, Boolean],
    title: String
  },
  data() {
    return {
      enums: enums,
      edit: false,
      form: {
        id: null,
        username: null,
        password: null,
        repassword: null
      },
      btnLoading: false,
      rules: {
        username: [
          {
            required: true,
            message: '请输入用户名',
            trigger: ['change', 'blur']
          }
        ],
        password: [
          {
            required: true,
            message: '请输入密码',
            trigger: ['change', 'blur']
          }
        ]
      }
    }
  },
  watch: {
    account: {
      handler: function() {
        if (this.account) {
          this.edit = true
          this.$Utils.copyProperties(this.account, this.form)
        } else {
          this.edit = false
        }
      },
      deep: true
    }
  },
  methods: {
    async btnOk() {
      let p = this.form.id ? accountApi.update : accountApi.save

      this.$refs['accountForm'].validate(valid => {
        if (valid) {
          p.request(this.form).then(res => {
            this.$message.success('操作成功')
            this.$emit('val-change', this.form)
            this.btnLoading = true
            setTimeout(() => {
              this.btnLoading = false
            }, 1000)
            //重置表单域
            this.$refs['accountForm'].resetFields()
            this.$Utils.resetProperties(this.form)
          })
        } else {
          this.$message.error('表单填写有误')
          return false
        }
      })
    },
    cancel() {
      this.$emit('cancel')
      setTimeout(() => {
        this.$refs['accountForm'].resetFields()
        //  重置对象属性为null
        this.$Utils.resetProperties(this.form)
      }, 200)
    }
  },
  mounted() {},
  components: {}
}
</script>
<style lang="less">
</style>
