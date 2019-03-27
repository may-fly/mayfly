<template>
  <div class="api-dialog">
    <el-dialog :title="title" :visible="visible" :show-close="false" width="25%">
      <el-form :model="form">
         <el-form-item label="">
          <el-select style="width: 99%" v-model="form.groupId" clearable placeholder="请选择权限组">
            <el-option v-for="item in groups" :key="item.id" :value="item.id" :label="item.name">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="">
          <el-input v-model="form.uriPattern" placeholder="请输入权限uri模式(支持RESTful风格)" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="">
          <el-select style="width: 99%" v-model="form.method" clearable placeholder="请选择请求方法">
            <el-option v-for="item in commonEnums.requestMethod" :key="item.value" :value="item.value" :label="item.label">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="">
          <el-input v-model="form.code" placeholder="请输入权限code(用于前后端控制权限)"></el-input>
        </el-form-item>
        <!-- <el-form-item label="">
					<el-select v-model="form.groupId" placeholder="请选择API组">
						<el-option v-for="item in groups" :key="item.name" :label="item.name" :value="item.id">
						</el-option>
					</el-select>
				</el-form-item> -->
        <el-form-item label="">
          <el-input v-model="form.description" type="textarea" :rows="2" placeholder="请输入API功能描述"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="$emit('cancel');">取 消</el-button>
        <el-button type="primary" :loading="btnLoading" @click="btnOk">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import permission from '../permissions.js'
  import commonEnums from '~/common/enums'
  export default {
    name: 'ApiEdit',
    props: {
      visible: Boolean,
      data: [Object, Boolean],
      title: String
    },
    data() {
      return {
        btns: {
          save: {}
        },
        commonEnums: commonEnums,
        form: {
          id: null,
          method: '',
          code: '',
          groupId: null,
          description: '',
          uriPattern: '',
        },
        groups: [],
        btnLoading: false
      }
    },
    watch: {
      data() {
        if (this.data) {
          for (let k in this.form) {
            this.form[k] = this.data[k];
          }
          permission.permissionGroup.all.request(null, res => {
            this.groups = res;
          })
        }
      }
    },
    methods: {
      handleChange() {},
      btnOk() {
        if (this.form.id) {
          permission.permission.update.request(this.form, res => {
            this.$emit('val-change', res);
          });
        } else {
          permission.permission.save.request(this.form, res => {
            this.$emit('val-change', res);
          });
        }
      }

    }
  }
</script>
<style lang="less">
  // 	.api-dialog {
  // 		.el-cascader {
  // 			width: 100%;
  // 		}
  // 	}
</style>
