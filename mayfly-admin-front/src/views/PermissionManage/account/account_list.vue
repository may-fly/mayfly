<template>
  <div class="role-list">
    <ToolBar>
      <el-button type="primary" icon="el-icon-plus" size="small" @click="edit()">添加</el-button>
      <div style="float: right">
        <el-input placeholder="请输入账号名" size="small" style="width: 140px" v-model="query.username" @clear="edit()"
          clearable>
        </el-input>
        <el-button @click="edit()" type="success" icon="el-icon-search" size="small"></el-button>
      </div>
    </ToolBar>
    <el-table :data="datas" border ref="table" style="width: 100%">
      <el-table-column label="序号" type="index"></el-table-column>
      <el-table-column prop="username" label="用户名">
      </el-table-column>
      <el-table-column prop="remark" label="描述">
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间">
      </el-table-column>
      <el-table-column prop="updateTime" label="修改时间">
      </el-table-column>
      <el-table-column label="操作" width="350px">
        <template slot-scope="scope">
          <el-button @click="edit()" type="primary" icon="el-icon-edit" size="small">编辑</el-button>
          <el-button v-if="scope.row.status == 0" @click="changeStatus(scope.row, 1)"
            type="success" :ref="scope.row.id" size="small">启用</el-button>
          <el-button v-if="scope.row.status == 1" @click="changeStatus(scope.row, 0)"
            type="danger" size="small">禁用</el-button>
          <el-button @click="edit()" type="success" icon="el-icon-setting" size="small">角色分配</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination @current-change="handlePageChange" style="text-align: center;margin-top: 20px;" background layout="prev, pager, next, total, jumper"
      :total="total" :current-page.sync="query.pageNum" :page-size="query.pageSize" />
  </div>
</template>

<script>
  import ToolBar from '~/components/ToolBar/ToolBar.vue';
  import HelpHint from '~/components/HelpHint/HelpHint.vue';
  import permission from '../permissions.js';
 
  export default {
    data() {
      return {
        permission: permission.account,
        query: {
          pageNum: 1,
          pageSize: 10
        },
        datas: [],
        total: null,
        menuDialog: {
          visible: false,
          role: {}
        },
        permissionDialog: {
          username: null,
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
      search() {
        this.permission.list.request(this.query).then(res => {
          this.datas = res.list;
          this.total = res.total;
        });
      },
      handlePageChange(curPage) {
        this.query.pageNum = curPage;
        this.search();
      },
      edit() {
        
      }
    },
    mounted() {
      this.search();
    },
    components: {
      ToolBar,
      HelpHint
    }
  }
</script>
<style lang="less">

</style>
