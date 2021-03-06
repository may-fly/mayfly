<template>
	<div class="account-dialog">
		<el-dialog :title="title" v-model="visible" :show-close="false" width="35%">
			<el-form :model="form" ref="accountForm" :rules="rules" label-width="85px" size="small">
				<el-form-item prop="username" label="用户名:" required>
					<el-input :disabled="edit" v-model.trim="form.username" placeholder="请输入用户名" auto-complete="off"></el-input>
				</el-form-item>
				<el-form-item prop="password" label="密码:" required>
					<el-input type="password" v-model.trim="form.password" placeholder="请输入密码" autocomplete="new-password"></el-input>
				</el-form-item>
				<el-form-item v-if="!edit" label="确认密码:" required>
					<el-input type="password" v-model.trim="form.repassword" placeholder="请输入确认密码" autocomplete="new-password"></el-input>
				</el-form-item>
			</el-form>

			<template #footer>
				<div class="dialog-footer">
					<el-button @click="cancel()" size="mini">取 消</el-button>
					<el-button type="primary" :loading="btnLoading" @click="btnOk" size="small">确 定</el-button>
				</div>
			</template>
		</el-dialog>
	</div>
</template>

<script lang="ts">
import { toRefs, reactive, watch, defineComponent, ref } from 'vue';
import { accountApi } from '../api';
import enums from '../enums';
import { ElMessage } from 'element-plus';

export default defineComponent({
	name: 'AccountEdit',
	props: {
		visible: {
			type: Boolean,
		},
		account: {
			type: [Boolean, Object],
		},
		title: {
			type: String,
		},
	},
	setup(props: any, { emit }) {
		const accountForm: any = ref(null);
		const state = reactive({
			edit: false,
			form: {
				id: null,
				username: null,
				password: null,
				repassword: null,
			},
			btnLoading: false,
			rules: {
				username: [
					{
						required: true,
						message: '请输入用户名',
						trigger: ['change', 'blur'],
					},
				],
				password: [
					{
						required: true,
						message: '请输入密码',
						trigger: ['change', 'blur'],
					},
				],
			},
		});

		watch(props, (newValue, oldValue) => {
			if (newValue.account) {
				state.form = { ...newValue.account };
			} else {
				state.form = {} as any;
			}
		});

		const btnOk = async () => {
			let p = state.form.id ? accountApi.update : accountApi.save;

			accountForm.value.validate((valid: boolean) => {
				if (valid) {
					p.request(state.form).then((res: any) => {
						ElMessage.success('操作成功');
						emit('val-change', state.form);
						state.btnLoading = true;
						setTimeout(() => {
							state.btnLoading = false;
						}, 1000);
						//重置表单域
						accountForm.resetFields();
						state.form = {} as any;
					});
				} else {
					ElMessage.error('表单填写有误');
					return false;
				}
			});
		};

		const cancel = () => {
			emit('update:visible', false);
			setTimeout(() => {
				emit('update:account', null);
			}, 800);
			emit('cancel');
			setTimeout(() => {
				accountForm.value.resetFields();
				//  重置对象属性为null
				state.form = {} as any;
			}, 200);
		};

		return {
			...toRefs(state),
			accountForm,
			btnOk,
			cancel,
		};
	},
});
</script>
<style lang="scss">
</style>
