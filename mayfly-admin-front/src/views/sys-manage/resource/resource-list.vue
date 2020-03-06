<template>
  <div class="menu">
    <ToolBar>
      <div>
        <span style="font-size: 14px;">
          <i class="el-icon-info"></i>红色字体表示禁用状态
        </span>
      </div>
      <el-button
        v-permission="permission.save.code"
        type="primary"
        icon="el-icon-plus"
        size="mini"
        @click="addResource(false)"
      >添加</el-button>
    </ToolBar>
    <el-tree
      class="none-select"
      :indent="38"
      node-key="id"
      :props="props"
      :data="data"
      @node-expand="handleNodeExpand"
      @node-collapse="handleNodeCollapse"
      :default-expanded-keys="defaultExpandedKeys"
      :expand-on-click-node="false"
    >
      <span class="custom-tree-node" slot-scope="{ node, data }">
        <span style="font-size: 13px" v-if="data.type === enums.ResourceTypeEnum.MENU.value">
          <span style="color: #3c8dbc">【</span>
          {{ data.name }}
          <span style="color: #3c8dbc">】</span>
          <el-tag v-if="data.children !== null" size="mini">{{data.children.length}}</el-tag>
        </span>
        <span style="font-size: 13px" v-if="data.type === enums.ResourceTypeEnum.PERMISSION.value">
          <span style="color: #3c8dbc">【</span>
          <span :style="data.status == 1 ? 'color: #67c23a;' : 'color: #f67c6c;'">{{ data.name }}</span>
          <span style="color: #3c8dbc">】</span>
        </span>

        <el-link
          @click="info(data)"
          style="margin-left: 25px;"
          icon="el-icon-view"
          type="info"
          :underline="false"
        />

        <el-link
          v-permission="permission.update.code"
          @click="editResource(data)"
          type="primary"
          icon="el-icon-edit"
          :underline="false"
        />

        <el-link
          v-permission="permission.save.code"
          @click="addResource(data)"
          v-if="data.type === enums.ResourceTypeEnum.MENU.value"
          icon="el-icon-circle-plus-outline"
          :underline="false"
          type="success"
        />

        <el-link
          v-permission="permission.resource.code"
          @click="changeStatus(data, 0)"
          v-if="data.status === 1 && data.type === enums.ResourceTypeEnum.PERMISSION.value"
          icon="el-icon-circle-close"
          :underline="false"
          type="warning"
        />

        <el-link
          v-permission="permission.resource.code"
          @click="changeStatus(data, 1)"
          v-if="data.status === 0 && data.type === enums.ResourceTypeEnum.PERMISSION.value"
          type="success"
          icon="el-icon-circle-check"
          :underline="false"
          plain
        />

        <el-link
          v-permission="permission.del.code"
          v-if="data.children == null"
          @click="deleteMenu(data)"
          type="danger"
          icon="el-icon-remove-outline"
          :underline="false"
          plain
        />
      </span>
    </el-tree>

    <ResourceEdit
      :title="dialogForm.title"
      :dialogFormVisible="dialogForm.visible"
      :data="dialogForm.data"
      :typeDisabled="dialogForm.typeDisabled"
      :departTree="data"
      :type="dialogForm.type"
      @val-change="valChange"
      @cancel="editorCancel()"
    ></ResourceEdit>
  </div>
</template>

<script>
import ToolBar from '~/components/tool-bar/tool-bar.vue'
import ResourceEdit from './resource-edit.vue'
import permissions from '../permissions.js'
import enums from '../enums.js'
import { resourceApi } from '../api'

export default {
  data() {
    return {
      enums: enums,
      permission: permissions.resource,
      showBtns: false,
      // 当前鼠标右击的节点数据
      rightClickData: {},
      //弹出框对象
      dialogForm: {
        title: '',
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
        resourceApi.del
          .request({
            id: data.id
          })
          .then(res => {
            this.$message.success('删除成功！')
            this.search()
          })
      })
    },
    async search() {
      let res = await resourceApi.list.request()
      this.data = res
    },
    addResource(data) {
      let dialog = this.dialogForm
      dialog.visible = true
      dialog.data = {}
      let menu = enums.ResourceTypeEnum.MENU.value
      let permission = enums.ResourceTypeEnum.PERMISSION.value
      // 添加顶级菜单情况
      if (!data) {
        dialog.typeDisabled = true
        dialog.data.type = menu
        dialog.title = '添加顶级菜单'
        return
      }
      // 添加子菜单，把当前菜单id作为新增菜单pid
      dialog.data.pid = data.id
      dialog.title = '添加“' + data.name + '”的子资源 '
      if (data.children === null || data.children.length === 0) {
        // 如果子节点不存在，则资源类型可选择
        dialog.typeDisabled = false
      } else {
        dialog.typeDisabled = true
        let hasPermission = false
        for (let c of data.children) {
          if (c.type === permission) {
            hasPermission = true
            break
          }
        }
        // 如果子节点中存在权限资源，则只能新增权限资源，否则只能新增菜单资源
        if (hasPermission) {
          dialog.data.type = permission
        } else {
          dialog.data.type = menu
        }
      }
    },
    async editResource(data) {
      this.dialogForm.visible = true
      this.dialogForm.data = await resourceApi.detail.request({
        id: data.id
      })
      this.dialogForm.typeDisabled = true
      this.dialogForm.title = '修改“' + data.name + '”菜单'
    },
    valChange(data) {
      this.search()
      this.dialogForm.visible = false
    },
    editorCancel() {
      this.dialogForm.visible = false
      this.dialogForm.data = null
    },
    async changeStatus(data, status) {
      await resourceApi.changeStatus.request({
        id: data.id,
        status: status
      })
      data.status = status
      this.$message.success('操作成功！')
    },
    // 节点被展开时触发的事件
    handleNodeExpand(data, node) {
      let id = node.data.id
      if (!this.defaultExpandedKeys.includes(id)) {
        this.defaultExpandedKeys.push(id)
      }
    },
    // 关闭节点
    handleNodeCollapse(data, node) {
      this.removeDeafultExpandId(node.data.id)

      let childNodes = node.childNodes
      for (let cn of childNodes) {
        if (cn.data.type == 2) {
          return
        }
        if (cn.expanded) {
          this.removeDeafultExpandId(cn.data.id)
        }
        // 递归删除展开的子节点节点id
        this.handleNodeCollapse(data, cn)
      }
    },
    removeDeafultExpandId(id) {
      let index = this.defaultExpandedKeys.indexOf(id)
      if (index > -1) {
        this.defaultExpandedKeys.splice(index, 1)
      }
    },
    async info(data) {
      let info = await resourceApi.detail.request({ id: data.id })
      if (info.type === enums.ResourceTypeEnum.MENU.value) {
        this.$alert(
          '<strong style="margin-right: 18px">名称:</strong>' +
            info.name +
            ' <br/><strong style="margin-right: 18px">图标:</strong>' +
            (info.icon ? info.icon : '无') +
            ' <br/><strong style="margin-right: 18px">路径:</strong>' +
            (info.code ? info.code : '无') +
            ' <br/><strong style="margin-right: 18px">权重:</strong>' +
            info.weight +
            ' <br/><strong style="margin-right: 18px">创建人:</strong>' +
            info.createAccount +
            ' <br/><strong style="margin-right: 18px">创建时间:</strong>' +
            info.createTime +
            ' <br/><strong style="margin-right: 18px">修改人:</strong>' +
            info.updateAccount +
            ' <br/><strong style="margin-right: 18px">修改时间:</strong>' +
            info.updateTime +
            '',
          '菜单详情',
          {
            type: 'info',
            dangerouslyUseHTMLString: true,
            closeOnClickModal: true,
            showConfirmButton: false
          }
        ).catch(r => {})
        return
      }

      this.$alert(
        '<strong style="margin-right: 18px">名称:</strong>' +
          info.name +
          ' <br/><strong style="margin-right: 18px">权限code:</strong>' +
          (info.code ? info.code : '无') +
          // ' <br/><br/><strong style="margin-right: 18px">路径:</strong>' + (info.path ? info.path : "无") +
          ' <br/><strong style="margin-right: 18px">权重:</strong>' +
          info.weight +
          ' <br/><strong style="margin-right: 18px">创建人:</strong>' +
          info.createAccount +
          ' <br/><strong style="margin-right: 18px">创建时间:</strong>' +
          info.createTime +
          ' <br/><strong style="margin-right: 18px">修改人:</strong>' +
          info.updateAccount +
          ' <br/><strong style="margin-right: 18px">修改时间:</strong>' +
          info.updateTime +
          '',
        '权限详情',
        {
          type: 'info',
          dangerouslyUseHTMLString: true,
          closeOnClickModal: true,
          showConfirmButton: false
        }
      ).catch(r => {})
    }
  },
  components: {
    ToolBar,
    ResourceEdit
  },
  mounted: function() {
    this.search()
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
