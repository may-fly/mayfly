<template>
  <div class="role-list">
    <div class="toolbar">
      <el-button
        v-permission="permission.role.code"
        type="primary"
        icon="el-icon-plus"
        size="mini"
        @click="editRole(false)"
      >添加</el-button>
      <el-button
        v-permission="permission.role.code"
        :disabled="chooseId == null"
        @click="editRole(chooseData)"
        type="primary"
        icon="el-icon-edit"
        size="mini"
      >编辑</el-button>
      <el-button
        v-permission="permission.saveResources.code"
        :disabled="chooseId == null"
        @click="editResource(chooseData)"
        type="success"
        icon="el-icon-setting"
        size="mini"
      >分配菜单&权限</el-button>
      <el-button
        v-permission="permission.del.code"
        :disabled="chooseId == null"
        @click="deleteRole(chooseData)"
        type="danger"
        icon="el-icon-delete"
        size="mini"
      >删除</el-button>

      <div style="float: right">
        <el-input
          placeholder="请输入角色名称！"
          size="small"
          style="width: 140px"
          v-model="query.name"
          @clear="search"
          clearable
        ></el-input>
        <el-button @click="search" type="success" icon="el-icon-search" size="mini"></el-button>
      </div>
    </div>
    <el-table :data="roles" @current-change="choose" border ref="table" style="width: 100%">
      <el-table-column label="选择" width="50px">
        <template slot-scope="scope">
          <el-radio v-model="chooseId" :label="scope.row.id">
            <i></i>
          </el-radio>
        </template>
      </el-table-column>
      <el-table-column prop="name" label="角色名称"></el-table-column>
      <el-table-column prop="remark" label="描述" min-width="180px" show-overflow-tooltip></el-table-column>
      <el-table-column prop="createTime" label="创建时间"></el-table-column>
      <el-table-column prop="updateTime" label="修改时间"></el-table-column>
      <el-table-column label="查看更多" min-width="80px">
        <template slot-scope="scope">
          <el-link
            v-permission="permission.role.code"
            @click="showResources(scope.row)"
            type="info"
          >菜单&权限</el-link>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      @current-change="handlePageChange"
      style="text-align: center;"
      background
      layout="prev, pager, next, total, jumper"
      :total="total"
      :current-page.sync="query.pageNum"
      :page-size="query.pageSize"
    />

    <role-edit
      :title="roleEdit.title"
      :visible="roleEdit.visible"
      :data="roleEdit.data"
      @val-change="roleEditChange"
      @cancel="roleEdit.visible = false"
    />
    <resource-edit
      :visible="resourceDialog.visible"
      :role="resourceDialog.role"
      :resources="resourceDialog.resources"
      :defaultCheckedKeys="resourceDialog.defaultCheckedKeys"
      @cancel="cancelEditResources()"
    />
    <show-resource
      :visible="showResourceDialog.visible"
      :title="showResourceDialog.title"
      :resources="showResourceDialog.resources"
      @close="closeShowResourceDialog"
    />
  </div>
</template>

<script>
import HelpHint from '@/components/help-hint/help-hint.vue'
import RoleEdit from './role-edit.vue'
import { rolePermission } from '../permissions'
import ResourceEdit from './resource-edit.vue'
import ShowResource from './show-resource.vue'
import { roleApi, resourceApi } from '../api'

export default {
  data() {
    return {
      permission: rolePermission,
      dialogFormVisible: false,
      currentEditPermissions: false,
      query: {
        pageNum: 1,
        pageSize: 10,
        name: null
      },
      total: 0,
      roles: [],
      chooseId: null,
      chooseData: null,
      resourceDialog: {
        visible: false,
        role: {},
        resources: [],
        defaultCheckedKeys: []
      },
      roleEdit: {
        title: '角色编辑',
        visible: false,
        role: {}
      },
      showResourceDialog: {
        visible: false,
        resources: [],
        title: ''
      }
    }
  },
  methods: {
    async search() {
      let res = await roleApi.list.request(this.query)
      this.roles = res.list
      this.total = res.total
    },
    handlePageChange(curPage) {
      this.query.pageNum = curPage
      this.search()
    },
    choose(item) {
      if (!item) {
        return
      }
      this.chooseId = item.id
      this.chooseData = item
    },
    roleEditChange(data) {
      this.roleEdit.visible = false
      this.$message.success('修改成功！')
      this.search()
    },
    editRole(data) {
      if (data) {
        this.roleEdit.data = data
      } else {
        this.roleEdit.data = false
      }

      this.roleEdit.visible = true
    },
    async deleteRole(data) {
      try {
        await this.$confirm(
          `此操作将删除 [${data.name}] 该角色，以及与该角色有关的账号角色关联信息和资源角色关联信息, 是否继续?`,
          '提示',
          {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }
        )
        await roleApi.del.request({
          id: data.id
        })
        this.$message.success('删除成功！')
        this.search()
      } catch (err) {}
    },
    async showResources(row) {
      this.showResourceDialog.resources = await roleApi.roleResources.request({
        id: row.id
      })
      this.showResourceDialog.title = '"' + row.name + '"的菜单&权限'
      this.showResourceDialog.visible = true
    },
    closeShowResourceDialog() {
      this.showResourceDialog.visible = false
      this.showResourceDialog.resources = []
    },
    async editResource(row) {
      let menus = await resourceApi.list.request()
      // 获取所有菜单列表
      this.resourceDialog.resources = menus
      // 获取该角色拥有的菜单id
      let roles = await roleApi.roleResourceIds.request({
        id: row.id
      })
      let hasIds = roles
      let hasLeafIds = []
      // 获取菜单的所有叶子节点
      let leafIds = this.getAllLeafIds(this.resourceDialog.resources)
      for (let id of leafIds) {
        // 判断角色拥有的菜单id中，是否含有该叶子节点，有则添加进入用户拥有的叶子节点
        if (hasIds.includes(id)) {
          hasLeafIds.push(id)
        }
      }
      this.resourceDialog.defaultCheckedKeys = hasLeafIds
      // 显示
      this.resourceDialog.visible = true
      this.resourceDialog.role = row
    },
    /**
     * 获取所有菜单树的叶子节点
     * @param {Object} trees  菜单树列表
     */
    getAllLeafIds(trees) {
      let leafIds = []
      for (let tree of trees) {
        this.setLeafIds(tree, leafIds)
      }
      return leafIds
    },
    setLeafIds(tree, ids) {
      if (tree.children !== null) {
        for (let t of tree.children) {
          this.setLeafIds(t, ids)
        }
      } else {
        ids.push(tree.id)
      }
    },
    /**
     * 取消编辑资源权限树
     */
    cancelEditResources() {
      this.resourceDialog.visible = false
      setTimeout(() => {
        this.resourceDialog.role = false
        this.resourceDialog.defaultCheckedKeys = []
      }, 10)
    }
  },
  mounted() {
    this.search()
  },
  components: {
    HelpHint,
    RoleEdit,
    ResourceEdit,
    ShowResource
  }
}
</script>
<style lang="less">
</style>
