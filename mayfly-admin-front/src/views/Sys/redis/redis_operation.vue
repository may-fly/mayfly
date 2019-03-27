<template>
	<div>
		<el-container>
			<el-aside width="30%">
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

				<div class="key-info">
					<div class="dbsize">
						<div>
							<el-input placeholder="请输入key" style="width: 60%;" clearable>
							</el-input>

							keys：{{dbsize}}
						</div>
					</div>
					<div class="key">

						<el-table :data="keys" style="width: 100%" border>
							<el-table-column type="selection" width="55">
							</el-table-column>
							<el-table-column prop="type" label="type" width="80">
							</el-table-column>
							<el-table-column prop="key" label="key" width="180">
							</el-table-column>
							<el-table-column prop="ttl" label="ttl">
							</el-table-column>
						</el-table>
					</div>
				</div>
			</el-aside>
			<el-main>
				<div class="value">

				</div>
			</el-main>
		</el-container>
	</div>
</template>

<script>
	// import Api from "../../../api/index.js"

	export default {
		data() {
			return {
				span: {
					title: 8,
					content: 16
				},
				redis: {
					id: '',
					info: "",
					conf: ""
				},
				keys: [],
				dbsize: 0
			};
		},
		methods: {

		},
		mounted() {
			this.redis.id = this.$route.params.id;
			Api.request.post(`/open/redis/${this.redis.id}/connect`, null, res => {
				// console.log(res)
				this.redis.info = res;

				Api.request.get(`/open/redis/${this.redis.id}/conf`, null, res => {
					// console.log(res)
					this.redis.conf = res;
				})
			})

			Api.request.get(`/open/redis/${this.redis.id}/scan`, null, res => {
				// console.log(res)
				this.keys = res.keys;
				this.dbsize = res.dbsize
			})
		}
	}
</script>

<style>
	.el-header,
	.el-footer {
		background-color: #B3C0D1;
		color: #333;
		text-align: center;
		line-height: 60px;
	}

	/* .el-aside {
		background-color: #D3DCE6;
		color: #333;

	} */

	.el-main {
		background-color: #E9EEF3;
		color: #333;
		/* text-align: center; */
		/* line-height: 160px; */
	}

	body>.el-container {
		margin-bottom: 40px;
	}

	.el-container:nth-child(5) .el-aside,
	.el-container:nth-child(6) .el-aside {
		line-height: 260px;
	}

	.el-container:nth-child(7) .el-aside {
		line-height: 320px;
	}


	.el-row {
		&:last-child {
			margin-bottom: 0;
		}
	}

	.el-col {
		border-radius: 4px;
	}

	.grid-title {
		border-radius: 4px;
		min-height: 36px;
		font-size: 14px;
		background: #e5e9f2;
		/* float: right; */
	}

	.grid-content {
		border-radius: 4px;
		min-height: 36px;
		font-size: 14px;
		background: #e5e9f2;
	}


	.key-info {
		float: left;
		white-space: nowrap;
		height: 600px;
	}

	.key-info .dbsize {
		margin-left: 10px;
	}

	.key a {
		display: block;
		margin-top: 2px;
		cursor: pointer;
		color: #000000;
	}

	.key a:hover {
		color: #367FA9
	}

	.value {
		float: left;
	}
</style>
