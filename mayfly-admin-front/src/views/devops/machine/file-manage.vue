<template>
  <div class="file-manage">

    <el-dialog :title="title" :visible.sync="visible" :show-close="true" :before-close="handleClose" width="800px">
      <div style="float: right;">
        <el-button v-permission="addFile.code" type="primary" @click="add" icon="el-icon-plus" size="mini" plain>添加</el-button>
      </div>
      <el-table :data="fileTable" stripe style="width: 100%">
        <el-table-column prop="name" label="名称" width="">
          <template slot-scope="scope">
            <el-input v-model="scope.row.name" size="mini" :disabled="scope.row.id != null" clearable></el-input>
          </template>
        </el-table-column>
        <el-table-column prop="name" label="类型" width="">
          <template slot-scope="scope">
            <el-select :disabled="scope.row.id != null" size="mini" v-model="scope.row.type" style="width: 100px"
              placeholder="请选择">
              <el-option v-for="item in enums.FileTypeEnum" :key="item.value" :label="item.label" :value="item.value">
              </el-option>
            </el-select>
          </template>
        </el-table-column>
        <el-table-column prop="path" label="路径" width="">
          <template slot-scope="scope">
            <el-input v-model="scope.row.path" :disabled="scope.row.id != null" size="mini" clearable></el-input>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="">
          <template slot-scope="scope">
            <el-button v-if="scope.row.id == null" @click="addFiles(scope.row)" type="success" :ref="scope.row"
              icon="el-icon-success" size="mini" plain>确定</el-button>
            <el-button v-if="scope.row.id != null" @click="getConf(scope.row)" type="primary" :ref="scope.row" icon="el-icon-tickets"
              size="mini" plain>查看</el-button>
            <el-button v-permission="delFile.code" type="danger" :ref="scope.row" @click="deleteRow(scope.$index, scope.row)"
              icon="el-icon-delete" size="mini" plain>删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>

    <el-dialog :title="tree.title" :visible.sync="tree.visible" width="650px">
      <div style="height: 45vh;overflow: auto;">
        <el-tree ref="fileTree" :load="loadNode" :props="props" lazy node-key="id" :expand-on-click-node="false">
          <span class="custom-tree-node" slot-scope="{ node, data }">
            <span v-if="data.type == 'd' && !node.expanded">
              <i class="el-icon-folder"></i>
            </span>
            <span v-if="data.type == 'd' && node.expanded">
              <i class="el-icon-folder-opened"></i>
            </span>
            <span v-if="data.type == '-'">
              <i class="el-icon-document"></i>
            </span>

            <span style="display: inline-block;width: 400px;">
              {{ node.label }}
              <span style="color: #67c23a;" v-if="data.type == '-'">
                &nbsp;&nbsp;[{{ data.size }}]
              </span>
            </span>

            <span>
              <el-link v-permission="permission.fileContent.code" @click="getFileContent(tree.folder.id, data.path)" v-if="data.type == '-'" type="info" icon="el-icon-view"
                :underline="false" />

              <el-upload v-permission="permission.uploadFile.code" :on-success="uploadSuccess" :headers="{token}" :data="{fileId: tree.folder.id, path: data.path}"
                :action="permission.uploadFile.getUrl()" :show-file-list="false" name="file"
                multiple :limit="100" style="display: inline-block;">
                <el-link v-if="data.type == 'd'" icon="el-icon-upload" :underline="false" />
              </el-upload>

              <el-link v-permission="permission.rmFile.code" v-if="!dontOperate(data)" @click="deleteFile(node, data)" type="danger" icon="el-icon-delete"
                :underline="false" />
            </span>
          </span>
        </el-tree>
      </div>
    </el-dialog>

    <el-dialog :title="fileContent.dialogTitle" :visible.sync="fileContent.contentVisible" width="650px">
      <el-form :model="form">
        <el-form-item>
          <el-input v-model="fileContent.content" type="textarea" :autosize="{ minRows: 10, maxRows:15}" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="fileContent.contentVisible = false" size="mini">取 消</el-button>
        <el-button v-permission="updateFileContent.code" type="primary" @click="updateContent" size="mini">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import permission from '../permissions.js'
  import enums from './enums.js'
  import Req from "~/common/request"
  export default {
    name: 'FileManage',
    props: {
      visible: Boolean,
      machineId: [Number],
      title: String,
    },
    data() {
      return {
        permission: permission.machine,
        addFile: permission.machine.addConf,
        delFile: permission.machine.delConf,
        updateFileContent: permission.machine.updateFileContent,
        files: permission.machine.files,
        enums: enums,
        activeName: "conf-file",
        token: sessionStorage.getItem("token"),
        form: {
          id: null,
          type: null,
          name: '',
          remark: '',
        },
        fileTable: [],
        btnLoading: false,
        fileContent: {
          fileId: null,
          content: '',
          contentVisible: false,
          dialogTitle: '',
          path: ''
        },
        tree: {
          title: '',
          visible: false,
          folder: {},
          node: {},
          resolve: {}
        },
        props: {
          label: 'name',
          children: 'zones',
          isLeaf: 'leaf'
        }
      }
    },
    watch: {
      'machineId': {
        handler: function() {
          if (this.machineId) {
            this.getFiles();
          }
        },
        deep: true
      }
    },
    methods: {
      async getFiles() {
        let res = await this.files.request({id: this.machineId});
        this.fileTable = res;
      },
      /**
       * tab切换触发事件
       * @param {Object} tab
       * @param {Object} event
       */
      handleClick(tab, event) {
        // if (tab.name == 'file-manage') {
        //   this.fileManage.node.childNodes = [];
        //   this.loadNode(this.fileManage.node, this.fileManage.resolve);
        // }
      },
      add() {
        // 往数组头部添加元素
        this.fileTable = [{}].concat(this.fileTable);
      },
      async addFiles(row) {
        row.machineId = this.machineId;
        await this.addFile.request(row);
        this.$message.success('添加成功');
        this.getFiles();
      },
      deleteRow(idx, row) {
        if (row.id) {
          this.$confirm(`此操作将删除 [${row.name}], 是否继续?`, '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            // 删除配置文件
            this.delFile.request({
              machineId: this.machineId,
              id: row.id
            }).then(res => {
              this.fileTable.splice(idx, 1);
            })
          });
        } else {
          this.fileTable.splice(idx, 1);
        }
      },
      getConf(row) {
        if (row.type == 1) {
          this.tree.folder = row;
          this.tree.title = row.name;
          this.tree.node.childNodes = [];
          this.loadNode(this.tree.node, this.tree.resolve);
          this.tree.visible = true;
          return;
        }
        this.getFileContent(row.id, row.path);
      },
      async getFileContent(fileId, path) {
        let res = await this.permission.fileContent.request({
          fileId, path
        });
        this.fileContent.content = res;
        this.fileContent.fileId = fileId;
        this.fileContent.dialogTitle = path;
        this.fileContent.path = path;
        this.fileContent.contentVisible = true;
      },
      async updateContent() {
        await this.updateFileContent.request({
          content: this.fileContent.content,
          id: this.fileContent.fileId,
          path: this.fileContent.path
        });
        this.$message.success('修改成功');
        this.fileContent.contentVisible = false;
        this.fileContent.content = '';
      },
      /**
       * 关闭取消按钮触发的事件
       */
      handleClose() {
        this.$emit('cancel');
        this.activeName = 'conf-file';
        this.fileTable = [];
        this.tree.folder = {};
      },

      /**
       * 加载文件树节点
       * @param {Object} node
       * @param {Object} resolve
       */
      async loadNode(node, resolve) {
        if (typeof resolve !== 'function') {
          return;
        }
        if (node.level === 0) {
          this.tree.node = node;
          this.tree.resolve = resolve;

          let folder = this.tree.folder;
          let path = folder ? folder.path : '/';
          return resolve([{
            name: path,
            type: 'd',
            path: path
          }]);
        }

        let path;
        let data = node.data;
        // 只有在第一级节点时，name==path，即上述level==0时设置的
        if (!data || data.name == data.path) {
          path = this.tree.folder.path;
        } else {
          path = data.path;
        }

        let res = await this.permission.lsFile.request({fileId: this.tree.folder.id, path});
        for (let file of res) {
          let type = file.type;
          if (type != 'd') {
            file.leaf = true;
          }
        }
        return resolve(res);
      },

      deleteFile(node, data) {
        let file = data.path
        this.$confirm(`此操作将删除 [${file}], 是否继续?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.permission.rmFile.request({fileId: this.tree.folder.id, path: file}).then(res => {
            this.$message.success("删除成功");
            this.$refs.fileTree.remove(node);
          })
        }).catch(() => {});
      },

      uploadSuccess(res) {
        if (res.success) {
          this.$message.success("文件上传中...");
        } else {
          this.$message.error(res.msg);
        }
      },

      dontOperate(data) {
        let path = data.path + data.name;
        let ls = ['/', '//', '/usr', '/opt', '/run', '/etc', '/proc', '/var', '/mnt', '/boot', '/dev', '/home',
          '/media', '/root'
        ];
        return ls.indexOf(path) != -1;
      }
    },
    components: {}
  }
</script>
<style lang="less">

</style>
