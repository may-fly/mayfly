<template>
  <div style="width: 50px;">
    <el-dialog :title="'编辑“'+role.name+'”菜单权限'" :visible="visible" :show-close="false" width="300px">
      <el-tree ref="menuTree" :data="menus" show-checkbox node-key="id" :default-checked-keys="defaultCheckedKeys"
        :props="defaultProps">
      </el-tree>
      <div slot="footer" class="dialog-footer">
        <el-button @click="$emit('cancel');" size="small">取 消</el-button>
        <el-button type="primary" @click="btnOk" size="small">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import permission from '../permissions.js'

  export default {
    props: {
      visible: Boolean,
      role: [Object, Boolean],
      title: String
    },
    data() {
      return {
        btns: {
          menuList: {}
        },
        permission: permission.role,
        menus: [],
        defaultProps: {
          children: 'children',
          label: 'name'
        },
        defaultCheckedKeys: []
      };
    },
    watch: {
      role() {
        permission.menu.list.request(null).then(res => {
          // 获取所有菜单列表
          this.menus = res;
          // 获取该角色拥有的菜单id
          this.permission.roleMenus.request({
            id: this.role.id
          }).then(res => {
            let hasIds = res;
            let hasLeafIds = [];
            // 获取菜单的所有叶子节点
            let leafIds = this.getAllLeafIds(this.menus);
            for (let id of leafIds) {
              // 判断角色拥有的菜单id中，是否含有该叶子节点，有则添加进入用户拥有的叶子节点
              if (hasIds.includes(id)) {
                hasLeafIds.push(id);
              }
            }
            this.defaultCheckedKeys = hasLeafIds;
          });
        })
      }
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
      btnOk() {
        let saveMenu = this.permission.saveMenu;
        let permission = this.$Permission.getPermission(saveMenu.code);

        if (!permission.show) {
          this.$message.error('您没有该权限!');
        } else {
          let menuIds = this.$refs.menuTree.getCheckedKeys();
          let halfMenuIds = this.$refs.menuTree.getHalfCheckedKeys()
          let menus = [].concat(menuIds, halfMenuIds).join(",");
          saveMenu.request({
            id: this.role.id,
            resourceIds: menus
          }).then(res => {
            if (res) {
              this.$message.success('保存成功!');
              this.$emit('cancel')
            }
          });
        }
      }
    },
    mounted() {

    }
  }
</script>

<style>

</style>
