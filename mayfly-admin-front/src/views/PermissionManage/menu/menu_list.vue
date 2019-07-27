<template>
  <div class="menu">
    <ToolBar>
      <el-button v-permission="permission.save.code" type="primary" icon="el-icon-plus" size="small" @click="addMenu(false)">添加</el-button>
    </ToolBar>
    <el-tree :indent="38" :props="props" :data="data" @node-click="handleNodeClick" :render-content="renderContent">
    </el-tree>
    <MenuEdit :title="dialogForm.title" :dialogFormVisible="dialogForm.visible" :data="dialogForm.data" :departTree="data"
      @val-change="valChange" @cancel="editorCancel()">
    </MenuEdit>
  </div>
</template>

<script>
  import ToolBar from '~/components/ToolBar/ToolBar.vue';
  import TreeDetails from './TreeDetails.vue';
  import MenuEdit from './menu_edit.vue'
  import permissions from '../permissions.js'
  export default {
    data() {
      return {
        permission: permissions.menu,
        //弹出框对象
        dialogForm: {
          title: "",
          visible: false,
          data: {}
        },
        currentEditCategory: null,
        data: [],
        props: {
          label: 'name',
          children: 'children'
        },
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
        self.currentEditCategory = null;
        if (!data) {
          this.dialogForm.data = {};
          this.dialogForm.title = '添加顶级菜单';
        } else {
          this.dialogForm.data = {};
          this.dialogForm.data.pid = data.id; //添加子菜单，把当前菜单id作为新增菜单pid
          this.dialogForm.title = '添加“' + data.name + '”的子资源 ';
        }

      },
      editMenu(data) {
        this.dialogForm.visible = true;
        this.dialogForm.data = data;
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
      changeStatus(data) {
        this.permission.changeStatus.request({
          id: data.id,
          status: data.status
        }).then(res => {
          this.$message.success("操作成功！");
        })
      },
      handleNodeClick(data, k) {

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
      MenuEdit
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
