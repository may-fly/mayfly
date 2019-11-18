<template>
  <div>
    <ToolBar>
      <el-button v-permission="permission.save.code" type="primary" icon="el-icon-plus" size="mini" @click="edit(false)" plain>添加</el-button>
      <el-button v-permission="permission.serviceManage.code" type="success" :disabled="currentId == null" @click="fileManage(currentData)" size="mini" plain>文件管理</el-button>
      <el-button v-permission="permission.del.code" :disabled="currentId == null" @click="deleteMachine(currentId)" type="danger" icon="el-icon-delete"
        size="mini">删除</el-button>
        
      <div style="float: right;">
        <el-input placeholder="host" size="mini" style="width: 140px;" v-model="params.host" @clear="search" plain
          clearable>
        </el-input>
        <el-button @click="search" type="success" icon="el-icon-search" size="mini"></el-button>
      </div>
    </ToolBar>

    <el-table :data="table" stripe style="width: 100%" @current-change="choose">
      <el-table-column label="选择" width="55px">
        <template slot-scope="scope">
          <el-radio v-model="currentId" :label="scope.row.id"><i></i></el-radio>
        </template>
      </el-table-column>
      <el-table-column prop="name" label="名称" width="">
      </el-table-column>
      <el-table-column prop="ip" label="IP" width="">
      </el-table-column>
      <el-table-column prop="port" label="端口" width="">
      </el-table-column>
      <el-table-column prop="username" label="用户名">
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间">
      </el-table-column>
      <el-table-column prop="updateTime" label="更新时间">
      </el-table-column>
      <!-- <el-table-column label="操作" width="200px">
        <template slot-scope="scope">
          <el-button type="primary" @click="info(scope.row.id)" :ref="scope.row" icom="el-icon-tickets" size="mini"
            plain>info</el-button>
          <el-button type="success" @click="serviceManager(scope.row)" :ref="scope.row" size="mini" plain>服务管理</el-button>
        </template>
      </el-table-column> -->
    </el-table>

    <FileManage :title="dialog.title" :visible="dialog.visible" :machineId="dialog.machineId" @cancel="cancel()" />

    <MachineEdit :visible="editDialog.visible" :machine="editDialog.data" @cancel="editCancel()"></MachineEdit>
  </div>
</template>

<script>
  import ToolBar from '~/components/ToolBar/ToolBar.vue';
  import Req from "~/common/request"
  import FileManage from "./file_manage.vue"
  import MachineEdit from "./machine_edit.vue"
  import permission from '../permissions.js'

  export default {
    data() {
      return {
        websocket: null,
        websocket2: null,
        table: [],
        permission: permission.machine,
        currentId: null,
        currentData: null,
        params: {
          host: null,
          clusterId: null
        },
        editDialog: {
          visible: false,
          data: null
        },
        dialog: {
          machineId: null,
          visible: false,
          title: null
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
      edit(data) {
        if (!data) {
          this.editDialog.visible = true;
        }
      },
      editCancel() {
        this.editDialog.visible = false
        this.editDialog.data = null
      },
      deleteMachine(id) {
        this.permission.list.request({id}).then(res => {
          this.$message.success("操作成功");
          this.search();
        })
      },
      fileManage(row) {
        this.dialog.machineId = row.id
        this.dialog.visible = true
        this.dialog.title = `${row.name} => ${row.ip}`
      },
      cancel() {
        this.dialog.visible = false
        this.dialog.machineId = null
      },
      search() {
        this.permission.list.request(this.params).then(res => {
          this.table = res;
        })
      }
    },
    mounted() {
      this.search();
      console.log(this.$SysMsgSocket)
      // this.websocket2 = this.$Utils.socketBuilder('ws://localhost:8080/mayfly/testsocket/' + sessionStorage.getItem('token')).message(event => {this.$message.success(event.data)})
      // this.websocket = this.$Utils.socketBuilder('ws://localhost:8080/mayfly/sysmsg/' + sessionStorage.getItem('token')).message(event => {this.$message.success(event.data)})
    },
    components: {
      ToolBar,
      FileManage,
      MachineEdit
    }
  }
</script>

<style>

</style>
