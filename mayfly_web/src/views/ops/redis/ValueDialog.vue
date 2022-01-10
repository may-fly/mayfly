<template>
  <el-dialog :title="keyValue.key" :visible="visible" :show-close="false" width="650px">
    <el-form>
      <el-form-item>
        <el-input
          v-model="keyValue.value"
          type="textarea"
          :autosize="{ minRows: 10, maxRows:15}"
          autocomplete="off"
        ></el-input>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button type="primary" size="small">确 定</el-button>
      <el-button @click="cancel()" size="small">取 消</el-button>
    </div>
  </el-dialog>
</template>
<script lang="ts">
import { Component, Vue, Prop } from 'vue-property-decorator'
@Component({
  name: 'ValueDialog'
})
export default class ValueDialog extends Vue {
  @Prop()
  visible: Boolean
  @Prop()
  keyValue: [Object]
  @Prop()
  title: String

  cancel() {
    this.$emit('update:visible', false)
    this.$emit('cancel')
  }
}
</script>