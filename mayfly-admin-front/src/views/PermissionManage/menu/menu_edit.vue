<template>
	<div class="menu-dialog">
		<el-dialog :title="title" :visible="dialogFormVisible" :show-close="false" width="25%">
			<el-form :model="form" ref="menuForm" :rules="rules">
				<el-form-item prop="name">
					<el-input v-model="form.name" placeholder="请输入菜单名" auto-complete="off"></el-input>
				</el-form-item>
				<el-form-item>
					<el-input v-model="form.icon" placeholder="请输入菜单图标样式"></el-input>
				</el-form-item>
				<el-form-item>
					<el-input v-model="form.path" placeholder="请输入路由路径"></el-input>
				</el-form-item>
				<el-form-item>
					<el-input v-model="form.weight" placeholder="请输入权重"></el-input>
				</el-form-item>
			</el-form>

			<div slot="footer" class="dialog-footer">
				<el-button @click="$emit('cancel');">取 消</el-button>
				<el-button type="primary" :loading="btnLoading" @click="btnOk">确 定</el-button>
			</div>
		</el-dialog>
	</div>
</template>

<script>
	import permission from '../permissions.js'
	export default {
		name: 'MenuEdit',
		props: {
			dialogFormVisible: Boolean,
			data: [Object, Boolean],
			title: String
		},
		data() {
			return {
				btns: {
					ok: {}
				},
        permission: permission,
				//弹出框对象
				dialogForm: {
					title: "",
					visible: false,
					data: {}
				},
				props: {
					value: 'id',
					label: 'name',
					children: 'children'
				},
				form: {
					id: null,
					name: '',
					pid: '',
					path: '',
					icon: '',
					weight: ''
				},
				operations: [{
						name: "新增"
					},
					{
						name: "删除"
					},
					{
						name: "删除"
					},
					{
						name: "删除"
					},
					{
						name: "删除"
					},
					{
						name: "删除"
					},
					{
						name: "删除"
					}
				],
				btnLoading: false,
				rules: {
					name: [{
							required: true,
							message: '请输入活动名称',
							trigger: 'blur'
						},
						{
							min: 2,
							max: 12,
							message: '长度在 2 到 12 个字符',
							trigger: 'blur'
						}
					],
				}
			}
		},
		watch: {
			'data': {
				handler: function() {
					if (this.data) {
						for (let k in this.form) {
							this.form[k] = this.data[k];
						}
					} else {
						for (let k in this.form) {
							this.form[k] = '';
						}
					}
				},
				deep: true
			}
		},
		methods: {
			handleChange() {},
			addOperation() {
				this.dialogForm.visible = true;
			},
			operationChange(operation) {
				this.operations.push(operation);
				this.dialogForm.visible = false;
			},
			btnOk() {
				let c = this.form.id ? code.menu.update : code.menu.save;
				this.btns.ok = this.$Permission.getPermission(c);

				if (!this.btns.ok.show) {
					this.$message.error('您没有该权限!');
				} else {
					this.$refs["menuForm"].validate((valid) => {
						if (valid) {
							this.btns.ok.request(this.form, res => {
								this.$emit('val-change', this.form);
								this.btnLoading = true;
								setTimeout(() => {
									this.btnLoading = false
								}, 1000);
								//重置表单域
								this.$refs["menuForm"].resetFields();
							})
						} else {
							this.$message.error('表单填写有误');
							return false;
						}
					});
				}
			},

		},
		mounted() {

		},
		components: {
		}
	}
</script>
<style lang="less">
	// 	.m-dialog {
	// 		.el-cascader {
	// 			width: 100%;
	// 		}
	// 	}
</style>
