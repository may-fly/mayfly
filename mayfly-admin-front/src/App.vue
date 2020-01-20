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
    },
  }
</script>

<style lang="less">
  #app {
    background-color: #222d32;
  }
</style>
