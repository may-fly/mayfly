<template>
  <div class="menu">
    <ToolBar>
      <el-button v-permission="permission.save.code" type="primary" icon="el-icon-plus" size="mini" @click="addMenu(false)">添加</el-button>
    </ToolBar>
    <el-tree :indent="38" node-key="id" :props="props" :data="data" @node-expand="handleNodeExpand" @node-collapse="handleNodeCollapse"
      :default-expanded-keys="defaultExpandedKeys" :render-content="renderContent">
    </el-tree>
    <ResourceEdit :title="dialogForm.title" :dialogFormVisible="dialogForm.visible" :data="dialogForm.data" :departTree="data"
      :type="dialogForm.type" @val-change="valChange" @cancel="editorCancel()">
    </ResourceEdit>
  </div>
</template>

<script>
  import ToolBar from '~/components/ToolBar/ToolBar.vue';
  import TreeDetails from './TreeDetails.vue';
  import ResourceEdit from './resource_edit.vue'
  import permissions from '../permissions.js'
  export default {
    data() {
      return {
        permission: permissions.menu,
        //弹出框对象
        dialogForm: {
          title: "",
          visible: false,
          data: {},
          // 1.新增顶级节点；2.新增子节点；3.编辑节点
          type: 1
        },
        currentEditCategory: null,
        data: [],
        props: {
          label: 'name',
          children: 'children'
        },
        // 展开的节点
        defaultExpandedKeys: []
      }
    },
    methods: {
      deleteMenu(data) {
        this.$confirm(`此操作将删除 [${data.name}] 该菜单, 是否继续?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.permission.del.request({
            id: data.id
          }).then(res => {
            this.$message.success("删除成功！");
            this.search();
          })
        }).catch(err => {
          this.$message.error(err);
        });
      },
      search() {
        this.permission.list.request(null).then(res => {
          this.data = res;
        })
      },
      addMenu(data) {
        this.dialogForm.visible = true;
        if (!data) {
          this.dialogForm.data = false;
          this.dialogForm.type = 1;
          this.dialogForm.title = '添加顶级菜单';
        } else {
          this.dialogForm.data = {};
          this.dialogForm.type = 2;
          this.dialogForm.data.pid = data.id; //添加子菜单，把当前菜单id作为新增菜单pid
          this.dialogForm.title = '添加“' + data.name + '”的子资源 ';
        }

      },
      editMenu(data) {
        this.dialogForm.visible = true;
        this.dialogForm.data = data;
        this.dialogForm.type = 3;
        this.dialogForm.title = '修改“' + data.name + '”菜单';
      },
      valChange(data) {
        this.search();
        this.dialogForm.visible = false;
      },
      editorCancel() {
        this.dialogForm.visible = false;
        this.dialogForm.data = null;
      },
      changeStatus(data, status) {
        this.permission.changeStatus.request({
          id: data.id,
          status: status
        }).then(res => {
          data.status = status;
          this.$message.success("操作成功！");
        })
      },
      // 节点被展开时触发的事件
      handleNodeExpand(data, node) {
        let id = node.data.id;
        if (!this.defaultExpandedKeys.includes(id)) {
          this.defaultExpandedKeys.push(id);
        }
      },
      // 关闭节点
      handleNodeCollapse(data, node) {
        this.removeDeafultExpandId(node.data.id);
        
        let childNodes = node.childNodes;
        for (let cn of childNodes) {
          if (cn.data.type == 2) {
            return;
          }
          if (cn.expanded) {
            this.removeDeafultExpandId(cn.data.id);
          }
          // 递归删除展开的子节点节点id
          this.handleNodeCollapse(data, cn);
        }
      },
      removeDeafultExpandId(id) {
        let index = this.defaultExpandedKeys.indexOf(id);
        if (index > -1) {
          this.defaultExpandedKeys.splice(index, 1);
        }
      },
      renderContent(h, {
        node,
        data,
        store
      }) {
        return this.$createElement('TreeDetails', {
          props: {
            data: node
          },
          on: {
            'add-menu': this.addMenu,
            'edit-menu': this.editMenu,
            'delete-menu': this.deleteMenu,
            'changeStatus': this.changeStatus
          }
        });
      },
    },
    components: {
      ToolBar,
      TreeDetails,
      ResourceEdit
    },
    mounted: function() {
      this.search();
    }
  }
</script>
<style lang="less">
  .menu {
    .el-tree-node__content {
      height: 40px;
      line-height: 40px;
    }
  }
</style>
