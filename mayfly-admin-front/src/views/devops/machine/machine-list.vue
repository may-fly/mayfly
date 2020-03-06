<template>
  <div>
    <ToolBar>
      <el-button
        v-permission="permission.machine.code"
        type="primary"
        icon="el-icon-plus"
        size="mini"
        @click="openFormDialog(false)"
        plain
      >添加</el-button>
      <el-button
        v-permission="permission.machine.code"
        type="primary"
        icon="el-icon-edit"
        size="mini"
        :disabled="currentId == null"
        @click="openFormDialog(currentData)"
        plain
      >编辑</el-button>
      <el-button
        v-permission="permission.machine.code"
        :disabled="currentId == null"
        @click="deleteMachine(currentId)"
        type="danger"
        icon="el-icon-delete"
        size="mini"
      >删除</el-button>
      <el-button
        v-permission="permission.machine.code"
        type="success"
        :disabled="currentId == null"
        @click="fileManage(currentData)"
        size="mini"
        plain
      >文件管理</el-button>

      <div style="float: right;">
        <el-input
          placeholder="host"
          size="mini"
          style="width: 140px;"
          v-model="params.host"
          @clear="search"
          plain
          clearable
        ></el-input>
        <el-button @click="search" type="success" icon="el-icon-search" size="mini"></el-button>
      </div>
    </ToolBar>

    <el-table :data="table" stripe style="width: 100%" @current-change="choose">
      <el-table-column label="选择" width="55px">
        <template slot-scope="scope">
          <el-radio v-model="currentId" :label="scope.row.id">
            <i></i>
          </el-radio>
        </template>
      </el-table-column>
      <el-table-column prop="name" label="名称" width></el-table-column>
      <el-table-column prop="ip" label="IP" width></el-table-column>
      <el-table-column prop="port" label="端口" width></el-table-column>
      <el-table-column prop="username" label="用户名"></el-table-column>
      <el-table-column prop="createTime" label="创建时间"></el-table-column>
      <el-table-column prop="updateTime" label="更新时间"></el-table-column>
      <!-- <el-table-column label="操作" width="200px">
        <template slot-scope="scope">
          <el-button type="primary" @click="info(scope.row.id)" :ref="scope.row" icom="el-icon-tickets" size="mini"
            plain>info</el-button>
          <el-button type="success" @click="serviceManager(scope.row)" :ref="scope.row" size="mini" plain>服务管理</el-button>
        </template>
      </el-table-column>-->
    </el-table>

    <FileManage
      :title="dialog.title"
      :visible="dialog.visible"
      :machineId="dialog.machineId"
      @cancel="cancel()"
    />

    <dynamic-form-dialog
      :visible="formDialog.visible"
      :title="formDialog.title"
      :formInfo="formDialog.formInfo"
      :formData="formDialog.formData"
      @cancel="closeDialog"
      @submitSuccess="submitSuccess"
    ></dynamic-form-dialog>
  </div>
</template>

<script>
import ToolBar from '~/components/tool-bar/tool-bar.vue'
import { DynamicFormDialog } from '~/components/dynamic-form'
import FileManage from './file-manage.vue'
import permission from './permissions.js'
import { machineApi } from './api'

export default {
  data() {
    return {
      table: [],
      permission: permission,
      currentId: null,
      currentData: null,
      params: {
        host: null,
        clusterId: null
      },
      dialog: {
        machineId: null,
        visible: false,
        title: null
      },
      formDialog: {
        visible: false,
        title: null,
        formInfo: {
          addPermission: machineApi.save,
          updatePermission: machineApi.update,
          formRows: [
            [
              {
                type: 'input',
                label: '名称：',
                name: 'name',
                placeholder: '请输入名称',
                rules: [
                  {
                    required: true,
                    message: '请输入名称',
                    trigger: ['blur', 'change']
                  }
                ]
              }
            ],
            [
              {
                type: 'input',
                label: 'ip：',
                name: 'ip',
                placeholder: '请输入ip',
                rules: [
                  {
                    required: true,
                    message: '请输入ip',
                    trigger: ['blur', 'change']
                  }
                ]
              }
            ],
            [
              {
                type: 'input',
                label: '端口号：',
                name: 'port',
                placeholder: '请输入端口号',
                inputType: 'number',
                rules: [
                  {
                    required: true,
                    message: '请输入ip',
                    trigger: ['blur', 'change']
                  }
                ]
              }
            ],
            [
              {
                type: 'input',
                label: '用户名：',
                name: 'username',
                placeholder: '请输入用户名',
                rules: [
                  {
                    required: true,
                    message: '请输入用户名',
                    trigger: ['blur', 'change']
                  }
                ]
              }
            ],
            [
              {
                type: 'input',
                label: '密码：',
                name: 'password',
                placeholder: '请输入密码',
                inputType: 'password'
              }
            ]
          ]
        },
        formData: null
      }
    }
  },
  methods: {
    choose(item) {
      if (!item) {
        return
      }
      this.currentId = item.id
      this.currentData = item
    },
    openFormDialog(redis) {
      let dialogTitle
      if (redis) {
        this.formDialog.formData = this.currentData
        dialogTitle = '编辑机器'
      } else {
        this.formDialog.formData = { port: 22 }
        dialogTitle = '添加机器'
      }

      this.formDialog.title = dialogTitle
      this.formDialog.visible = true
    },
    async deleteMachine(id) {
      await machineApi.del.request({ id })
      this.$message.success('操作成功')
      this.search()
    },
    fileManage(row) {
      this.dialog.machineId = row.id
      this.dialog.visible = true
      this.dialog.title = `${row.name} => ${row.ip}`
    },
    cancel() {
      this.dialog.visible = false
      this.dialog.machineId = null
    },
    closeDialog() {
      this.formDialog.visible = false
      this.formDialog.formData = null
    },
    submitSuccess() {
      this.currentId = null
      ;(this.currentData = null), this.search()
    },
    async search() {
      let res = await machineApi.list.request(this.params)
      this.table = res
    }
  },
  mounted() {
    this.search()
  },
  components: {
    ToolBar,
    FileManage,
    DynamicFormDialog
  }
}
</script>

<style>
</style>
