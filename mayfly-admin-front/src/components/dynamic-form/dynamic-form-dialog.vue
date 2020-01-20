<template>
  <div class="form-dialog">
    <el-dialog
      :title="title"
      :visible="visible"
      :show-close="false"
      :width="dialogWidth ? dialogWidth : '500px'"
    >
      <dynamic-form
        :form-info="formInfo"
        :form-data="formData"
        @submit-success="submitSuccess"
        @cancel="cancel"
      ></dynamic-form>
    </el-dialog>
  </div>
</template>

<script>
import DynamicForm from "./dynamic-form.vue";

export default {
  name: "DynamicFormDialog",
  props: {
    visible: Boolean,
    dialogWidth: String,
    title: String,
    formInfo: Object,
    formData: [Object, Boolean]
  },
  methods: {
    cancel() {
      this.$emit("cancel");
    },
    submitSuccess() {
      this.$emit("submitSuccess");
    }
  },

  mounted() {},
  components: {
    DynamicForm
  }
};
</script>
<style lang="less">
</style>
