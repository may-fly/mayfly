<template>
  <div>
    <!-- <el-container>
      <el-aside width="500"> -->
    <!-- <el-row>
					<el-col :span="span.title">
						<div class="grid-title">版本:</div>
					</el-col>
					<el-col :span="span.content">
						<div class="grid-content">{{redis.info.Server.redis_version}}</div>
					</el-col>
				</el-row>
				
				<el-row>
					<el-col :span="span.title">
						<div class="grid-title">版本:</div>
					</el-col>
					<el-col :span="span.content">
						<div class="grid-content">{{redis.info.Server.redis_version}}</div>
					</el-col>
				</el-row>
				 -->
    <!-- <el-row>
					<el-col :span="span.title">
						<div class="grid-title">版本:</div>
					</el-col>
					<el-col :span="span.content">
						<div class="grid-content">{{redis.info.Server.redis_version}}</div>
					</el-col>
				</el-row>

				<el-row>
					<el-col :span="span.title">
						<div class="grid-title">运行模式:</div>
					</el-col>
					<el-col :span="span.content">
						<div class="grid-content">{{redis.info.Server.redis_mode}}</div>
					</el-col>
				</el-row>

				<el-row>
					<el-col :span="span.title">
						<div class="grid-title">服务器系统:</div>
					</el-col>
					<el-col :span="span.content">
						<div class="grid-content">{{redis.info.Server.os}}</div>
					</el-col>
				</el-row>

				<el-row>
					<el-col :span="span.title">
						<div class="grid-title">客户端连接数:</div>
					</el-col>
					<el-col :span="span.content">
						<div class="grid-content">{{redis.info.Clients.connected_clients}}</div>
					</el-col>
				</el-row>

				<el-row>
					<el-col :span="span.title">
						<div class="grid-title">系统总内存:</div>
					</el-col>
					<el-col :span="span.content">
						<div class="grid-content">{{redis.info.Memory.total_system_memory_human}}</div>
					</el-col>
				</el-row>

				<el-row>
					<el-col :span="span.title">
						<div class="grid-title">分配内存:</div>
					</el-col>
					<el-col :span="span.content">
						<div class="grid-content">{{redis.info.Memory.used_memory_human}}</div>
					</el-col>
				</el-row>

				<el-row>
					<el-col :span="span.title">
						<div class="grid-title">已分配内存:</div>
					</el-col>
					<el-col :span="span.content">
						<div class="grid-content">{{redis.info.Memory.used_memory_rss_human}}</div>
					</el-col>
				</el-row>

				<el-row>
					<el-col :span="span.title">
						<div class="grid-title">内存消耗峰值:</div>
					</el-col>
					<el-col :span="span.content">
						<div class="grid-content">{{redis.info.Memory.used_memory_peak_human}}</div>
					</el-col>
				</el-row>

				<el-row>
					<el-col :span="span.title">
						<div class="grid-title">内存比例:</div>
					</el-col>
					<el-col :span="span.content">
						<div class="grid-content">{{redis.info.Memory.mem_fragmentation_ratio}}%</div>
					</el-col>
				</el-row> -->
    <!-- <div>
					<span>Server</span>
					<div>
						<span>版本:</span><span>{{redis.info.Server.redis_version}}</span>
					</div>
					<div>
						<span>服务器系统:</span><span>{{redis.info.Server.os}}</span>
					</div>
				</div> -->


    <!-- </el-aside>
      <el-main>
        <div class="value">

        </div>
      </el-main>
    </el-container> -->
    <ToolBar>
      <div style="float: left">
        <el-input placeholder="请输入key" style="width: 180px" v-model="scanParam.match" size="small" @clear="clear()"
          clearable>
        </el-input>
        <el-button @click="search()" type="success" icon="el-icon-search" size="small" plain>搜索</el-button>
        <el-button type="primary" icon="el-icon-plus" size="small" @click="save(false)" plain>添加</el-button>
      </div>
      <div style="float: right;">
        <!-- <el-button @click="scan()" icon="el-icon-refresh" size="small" plain>刷新</el-button> -->
        <span>keys:{{dbsize}}</span>
      </div>
    </ToolBar>
    <el-table v-loading="loading" :data="keys" border stripe :highlight-current-row="true" style="cursor: pointer;" height="750">
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
          <el-button @click="" type="success" icon="el-icon-search" size="small" plain>查看</el-button>
          <el-button @click="" type="primary" icon="el-icon-edit" size="small" plain>修改</el-button>
          <el-button @click="" type="danger" size="small" icon="el-icon-delete" plain>删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- <el-pagination @current-change="handlePageChange" style="text-align: center;margin-top: 20px;" background layout="prev, pager, next, total"
      :total="dbsize" :current-page.sync="scanParam.pageNum" :page-size="scanParam.count">
    </el-pagination> -->
    <div style="text-align: center; margin-top: 10px;">
      <el-button @click="scan()" icon="el-icon-refresh" size="small" plain>换一批</el-button>
      
    </div>
  </div>
</template>

<script>
  // import Api from "../../../api/index.js"
  import ToolBar from '~/components/ToolBar/ToolBar.vue'
  import Req from "~/common/request"
  import enums from './enums'
  export default {
    data() {
      return {
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
          pageNum: 1
        },
        keys: [],
        dbsize: 0
      };
    },
    methods: {
      scan() {
        this.loading = true;
        let id = this.cluster == 0 ? this.redis.id : this.cluster;
        let isCluster = this.cluster == 0 ? 0 : 1;
        Req.get(`/open/redis/${isCluster}/${id}/scan`, this.scanParam, res => {
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
        let id = this.cluster == 0 ? this.redis.id : this.cluster;
        let isCluster = this.cluster == 0 ? 0 : 1;
        Req.get(`/open/redis/${isCluster}/${id}/scan`, this.scanParam, res => {
          // console.log(res)
          this.keys = res.keys;
          this.dbsize = this.keys.length;
          this.scanParam.cursor = res.cursor;
        })
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
      ToolBar
    }
  }
</script>

<style>

</style>
