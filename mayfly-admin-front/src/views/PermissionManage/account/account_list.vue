<template>
  <div class="role-list">
    <ToolBar>
      <el-button v-permission="permission.save.code" type="primary" icon="el-icon-plus" size="mini" @click="editAccount(true)">添加</el-button>
      <el-button v-permission="permission.save.code" :disabled="currentId == null" @click="editAccount(false)" type="primary" icon="el-icon-edit" size="mini">编辑</el-button>
      <el-button v-permission="permission.saveRoles.code" :disabled="currentId == null" @click="roleEdit()" type="success" icon="el-icon-setting" size="mini">角色分配</el-button>
      <el-button v-permission="permission.del.code" :disabled="currentId == null" @click="deleteAccount()" type="danger" icon="el-icon-delete" size="mini">删除</el-button>
      <div style="float: right">
        <el-input placeholder="请输入账号名" size="small" style="width: 140px" v-model="query.username" @clear="edit()"
          clearable>
        </el-input>
        <el-button @click="edit()" type="success" icon="el-icon-search" size="mini"></el-button>
      </div>
    </ToolBar>
    <el-table :data="datas" border ref="table" style="width: 100%" @current-change="choose">
      <el-table-column label="选择" width="55px">
        <template slot-scope="scope">
          <el-radio v-model="currentId" :label="scope.row.id"><i></i></el-radio>
        </template>
      </el-table-column>
      <el-table-column label="序号" type="index"></el-table-column>
      <el-table-column prop="username" label="用户名" width="115vh">
      </el-table-column>
      <el-table-column prop="status" label="角色">
        <template slot-scope="scope">
          <el-tag v-for="role in scope.row.roles" :type="role.status === 1 ? 'success' : 'danger'">{{role.name}}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="status" label="状态" width="60">
        <template slot-scope="scope">
          {{scope.row.status == 1 ? '启用' : '禁用'}}
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间">
      </el-table-column>
      <el-table-column prop="updateTime" label="修改时间">
      </el-table-column>
      <el-table-column prop="remark" label="备注">
      </el-table-column>
      <el-table-column label="操作" width="80px">
        <template slot-scope="scope">
          <el-button v-permission="permission.changeStatus.code" v-if="scope.row.status == 0" @click="changeStatus(scope.row.id, 1)" type="success" size="mini">启用</el-button>
          <el-button v-permission="permission.changeStatus.code" v-if="scope.row.status == 1" @click="changeStatus(scope.row.id, 0)" type="danger" size="mini">禁用</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination @current-change="handlePageChange" style="text-align: center;margin-top: 20px;" background layout="prev, pager, next, total, jumper"
      :total="total" :current-page.sync="query.pageNum" :page-size="query.pageSize" />

    <RoleEdit :visible="roleDialog.visible" :account="roleDialog.account" @cancel="cancel()"></RoleEdit>
    <AccountEdit :visible="accountDialog.visible" :account="accountDialog.data" @cancel="accountDialogCancel()"
      @val-change="valChange()"></AccountEdit>
  </div>
</template>

<script>
  import ToolBar from '~/components/ToolBar/ToolBar.vue';
  import HelpHint from '~/components/HelpHint/HelpHint.vue';
  import permission from '../permissions.js';
  import RoleEdit from './role_edit.vue';
  import AccountEdit from './account_edit.vue'

  export default {
    data() {
      return {
        permission: permission.account,
        currentId: null,
        currentData: null,
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
          return;
        }
        this.currentId = item.id;
        this.currentData = item;
      },
      search() {
        this.permission.list.request(this.query).then(res => {
          this.datas = res.list;
          this.total = res.total;
        });
      },
      changeStatus(id, status) {
        this.permission.changeStatus.request({
          id,
          status
        }).then(res => {
          this.$message.success("操作成功");
          this.search();
        })
      },
      handlePageChange(curPage) {
        this.query.pageNum = curPage;
        this.search();
      },
      roleEdit() {
        if (!this.currentId) {
          this.$message.error("请选择账号");
        }
        this.roleDialog.visible = true;
        this.roleDialog.account = this.currentData;
      },
      editAccount(isAdd = false) {
        if (isAdd) {
          this.accountDialog.data = false
        } else {
          this.accountDialog.data = this.currentData
        }
        this.accountDialog.visible = true;
      },
      cancel() {
        this.roleDialog.visible = false;
        this.roleDialog.account = false;
        this.search();
      },
      accountDialogCancel() {
        this.accountDialog.visible = false;
        setTimeout(() => {
          this.accountDialog.data = false;
        }, 800)
      },
      valChange() {
        this.accountDialog.visible = false;
        this.search();
      },
      deleteAccount() {
        this.permission.del.request({id: this.currentId}).then(res => {
          this.$message.success("删除成功");
          this.search();
        })
      }
    },
    mounted() {
      this.search();
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
