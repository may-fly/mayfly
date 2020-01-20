<template>
  <div>
    <template v-for="menu in this.menus">
                                                                                                    <!-- 只有菜单的子节点为菜单类型才继续展开 -->
      <el-submenu :key="menu.id" :index="!menu.code ? menu.id + '' : menu.code" v-if="menu.children && menu.children[0].type === 1">
        <template slot="title">
          <i :class="menu.icon"></i>
          <span slot="title">{{menu.name}}</span>
        </template>
        <MenuTree @toPath="toPath" :menus="menu.children"></MenuTree>
      </el-submenu>
      <el-menu-item @click="toPath(menu.code)" :key="menu.id" :index="!menu.code ? menu.id + '' : menu.code" v-else>
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
