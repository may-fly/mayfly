<template>
  <div id="app">
    <router-view name="blank"></router-view>
    <router-view></router-view>
  </div>
</template>

<script>
  export default {
    name: 'app',
    data() {
      return {
        sysMsgSocket: null
      }
    },
    mounted() {
      // this.sysMsgSocket = this.$Utils.socketBuilder('ws://localhost:8080/mayfly/sysmsg/' + sessionStorage.getItem('token'))
      //   .message(event => {
      //     ElementUI.Notification.error({
      //       title: '系统消息',
      //       message: event.data
      //     });
      //   })
    },
  }
</script>

<style lang="less">
  #app {
    background-color: #222d32;
  }
</style>
