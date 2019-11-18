<template>
  <div class="menu">
    <ToolBar>
      <div><span style="font-size: 14px;"><i class="el-icon-info"></i>可右击树节点对右击节点进行操作</span></div>
      <el-button v-permission="permission.save.code" type="primary" icon="el-icon-plus" size="mini" @click="addResource(false)">添加</el-button>
    </ToolBar>
    <el-tree class="none-select" :indent="38" node-key="id" :props="props" :data="data" @node-expand="handleNodeExpand"
      @node-collapse="handleNodeCollapse" @node-contextmenu="rightClick" :default-expanded-keys="defaultExpandedKeys">
      <span class="custom-tree-node" slot-scope="{ node, data }">
        <span style="font-size: 13px" v-if="data.type === enums.ResourceTypeEnum.MENU.value">
          <span style="color: #3c8dbc">【</span>{{ data.name }}<span style="color: #3c8dbc">】</span>
          <el-tag v-if="data.children !== null" size="mini">{{data.children.length}}</el-tag>
        </span>
        <span style="font-size: 13px" v-if="data.type === enums.ResourceTypeEnum.PERMISSION.value">
          <span style="color: #3c8dbc">【</span>
          <span :style="data.status == 1 ? 'color: #67c23a;' : 'color: #f67c6c;'">{{ data.name }}</span>
          <span style="color: #3c8dbc">】</span>
        </span>
      </span>
    </el-tree>

    <ResourceEdit :title="dialogForm.title" :dialogFormVisible="dialogForm.visible" :data="dialogForm.data"
      :typeDisabled="dialogForm.typeDisabled" :departTree="data" :type="dialogForm.type" @val-change="valChange"
      @cancel="editorCancel()">
    </ResourceEdit>

    <!-- 鼠标右击后显示的按钮 -->
    <div style="position: absolute; width: 300px; height: 50px;" @mouseover="btnMouseOver" @mouseleave="btnMouseLeave"
      v-show="showBtns" id="btns">
      <el-button @click="info()" style="margin-left: 25px;" type="info" size="mini" plain>详情</el-button>

      <el-button v-permission="permission.update.code" @click="editResource(rightClickData)" type="primary" size="mini"
        plain>编辑</el-button>

      <el-button v-permission="permission.save.code" @click="addResource(rightClickData)" v-if="rightClickData.type === enums.ResourceTypeEnum.MENU.value"
        type="success" size="mini" plain>新增</el-button>

      <el-button v-permission="permission.changeStatus.code" @click="changeStatus(rightClickData, 0)" v-if="rightClickData.status === 1 && rightClickData.type === enums.ResourceTypeEnum.PERMISSION.value"
        type="warning" size="mini" plain>禁用</el-button>

      <el-button v-permission="permission.changeStatus.code" @click="changeStatus(rightClickData, 1)" v-if="rightClickData.status === 0 && rightClickData.type === enums.ResourceTypeEnum.PERMISSION.value"
        type="success" size="mini" plain>启用</el-button>

      <el-button v-permission="permission.del.code" v-if="rightClickData.children == null" @click="deleteMenu(rightClickData)"
        type="danger" size="mini" plain>删除</el-button>
      </el-button>
    </div>
  </div>
</template>

<script>
  import ToolBar from '~/components/ToolBar/ToolBar.vue';
  import TreeDetails from './TreeDetails.vue';
  import ResourceEdit from './resource_edit.vue'
  import permissions from '../permissions.js'
  import enums from '../enums.js'

  export default {
    data() {
      return {
        enums: enums,
        permission: permissions.menu,
        showBtns: false,
        // 当前鼠标右击的节点数据
        rightClickData: {},
        //弹出框对象
        dialogForm: {
          title: "",
          visible: false,
          data: {},
          // 资源类型选择是否选
          typeDisabled: true
        },
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
        this.$confirm(`此操作将删除 [${data.name}], 是否继续?`, '提示', {
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
        });
      },
      search() {
        this.permission.list.request(null).then(res => {
          this.data = res;
        })
      },
      addResource(data) {
        let dialog = this.dialogForm;
        dialog.visible = true;
        dialog.data = {};
        let menu = enums.ResourceTypeEnum.MENU.value;
        let permission = enums.ResourceTypeEnum.PERMISSION.value;
        // 添加顶级菜单情况
        if (!data) {
          dialog.typeDisabled = true;
          dialog.data.type = menu;
          dialog.title = '添加顶级菜单';
          return;
        }
        // 添加子菜单，把当前菜单id作为新增菜单pid
        dialog.data.pid = data.id;
        dialog.title = '添加“' + data.name + '”的子资源 ';
        if (data.children === null || data.children.length === 0) {
          // 如果子节点不存在，则资源类型可选择
          dialog.typeDisabled = false;
        } else {
          dialog.typeDisabled = true;
          let hasPermission = false;
          for (let c of data.children) {
            if (c.type === permission) {
              hasPermission = true;
              break;
            }
          }
          // 如果子节点中存在权限资源，则只能新增权限资源，否则只能新增菜单资源
          if (hasPermission) {
            dialog.data.type = permission;
          } else {
            dialog.data.type = menu;
          }
        }
      },
      editResource(data) {
        this.dialogForm.visible = true;
        this.dialogForm.data = data;
        this.dialogForm.typeDisabled = true;
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
      /**
       * 右击节点事件
       * @param {Object} event
       * @param {Object} data
       */
      rightClick(event, data) {
        this.showBtns = true;
        this.rightClickData = data;
        let btns = document.querySelector("#btns");
        /* 菜单定位基于鼠标点击位置 */
        btns.style.left = event.clientX - 15 + "px";
        btns.style.top = event.clientY - 10 + "px";
      },
      /**
       * 右击节点后鼠标在btns内
       */
      btnMouseOver() {
        this.showBtns = true;
      },
      /**
       * 右击事件后鼠标移出btns块
       */
      btnMouseLeave() {
        this.showBtns = false;
        this.rightClickData = {};
      },
      info() {
        let info = this.rightClickData;
        if (info.type === enums.ResourceTypeEnum.MENU.value) {
          this.$alert('<strong style="margin-right: 18px">名称:</strong>' + info.name +
            ' <br/><br/><strong style="margin-right: 18px">图标:</strong>' + (info.icon ? info.icon : "无") +
            ' <br/><br/><strong style="margin-right: 18px">路径:</strong>' + (info.path ? info.path : "无") +
            ' <br/><br/><strong style="margin-right: 18px">权重:</strong>' + info.weight +
            ' <br/><br/><strong style="margin-right: 18px">新建时间:</strong>' + info.createTime +
            ' <br/><br/><strong style="margin-right: 18px">修改时间:</strong>' + info.updateTime +
            '', '菜单详情', {
              type: 'info',
              dangerouslyUseHTMLString: true,
              closeOnClickModal: true,
              showConfirmButton: false
            }).catch(r => {});
          return;
        }

        this.$alert('<strong style="margin-right: 18px">名称:</strong>' + info.name +
          ' <br/><br/><strong style="margin-right: 18px">权限code:</strong>' + (info.code ? info.code : "无") +
          // ' <br/><br/><strong style="margin-right: 18px">路径:</strong>' + (info.path ? info.path : "无") +
          ' <br/><br/><strong style="margin-right: 18px">权重:</strong>' + info.weight +
          ' <br/><br/><strong style="margin-right: 18px">新建时间:</strong>' + info.createTime +
          ' <br/><br/><strong style="margin-right: 18px">修改时间:</strong>' + info.updateTime +
          '', '权限详情', {
            type: 'info',
            dangerouslyUseHTMLString: true,
            closeOnClickModal: true,
            showConfirmButton: false
          }).catch(r => {});
      },
      // 自定义节点内容(废弃)
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

  .none-select {
    moz-user-select: -moz-none;
    -moz-user-select: none;
    -o-user-select: none;
    -khtml-user-select: none;
    -webkit-user-select: none;
    -ms-user-select: none;
    user-select: none;
  }
</style>
