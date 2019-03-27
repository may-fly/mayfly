<template>
  <div class="role-dialog">
    <el-dialog :title="'编辑“'+role.name+'”的权限'" :visible="visible" :show-close="false">
      <ToolBar>
        <div style="float: left">
          <el-input placeholder="请输入权限code" size="small" style="width: 150px" v-model="query.code" @clear="clear()"
            clearable>
          </el-input>
          <el-button @click="search" type="success" icon="el-icon-search" size="small"></el-button>
        </div>
      </ToolBar>
      <el-table :data="allPermissions" stripe border ref="permissionsTable" @select="select" style="width: 100%">
        <el-table-column type="selection" width="35">
        </el-table-column>
        <el-table-column prop="code" label="权限code">
        </el-table-column>
        <el-table-column prop="description" label="权限描述">
          <div slot-scope="scope">
            {{ scope.row.description ? scope.row.description : '暂无描述' }}
          </div>
        </el-table-column>
        <el-table-column prop="uriPattern" label="URI">
        </el-table-column>
      </el-table>
      <el-pagination @current-change="handlePageChange" style="text-align: center;margin-top: 20px;" background layout="prev, pager, next, total, jumper"
        :total="total" :current-page.sync="query.pageNum" :page-size="query.pageSize">
      </el-pagination>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancel()">取 消</el-button>
        <el-button type="primary" :loading="btnLoading" @click="btnOk">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import AllRouter from '~/router/'
  import permission from '../permissions.js'
  import ToolBar from '~/components/ToolBar/ToolBar.vue';

  export default {
    name: 'PermissionsEdit',
    props: {
      visible: false,
      role: [Object, Boolean],
    },
    data() {
      return {
        permission: permission,
        btnLoading: false,
        allPermissions: [],
        searchPermissionsDisplayName: '',
        multipleSelection: [],
        query: {
          status: 1,
          pageNum: 1,
          pageSize: 8,
          code: null
        },
        total: 0,
        rolePermissions: []
      }
    },
    watch: {
      'role': {
        handler: function() {
          if (!this.role) 
            return;
          let rolePermissions = this.permission.role.rolePermissions;
          let permission = this.$Permission.getPermission(rolePermissions.code);
          if (!permission.show) {
            this.$message.error('您没有该权限!');
          } else {
            rolePermissions.request({
              id: this.role.id
            }, res => {
              this.rolePermissions = res;
              this.search();
            })
          }
        },
        deep: true
      }
    },
    methods: {
      handlePageChange(curPage) {
        this.search();
      },
      select(val, row) {
        let rolePermissions = this.rolePermissions;
        // 如果角色的权限列表id存在则为取消该权限(删除角色权限id列表中的该记录id)，否则为新增权限
        if (rolePermissions.includes(row.id)) {
          for (let i = 0; i < rolePermissions.length; i++) {
            let item = rolePermissions[i];
            if (item === row.id) {
              rolePermissions.splice(i, 1);
              break;
            }
          }
        } else {
          rolePermissions.push(row.id);
        }
      },
      /**
       * 检查是否勾选权限,即是否拥有权限
       */
      checkSelected() {
        // 必须用异步，否则勾选不了
        setTimeout(() => {
          this.$refs.permissionsTable.clearSelection();
          this.allPermissions.forEach(permission => {
            if (this.rolePermissions.includes(permission.id)) {
              this.$refs.permissionsTable.toggleRowSelection(permission, true);
            }
          })
        }, 50)
      },
      btnOk() {
        let savePermission = this.permission.role.savePermission;
        let permission = this.$Permission.getPermission(savePermission.code);
        if (!permission.show) {
          this.$message.error('您没有该权限!');
        } else {
          let permissionIds = this.rolePermissions.join(",");
          savePermission.request({
            id: this.role.id,
            resourceIds: permissionIds
          }, res => {
            if (res) {
              this.$message.success('保存成功!');
              this.cancel();
            }
          })
        }
      },
      /**
       * 取消
       */
      cancel() {
        this.query.pageNum = 1;
        this.query.code = null;
        this.$emit('cancel');
      },
      /**
       * 清空查询框
       */
      clear() {
        this.query.pageNum = 1;
        this.query.code = null;
        this.search();
      },
      search() {
        let listPermission = this.permission.permission.list;
        let permission = this.$Permission.getPermission(listPermission.code);
        if (!permission.show) {
          this.$message.error('您没有该权限!');
        } else {
          listPermission.request(this.query, res => {
            this.allPermissions = res.list;
            this.total = res.total;
            this.checkSelected();
          })
        }
      }
    },
    components: {
      ToolBar
    }
  }
</script>
<style lang="less">
  .permissions-dialog {}
</style>
