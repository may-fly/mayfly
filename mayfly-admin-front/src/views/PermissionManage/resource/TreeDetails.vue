<template>
  <div class="tree-details">
    <el-row type="flex">
      <el-col style="flex: 1">
        <span style="font-size: 13px" v-if="data.data.type === enums.ResourceTypeEnum.MENU.value">
          <span style="color: #3c8dbc">【</span>{{ data.data.name }}<span style="color: #3c8dbc">】</span>
          <el-tag v-if="data.data.children !== null" size="mini">{{data.data.children.length}}</el-tag>
        </span>
        <span style="font-size: 13px" v-if="data.data.type === enums.ResourceTypeEnum.PERMISSION.value">
          <span style="color: #3c8dbc">【</span><span :style="data.data.status == 1 ? 'color: #67c23a;' : 'color: #f67c6c;'">{{ data.data.name }}</span><span
            style="color: #3c8dbc">】</span>
        </span>
      </el-col>
      <el-col style="width: 850px;">
        <el-tooltip content="详情" placement="top" effect="light">
          <el-button @click="info(data,$event)" type="info" icon="el-icon-tickets" size="mini" circle></el-button>
        </el-tooltip>

        <el-tooltip content="编辑" placement="top" effect="light">
          <el-button v-permission="permission.update.code" @click="edit(data,$event)" type="primary" icon="el-icon-edit"
            size="mini" circle></el-button>
        </el-tooltip>

        <el-tooltip content="新增" placement="top" effect="light">
          <el-button v-permission="permission.save.code" v-if="data.data.type === enums.ResourceTypeEnum.MENU.value"
            @click="add(data, $event)" type="success" icon="el-icon-plus" size="mini" circle></el-button>
        </el-tooltip>

        <el-tooltip content="禁用" placement="top" effect="light">
          <el-button v-permission="permission.changeStatus.code" v-if="data.data.status === 1 && data.data.type === enums.ResourceTypeEnum.PERMISSION.value"
            @click="changeStatus(data, $event, 0)" type="warning" icon="el-icon-close" size="mini" circle></el-button>
        </el-tooltip>

        <el-tooltip content="启用" placement="top" effect="light">
          <el-button v-permission="permission.changeStatus.code" v-if="data.data.status === 0 && data.data.type === enums.ResourceTypeEnum.PERMISSION.value"
            @click="changeStatus(data, $event, 1)" type="success" icon="el-icon-check" size="mini" circle></el-button>
        </el-tooltip>

        <el-tooltip content="删除" placement="top" effect="light">
          <el-button v-if="data.data.children == null" v-permission="permission.del.code" @click="deleteDepart(data, $event)" type="danger" icon="el-icon-delete"
            size="mini" circle></el-button>
          </el-button>
        </el-tooltip>


        <!-- <el-button @click="add(data,$event)" type="primary" icon="el-icon-edit"  size="mini">编辑操作</el-button> -->
      </el-col>
    </el-row>
  </div>
</template>

<script>
  import permission from '../permissions.js'
  import enums from '../enums.js'
  export default {
    name: 'TreeCategory',
    props: {
      data: Object,
    },
    data() {
      return {
        enums: enums,
        permission: permission.menu
      }
    },
    methods: {
      deleteDepart(node, e) {
        this.$emit('delete-menu', node.data);
        e.cancelBubble = true;
      },
      add(node, e) {
        this.$emit('add-menu', node.data);
        e.cancelBubble = true;
      },
      edit(node, e) {
        this.$emit('edit-menu', node.data);
        e.cancelBubble = true;
      },
      changeStatus(node, e, changeStatus) {
        this.$emit('changeStatus', node.data, changeStatus);
        e.cancelBubble = true;
      },
      info(node, e) {
        let info = node.data;
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
            }).then(r => {});
          e.cancelBubble = true;
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
          }).then(r => {});

        e.cancelBubble = true;
      }

    },
    mounted: function() {
      // 			this.btns.add = this.$Permission.getPermission(code.menu.save);
      // 			this.btns.del = this.$Permission.getPermission(code.menu.del); 
    },
    components: {

    }
  }
</script>
<style lang="less">
  .tree-details {
    flex: 1;

    .el-button.is-circle {
      padding: 8px;
    }
  }

  .category-description {
    text-indent: 2em;
  }
</style>
