<template>
	<div class="tree-details">
		<el-row type="flex">
			<el-col style="flex: 1">
				<span style="font-size: 13px">
					<span style="color: #3c8dbc">【</span>{{ data.data.name }}<span style="color: #3c8dbc">】</span>
					<!--<el-tag size="mini">{{data.childNodes.length}}</el-tag>-->
				</span>
			</el-col>
			<el-col style="width: 600px;">
				<el-button @click="info(data,$event)" type="info" icon="el-icon-tickets" size="mini">查看</el-button>
				<el-button v-permission="permission.update.code" @click="edit(data,$event)" type="primary" icon="el-icon-edit" size="mini">编辑</el-button>
				<el-button v-permission="permission.save.code" @click="add(data,$event)" type="success" icon="el-icon-plus" size="mini">新增</el-button>
				<!-- <el-button @click="add(data,$event)" type="primary" icon="el-icon-edit"  size="mini">编辑操作</el-button> -->
				<el-button v-permission="permission.del.code" @click="deleteDepart(data, $event)" type="danger" icon="el-icon-delete" size="mini">删除</el-button>
			</el-col>
		</el-row>
	</div>
</template>

<script>
	import permission from '../permissions.js'
	export default {
		name: 'TreeCategory',
		props: {
			data: Object,
		},
		data() {
			return {
				permission: permission.menu
			}
		},
		methods: {
			deleteDepart(node, e) {
				this.$emit('delete-menu', node.data);
				e.cancelBubble = true;
			},
			add(node, e) {
				this.$emit('add-menu', node.data);
				e.cancelBubble = true;
			},
			edit(node, e) {
				this.$emit('edit-menu', node.data);
				e.cancelBubble = true;
			},
			info(node, e) {
				let info = node.data;
				this.$alert('<strong style="margin-right: 18px">名称:</strong>' + info.name +
					' <br/><br/><strong style="margin-right: 18px">图标:</strong>' + (info.icon ? info.icon : "无") +
					' <br/><br/><strong style="margin-right: 18px">路径:</strong>' + (info.path ? info.path : "无") +
					' <br/><br/><strong style="margin-right: 18px">权重:</strong>' + info.weight +
					'', '菜单详情', {
						type: 'info',
						dangerouslyUseHTMLString: true,
					}).then(r => {});

				e.cancelBubble = true;
			}

		},
		mounted: function() {
// 			this.btns.add = this.$Permission.getPermission(code.menu.save);
// 			this.btns.del = this.$Permission.getPermission(code.menu.del); 
		},
		components: {

		}
	}
</script>
<style lang="less">
	.tree-details {
		flex: 1;

		.el-button.is-circle {
			padding: 8px;
		}
	}

	.category-description {
		text-indent: 2em;
	}
</style>
