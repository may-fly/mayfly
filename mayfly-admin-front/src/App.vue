<template>
  <div id="app">
    <router-view name="blank"></router-view>
    <router-view></router-view>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator'
@Component({
  name: 'App'
})
export default class extends Vue {}
</script>

<style lang="less">
#app {
  background-color: #222d32;
}
</style>
