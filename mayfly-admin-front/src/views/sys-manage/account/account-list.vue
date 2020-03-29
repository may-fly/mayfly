<template>
  <div class="role-list">
    <ToolBar>
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
    </ToolBar>
    <el-table :data="datas" border ref="table" @current-change="choose" show-overflow-tooltip>
      <el-table-column label="选择" width="50px">
        <template slot-scope="scope">
          <el-radio v-model="chooseId" :label="scope.row.id">
            <i></i>
          </el-radio>
        </template>
      </el-table-column>
      <el-table-column prop="username" label="用户名" min-width="115"></el-table-column>

      <el-table-column prop="status" label="状态" min-width="95">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.status == enums.accountStatus.DISABLE.value" type="danger">禁用</el-tag>
          <el-tag v-else type="success">正常</el-tag>
          <el-popover placement="bottom" width="60" trigger="click">
            <el-select
              size="mini"
              @change="changeStatus(scope.row.id, scope.row.status)"
              v-model="scope.row.status"
              placeholder="状态"
            >
              <el-option
                v-for="item in enums.accountStatus"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              ></el-option>
            </el-select>
            <el-link slot="reference" icon="el-icon-edit" />
          </el-popover>
        </template>
      </el-table-column>

      <el-table-column min-width="115" prop="createAccount" label="创建账号"></el-table-column>
      <el-table-column min-width="160" prop="createTime" label="创建时间"></el-table-column>
      <el-table-column min-width="115" prop="updateAccount" label="更新账号"></el-table-column>
      <el-table-column min-width="160" prop="updateTime" label="修改时间"></el-table-column>
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

    <RoleEdit :visible="roleDialog.visible" :account="roleDialog.account" @cancel="cancel()"></RoleEdit>
    <AccountEdit
      :visible="accountDialog.visible"
      :account="accountDialog.data"
      @cancel="accountDialogCancel()"
      @val-change="valChange()"
    ></AccountEdit>
  </div>
</template>

<script>
import ToolBar from '~/components/tool-bar/tool-bar.vue'
import HelpHint from '~/components/help-hint/help-hint.vue'
import { accountPermission } from '../permissions.js'
import RoleEdit from './role-edit.vue'
import AccountEdit from './account-edit.vue'
import enums from '../enums'
import { accountApi } from '../api'

export default {
  data() {
    return {
      enums: enums,
      permission: accountPermission,
      chooseId: null,
      chooseData: null,
      query: {
        pageNum: 1,
        pageSize: 10
      },
      datas: [],
      total: null,
      roleDialog: {
        visible: false,
        account: {},
        roles: []
      },
      accountDialog: {
        visible: false,
        data: false
      }
    }
  },
  methods: {
    choose(item) {
      if (!item) {
        return
      }
      this.chooseId = item.id
      this.chooseData = item
    },
    async search() {
      let res = await accountApi.list.request(this.query)
      this.datas = res.list
      this.total = res.total
    },
    showResources(row) {},
    showRoles(row) {},
    async changeStatus(id, status) {
      await accountApi.changeStatus.request({
        id,
        status
      })
      this.$message.success('操作成功')
      this.search()
    },
    handlePageChange(curPage) {
      this.query.pageNum = curPage
      this.search()
    },
    roleEdit() {
      if (!this.chooseId) {
        this.$message.error('请选择账号')
      }
      this.roleDialog.visible = true
      console.log(this.chooseData)
      this.roleDialog.account = this.chooseData
    },
    editAccount(isAdd = false) {
      if (isAdd) {
        this.accountDialog.data = false
      } else {
        this.accountDialog.data = this.chooseData
      }
      this.accountDialog.visible = true
    },
    cancel() {
      this.roleDialog.visible = false
      this.roleDialog.account = false
      this.search()
    },
    accountDialogCancel() {
      this.accountDialog.visible = false
      setTimeout(() => {
        this.accountDialog.data = false
      }, 800)
    },
    valChange() {
      this.accountDialog.visible = false
      this.search()
    },
    deleteAccount() {
      accountApi.del.request({ id: this.chooseId }).then(res => {
        this.$message.success('删除成功')
        this.search()
      })
    }
  },
  mounted() {
    this.search()
  },
  components: {
    ToolBar,
    HelpHint,
    RoleEdit,
    AccountEdit
  }
}
</script>
<style lang="less">
</style>
