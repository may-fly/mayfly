<template>
  <div class="dynamic-form">
    <el-form
      :model="form"
      ref="dynamicForm"
      :label-width="formInfo.labelWidth ? formInfo.labelWidth : '80px'"
      :size="formInfo.size ? formInfo.size : 'small'"
    >
      <el-form-item
        v-for="item in formInfo.formItems"
        :key="item.key"
        :prop="item.name"
        :label="item.label"
        :required="item.required"
        :rules="item.rules"
      >
        <el-input
          v-if="item.type === 'input'"
          v-model.trim="form[item.name]"
          :placeholder="item.placeholder"
          :type="item.inputType"
          clearable
          autocomplete="new-password"
        ></el-input>

        <el-select
          v-else-if="item.type === 'select'"
          v-model.trim="form[item.name]"
          :placeholder="item.placeholder"
          clearable
        >
          <el-option v-for="i in item.enums" :key="i.value" :label="i.label" :value="i.value"></el-option>
        </el-select>
      </el-form-item>

      <el-form-item size="large">
        <el-button @click="cancel" size="mini">取 消</el-button>
        <el-button type="primary" @click="submit" size="mini">确 定</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
export default {
  name: "DynamicForm",
  props: {
    formInfo: Object,
    formData: [Object, Boolean]
  },
  data() {
    return {
      form: {}
    };
  },
  watch: {
    formData: {
      handler: function() {
        if (this.formData) {
          this.form = { ...this.formData };
        }
      },
      deep: true
    }
  },
  methods: {
    submit() {
      this.$refs["dynamicForm"].validate(valid => {
        if (valid) {
          let operation = this.form.id
            ? this.formInfo.updatePermission
            : this.formInfo.addPermission;
          if (operation) {
            operation.request(this.form).then(res => {
              this.$message.success("保存成功");
              this.$emit("submitSuccess");
              this.cancel();
            });
          } else {
            this.$message.error("表单未设置对应的提交操作");
          }
        } else {
          return false;
        }
      });

      // this.cancel();
    },
    cancel() {
      this.$emit("cancel");
      setTimeout(() => {
        this.$refs["dynamicForm"].resetFields();
        //  重置对象属性为null
        this.form = {};
      }, 300);
    }
  },
  mounted() {
    // 组件可能还没有初始化，第一次初始化的时候无法watch对象
    this.form = { ...this.formData };
  }
};
</script>
<style lang="less">
</style>
