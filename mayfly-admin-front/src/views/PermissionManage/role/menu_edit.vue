<template>
	<div style="width: 50px;">
		<el-dialog :title="'编辑“'+role.name+'”菜单权限'" :visible="visible" :show-close="false" width="300px">
			<el-tree ref="menuTree" :data="menus" show-checkbox node-key="id" :default-checked-keys="defaultCheckedKeys" :props="defaultProps">
			</el-tree>
			<div slot="footer" class="dialog-footer">
				<el-button @click="$emit('cancel');">取 消</el-button>
				<el-button type="primary" @click="btnOk">确 定</el-button>
			</div>
		</el-dialog>
	</div>
</template>

<script>
	import permission from '../permissions.js'

	export default {
		props: {
			visible: Boolean,
			role: [Object, Boolean],
			title: String
		},
		data() {
			return {
				btns: {
					menuList: {}
				},
        permission: permission.role,
				menus: [],
				defaultProps: {
					children: 'children',
					label: 'name'
				},
				defaultCheckedKeys: []
			};
		},
		watch: {
			role() {
				permission.role.roleMenus.request({
					id: this.role.id
				}, res => {
					this.defaultCheckedKeys = res;
				});
			}
		},
		methods: {
			btnOk() {
				let saveMenu = this.$Permission.getPermission(code.role.saveMenu);
				console.log(saveMenu)
				// return;
				if (!saveMenu.show) {
					this.$message.error('您没有该权限!');
				} else {
					let menuIds = this.$refs.menuTree.getCheckedKeys();
					let halfMenuIds = this.$refs.menuTree.getHalfCheckedKeys()
					let menus = [].concat(menuIds, halfMenuIds).join(",");
					saveMenu.request({
						id: this.role.id,
						resourceIds: menus
					}, res => {
						if (res) {
							this.$message.success('保存成功!');
							this.$emit('cancel')
						}
					})
				}
			}
		},
		mounted() {
			permission.menu.list.request(null, res => {
				this.menus = res;
			})
		}
	}
</script>

<style>

</style>
