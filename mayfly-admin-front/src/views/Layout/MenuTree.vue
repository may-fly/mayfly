<template>
  <div>
    <template v-for="menu in this.menus">
      <el-submenu :key="menu.id" :index="menu.path === null ? menu.id + '' : menu.path" v-if="menu.children">
        <template slot="title">
          <i :class="menu.icon"></i>
          <span slot="title">{{menu.name}}</span>
        </template>
        <MenuTree @toPath="toPath" :menus="menu.children"></MenuTree>
      </el-submenu>
      <el-menu-item @click="toPath(menu.path)" :key="menu.id" :index="menu.path === null ? menu.id + '' : menu.path"
        v-else>
        <i class="iconfont" :class="menu.icon"></i>
        <span slot="title">{{menu.name}}</span>
      </el-menu-item>
    </template>
  </div>
</template>

<script>
  export default {
    props: ['menus'],
    name: 'MenuTree',
    methods: {
      toPath(path) {
        // if (path.startsWith('/')) {
        //   this.$router.push({
        //     path
        //   });
        // } else {
        //   location.href = path
        // }
        this.$emit('toPath', path);
      }
    }
  }
</script>
