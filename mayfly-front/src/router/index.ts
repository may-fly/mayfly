import Vue from 'vue'
import VueRouter, { RouteConfig } from 'vue-router'
import Layout from "@/layout/Layout.vue"
import Permission from '../common/Permission';
import sockets from '@/common/sockets.ts'

Vue.use(VueRouter)

const routes: Array<RouteConfig> = [
  {
    path: '/login',
    name: 'Login',
    meta: {
      title: '登录',
      keepAlive: false
    },
    component: () => import('@/views/login/Login.vue')
  },
  {
    path: '/',
    component: Layout,
    meta: {
      title: '首页',
      keepAlive: false,
    },
    children: [
      {
        path: '/',
        name: 'Dashboard',
        meta: {
          title: '首页',
          keepAlive: false
        },
        component: () => import('@/views/home')
      },
      {
        path: '/roles',
        name: 'roles',
        meta: {
          title: '角色列表',
          keepAlive: true
        },
        component: () => import('@/views/sys-manage/role')
      },
      {
        path: '/resources',
        name: 'resources',
        meta: {
          title: '菜单管理',
          keepAlive: true
        },
        component: () => import('@/views/sys-manage/resource')
      },
      {
        path: '/accounts',
        name: 'accounts',
        meta: {
          title: '账号管理',
          keepAlive: true
        },
        component: () => import('@/views/sys-manage/account')
      },
      {
        path: '/logs',
        name: 'logs',
        meta: {
          title: '操作日志',
          keepAlive: true
        },
        component: () => import('@/views/sys-manage/log')
      },
      {
        path: 'machines',
        name: 'machines',
        meta: {
          title: '机器列表',
          keepAlive: false
        },
        component: () => import('@/views/devops/machine')
      },
      {
        path: '/redis',
        name: 'redis',
        meta: {
          title: 'redis机器列表',
          keepAlive: true
        },
        component: () => import('@/views/devops/redis')
      },
      {
        path: '/redis_operation/:cluster/:id',
        name: 'redis_operation',
        meta: {
          title: 'redis管理',
          keepAlive: true
        },
        component: () => import('@/views/devops/redis/RedisOperation.vue')
      },]
  },
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

router.beforeEach((to: any, from: any, next: any) => {
  window.document.title = to.meta.title
  if (to.path == '/login') {
    // 如果是退出登录，则有系统通知socket，需要关闭
    if (Vue.prototype.$SysMsgSocket) {
      Vue.prototype.$SysMsgSocket.close();
      Vue.prototype.$SysMsgSocket = null;
    }
  }
  if (!Permission.getToken() && to.path != '/login') {
    next({ path: '/login' });
  } else {
    next();
    // 如果不存在系统通知socket，则连接
    if (!Vue.prototype.$SysMsgSocket) {
      Vue.prototype.$SysMsgSocket = sockets.sysMsgSocket()
    }
  }
});

export default router
