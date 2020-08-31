<template>
  <div>
    <div class="toolbar">
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
    </div>

    <el-table :data="table" stripe style="width: 100%" @current-change="choose">
      <el-table-column label="选择" width="55px">
        <template slot-scope="scope">
          <el-radio v-model="currentId" :label="scope.row.id">
            <i></i>
          </el-radio>
        </template>
      </el-table-column>
      <el-table-column prop="name" label="名称" width></el-table-column>
      <el-table-column prop="ip" label="IP" width>
        <template slot-scope="scope">
          <el-popover placement="bottom-start" width="250px" trigger="click">
            <div style="white-space: pre-line;">{{machineInfo}}</div>
            <el-link
              type="primary"
              @click="showInfo(scope.row.id)"
              slot="reference"
              :underline="false"
            >{{scope.row.ip}}</el-link>
          </el-popover>
        </template>
      </el-table-column>
      <el-table-column prop="port" label="端口" width></el-table-column>
      <el-table-column prop="username" label="用户名"></el-table-column>
      <el-table-column prop="createTime" label="创建时间"></el-table-column>
      <el-table-column prop="updateTime" label="更新时间"></el-table-column>
      <el-table-column label="操作" min-width="200px">
        <template slot-scope="scope">
          <el-button
            type="primary"
            @click="top(scope.row.id)"
            :ref="scope.row"
            icom="el-icon-tickets"
            size="mini"
            plain
          >top</el-button>
          <el-button
            type="primary"
            @click="monitor(scope.row.id)"
            :ref="scope.row"
            icom="el-icon-tickets"
            size="mini"
            plain
          >监控</el-button>
          <el-button
            type="success"
            @click="serviceManager(scope.row)"
            :ref="scope.row"
            size="mini"
            plain
          >服务管理</el-button>
        </template>
      </el-table-column>
    </el-table>

    <file-manage
      :title="dialog.title"
      :visible.sync="dialog.visible"
      :machineId.sync="dialog.machineId"
    />

    <el-dialog @close="closeTop" title="Top信息" :visible.sync="topDialog.visible" width="70%">
      <top-info ref="topDialog" :machineId="topDialog.machineId" />
    </el-dialog>

    <el-dialog title="监控信息" :visible.sync="monitorDialog.visible" width="70%">
      <monitor ref="monitorDialog" :machineId="monitorDialog.machineId" />
    </el-dialog>

    <dynamic-form-dialog
      :visible.sync="formDialog.visible"
      :title="formDialog.title"
      :formInfo="formDialog.formInfo"
      :formData.sync="formDialog.formData"
      @submitSuccess="submitSuccess"
    ></dynamic-form-dialog>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator'
import { DynamicFormDialog } from '@/components/dynamic-form'
import FileManage from './FileManage.vue'
import { machinePermission } from '../permissions'
import { machineApi } from '../api'
import Monitor from './Monitor.vue'
import TopInfo from './TopInfo.vue'

@Component({
  name: 'MachineList',
  components: {
    FileManage,
    DynamicFormDialog,
    Monitor,
    TopInfo,
  },
})
export default class MachineList extends Vue {
  table = []
  permission = machinePermission
  currentId = null
  currentData: any = null
  params = {
    host: null,
    clusterId: null,
  }
  machineInfo = {}
  monitorDialog = {
    visible: false,
    machineId: 0,
  }
  topDialog = {
    visible: false,
    machineId: 0,
  }
  dialog = {
    machineId: null,
    visible: false,
    title: '',
  }
  formDialog = {
    visible: false,
    title: '',
    formInfo: {
      createApi: machineApi.save,
      updateApi: machineApi.update,
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
                trigger: ['blur', 'change'],
              },
            ],
          },
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
                trigger: ['blur', 'change'],
              },
            ],
          },
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
                trigger: ['blur', 'change'],
              },
            ],
          },
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
                trigger: ['blur', 'change'],
              },
            ],
          },
        ],
        [
          {
            type: 'input',
            label: '密码：',
            name: 'password',
            placeholder: '请输入密码',
            inputType: 'password',
          },
        ],
      ],
    },
    formData: { port: 22 },
  }

  mounted() {
    this.search()
  }

  choose(item: any) {
    if (!item) {
      return
    }
    this.currentId = item.id
    this.currentData = item
  }

  async showInfo(id: number) {
    const res = await machineApi.info.request({ id })
    this.machineInfo = res
  }

  monitor(id: number) {
    this.monitorDialog.machineId = id
    this.monitorDialog.visible = true
    // // 如果重复打开同一个则开启定时任务
    // const md: any = this.$refs['monitorDialog']
    // if (md) {
    //   md.startInterval()
    // }
  }

  top(id: number) {
    this.topDialog.machineId = id
    this.topDialog.visible = true
    // 如果重复打开同一个则开启定时任务
    const md: any = this.$refs['topDialog']
    if (md) {
      md.startInterval()
    }
  }

  closeTop() {
    // 关闭窗口，取消定时任务
    const md: any = this.$refs['topDialog']
    md.cancelInterval()
  }

  openFormDialog(redis: any) {
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
  }

  async deleteMachine(id: number) {
    await machineApi.del.request({ id })
    this.$message.success('操作成功')
    this.search()
  }

  fileManage(row: any) {
    this.dialog.machineId = row.id
    this.dialog.visible = true
    this.dialog.title = `${row.name} => ${row.ip}`
  }

  submitSuccess() {
    this.currentId = null
    ;(this.currentData = null), this.search()
  }

  async search() {
    this.table = await machineApi.list.request(this.params)
  }
}
</script>

<style>
</style>
