<template>
  <div class="role-list">
    <ToolBar>
      <el-button
        :disabled="currentId == null"
        @click="deleteAccount()"
        type="danger"
        icon="el-icon-delete"
        size="mini"
      >删除</el-button>
      <div style="float: right">
        <el-select size="small" style=" width:120px" v-model="query.type" placeholder="状态">
          <el-option
            label="全部"
            :value="null"
          ></el-option>
          <el-option
            v-for="item in enums.logType"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          ></el-option>
        </el-select>
        <el-input
          placeholder="请输入账号名"
          size="small"
          style="width: 140px"
          v-model="query.createAccount"
          @clear="query.createAccount = null"
          clearable
        ></el-input>
        <el-button @click="search(true)" type="success" icon="el-icon-search" size="mini"></el-button>
      </div>
    </ToolBar>
    <el-table :data="datas" border ref="table" show-overflow-tooltip>
      <el-table-column label="序号" type="index"></el-table-column>
      <el-table-column min-width="600" prop="operation" label="操作记录" show-overflow-tooltip></el-table-column>
      <el-table-column min-width="80" prop="type" label="操作类型" align="center">
        <template slot-scope="scope">
          <el-tag type="info" effect="plain">{{ enums.logType.getLabelByValue(scope.row.type) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column min-width="115" prop="createAccount" label="操作账号"></el-table-column>
      <el-table-column min-width="160" prop="createTime" label="操作时间"></el-table-column>
    </el-table>
    <el-pagination
      @current-change="handlePageChange"
      style="text-align: center;margin-top: 20px;"
      background
      layout="prev, pager, next, total, jumper"
      :total="total"
      :current-page.sync="query.pageNum"
      :page-size="query.pageSize"
    />
  </div>
</template>

<script>
import ToolBar from '~/components/tool-bar/tool-bar.vue'
import HelpHint from '~/components/help-hint/help-hint.vue'
import { logApi } from '../api'
import enums from '../enums'

export default {
  data() {
    return {
      enums: enums,
      currentId: null,
      currentData: null,
      query: {
        pageNum: 1,
        pageSize: 10,
        createAccount: null,
        type: null
      },
      datas: [],
      total: null
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
    async search(resetPageNum) {
      if (resetPageNum) {
        this.query.pageNum = 1
      }
      let res = await logApi.list.request(this.query)
      this.datas = res.list
      this.total = res.total
    },
    handlePageChange(curPage) {
      this.query.pageNum = curPage
      this.search()
    }
  },
  mounted() {
    this.search()
  },
  components: {
    ToolBar,
    HelpHint
  }
}
</script>
<style lang="less">
.el-tooltip__popper {
  font-size: 14px;
  max-width: 50%;
} /*设置显示隐藏部分内容，按50%显示*/
</style>
