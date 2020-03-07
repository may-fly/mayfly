<template>
  <div>
    <el-dialog title="info" :visible="visible" :show-close="true" width="35%" @close="close()">
      <el-collapse v-model="name">
        <el-collapse-item title="Server(Redis服务器的一般信息)" name="server">
          <div class="row">
            <span class="title">redis_version(版本):</span>
            <span class="value">{{ info.Server.redis_version }}</span>
          </div>
          <div class="row">
            <span class="title">tcp_port(端口):</span>
            <span class="value">{{ info.Server.tcp_port }}</span>
          </div>
          <div class="row">
            <span class="title">redis_mode(模式):</span>
            <span class="value">{{ info.Server.redis_mode }}</span>
          </div>
          <div class="row">
            <span class="title">os(宿主操作系统):</span>
            <span class="value">{{ info.Server.os }}</span>
          </div>
          <div class="row">
            <span class="title">uptime_in_days(运行天数):</span>
            <span class="value">{{ info.Server.uptime_in_days }}</span>
          </div>
          <div class="row">
            <span class="title">executable(可执行文件路径):</span>
            <span class="value">{{ info.Server.executable }}</span>
          </div>
          <div class="row">
            <span class="title">config_file(配置文件路径):</span>
            <span class="value">{{ info.Server.config_file }}</span>
          </div>
        </el-collapse-item>
        <el-collapse-item title="Clients(客户端连接部分)" name="client">
          <div class="row">
            <span class="title">connected_clients(已连接客户端数):</span>
            <span class="value">{{ info.Clients.connected_clients }}</span>
          </div>
        </el-collapse-item>
        <el-collapse-item title="Memory(内存消耗相关信息)" name="memory">
          <div class="row">
            <span class="title">used_memory(分配内存总量):</span>
            <span class="value">{{ info.Memory.used_memory_human }}</span>
          </div>
          <div class="row">
            <span class="title">used_memory_rss(已分配的内存总量，操作系统角度):</span>
            <span class="value">{{ info.Memory.used_memory_rss_human }}</span>
          </div>
          <div class="row">
            <span class="title">mem_fragmentation_ratio(used_memory_rss和used_memory 之间的比率):</span>
            <span class="value">{{ info.Memory.mem_fragmentation_ratio }}</span>
          </div>
          <div class="row">
            <span class="title">used_memory_peak(内存消耗峰值):</span>
            <span class="value">{{ info.Memory.used_memory_peak_human }}</span>
          </div>
          <div class="row">
            <span class="title">total_system_memory(主机总内存):</span>
            <span class="value">{{ info.Memory.total_system_memory_human }}</span>
          </div>
        </el-collapse-item>
        <el-collapse-item title="CPU" name="cpu">
          <div class="row">
            <span class="title">used_cpu_sys(由Redis服务器消耗的系统CPU):</span>
            <span class="value">{{ info.CPU.used_cpu_sys }}</span>
          </div>
          <div class="row">
            <span class="title">used_cpu_user(由Redis服务器消耗的用户CPU):</span>
            <span class="value">{{ info.CPU.used_cpu_user }}</span>
          </div>
          <div class="row">
            <span class="title">used_cpu_sys_children(由后台进程消耗的系统CPU):</span>
            <span class="value">{{ info.CPU.used_cpu_sys_children }}</span>
          </div>
          <div class="row">
            <span class="title">used_cpu_user_children(由后台进程消耗的用户CPU):</span>
            <span class="value">{{ info.CPU.used_cpu_user_children }}</span>
          </div>
        </el-collapse-item>
      </el-collapse>
    </el-dialog>
  </div>
</template>

<script>
  import Req from "~/common/request"
  export default {
    props: {
      visible: Boolean,
      info: Object
    },
    data() {
      return {
        name: ""
      }
    },
    methods: {
      close() {
        this.name = "";
        this.$emit('close');
      }
    }
  }
</script>

<style>
  .row .title {
    font-size: 12px;
  }

  .row .value {
    font-size: 12px;
    color: black;
  }
</style>
