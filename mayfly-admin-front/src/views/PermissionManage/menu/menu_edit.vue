<template>
  <div class="menu-dialog">
    <el-dialog :title="title" :visible="dialogFormVisible" :show-close="false" width="25%">
      <el-form :model="form" ref="menuForm" :rules="rules" label-width="85px" size="small">
        <el-form-item prop="name" label="名称:" required>
          <el-input v-model.trim="form.name" placeholder="请输入菜单名" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="样式:">
          <el-input v-model.trim="form.icon" placeholder="请输入菜单图标样式"></el-input>
        </el-form-item>
        <el-form-item label="路由路径:">
          <el-input v-model.trim="form.path" placeholder="请输入路由路径"></el-input>
        </el-form-item>
        <el-form-item label="权重:" required>
          <el-input v-model.trim="form.weight" placeholder="请输入权重"></el-input>
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
  import permission from '../permissions.js'
  export default {
    name: 'MenuEdit',
    props: {
      dialogFormVisible: Boolean,
      data: [Object, Boolean],
      title: String
    },
    data() {
      return {
        permission: permission.menu,
        //弹出框对象
        dialogForm: {
          title: "",
          visible: false,
          data: {}
        },
        props: {
          value: 'id',
          label: 'name',
          children: 'children'
        },
        form: {
          id: null,
          name: '',
          pid: '',
          path: '',
          icon: '',
          weight: ''
        },
        operations: [{
            name: "新增"
          },
          {
            name: "删除"
          },
          {
            name: "删除"
          },
          {
            name: "删除"
          },
          {
            name: "删除"
          },
          {
            name: "删除"
          },
          {
            name: "删除"
          }
        ],
        btnLoading: false,
        rules: {
          name: [{
            required: true,
            message: '请输入菜单名称',
            trigger: 'change'
          }],
        }
      }
    },
    watch: {
      'data': {
        handler: function() {
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
      handleChange() {},
      addOperation() {
        this.dialogForm.visible = true;
      },
      operationChange(operation) {
        this.operations.push(operation);
        this.dialogForm.visible = false;
      },
      btnOk() {
        let p = this.form.id ? this.permission.update : this.permission.save;
        let pi = this.$Permission.getPermission(p.code);

        if (!pi.show) {
          this.$message.error('您没有该权限!');
        } else {
          this.$refs["menuForm"].validate((valid) => {
            if (valid) {
              p.request(this.form).then(res => {
                this.$emit('val-change', this.form);
                this.btnLoading = true;
                setTimeout(() => {
                  this.btnLoading = false
                }, 1000);
                //重置表单域
                this.$refs["menuForm"].resetFields();
              })
            } else {
              this.$message.error('表单填写有误');
              return false;
            }
          });
        }
      },
      cancel() {
        this.$emit('cancel');
        this.$refs["menuForm"].resetFields();
      }
    },
    mounted() {

    },
    components: {}
  }
</script>
<style lang="less">
  // 	.m-dialog {
  // 		.el-cascader {
  // 			width: 100%;
  // 		}
  // 	}
</style>
