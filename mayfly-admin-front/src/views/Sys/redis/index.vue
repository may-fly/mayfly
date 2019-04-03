<template>
	<div>
		<ToolBar>
			<el-button type="primary" icon="el-icon-plus" size="small" @click="save(false)">添加</el-button>
			<el-input placeholder="host" size="small" style="width: 140px" v-model="params.host" @clear="search"
			 clearable>
			</el-input>
      <!-- <el-input placeholder="请输入api功能！" size="small" style="width: 140px" v-model="params.name" @clear="search"
       clearable>
      </el-input> -->
			<el-select v-model="params.clusterId" size="small" clearable placeholder="集群选择">
			  <el-option v-for="item in clusters" :key="item.id" :value="item.id" :label="item.name">
			  </el-option>
			</el-select>
			<el-button @click="search" type="success" icon="el-icon-search" size="small"></el-button>
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
					<el-button type="success" @click="show(scope.row)" :ref="scope.row" size="small">数据管理</el-button>
				</template>
			</el-table-column>
		</el-table>
	</div>
</template>

<script>
	import ToolBar from '~/components/ToolBar/ToolBar.vue';
	import Req from "~/common/request"
// 
	export default {
		data() {
			return {
				redisTable: [{
					id: 1,
					ip: "127.0.0.1",
					port: 6379,
					desc: "测试"
				}],
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
        clusters: [
          {id:0, name:'单机'}
        ]
			};
		},
		methods: {
			connect() {
				Req.post("/open/redis/connect", this.form, res => {
					this.redisInfo = res;
				})
			},
			show(row) {
				this.$router.push(`/redis_operation/${row.id}`);
			},
      search() {
        Req.get("/open/redis", this.params, res => {
        	this.redisTable = res;
        })
      }
		},
		mounted() {
			this.search();
		},
		components: {
			ToolBar
		}
	}
</script>

<style>

</style>
