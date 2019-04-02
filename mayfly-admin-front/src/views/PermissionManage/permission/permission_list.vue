<template>
	<div class="user-list">
		<ToolBar>
			<div style="float: left">
        <el-button v-permission="permission.save.code" type="primary" icon="el-icon-plus" size="small" @click="save(false)">添加</el-button>
				<el-input placeholder="请输入api功能！" size="small" style="width: 140px" v-model="params.name" @clear="searchApi"
				 clearable>
				</el-input>
        <el-select v-model="query.groupId" size="small" clearable placeholder="请选择权限组">
          <el-option v-for="item in groups" :key="item.id" :value="item.id" :label="item.name">
          </el-option>
        </el-select>
				<el-button v-permission="permission.list.code" @click="searchApi" type="success" icon="el-icon-search" size="small"></el-button>
			</div>
		</ToolBar>
		<el-table :data="permissionList" border ref="table" style="width: 100%">
			<el-table-column label="序号" type="index"> </el-table-column>
			<el-table-column prop="code" label="code">
			</el-table-column>
			<el-table-column min-width="30" prop="method" label="调用方法">
				<div slot-scope="scope" style="width: 100%;">
					<el-tag v-if="scope.row.method == 1">GET</el-tag>
					<el-tag type="success" v-if="scope.row.method == 2">POST</el-tag>
					<el-tag type="warning" v-if="scope.row.method == 3">PUT</el-tag>
					<el-tag type="warning" v-if="scope.row.method == 4">DELETE</el-tag>
				</div>
			</el-table-column>
			<el-table-column prop="uriPattern" label="uri">
			</el-table-column>
			<el-table-column prop="description" label="描述">
			</el-table-column>
			<el-table-column min-width="30" prop="status" label="状态">
				<div slot-scope="scope">
					<el-tag type="danger" v-if="scope.row.status == 0">禁用</el-tag>
					<el-tag type="success" v-if="scope.row.status == 1">启用</el-tag>
				</div>
			</el-table-column>
			<el-table-column prop="createTime" label="新增时间">
			</el-table-column>
			<el-table-column prop="updateTime" label="更新时间">
			</el-table-column>
			<el-table-column label="操作" width="250">
				<template slot-scope="scope">
					<el-button v-permission="permission.changeStatus.code" v-if="scope.row.status == 0" @click="changeStatus(scope.row, 1)" type="success"
					 :ref="scope.row.id" size="small">启用</el-button>
					<el-button v-permission="permission.changeStatus.code" v-if="scope.row.status == 1" @click="changeStatus(scope.row, 0)" type="danger"
					 size="small">禁用</el-button>
					<el-button v-permission="permission.del.code" v-if="scope.row.status != 3" @click="deleteApi(scope.row.id)" type="danger" size="small">删除</el-button>
					<el-button  v-permission="permission.update.code" @click="save(scope.row)" type="primary" size="small">修改</el-button>
				</template>
			</el-table-column>
		</el-table>
		<el-pagination @current-change="handlePageChange" style="text-align: center;margin-top: 20px;" background layout="prev, pager, next, total, jumper"
		 :total="total" :current-page.sync="query.pageNum" :page-size="query.pageSize">
		</el-pagination>
		<PermissionEdit :title="dialogForm.title" :visible="dialogForm.visible" :data="dialogForm.data" @cancel="dialogForm.visible = false"
		 @val-change="valChange">
		</PermissionEdit>
	</div>
</template>

<script>
	import ToolBar from '~/components/ToolBar/ToolBar.vue';
	import HelpHint from '~/components/HelpHint/HelpHint.vue';
	import PermissionEdit from './permission_edit.vue'
	import permission from '../permissions.js'

	export default {
		data() {
			return {
        permission: permission.permission,
				//弹出框对象
				dialogForm: {
					title: "保存api",
					visible: false,
					data: {}

				},
				params: {
					name: '',
				},
        query: {
          pageNum: 1,
          pageSize: 10,
          groupId: null
        },
				//api列表对象
				permissionList: [],
        groups:[],
        total: 0
			}
		},
		methods: {
			searchApi() {
				this.query.pageNum = 1;
        this.search();
			},
			handlePageChange(curPage) {
				this.query.pageNum = curPage;
        this.search();
			},
			save(api) {
				this.dialogForm.visible = true;
				this.dialogForm.data = api;
			},
			//修改成功后的回调
			valChange(permission) {
				this.search();
        this.$message.success('保存成功!');
        this.dialogForm.visible = false;
			},
			deleteApi(id) {
				//发送删除请求
				this.permission.del.request({
					id
				}, res => {
					this.permission.search.request(null, res => {
						this.permissionList = res;
					})
				})
			},
			changeStatus(permission, status) {
				this.permission.changeStatus.request({
					id: permission.id,
					status
				}, res => {
					permission.status = res.status;
					permission.updateTime = res.updateTime;
				})
			},
      search() {
        let searchPermission = this.permission.list;
        let permission = this.$Permission.getPermission(searchPermission.code);
        if (!permission.show) {
        	this.$message.error('您没有该权限!');
        } else {
        	searchPermission.request(this.query, res => {
            console.log(res)
        		this.permissionList = res.list;
            this.total = res.total;
        	})
        }
      }
		},
		mounted: function() {
			this.search();
      permission.permissionGroup.all.request(null, res => {
        this.groups = res;
      })
		},
		components: {
			ToolBar,
			HelpHint,
			PermissionEdit
		}
	}
</script>
<style lang="less">

</style>
