<template>
  <div class="main">
    <!-- <el-container>
      <el-header>
        
      </el-header>
    </el-container> -->
    <div class="header">
      <div class="logo">
        <span class="big">{{ $Config.name.siteName }}</span>
        <!-- <img width="40" style="margin-top: 5px" src="../../assets/images/logo.svg" alt=""> -->
        <span class="min">
          <img width="40" style="margin-top: 5px" src="../../assets/images/logo.svg" alt="">
        </span>
      </div>
      <!-- <span class="header-btn" @click="hiddenSidebar">
        <i class="el-icon-menu"></i>
      </span> -->
      <div class="right">
        <span class="header-btn" @click="screenfullToggle">
          <i class="fa fa-arrows-alt"></i>
        </span>

      <!--  <el-dropdown>
          <span class="header-btn">
            <i class="el-icon-setting"></i>
          </span>
          <el-dropdown-menu slot="dropdown">
            <div style="padding: 10px;text-align: center;width: 420px">
              <div class="setting-category">
                <el-switch @change="saveSwitchTabBarVal" v-model="switchTabBar" active-text="开启TabBar" inactive-text="关闭TabBar">
                </el-switch>
                <el-switch @change="saveFixedTabBar" v-if="switchTabBar" v-model="fixedTabBar" style="margin-top: 10px"
                  active-text="固定在顶部" inactive-text="随页面滚动">
                </el-switch>
                <el-alert v-if="switchTabBar" style="margin-top: 10px" title="导航标签超过容器时,可在导航上滚动鼠标来移动标签" type="info"
                  show-icon>
                </el-alert>
              </div> -->
         <!--   </div>
          </el-dropdown-menu>
        </el-dropdown>
 -->
        <span class="header-btn">
          <el-badge :value="3" class="badge">
            <i class="el-icon-bell"></i>
          </el-badge>
        </span>
        <el-dropdown>
          <span class="header-btn">
            {{username}}<i class="el-icon-arrow-down el-icon--right"></i>
          </span>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item @click.native="$router.push('/personal')"><i style="padding-right: 8px" class="fa fa-cog"></i>个人中心</el-dropdown-item>
            <el-dropdown-item @click.native="logout"><i style="padding-right: 8px" class="fa fa-key"></i>退出系统</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </div>
    </div>


    <div class="app">
      <div class="aside">
        <div class="menu">
          <el-menu background-color="#222d32" text-color="#bbbbbb" active-text-color="#fff" :default-active="$route.path" class="menu" :collapse="isCollapse">
            <MenuTree @toPath="toPath" :menus="this.menus"></MenuTree>
          </el-menu>
        </div>
      </div>
      <div class="app-body">
        <NavBar id="nav-bar" v-if="switchTabBar" :style="fixedTabBar && switchTabBar ? 'position: fixed;top: 0;' : ''"></NavBar>
        <!-- <div v-else style="margin-top: 45px;"></div> -->
        <div id="mainContainer" :style="fixedTabBar && switchTabBar ? 'margin-top: 88px;':''" class="main-container">
          <router-view v-if="!iframe"></router-view>
          <iframe v-else :src="iframeSrc"></iframe>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import Screenfull from 'screenfull'
  import EuiFooter from '~/views/Layout/Footer.vue';
  import NavBar from './NavBar.vue'
  import MenuTree from './MenuTree.vue'

  export default {
    data() {
      return {
        iframe: false,
        iframeSrc: null,
        fixedTabBar: true,
        switchTabBar: true,
        isCollapse: false,
        username: null,
        menus: [{
          "id": 8,
          "pid": 0,
          "weight": 10,
          "name": "首页",
          "icon": "fa fa-tachometer",
          "path": "/",
          "code": "index",
          "status": null,
          "createTime": null,
          "updateTime": null,
          "children": null
        }, {
          "id": 1,
          "pid": 0,
          "weight": 3,
          "name": "权限管理",
          "icon": "fa fa-qrcode",
          "path": null,
          "code": "permission",
          "status": null,
          "createTime": null,
          "updateTime": null,
          "children": [{
            "id": 2,
            "pid": 1,
            "weight": 3,
            "name": "权限列表",
            "icon": null,
            "path": "/permission_list",
            "code": "permission:api",
            "status": null,
            "createTime": null,
            "updateTime": null,
            "children": null
          }, {
            "id": 9,
            "pid": 1,
            "weight": 3,
            "name": "菜单管理",
            "icon": null,
            "path": "/menu_manage",
            "code": "permission:menu:list",
            "status": null,
            "createTime": null,
            "updateTime": null,
            "children": null
          }, {
            "id": 4,
            "pid": 1,
            "weight": 1,
            "name": "权限组管理",
            "icon": null,
            "path": "/permission_group_list",
            "code": "permission:group",
            "status": null,
            "createTime": null,
            "updateTime": null,
            "children": null
          }]
        }, {
          "id": 26,
          "pid": 0,
          "weight": 2,
          "name": "账号管理",
          "icon": "el-icon-menu",
          "path": "/account_manage",
          "code": null,
          "status": null,
          "createTime": null,
          "updateTime": null,
          "children": [{
            "id": 27,
            "pid": 26,
            "weight": 2,
            "name": "角色管理",
            "icon": null,
            "path": "/role_manage",
            "code": null,
            "status": null,
            "createTime": null,
            "updateTime": null,
            "children": null
          }, {
            "id": 28,
            "pid": 26,
            "weight": 1,
            "name": "账号管理",
            "icon": null,
            "path": "http://www.baidu.com",
            "code": null,
            "status": null,
            "createTime": null,
            "updateTime": null,
            "children": null
          }]
        }, {
          "id": 16,
          "pid": 0,
          "weight": 1,
          "name": "Redis管理",
          "icon": "el-icon-menu",
          "path": null,
          "code": "sys",
          "status": null,
          "createTime": null,
          "updateTime": null,
          "children": [{
            "id": 21,
            "pid": 16,
            "weight": 10,
            "name": "redis集群管理",
            "icon": null,
            "path": "redis_clusters",
            "code": null,
            "status": null,
            "createTime": null,
            "updateTime": null,
            "children": null
          }, {
            "id": 19,
            "pid": 16,
            "weight": 1,
            "name": "redis机器管理",
            "icon": null,
            "path": "/redis_manage",
            "code": null,
            "status": null,
            "createTime": null,
            "updateTime": null,
            "children": null
          }]
        }],
      };
    },
    methods: {
      toPath(path) {
        // 如果是请求其他地址，则使用iframe展示
        if (path && (path.startsWith('http://') || path.startsWith('https://'))) {
          this.iframe = true;
          this.iframeSrc = path;
          return;
        }

        this.iframe = false;
        this.iframeSrc = null;
        this.$router.push({
          path
        });
      },
      NavBarWidth() {
        let navBar = document.getElementById('nav-bar');
        if (!navBar) return;
        if (!(this.fixedTabBar && this.switchTabBar)) {
          navBar.style.width = '100%';
          return;
        }
        let sidebarClose = document.body.classList.contains('sidebar-close')
        if (sidebarClose) {
          navBar.style.width = '100%';
          return;
        }
        if (this.isCollapse) navBar.style.width = 'calc(100% - 64px)';
        else navBar.style.width = 'calc(100% - 230px)';

      },
      screenfullToggle() {
        if (!Screenfull.enabled) {
          this.$message({
            message: '你的浏览器不支持全屏！',
            type: 'warning'
          })
          return false
        }
        Screenfull.toggle();
      },
      saveFixedTabBar(v) {
        v ? localStorage.setItem('fixedTabBar', v) : localStorage.removeItem('fixedTabBar');
        this.NavBarWidth();
      },
      saveSwitchTabBarVal(v) {
        let containerDom = document.getElementById('mainContainer');
        v ? containerDom.style.minHeight = 'calc(100vh - 139px)' : containerDom.style.minHeight = 'calc(100vh - 101px)';
        v ? localStorage.setItem('switchTabBar', v) : localStorage.removeItem('switchTabBar');
        this.NavBarWidth();
      },
      sidebarToggle(e) {
        e.preventDefault();
        if (this.isCollapse) {
          document.body.classList.remove('sidebar-hidden')
          this.siteName = this.$Config.siteName
          this.isCollapse = false;
        } else {
          document.body.classList.add('sidebar-hidden')
          this.isCollapse = true;
        }
        this.NavBarWidth();

      },
      hiddenSidebar(e) {
        e.preventDefault();
        document.body.classList.toggle('sidebar-close');
        this.NavBarWidth();
      },
      logout() {
        sessionStorage.clear();
        this.$router.push({
          path: '/login'
        });
      }
    },
    mounted: function() {
      this.menus = JSON.parse(sessionStorage.getItem(this.$Config.name.resourcesKey));
      this.username = JSON.parse(sessionStorage.getItem(this.$Config.name.adminKey)).username;

      // this.switchTabBar = localStorage.getItem('switchTabBar') ? true : false;
      // this.fixedTabBar = localStorage.getItem('fixedTabBar') ? true : false;
      if (this.switchTabBar) document.getElementById('mainContainer').style.minHeight = 'calc(100vh - 88px)';

      if (!this.isCollapse) {
        document.body.classList.remove('sidebar-hidden')
        this.siteName = this.$Config.siteName
      } else {
        document.body.classList.add('sidebar-hidden')
      }

      setTimeout(() => {
        this.NavBarWidth();
      }, 1000)
    },
    components: {
      EuiFooter,
      NavBar,
      MenuTree
    },
  }
</script>
<style lang="less">
  .sidebar-hidden {
    .header {
      .logo {
        background: #222d32;

        .big {
          display: none;
        }

        .min {
          display: block;
        }

        width: 64px;
      }
    }

    .aside {
      .sidebar-toggle {
        .icon-left {
          transform: rotate(180deg);
        }
      }
    }

    .main {
      .app-body {
        margin-left: 64px;
      }
    }
  }

  .sidebar-close {
    .header {
      .logo {
        width: 0;
        overflow: hidden;
      }
    }

    .aside {
      margin-left: -230px;
    }

    .main {
      .app-body {
        margin-left: 0;
      }
    }
  }

  .sidebar-hidden.sidebar-close {
    .aside {
      margin-left: -64px;
    }
  }


  .main {
    display: flex;

    .el-menu:not(.el-menu--collapse) {
      width: 230px;
    }

    .app {
      width: 100%;
      background-color: #ecf0f5;
    }

    .aside {
      position: fixed;
      margin-top: 50px;
      z-index: 10;
      background-color: #222d32;
      transition: all 0.3s ease-in-out;

      .menu {
        overflow-y: auto;
        height: calc(~'100vh');
      }
    }

    .app-body {
      margin-left: 230px;
      -webkit-transition: margin-left 0.3s ease-in-out;
      transition: margin-left 0.3s ease-in-out;
    }

    .main-container {
      margin-top: 50px;
      padding: 2px;
      min-height: calc(~'100vh - 50px');
    }
  }

  .header {
    width: 100%;
    position: fixed;
    display: flex;
    height: 50px;
    background-color: #303643;
    z-index: 10;

    .logo {
      .min {
        display: none;
      }

      width: 230px;
      height: 50px;
      text-align: center;
      line-height: 50px;
      color: #fff;
      background-color: #303643;
      -webkit-transition: width 0.35s;
      transition: all 0.3s ease-in-out;
    }

    .right {
      position: absolute;
      right: 0;
    }

    .header-btn {
      .el-badge__content {
        top: 14px;
        right: 7px;
        text-align: center;
        font-size: 9px;
        padding: 0 3px;
        background-color: #00a65a;
        color: #fff;
        border: none;
        white-space: nowrap;
        vertical-align: baseline;
        border-radius: .25em;
      }

      overflow: hidden;
      height: 50px;
      display: inline-block;
      text-align: center;
      line-height: 50px;
      cursor: pointer;
      padding: 0 14px;
      color: #fff;

      &:hover {
        background-color: #222d32
      }
    }

  }

  .menu {
    border-right: none;
  }

  .el-menu--vertical {
    min-width: 190px;
  }

  .setting-category {
    padding: 10px 0;
    border-bottom: 1px solid #eee;
  }

  #mainContainer iframe {
    border: none;
    outline: none;
    width: 100%;
    height: 100%;
    position: absolute;
    background-color: #ecf0f5;
  }
  
  .el-submenu__title {
    font-weight: 500;
  }
  .el-menu-item {
    font-weight: 500;
  }
</style>
