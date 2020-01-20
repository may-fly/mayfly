<template>
    <el-dialog :title="keyValue.key" :visible.sync="visible" width="650px">
      <el-form>
        <el-form-item>
          <el-input v-model="keyValue.value" type="textarea" :autosize="{ minRows: 10, maxRows:15}" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="$emit('cancel')" size="mini">取 消</el-button>
        <el-button type="primary" size="mini">确 定</el-button>
      </div>
    </el-dialog>
</template>
<script>
export default {
    name: 'valueDialog',
    props: {
      visible: Boolean,
      keyValue: [Object],
      title: String,
    },
    data() {
        return {

        }
    }
}
</script>