<template>
  <div>
    <el-dialog :title="'分配“'+role.name+'”菜单&权限'" :visible="visible" :show-close="false" width="400px" height="400px">
      <el-tree style="height: 50vh;overflow: auto;" ref="menuTree" :data="resources" show-checkbox node-key="id" :default-checked-keys="defaultCheckedKeys"
        :props="defaultProps">
        <span class="custom-tree-node" slot-scope="{ node, data }">
          <span v-if="data.type == enums.ResourceTypeEnum.MENU.value">{{ node.label }}</span>
          <span v-if="data.type == enums.ResourceTypeEnum.PERMISSION.value" style="color: #67c23a;">{{ node.label }}</span>
        </span>
      </el-tree>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="btnOk" size="small">确 定</el-button>
        <el-button @click="$emit('cancel');" size="small">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import { roleApi } from '../api'
  import enums from '../enums'
  
  export default {
    props: {
      visible: Boolean,
      role: [Object, Boolean],
      // 默认勾选的节点
      defaultCheckedKeys: Array,
      // 所有资源树
      resources: Array,
      title: String
    },
    data() {
      return {
        enums: enums,
        defaultProps: {
          children: 'children',
          label: 'name'
        },
      };
    },
    methods: {
      /**
       * 获取所有菜单树的叶子节点
       * @param {Object} trees  菜单树列表
       */
      getAllLeafIds(trees) {
        let leafIds = [];
        for (let tree of trees) {
          this.setLeafIds(tree, leafIds)
        }
        return leafIds;
      },
      setLeafIds(tree, ids) {
        if (tree.children !== null) {
          for (let t of tree.children) {
            this.setLeafIds(t, ids);
          }
        } else {
          ids.push(tree.id);
        }
      },
      async btnOk() {
        let menuIds = this.$refs.menuTree.getCheckedKeys();
        let halfMenuIds = this.$refs.menuTree.getHalfCheckedKeys();
        let resources = [].concat(menuIds, halfMenuIds).join(",");
        await roleApi.saveResources.request({
          id: this.role.id,
          resourceIds: resources
        });
        this.$message.success('保存成功!');
        this.$emit('cancel');
      }
    },
    mounted() {

    }
  }
</script>

<style>

</style>
