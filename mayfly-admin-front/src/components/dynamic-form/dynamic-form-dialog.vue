<template>
  <div class="form-dialog">
    <el-dialog
      :title="title"
      :visible="visible"
      :width="dialogWidth ? dialogWidth : '500px'"
    >
      <dynamic-form
        ref="df"
        :form-info="formInfo"
        :form-data="formData"
        @submitSuccess="submitSuccess"
      >
        <template slot="btns" slot-scope="props">
          <slot name="btns">
            <el-button
              :disabled="props.submitDisabled"
              type="primary"
              @click="props.submit"
              size="mini"
            >保 存</el-button>
            <el-button :disabled="props.submitDisabled" @click="cancel()" size="mini">取 消</el-button>
          </slot>
        </template>
      </dynamic-form>
    </el-dialog>
  </div>
</template>

<script>
import DynamicForm from './dynamic-form.vue'

export default {
  name: 'DynamicFormDialog',
  props: {
    visible: Boolean,
    dialogWidth: String,
    title: String,
    formInfo: Object,
    formData: [Object, Boolean]
  },
  methods: {
    cancel() {
      this.$emit('cancel')
      // 取消动态表单的校验以及form数据
      setTimeout(() => {
        this.$refs.df.resetFieldsAndData()
      }, 200)
    },
    submitSuccess(form) {
      this.$emit('submitSuccess', form)
      this.cancel()
    }
  },
  mounted() {},
  components: {
    DynamicForm
  }
}
</script>
