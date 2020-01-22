<template>
  <div>
    <ToolBar>
      <div style="float: left">
        <el-input placeholder="请输入key" style="width: 180px" v-model="scanParam.match" size="mini" @clear="clear()"
          clearable>
        </el-input>
        <el-button @click="search()" type="success" icon="el-icon-search" size="mini" plain>搜索</el-button>
        <el-button type="primary" icon="el-icon-plus" size="mini" @click="save(false)" plain>添加</el-button>
        <el-button @click="scan()" icon="el-icon-refresh" size="mini" plain>换一批</el-button>
      </div>
      <div style="float: right;">
        <!-- <el-button @click="scan()" icon="el-icon-refresh" size="small" plain>刷新</el-button> -->
        <span>keys:{{dbsize}}</span>
      </div>
    </ToolBar>
    <el-table v-loading="loading" :data="keys" border stripe :highlight-current-row="true" style="cursor: pointer;">
      <el-table-column type="selection" width="55">
      </el-table-column>
      <el-table-column show-overflow-tooltip prop="key" label="key"></el-table-column>
      <el-table-column prop="type" label="type" width="80">
        <template slot-scope="scope">
          {{enums.ValueTypeEnum.getLabelByValue(scope.row.type)}}
        </template>
      </el-table-column>
      <el-table-column prop="ttl" label="ttl(过期时间)" width="120">
        <template slot-scope="scope">
          {{ttlConveter(scope.row.ttl)}}
        </template>
      </el-table-column>
      <el-table-column label="操作">
        <template slot-scope="scope">
          <el-button v-permission="permission.value.code" @click="getValue(scope.row.key)" type="success" icon="el-icon-search"
            size="mini" plain>查看</el-button>
          <el-button v-permission="permission.update.code" @click="update(scope.row.key)" type="primary" icon="el-icon-edit"
            size="mini" plain>修改</el-button>
          <el-button v-permission="permission.del.code" @click="del(scope.row.key)" type="danger" size="mini" icon="el-icon-delete"
            plain>删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- <el-pagination @current-change="handlePageChange" style="text-align: center;margin-top: 20px;" background layout="prev, pager, next, total"
      :total="dbsize" :current-page.sync="scanParam.pageNum" :page-size="scanParam.count">
    </el-pagination> -->
    <div style="text-align: center; margin-top: 10px;">

    </div>

    <value-dialog :visible="valueDialog.visible" :keyValue="valueDialog.value" @cancel="closeValueDialog"/>

  </div>
</template>

<script>
  // import Api from "../../../api/index.js"
  import ToolBar from '~/components/tool-bar/tool-bar.vue'
  import Req from "~/common/request"
  import enums from './enums'
  import permission from './permissions.js'
  import ValueDialog from './value-dialog.vue';

  export default {
    data() {
      return {
        permission: permission.redisKey,
        loading: true,
        enums: enums,
        cluster: this.$route.params.cluster,
        redis: {
          id: '',
          info: "",
          conf: ""
        },
        scanParam: {
          match: null,
          count: 12,
          cursor: null,
          prevCursor: null,
        },
        valueDialog: {
          visible: false,
          value: {}
        },
        keys: [],
        dbsize: 0
      };
    },
    methods: {
      scan() {
        this.loading = true;
        this.scanParam.id = this.cluster == 0 ? this.redis.id : this.cluster;
        this.scanParam.cluster = this.cluster == 0 ? 0 : 1;
        this.permission.scan.request(this.scanParam).then(res => {
          // console.log(res)
          this.keys = res.keys;
          this.dbsize = res.dbsize;
          this.scanParam.cursor = res.cursor;
          this.loading = false;
        })
      },
      search() {
        // this.scanParam.match = null;
        this.scanParam.cursor = null;
        // this.scanParam.id = this.cluster == 0 ? this.redis.id : this.cluster;
        // this.scanParam.cluster = this.cluster == 0 ? 0 : 1;
        // Req.request('get', `/open/redis/${isCluster}/${id}/scan`, this.scanParam).then(res => {
        //   // console.log(res)
        //   this.keys = res.keys;
        //   this.dbsize = this.keys.length;
        //   this.scanParam.cursor = res.cursor;
        // })
        this.scan();
      },
      handlePageChange(curPage) {
        this.scanParam.pageNum = curPage;
        this.scan();
      },
      clear() {
        this.scanParam.match = null;
        this.scanParam.cursor = null;
        this.scan();
      },
      async getValue(key) {
        let id = this.cluster == 0 ? this.redis.id : this.cluster;
        let res = await this.permission.value.request({
          cluster: this.cluster,
          key,
          id
        });
        this.valueDialog.value = res;
        this.valueDialog.visible = true;
      },
      closeValueDialog() {
        this.valueDialog.visible = false;
        this.valueDialog.value = {};
      },
      update(key) {

      },
      del(key) {
        this.$confirm(`此操作将删除对应的key , 是否继续?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          let id = this.cluster == 0 ? this.redis.id : this.cluster;
          this.permission.del.request({
            cluster: this.cluster,
            key,
            id
          }).then(res => {
            this.$message.success("删除成功！");
            this.scan();
          })
        }).catch(err => {

        });
      },
      ttlConveter(ttl) {
        if (ttl === -1) {
          return "永久";
        }
        if (!ttl) {
          ttl = 0;
        }
        let second = parseInt(ttl); // 秒
        let min = 0; // 分
        let hour = 0; // 小时
        let day = 0;
        if (second > 60) {
          min = parseInt(second / 60);
          second = parseInt(second % 60);
          if (min > 60) {
            hour = parseInt(min / 60);
            min = parseInt(min % 60);
            if (hour > 24) {
              day = parseInt(hour / 24);
              hour = parseInt(hour % 24);
            }
          }
        }
        let result = "" + second + "s";
        if (min > 0) {
          result = "" + min + "m:" + result;
        }
        if (hour > 0) {
          result = "" + hour + "h:" + result;
        }
        if (day > 0) {
          result = "" + day + "d:" + result;
        }
        return result;
      }
    },
    mounted() {
      this.redis.id = this.$route.params.id;
      this.scan();
    },
    components: {
      ToolBar,
      ValueDialog
    }
  }
</script>

<style>

</style>
