<template>
  <div class="menu-dialog">
    <el-dialog :title="title" :visible="dialogFormVisible" :show-close="false" width="35%">
      <el-form :model="form" ref="menuForm" :rules="rules" label-width="85px" size="small">
        <el-form-item prop="name" label="名称:" required>
          <el-input v-model.trim="form.name" placeholder="请输入资源名" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item prop="type" label="类型:" required>
          <el-radio :disabled="typeDisabled" v-for="item in enums.ResourceTypeEnum" :key="item.value" v-model="form.type" :label="item.value">{{item.label}}</el-radio>
        </el-form-item>
        <el-form-item v-if="form.type === enums.ResourceTypeEnum.MENU.value" label="样式:">
          <el-input v-model.trim="form.icon" placeholder="请输入菜单图标样式"></el-input>
        </el-form-item>
        <el-form-item v-if="form.type === enums.ResourceTypeEnum.MENU.value" prop="code" label="路由code:">
          <el-input v-model.trim="form.code" placeholder="请输入路由code"></el-input>
        </el-form-item>
        <el-form-item v-if="form.type === enums.ResourceTypeEnum.PERMISSION.value" prop="code" label="权限code:" required>
          <el-input v-model.trim="form.code" placeholder="请输入权限code"></el-input>
        </el-form-item>
        <el-form-item label="序号:" prop="weight" required>
          <el-input v-model.trim="form.weight" type="number" placeholder="请输入序号"></el-input>
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
  import enums from '../enums.js'
  export default {
    name: 'ResourceEdit',
    props: {
      dialogFormVisible: Boolean,
      data: [Object, Boolean],
      title: String,
      type: Number,
      typeDisabled: Boolean
    },
    data() {
      return {
        permission: permission.menu,
        enums: enums,
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
          name: null,
          pid: null,
          code: null,
          icon: null,
          type: null,
          weight: null
        },
        // 资源类型选择是否禁用
        // typeDisabled: false,
        btnLoading: false,
        rules: {
          name: [{
            required: true,
            message: '请输入资源名称',
            trigger: ['change', 'blur']
          }],
          weight: [{
            required: true,
            message: '请输入序号',
            trigger: ['change', 'blur']
          }]
        }
      }
    },
    watch: {
      'data': {
        handler: function() {
          if (this.data) {
            this.$Utils.copyProperties(this.data, this.form);
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
                  this.btnLoading = false;
                }, 1000);
                
                this.cancel();
              })
            } else {
              return false;
            }
          });
        }
      },
      cancel() {
        this.$emit('cancel');
        setTimeout(() => {
          this.$refs["menuForm"].resetFields();
          //  重置对象属性为null
          this.$Utils.resetProperties(this.form);
        }, 200);
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
