<template>
  <div>
    <ToolBar>
      <el-button
        type="primary"
        icon="el-icon-plus"
        size="mini"
        @click="openFormDialog(false)"
        plain
      >添加</el-button>
      <el-button
        type="primary"
        icon="el-icon-edit"
        :disabled="currentId == null"
        size="mini"
        @click="openFormDialog(currentData)"
        plain
      >编辑</el-button>
      <el-button
        type="danger"
        icon="el-icon-delete"
        :disabled="currentId == null"
        size="mini"
        @click="deleteNode"
        plain
      >删除</el-button>
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
        <el-select v-model="params.clusterId" size="mini" clearable placeholder="集群选择">
          <el-option v-for="item in clusters" :key="item.id" :value="item.id" :label="item.name"></el-option>
        </el-select>
        <el-button @click="search" type="success" icon="el-icon-search" size="mini"></el-button>
      </div>
    </ToolBar>
    <el-table :data="redisTable" stripe style="width: 100%" @current-change="choose">
      <el-table-column label="选择" width="50px">
        <template slot-scope="scope">
          <el-radio v-model="currentId" :label="scope.row.id">
            <i></i>
          </el-radio>
        </template>
      </el-table-column>
      <el-table-column prop="host" label="IP" width></el-table-column>
      <el-table-column prop="port" label="端口" width></el-table-column>
      <el-table-column prop="clusterId" label="集群id"></el-table-column>
      <el-table-column prop="description" label="描述"></el-table-column>
      <el-table-column label="操作" width>
        <template slot-scope="scope">
          <el-button
            v-permission="permission.redis.code"
            type="primary"
            @click="info(scope.row.id)"
            :ref="scope.row"
            icom="el-icon-tickets"
            size="mini"
            plain
          >info</el-button>
          <el-button
            v-permission="keyPermission.redisKey.code"
            type="success"
            @click="manage(scope.row)"
            :ref="scope.row"
            size="mini"
            plain
          >数据管理</el-button>
        </template>
      </el-table-column>
    </el-table>

    <Info :visible="infoDialog.visible" :info="infoDialog.info" @close="infoDialog.visible = false"></Info>

    <dynamic-form-dialog
      :dialog-width="formDialog.dialogWidth"
      :visible="formDialog.visible"
      :title="formDialog.title"
      :form-info="formDialog.formInfo"
      :form-data="formDialog.formData"
      @cancel="closeDialog"
      @submitSuccess="submitSuccess"
    ></dynamic-form-dialog>
  </div>
</template>

<script>
import ToolBar from '@/components/tool-bar/tool-bar.vue'
import Info from './info.vue'
import { redisPermission, redisKeyPermission } from '../permissions'
import { redisApi, redisKeyApi } from '../api'
import { DynamicFormDialog } from '@/components/dynamic-form'

//
export default {
  data() {
    var validatePort = (rule, value, callback) => {
      if (value > 65535 || value < 1) {
        callback(new Error('端口号错误'))
      }
      callback()
    }

    return {
      redisTable: [],
      permission: redisPermission,
      keyPermission: redisKeyPermission,
      currentId: null,
      currentData: null,
      params: {
        host: null,
        clusterId: null
      },
      redisInfo: {
        url: ''
      },
      clusters: [
        {
          id: 0,
          name: '单机'
        }
      ],
      infoDialog: {
        visible: false,
        info: {
          Server: {},
          Keyspace: {},
          Clients: {},
          CPU: {},
          Memory: {}
        }
      },
      formDialog: {
        visible: false,
        title: null,
        formInfo: {
          createApi: redisApi.save,
          updateApi: redisApi.update,
          formRows: [
            [
              {
                type: 'input',
                label: '主机：',
                name: 'host',
                placeholder: '请输入节点ip',
                rules: [
                  {
                    required: true,
                    message: '请输入节点ip',
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
                placeholder: '请输入节点端口号',
                inputType: 'number',
                rules: [
                  {
                    required: true,
                    message: '请输入节点端口号',
                    trigger: ['blur', 'change']
                  }
                ]
              }
            ],
            [
              {
                type: 'input',
                label: '密码：',
                name: 'pwd',
                placeholder: '请输入节点密码',
                inputType: 'password'
              }
            ],
            [
              {
                type: 'input',
                label: '描述：',
                name: 'description',
                placeholder: '请输入节点描述',
                inputType: 'textarea'
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
    connect() {
      Req.post('/open/redis/connect', this.form, res => {
        this.redisInfo = res
      })
    },
    async deleteNode() {
      await redisApi.del.request({ id: this.currentId })
      this.$message.success('删除成功')
      this.search()
    },
    manage(row) {
      this.$router.push(`/redis_operation/${row.clusterId}/${row.id}`)
    },
    info(id) {
      redisApi.info.request({ id }).then(res => {
        this.infoDialog.info = res
        this.infoDialog.id = id
        this.infoDialog.visible = true
      })
    },
    search() {
      redisApi.list.request(this.params).then(res => {
        this.redisTable = res
      })
    },
    openFormDialog(redis) {
      let dialogTitle
      if (redis) {
        this.formDialog.formData = this.currentData
        dialogTitle = '编辑redis节点'
      } else {
        this.formDialog.formData = { port: 6379 }
        dialogTitle = '添加redis节点'
      }

      this.formDialog.title = dialogTitle
      this.formDialog.visible = true
    },
    closeDialog() {
      this.formDialog.visible = false
      this.formDialog.formData = null
    },
    submitSuccess() {
      this.currentId = null
      this.currentData = null
      this.search()
    }
  },
  mounted() {
    this.search()
  },
  components: {
    ToolBar,
    Info,
    DynamicFormDialog
  }
}
</script>

<style>
</style>
