<template>
  <div class="role-list">
    <ToolBar>
      <el-button type="primary" icon="el-icon-plus" size="small" @click="editRole(false)">添加</el-button>
      <div style="float: right">
        <el-input placeholder="请输入角色名称！" size="small" style="width: 140px" v-model="params.name" @clear="searchRole"
          clearable>
        </el-input>
        <el-button @click="searchRole" type="success" icon="el-icon-search" size="small"></el-button>
      </div>
    </ToolBar>
    <el-table :data="roles" border ref="table" style="width: 100%">
      <el-table-column label="序号" type="index"></el-table-column>
      <el-table-column prop="name" label="角色名称">
      </el-table-column>
      <el-table-column prop="remark" label="描述">
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间">
      </el-table-column>
      <el-table-column prop="updateTime" label="修改时间">
      </el-table-column>
      <el-table-column label="操作" :render-header="tableAction" width="350px">
        <template slot-scope="scope">
          <el-button @click="editRole(scope.row)" type="primary" icon="el-icon-edit" size="small">编辑</el-button>
          <el-button @click="editMenu(scope.row)" type="primary" icon="el-icon-setting" size="small">分配菜单</el-button>
          <el-button @click="editPermission(scope.row)" type="success" icon="el-icon-setting" size="small">分配权限</el-button>
        </template>
      </el-table-column>
    </el-table>
    <RoleEdit :title="roleEdit.title" :visible="roleEdit.visible" :data="roleEdit.data" @val-change="roleEditChange"
      @cancel="roleEdit.visible = false">
    </RoleEdit>
    <PermissionsEdit :visible="permissionDialog.visible" :role="permissionDialog.role" @success="permissionDialog.visible = false;"
      @cancel="cancelEditPermisson"></PermissionsEdit>
    <MenuEdit :visible="menuDialog.visible" :role="menuDialog.role" @cancel="menuDialog.visible = false;"></MenuEdit>
  </div>
</template>

<script>
  import ToolBar from '~/components/ToolBar/ToolBar.vue';
  import HelpHint from '~/components/HelpHint/HelpHint.vue';
  import RoleEdit from './role_edit.vue'
  import PermissionsEdit from './permission_edit.vue'
  import permission from '../permissions.js';
  import MenuEdit from './menu_edit.vue';

  export default {
    data() {
      return {
        btns: {
          search: {},
          editRole: {},
          editRole: {},
          editMenu: {}
        },
        permission: permission.role,
        dialogFormVisible: false,
        currentEditPermissions: false,
        params: {
          name: '',
        },
        roles: [],
        menuDialog: {
          visible: false,
          role: {}
        },
        permissionDialog: {
          role: {},
          visible: false
        },
        roleEdit: {
          title: "角色编辑",
          visible: false,
          role: {}
        }
      }
    },
    methods: {
      searchRole() {
        let tableRow = this.$refs.table.$el.querySelectorAll('tbody tr');
        let tableRowHeight = tableRow[1].offsetHeight;
        let isjump = false;
        for (let i = 0; i < this.roleData.length; i++) {
          if (this.params.name && this.roleData[i].name.indexOf(this.params.name) != -1) {
            tableRow[i].style.backgroundColor = '#85ce61';
            if (!isjump) {
              scrollTo(0, i * tableRowHeight + 66);
              isjump = true;
            }
          } else {
            tableRow[i].style.backgroundColor = '#fff';
          }
        }
      },
      tableAction() {
        return this.$createElement('HelpHint', {
          props: {
            content: '编辑角色 / 编辑角色权限'
          }
        }, '操作');
      },
      roleEditChange(data) {
        console.log(data);
        let self = this;

        self.$notify({
          title: self.roleEditTitle + '成功！',
          message: '自己对接Api,并调用你的初始化列表函数！',
          type: 'success'
        });
        self.roleEdit.visible = false;
        // self.$Api.editRole(data,r=>{
        //   self.$message.success(self.roleEditTitle+'成功！');
        //   self.init();
        //   self.dialogFormVisible = false;
        // });
      },
      editRole(data) {
        if (data) {
          this.roleEdit.data = data;
        } else {
          this.roleEdit.data = false;
        }

        this.roleEdit.visible = true;

      },
      editMenu(row) {
        console.log(row)
        this.menuDialog.visible = true;
        this.menuDialog.role = row;
      },
      /**
       * 编辑(分配)权限
       */
      editPermission(row) {
        this.permissionDialog.visible = true;
        this.permissionDialog.role = row;
      },
      // 取消编辑权限列表
      cancelEditPermisson() {
        this.permissionDialog.visible = false;
        this.permissionDialog.role = false;
      },
      UploadRole(data) {

      },
      deleteRole(id) {

        this.$message({
          message: '这里请求api删除或者恢复用户之后刷新分页组件，列表自动更新',
          type: 'success'
        });

      },
      resetting(id) {

        let dom = this.$refs[id].$el;
        dom.style.transform = 'rotate(180deg)';
        setTimeout(() => {
          dom.style.transform = 'rotate(0deg)'
        }, 600)
        this.$message({
          message: '已经成功重置密码',
          type: 'success'
        });

      },


    },
    mounted() {
      this.permission.list.request(null).then(res => {
        this.roles = res;
      });
    },
    components: {
      ToolBar,
      HelpHint,
      RoleEdit,
      PermissionsEdit,
      MenuEdit
    }
  }
</script>
<style lang="less">

</style>
