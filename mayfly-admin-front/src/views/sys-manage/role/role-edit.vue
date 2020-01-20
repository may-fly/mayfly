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
        <el-button @click="$emit('cancel');" size="small">取 消</el-button>
        <el-button type="primary" :loading="btnLoading" @click="btnOk" size="small">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import permission from '../permissions.js'
  export default {
    name: 'RoleEdit',
    props: {
      visible: Boolean,
      data: [Object, Boolean],
      title: String,
    },
    data() {
      return {
        permission: permission.role,
        form: {
          id: null,
          name: '',
          remark: '',
        },
        btnLoading: false,
      }
    },
    watch: {
      'data': {
        handler: function () {
          if (this.data) {
            for (let k in this.form) {
              this.form[k] = this.data[k];
            }
          } else {
            for (let k in this.form) {
              this.form[k] = '';
            }
          }
        },
        deep: true
      }
    },
    methods: {
      handleChange() {
      },
      async btnOk() {
        let formData = this.form;
        let p = this.form.id ? this.permission.update : this.permission.save;
        await p.request(this.form)
        this.$emit('val-change', this.form);
        this.btnLoading = true;
        setTimeout(() => {
          this.btnLoading = false
        }, 1000);
      },
    },
    components: {}
  }
</script>
<style lang="less">
</style>
