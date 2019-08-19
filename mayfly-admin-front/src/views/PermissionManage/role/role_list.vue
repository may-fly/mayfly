<template>
  <div class="role-list">
    <ToolBar>
      <el-button type="primary" icon="el-icon-plus" size="mini" @click="editRole(false)">添加</el-button>
      <div style="float: right">
        <el-input placeholder="请输入角色名称！" size="small" style="width: 140px" v-model="params.name" @clear="searchRole"
          clearable>
        </el-input>
        <el-button @click="searchRole" type="success" icon="el-icon-search" size="mini"></el-button>
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
      <el-table-column label="操作" width="350px">
        <template slot-scope="scope">
          <el-button @click="editRole(scope.row)" type="primary" icon="el-icon-edit" size="mini">编辑</el-button>
          <el-button @click="editResource(scope.row)" type="primary" icon="el-icon-setting" size="mini">分配菜单&权限</el-button>
        </template>
      </el-table-column>
    </el-table>
    <RoleEdit :title="roleEdit.title" :visible="roleEdit.visible" :data="roleEdit.data" @val-change="roleEditChange"
      @cancel="roleEdit.visible = false">
    </RoleEdit>
    <ResourceEdit :visible="resourceDialog.visible" :role="resourceDialog.role" :resources="resourceDialog.resources"
      :defaultCheckedKeys="resourceDialog.defaultCheckedKeys" @cancel="cancelEditResources()"></ResourceEdit>
  </div>
</template>

<script>
  import ToolBar from '~/components/ToolBar/ToolBar.vue';
  import HelpHint from '~/components/HelpHint/HelpHint.vue';
  import RoleEdit from './role_edit.vue'
  import permission from '../permissions.js';
  import ResourceEdit from './resource_edit.vue';

  export default {
    data() {
      return {
        permission: permission.role,
        dialogFormVisible: false,
        currentEditPermissions: false,
        params: {
          name: '',
        },
        roles: [],
        resourceDialog: {
          visible: false,
          role: {},
          resources: [],
          defaultCheckedKeys: []
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
      roleEditChange(data) {
        let self = this;
        self.roleEdit.visible = false;
      },
      editRole(data) {
        if (data) {
          this.roleEdit.data = data;
        } else {
          this.roleEdit.data = false;
        }

        this.roleEdit.visible = true;
      },
      editResource(row) {
        permission.menu.list.request(null).then(res => {
          // 获取所有菜单列表
          this.resourceDialog.resources = res;
          // 获取该角色拥有的菜单id
          this.permission.rolePermissions.request({
            id: row.id
          }).then(res => {
            let hasIds = res;
            let hasLeafIds = [];
            // 获取菜单的所有叶子节点
            let leafIds = this.getAllLeafIds(this.resourceDialog.resources);
            for (let id of leafIds) {
              // 判断角色拥有的菜单id中，是否含有该叶子节点，有则添加进入用户拥有的叶子节点
              if (hasIds.includes(id)) {
                hasLeafIds.push(id);
              }
            }
            this.resourceDialog.defaultCheckedKeys = hasLeafIds;
          });
        });
        this.resourceDialog.visible = true;
        this.resourceDialog.role = row;
      },
      /**
       * 获取所有菜单树的叶子节点
       * @param {Object} trees  菜单树列表
       */
      getAllLeafIds(trees) {
        let leafIds = [];
        for (let tree of trees) {
          this.setLeafIds(tree, leafIds)
        }
        return leafIds;
      },
      setLeafIds(tree, ids) {
        if (tree.children !== null) {
          for (let t of tree.children) {
            this.setLeafIds(t, ids);
          }
        } else {
          ids.push(tree.id);
        }
      },
      /**
       * 取消编辑资源权限树
       */
      cancelEditResources() {
        this.resourceDialog.visible = false;
        this.resourceDialog.role = false;
        this.resourceDialog.defaultCheckedKeys = [];
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
      ResourceEdit
    }
  }
</script>
<style lang="less">

</style>
