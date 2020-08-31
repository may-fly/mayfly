<template>
  <div>
    <el-row :gutter="20">
      监控周期：
      <el-select @change="changeMonitorTime" size="small" v-model="timeOptionValue" placeholder="请选择">
        <el-option
          v-for="item in timeOptions"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        ></el-option>
      </el-select>
    </el-row>

    <el-row :gutter="20">
      <el-col :lg="12" :md="24">
        <ChartContinuou :value="this.memData" title="内存" />
      </el-col>
      <el-col :lg="12" :md="24">
        <ChartContinuou :value="this.cpuData" title="CPU" />
      </el-col>
    </el-row>

    <el-row :gutter="20">
      <el-col :lg="12" :md="24">
        <!-- <HomeCard desc="load info" title="负载情况">
          <BaseChart :option="this.loadChartOption" title="负载情况" />
        </HomeCard>-->
        <BaseChart ref="loadChart" :option="this.loadChartOption" title="负载情况" />
      </el-col>
      <el-col :lg="12" :md="24">
        <ChartContinuou :value="this.data" title="磁盘IO" />
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
  name: 'Monitor',
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

  timeOptions = [
    {
      value: 0,
      label: '今天',
    },
    {
      value: 7,
      label: '最近7天',
    },
    {
      value: 15,
      label: '最近15天',
    },
  ]

  timeOptionValue = 0

  cpuData = new Array<any>()

  memData = new Array<any>()

  oneMinLoadavg = new Array<any>()

  fiveMinLoadavg = new Array<any>()

  fifMinLoadavg = new Array<any>()

  times = new Array<string>()

  data = [
    ['06/05 15:01', 116.12],
    ['06/05 15:06', 129.21],
    ['06/05 15:11', 135.43],
    ['2000-06-08', 86.33],
    ['2000-06-09', 73.98],
    ['2000-06-10', 85],
    ['2000-06-11', 73],
    ['2000-06-12', 68],
    ['2000-06-13', 92],
    ['2000-06-14', 130],
    ['2000-06-15', 245],
    ['2000-06-16', 139],
    ['2000-06-17', 115],
    ['2000-06-18', 111],
    ['2000-06-19', 309],
    ['2000-06-20', 206],
    ['2000-06-21', 137],
    ['2000-06-22', 128],
    ['2000-06-23', 85],
    ['2000-06-24', 94],
    ['2000-06-25', 71],
    ['2000-06-26', 106],
    ['2000-06-27', 84],
    ['2000-06-28', 93],
    ['2000-06-29', 85],
    ['2000-06-30', 73],
    ['2000-07-01', 83],
    ['2000-07-02', 125],
    ['2000-07-03', 107],
    ['2000-07-04', 82],
    ['2000-07-05', 44],
    ['2000-07-06', 72],
    ['2000-07-07', 106],
    ['2000-07-08', 107],
    ['2000-07-09', 66],
    ['2000-07-10', 91],
    ['2000-07-11', 92],
    ['2000-07-12', 113],
    ['2000-07-13', 107],
    ['2000-07-14', 131],
    ['2000-07-15', 111],
    ['2000-07-16', 64],
    ['2000-07-17', 69],
    ['2000-07-18', 88],
    ['2000-07-19', 77],
    ['2000-07-20', 83],
    ['2000-07-21', 111],
    ['2000-07-22', 57],
    ['2000-07-23', 55],
    ['2000-07-24', 60],
  ]

  loadChartOption = {
    // Make gradient line here
    visualMap: [
      {
        show: false,
        type: 'continuous',
        seriesIndex: 0,
        min: 0,
        max: 400,
      },
    ],
    title: [
      {
        left: 'left',
        text: '负载情况',
      },
    ],
    legend: {
      data: ['1分钟', '5分钟', '15分钟'],
    },
    tooltip: {
      trigger: 'axis',
    },
    xAxis: [
      {
        data: this.times,
      },
    ],
    yAxis: [
      {
        splitLine: { show: false },
      },
    ],
    grid: [{}],
    series: [
      {
        name: '1分钟',
        type: 'line',
        showSymbol: false,
        data: this.oneMinLoadavg,
      },
      {
        name: '5分钟',
        type: 'line',
        showSymbol: false,
        data: this.fiveMinLoadavg,
      },
      {
        name: '15分钟',
        type: 'line',
        showSymbol: true,
        data: this.fifMinLoadavg,
      },
    ],
  }

  @Watch('machineId', { deep: true })
  onDataChange() {
    if (this.machineId) {
      this.clearData()
      this.getMonitors()
    }
  }

  clearData() {
    this.cpuData = []
    this.memData = []
    this.times = []
    this.oneMinLoadavg = []
    this.fiveMinLoadavg = []
    this.fifMinLoadavg = []
  }

  changeMonitorTime() {
    this.clearData()
    this.getMonitors()
  }

  async getMonitors() {
    const monitors = await machineApi.monitors.request({
      id: this.machineId,
      type: this.timeOptionValue,
    })
    for (const m of monitors) {
      const time: string = m.createTime
      this.times.push(time)
      this.cpuData.push([time, m.cpuRateAvg])
      this.memData.push([time, m.memRateAvg])
      this.oneMinLoadavg.push(m.oneMinLoadavg)
      this.fiveMinLoadavg.push(m.fiveMinLoadavg)
      this.fifMinLoadavg.push(m.fifMinLoadavg)
    }
    const loadChartRef: any = this.$refs['loadChart']
    loadChartRef.initChart()
  }

  mounted() {
    this.getMonitors()
  }
}
</script>

<style lang="less">
.count-style {
  font-size: 50px;
}
</style>