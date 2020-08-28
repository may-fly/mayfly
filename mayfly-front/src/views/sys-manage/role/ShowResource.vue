<template>
  <div>
    <el-dialog @close="closeDialog" :title="title" :visible="visible" width="400px" height="400px">
      <el-tree
        style="height: 50vh;overflow: auto;"
        :data="resources"
        node-key="id"
        :props="defaultProps"
        :expand-on-click-node="false"
      >
        <span class="custom-tree-node" slot-scope="{ node, data }">
          <span v-if="data.type == enums.ResourceTypeEnum.MENU.value">{{ node.label }}</span>
          <span
            v-if="data.type == enums.ResourceTypeEnum.PERMISSION.value"
            style="color: #67c23a;"
          >{{ node.label }}</span>

          <el-link
            @click="info(data)"
            style="margin-left: 25px;"
            icon="el-icon-view"
            type="info"
            :underline="false"
          />
        </span>
      </el-tree>
    </el-dialog>
  </div>
</template>

<script lang="ts">
import { Component, Vue, Prop } from 'vue-property-decorator'
import enums from '../enums'

@Component({
  name: 'ShowResource'
})
export default class ShowResource extends Vue {
  @Prop()
  visible: Boolean
  // 角色拥有的资源树
  @Prop()
  resources: Array<any>
  @Prop()
  title: String

  enums = enums

  defaultProps = {
    children: 'children',
    label: 'resourceName'
  }

  info(info: any) {
    this.$alert(
      '<strong style="margin-right: 18px">资源名称:</strong>' +
        info.resourceName +
        ' <br/><strong style="margin-right: 18px">分配账号:</strong>' +
        info.creator +
        ' <br/><strong style="margin-right: 18px">分配时间:</strong>' +
        info.createTime +
        '',
      '分配信息',
      {
        type: 'info',
        dangerouslyUseHTMLString: true,
        closeOnClickModal: true,
        showConfirmButton: false
      }
    ).catch(r => {})
    return
  }
  closeDialog() {
    this.$emit('update:visible', false)
    this.$emit('update:resources', [])
  }
}
</script>

<style>
</style>
