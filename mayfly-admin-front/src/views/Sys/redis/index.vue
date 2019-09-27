<template>
  <div>
    <ToolBar>
      <el-button type="primary" icon="el-icon-plus" size="mini" @click="save(false)" plain>添加</el-button>
      <el-input placeholder="host" size="mini" style="width: 140px" v-model="params.host" @clear="search" plain
        clearable>
      </el-input>
      <!-- <el-input placeholder="请输入api功能！" size="small" style="width: 140px" v-model="params.name" @clear="search"
       clearable>
      </el-input> -->
      <el-select v-model="params.clusterId" size="mini" clearable placeholder="集群选择">
        <el-option v-for="item in clusters" :key="item.id" :value="item.id" :label="item.name">
        </el-option>
      </el-select>
      <el-button @click="search" type="success" icon="el-icon-search" size="mini"></el-button>
    </ToolBar>
    <el-table :data="redisTable" stripe style="width: 100%">
      <el-table-column prop="host" label="IP" width="">
      </el-table-column>
      <el-table-column prop="port" label="端口" width="">
      </el-table-column>
      <el-table-column prop="clusterId" label="集群id">
      </el-table-column>
      <el-table-column prop="desc" label="描述">
      </el-table-column>
      <el-table-column label="操作" width="">
        <template slot-scope="scope">
          <el-button v-permission="permission.info.code" type="primary" @click="info(scope.row.id)" :ref="scope.row" icom="el-icon-tickets" size="mini"
            plain>info</el-button>
          <el-button v-permission="keyPermission.scan.code" type="success" @click="manage(scope.row)" :ref="scope.row" size="mini" plain>数据管理</el-button>
        </template>
      </el-table-column>
    </el-table>

    <Info :visible="infoDialog.visible" :info="infoDialog.info" @close="infoDialog.visible = false"></Info>
  </div>
</template>

<script>
  import ToolBar from '~/components/ToolBar/ToolBar.vue';
  import Info from './info.vue';
  import Req from "~/common/request"
  import permission from '../permissions.js'
  // 
  export default {
    data() {
      return {
        redisTable: [],
        permission: permission.redis,
        keyPermission: permission.redisKey,
        form: {
          host: "",
          port: 6379,
          password: ''
        },
        params: {
          host: null,
          clusterId: null
        },
        redisInfo: {
          url: ""
        },
        clusters: [{
          id: 0,
          name: '单机'
        }],
        infoDialog: {
          visible: false,
          info: {
            Server: {},
            Keyspace: {},
            Clients: {},
            CPU: {},
            Memory: {}
          }
        }
      };
    },
    methods: {
      connect() {
        Req.post("/open/redis/connect", this.form, res => {
          this.redisInfo = res;
        })
      },
      manage(row) {
        this.$router.push(`/redis_operation/${row.clusterId}/${row.id}`);
      },
      info(id) {
        this.permission.info.request({id}).then(res => {
          this.infoDialog.info = res;
          this.infoDialog.id = id;
          this.infoDialog.visible = true;
        })
      },
      search() {
        this.permission.list.request(this.params).then(res => {
          this.redisTable = res;
        })
      }
    },
    mounted() {
      this.search();
    },
    components: {
      ToolBar,
      Info
    }
  }
</script>

<style>

</style>
