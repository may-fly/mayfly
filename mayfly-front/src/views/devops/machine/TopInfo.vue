<template>
  <div>
    <el-row>
      <el-col>
        <HomeCard desc="Base info" title="基础信息">
          <ActivePlate :infoList="infoCardData" />
        </HomeCard>
      </el-col>
    </el-row>
    <el-row :gutter="20">
      <el-col :lg="6" :md="24">
        <HomeCard desc="Task info" title="任务">
          <ChartPie :value.sync="taskData" />
        </HomeCard>
      </el-col>
      <el-col :lg="6" :md="24">
        <HomeCard desc="Mem info" title="内存">
          <ChartPie :value.sync="memData" />
        </HomeCard>
      </el-col>
      <el-col :lg="6" :md="24">
        <HomeCard desc="Cpu info" title="CPU">
          <ChartPie :value.sync="cpuData" />
        </HomeCard>
      </el-col>
    </el-row>
  </div>
</template>

<script lang="ts">
import { Component, Vue, Prop, Watch } from 'vue-property-decorator'
import ActivePlate from '@/components/chart/ActivePlate.vue'
import HomeCard from '@/components/chart/Card.vue'
import ChartPie from '@/components/chart/ChartPie.vue'
import ChartLine from '@/components/chart/ChartLine.vue'
import ChartGauge from '@/components/chart/ChartGauge.vue'
import ChartBar from '@/components/chart/ChartBar.vue'
import ChartFunnel from '@/components/chart/ChartFunnel.vue'
import ChartContinuou from '@/components/chart/ChartContinuou.vue'
import BaseChart from '@/components/chart/BaseChart.vue'
import { machineApi } from '../api'

@Component({
  name: 'TopInfo',
  components: {
    HomeCard,
    ActivePlate,
    ChartPie,
    ChartFunnel,
    ChartLine,
    ChartGauge,
    ChartBar,
    ChartContinuou,
    BaseChart,
  },
})
export default class Monitor extends Vue {
  @Prop()
  machineId: number

  timer: number

  infoCardData = [
    {
      title: 'total task',
      icon: 'md-person-add',
      count: 0,
      color: '#11A0F8',
    },
    { title: '总内存', icon: 'md-locate', count: '', color: '#FFBB44 ' },
    {
      title: '可用内存',
      icon: 'md-help-circle',
      count: '',
      color: '#FFBB44',
    },
    { title: '空闲交换空间', icon: 'md-share', count: 657, color: '#91AFC8' },
    {
      title: '使用中交换空间',
      icon: 'md-chatbubbles',
      count: 12,
      color: '#91AFC8',
    },
    { title: '1分钟负载', icon: 'md-map', count: 14, color: '#91A118' },
    { title: '5分钟负载', icon: 'md-map', count: 14, color: '#91A118' },
    { title: '15分钟负载', icon: 'md-map', count: 14, color: '#91A118' },
  ]
  taskData = [
    { value: 0, name: '运行中', color: '#3AA1FFB' },
    { value: 0, name: '睡眠中', color: '#36CBCB' },
    { value: 0, name: '结束', color: '#4ECB73' },
    { value: 0, name: '僵尸', color: '#F47F92' },
  ]

  memData = [
    { value: 0, name: '空闲', color: '#3AA1FFB' },
    { value: 0, name: '使用中', color: '#36CBCB' },
    { value: 0, name: '缓存', color: '#4ECB73' },
  ]

  swapData = [
    { value: 0, name: '空闲', color: '#3AA1FFB' },
    { value: 0, name: '使用中', color: '#36CBCB' },
  ]

  cpuData = [
    { value: 0, name: '用户空间', color: '#3AA1FFB' },
    { value: 0, name: '内核空间', color: '#36CBCB' },
    { value: 0, name: '改变优先级', color: '#4ECB73' },
    { value: 0, name: '空闲率', color: '#4ECB73' },
    { value: 0, name: '等待IO', color: '#4ECB73' },
    { value: 0, name: '硬中断', color: '#4ECB73' },
    { value: 0, name: '软中断', color: '#4ECB73' },
    { value: 0, name: '虚拟机', color: '#4ECB73' },
  ]

  @Watch('machineId', { deep: true })
  onDataChange() {
    if (this.machineId) {
      this.intervalGetTop()
    }
  }

  mounted() {
    this.intervalGetTop()
  }

  beforeDestroy() {
    this.cancelInterval()
  }

  cancelInterval() {
    clearInterval(this.timer)
    this.timer = 0
  }

  startInterval() {
    if (!this.timer) {
      this.timer = setInterval(this.getTop, 3000)
    }
  }

  intervalGetTop() {
    this.getTop()
    this.startInterval()
  }

  async getTop() {
    const topInfo = await machineApi.top.request({ id: this.machineId })
    this.infoCardData[0].count = topInfo.totalTask
    this.infoCardData[1].count = Math.round(topInfo.totalMem / 1024) + 'M'
    this.infoCardData[2].count = Math.round(topInfo.availMem / 1024) + 'M'
    this.infoCardData[3].count = Math.round(topInfo.freeSwap / 1024) + 'M'
    this.infoCardData[4].count = Math.round(topInfo.usedSwap / 1024) + 'M'
    this.infoCardData[5].count = topInfo.oneMinLoadavg
    this.infoCardData[6].count = topInfo.fiveMinLoadavg
    this.infoCardData[7].count = topInfo.fifteenMinLoadavg

    this.taskData[0].value = topInfo.runningTask
    this.taskData[1].value = topInfo.sleepingTask
    this.taskData[2].value = topInfo.stoppedTask
    this.taskData[3].value = topInfo.zombieTask

    this.memData[0].value = Math.round(topInfo.freeMem / 1024)
    this.memData[1].value = Math.round(topInfo.usedMem / 1024)
    this.memData[2].value = Math.round(topInfo.cacheMem / 1024)

    this.cpuData[0].value = topInfo.cpuUs
    this.cpuData[1].value = topInfo.cpuSy
    this.cpuData[2].value = topInfo.cpuNi
    this.cpuData[3].value = topInfo.cpuId
    this.cpuData[4].value = topInfo.cpuWa
    this.cpuData[5].value = topInfo.cpuHi
    this.cpuData[6].value = topInfo.cpuSi
    this.cpuData[7].value = topInfo.cpuSt
  }
}
</script>

<style lang="less">
.count-style {
  font-size: 50px;
}
</style>