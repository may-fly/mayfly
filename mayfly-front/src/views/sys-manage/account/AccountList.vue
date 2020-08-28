<template>
  <div class="role-list">
    <div class="toolbar">
      <el-button
        v-permission="permission.account.code"
        type="primary"
        icon="el-icon-plus"
        size="mini"
        @click="editAccount(true)"
      >添加</el-button>
      <el-button
        v-permission="permission.account.code"
        :disabled="chooseId == null"
        @click="editAccount(false)"
        type="primary"
        icon="el-icon-edit"
        size="mini"
      >编辑</el-button>
      <el-button
        v-permission="permission.saveRoles.code"
        :disabled="chooseId == null"
        @click="roleEdit()"
        type="success"
        icon="el-icon-setting"
        size="mini"
      >角色分配</el-button>
      <el-button
        v-permission="permission.del.code"
        :disabled="chooseId == null"
        @click="deleteAccount()"
        type="danger"
        icon="el-icon-delete"
        size="mini"
      >删除</el-button>
      <div style="float: right">
        <el-input
          placeholder="请输入账号名"
          size="small"
          style="width: 140px"
          v-model="query.username"
          @clear="search()"
          clearable
        ></el-input>
        <el-button @click="search()" type="success" icon="el-icon-search" size="mini"></el-button>
      </div>
    </div>
    <el-table :data="datas" border ref="table" @current-change="choose" show-overflow-tooltip>
      <el-table-column label="选择" width="50px">
        <template slot-scope="scope">
          <el-radio v-model="chooseId" :label="scope.row.id">
            <i></i>
          </el-radio>
        </template>
      </el-table-column>
      <el-table-column prop="username" label="用户名" min-width="115"></el-table-column>

      <el-table-column align="center" prop="status" label="状态" min-width="63">
        <template slot-scope="scope">
          <el-tooltip :content="scope.row.status == 1 ? '启用' : '禁用'" placement="top">
            <el-switch
              v-model="scope.row.status"
              :active-value="1"
              active-color="#13ce66"
              inactive-color="#ff4949"
              @change="changeStatus(scope.row)"
              :disabled="!$Permission.getPermission(permission.changeStatus.code).show || $Permission.getPermission(permission.changeStatus.code).disabled"
            ></el-switch>
          </el-tooltip>
        </template>
      </el-table-column>

      <el-table-column min-width="115" prop="creator" label="创建账号"></el-table-column>
      <el-table-column min-width="160" prop="createTime" label="创建时间"></el-table-column>
      <el-table-column min-width="115" prop="modifier" label="更新账号"></el-table-column>
      <el-table-column min-width="160" prop="updateTime" label="修改时间"></el-table-column>
      <el-table-column min-width="160" prop="lastLoginTime" label="最后登录时间"></el-table-column>
      <el-table-column label="查看更多" min-width="150">
        <template slot-scope="scope">
          <el-link
            v-permission="permission.account.code"
            @click="showRoles(scope.row)"
            type="success"
          >角色</el-link>

          <el-link
            v-permission="permission.account.code"
            @click="showResources(scope.row)"
            type="info"
          >菜单&权限</el-link>
        </template>
      </el-table-column>
      <el-table-column min-width="120" prop="remark" label="备注" show-overflow-tooltip></el-table-column>
    </el-table>
    <el-pagination
      @current-change="handlePageChange"
      style="text-align: center"
      background
      layout="prev, pager, next, total, jumper"
      :total="total"
      :current-page.sync="query.pageNum"
      :page-size="query.pageSize"
    />

    <el-dialog width="500px" :title="showRoleDialog.title" :visible.sync="showRoleDialog.visible">
      <el-table border :data="showRoleDialog.accountRoles">
        <el-table-column property="name" label="角色名" width="125"></el-table-column>
        <el-table-column property="creator" label="分配账号" width="125"></el-table-column>
        <el-table-column property="createTime" label="分配时间"></el-table-column>
      </el-table>
    </el-dialog>

    <el-dialog
      :title="showResourceDialog.title"
      :visible.sync="showResourceDialog.visible"
      width="400px"
      height="400px"
    >
      <el-tree
        style="height: 50vh;overflow: auto;"
        :data="showResourceDialog.resources"
        node-key="id"
        :props="showResourceDialog.defaultProps"
        :expand-on-click-node="false"
      >
        <span class="custom-tree-node" slot-scope="{ node, data }">
          <span v-if="data.type == enums.ResourceTypeEnum.MENU.value">{{ node.label }}</span>
          <span
            v-if="data.type == enums.ResourceTypeEnum.PERMISSION.value"
            style="color: #67c23a;"
          >{{ node.label }}</span>
        </span>
      </el-tree>
    </el-dialog>

    <role-edit :visible="roleDialog.visible" :account="roleDialog.account" @cancel="cancel()" />
    <account-edit
      :visible.sync="accountDialog.visible"
      :account.sync="accountDialog.data"
      @val-change="valChange()"
    />
  </div>
</template>

<script lang='ts'>
import { Component, Vue } from 'vue-property-decorator'
import HelpHint from '@/components/help-hint/HelpHint.vue'
import { accountPermission } from '../permissions'
import RoleEdit from './RoleEdit.vue'
import AccountEdit from './AccountEdit.vue'
import enums from '../enums'
import { accountApi } from '../api'

@Component({
  name: 'account-list',
  components: {
    HelpHint,
    RoleEdit,
    AccountEdit
  }
})
export default class AccountList extends Vue {
  private enums: any = enums
  private permission: any = accountPermission
  /**
   * 选中的id
   */
  private chooseId: number | null = null
  /**
   * 选中的数据
   */
  private chooseData: any | null = null
  /**
   * 查询条件
   */
  private query = {
    pageNum: 1,
    pageSize: 10
  }
  private datas: Array<any> = []
  private total: number = 0
  private showRoleDialog = {
    title: '',
    visible: false,
    accountRoles: []
  }
  private showResourceDialog = {
    title: '',
    visible: false,
    resources: [],
    defaultProps: {
      children: 'children',
      label: 'name'
    }
  }
  private roleDialog = {
    visible: false,
    account: null,
    roles: []
  }
  private accountDialog = {
    visible: false,
    data: null
  }

  mounted() {
    this.search()
  }

  choose(item: any) {
    if (!item) {
      return
    }
    this.chooseId = item.id
    this.chooseData = item
  }

  async search() {
    let res: any = await accountApi.list.request(this.query)
    this.datas = res.list
    this.total = res.total
  }

  async showResources(row: any) {
    let showResourceDialog = this.showResourceDialog
    showResourceDialog.title = '"' + row.username + '" 的菜单&权限'
    showResourceDialog.resources = []
    showResourceDialog.resources = await accountApi.resources.request({
      id: row.id
    })
    showResourceDialog.visible = true
  }

  async showRoles(row: any) {
    let showRoleDialog = this.showRoleDialog
    showRoleDialog.title = '"' + row.username + '" 的角色信息'
    showRoleDialog.accountRoles = await accountApi.roles.request({
      id: row.id
    })
    showRoleDialog.visible = true
  }

  async changeStatus(row: any) {
    let id = row.id
    let status = row.status ? 1 : 0
    await accountApi.changeStatus.request({
      id,
      status
    })
    this.$message.success('操作成功')
    this.search()
  }

  handlePageChange(curPage: number) {
    this.query.pageNum = curPage
    this.search()
  }

  roleEdit() {
    if (!this.chooseId) {
      this.$message.error('请选择账号')
    }
    this.roleDialog.visible = true
    this.roleDialog.account = this.chooseData
  }

  editAccount(isAdd = false) {
    if (isAdd) {
      this.accountDialog.data = null
    } else {
      this.accountDialog.data = this.chooseData
    }
    this.accountDialog.visible = true
  }

  cancel() {
    this.roleDialog.visible = false
    this.roleDialog.account = null
    this.search()
  }

  valChange() {
    this.accountDialog.visible = false
    this.search()
  }

  async deleteAccount() {
    try {
      await accountApi.del.request({ id: this.chooseId })
      this.$message.success('删除成功')
      this.search()
    } catch (error) {
      this.$message.error('刪除失败')
    }
  }
}
</script>
<style lang="less">
</style>
