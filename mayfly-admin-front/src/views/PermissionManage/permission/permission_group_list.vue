<template>
  <div class="user-list">
    <ToolBar>
      <!-- <div style="float: left">
				<el-input placeholder="请输入api功能！" size="small" style="width: 140px" v-model="params.name" @clear="searchApi"
				 clearable>
				</el-input>
				<el-button @click="searchApi" type="success" icon="el-icon-search" size="small"></el-button>
			</div> -->
      <div style="float: left">
        <el-button @click="editApiGroup" type="primary" icon="el-icon-plus" size="small">添加</el-button>
      </div>
    </ToolBar>
    <el-table :data="groupList" border ref="table" style="width: 100%">
      <el-table-column label="序号" type="index"> </el-table-column>
      <el-table-column prop="name" label="名称">
      </el-table-column>
      <el-table-column prop="description" label="描述">
      </el-table-column>
      <el-table-column prop="status" label="状态">
        <div slot-scope="scope" style="width: 100%;">
          <span v-if="scope.row.status == 0">禁用</span>
          <span type="success" v-if="scope.row.status == 1">启用</span>
        </div>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间">
      </el-table-column>
      <el-table-column prop="updateTime" label="更新时间">
      </el-table-column>
      <el-table-column label="操作">
       <!-- <template slot-scope="scope">
          <el-button @click="changeStatus(scope.row, 1)"
            type="success" :ref="scope.row.id" size="small">启用</el-button>
          <el-button @click="changeStatus(scope.row, 0)"
            type="danger" size="small">禁用</el-button>
          <el-button @click="deleteApi(scope.row.id)" type="danger"
            size="small">删除</el-button>
        </template> -->
      </el-table-column>
    </el-table>
    <el-pagination @current-change="handlePageChange" style="text-align: center;margin-top: 20px;" background layout="prev, pager, next, total, jumper"
      :total="pageInfo.total" :current-page.sync="pageInfo.pageNum" :page-size="pageInfo.pageSize">
    </el-pagination>
    <ApiGroupEdit :title="dialogForm.title" :visible="dialogForm.visible" :data="dialogForm.data" @cancel="dialogForm.visible = false"
      @val-change="valChange">
    </ApiGroupEdit>
  </div>
</template>

<script>
  import ToolBar from '~/components/ToolBar/ToolBar.vue';
  import HelpHint from '~/components/HelpHint/HelpHint.vue';
  import ApiGroupEdit from './permission_group_edit.vue'
  import permissions from '../permissions'

  export default {
    data() {
      return {
        permission: permissions.permissionGroup,
        //弹出框对象
        dialogForm: {
          title: "保存权限组",
          visible: false,
          data: {}

        },
        params: {
          name: '',
        },
        //api组列表对象
        groupList: [],
        //分页对象
        pageInfo: {
          pageSize: 8,
          pageNum: 1,
          total: 0
        }
      }
    },
    methods: {
      editApiGroup() {
        this.dialogForm.visible = true;
      },
      handlePageChange(curPage) {
        // 				this.$Api.permission.getApis(null, data => {
        // 					this.apiList = data;
        // 				})
      },
      save(group) {
        this.dialogForm.visible = true;
        this.dialogForm.data = group;
      },
      //修改成功后的回调
      valChange(api) {
        this.btns.list.request(null, res => {
          this.groupList = res;
          this.dialogForm.visible = false;
        })
      },
      deleteApi(id) {
        //发送删除请求
        this.btns.del.request({
          id
        }, res => {
          this.btns.search.request(null, res => {
            this.apiList = res;
          })
        })
      },
      changeStatus(api, status) {
        this.btns.changeStatus.request({
          id: api.id,
          status
        }, res => {
          api.status = res.status;
          api.updateTime = res.updateTime;
        })
      }
    },
    mounted: function() {
      this.permission.list.request(this.pageInfo, res => {
        this.groupList = res.list;
        this.pageInfo.total = res.total;
      })
    },
    components: {
      ToolBar,
      HelpHint,
      ApiGroupEdit
    }
  }
</script>
<style lang="less">

</style>
