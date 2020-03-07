<template>
  <div class="role-list">
    <ToolBar>
      <el-button v-permission="permission.role.code" type="primary" icon="el-icon-plus" size="mini" @click="editRole(false)">添加</el-button>
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
      <el-table-column prop="remark" label="描述" show-overflow-tooltip>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间">
      </el-table-column>
      <el-table-column prop="updateTime" label="修改时间">
      </el-table-column>
      <el-table-column label="操作" width="350px">
        <template slot-scope="scope">
          <el-button v-permission="permission.role.code" @click="editRole(scope.row)" type="primary" icon="el-icon-edit"
            size="mini">编辑</el-button>
          <el-button v-permission="permission.saveResources.code" @click="editResource(scope.row)" type="primary" icon="el-icon-setting"
            size="mini">分配菜单&权限</el-button>
          <el-button v-permission="permission.del.code" @click="deleteRole(scope.row)" type="danger" icon="el-icon-delete"
            size="mini">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <role-edit :title="roleEdit.title" :visible="roleEdit.visible" :data="roleEdit.data" @val-change="roleEditChange"
      @cancel="roleEdit.visible = false">
    </role-edit>
    <resource-edit :visible="resourceDialog.visible" :role="resourceDialog.role" :resources="resourceDialog.resources"
      :defaultCheckedKeys="resourceDialog.defaultCheckedKeys" @cancel="cancelEditResources()"></resource-edit>
  </div>
</template>

<script>
  import ToolBar from '~/components/tool-bar/tool-bar.vue';
  import HelpHint from '~/components/help-hint/help-hint.vue';
  import RoleEdit from './role-edit.vue'
  import { rolePermission } from '../permissions.js';
  import ResourceEdit from './resource-edit.vue';
  import { roleApi, resourceApi } from '../api'

  export default {
    data() {
      return {
        permission: rolePermission,
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
        this.roleEdit.visible = false;
        this.$message.success("修改成功！");
        this.search();
      },
      editRole(data) {
        if (data) {
          this.roleEdit.data = data;
        } else {
          this.roleEdit.data = false;
        }

        this.roleEdit.visible = true;
      },
      async deleteRole(data) {
        try {
          await this.$confirm(`此操作将删除 [${data.name}] 该角色，以及角色关联的用户和资源信息, 是否继续?`, '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          });
          await roleApi.del.request({
            id: data.id
          });
          this.$message.success("删除成功！");
          this.search();
        } catch (err) {
          
        }
      },
      async editResource(row) {
        let menus = await resourceApi.list.request(null);
        // 获取所有菜单列表
        this.resourceDialog.resources = menus;
        // 获取该角色拥有的菜单id
        let roles = await roleApi.rolePermissions.request({
          id: row.id
        });
        let hasIds = roles;
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
        // 显示
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
        setTimeout(() => {
          this.resourceDialog.role = false;
          this.resourceDialog.defaultCheckedKeys = [];
        }, 10);
      },
      async search() {
        let res = await roleApi.list.request(null);
        this.roles = res;
      }
    },
    mounted() {
      this.search();
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
