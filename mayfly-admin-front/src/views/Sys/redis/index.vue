<template>
	<div>
		<ToolBar>
			
		</ToolBar>
		<el-table :data="redisTable" stripe style="width: 100%">
			<el-table-column prop="ip" label="IP" width="180">
			</el-table-column>
			<el-table-column prop="port" label="端口" width="180">
			</el-table-column>
			<el-table-column prop="desc" label="描述">
			</el-table-column>
			<el-table-column label="操作" width="180">
				<template slot-scope="scope">
					<el-button type="success" @click="show(scope.row)" :ref="scope.row" size="small">查看</el-button>
				</template>
			</el-table-column>
		</el-table>
	</div>
</template>

<script>
	import ToolBar from '~/components/ToolBar/ToolBar.vue';
// 	import Api from "../../../api/index.js"
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
				redisInfo: {
					url: ""
				}
			};
		},
		methods: {
			connect() {
				Api.request.post("/open/redis/connect", this.form, res => {
					this.redisInfo = res;
				})
			},
			show(row) {
				this.$router.push(`/redis_operation/${row.id}`);
			}
		},
		mounted() {
			Api.request.get("/open/redis/list", null, res => {
				this.redisTable = res;
			})
		},
		components: {
			ToolBar
		}
	}
</script>

<style>

</style>
