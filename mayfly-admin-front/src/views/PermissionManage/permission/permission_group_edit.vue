<template>
	<div class="api-dialog">
		<el-dialog :title="title" :visible="visible" :show-close="false" width="25%">
			<el-form ref="form" :model="form">
				<el-form-item label="名称">
					<el-input v-model="form.name" auto-complete="off"></el-input>
				</el-form-item>
				<el-form-item label="描述">
					<el-input v-model="form.description" type="textarea" :rows="2" placeholder="请输入权限组描述"></el-input>
				</el-form-item>
			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button @click="cancel">取 消</el-button>
				<el-button type="primary" :loading="btnLoading" @click="btnOk">确 定</el-button>
			</div>
		</el-dialog>
	</div>
</template>

<script>
	export default {
		name: 'ApiEdit',
		props: {
			visible: Boolean,
			data: [Object, Boolean],
			title: String
		},
		data() {
			return {
				form: {
					id: null,
					name: '',
					description: ''
				},
				btnLoading: false,
			}
		},
		watch: {
			data() {
				if (this.data) {
					for (let k in this.form) {
						this.form[k] = this.data[k];
					}
				} else {
					for (let k in this.form) {
						this.form[k] = '';
					}
				}
			}
		},
		methods: {
			handleChange() {},
			btnOk() {
				if (!this.btns.save.show) {
					this.$message.error('您没有该权限!');
				} else {
					this.btns.save.request(this.form, res => {
						this.$emit('val-change', res);
					});
				}
			},
      cancel() {
        // 重置表单
        this.$refs.form.resetFields();
        this.$emit('cancel');
      }
		},
		components: {

		},
		mounted() {
			//api操作相关code
			let groupCode = this.$Permission.ApiCode.permission.apiGroup;
			this.btns.save = this.$Permission.getApi(groupCode.save);
		}
	}
</script>
<style lang="less">
	.api-dialog {
		.el-cascader {
			width: 100%;
		}
	}
</style>
