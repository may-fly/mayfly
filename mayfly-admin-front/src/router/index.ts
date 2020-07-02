import Vue from 'vue'
import Router, { RouteConfig } from 'vue-router'

Vue.use(Router)

let constantRoutes: RouteConfig[] = [{
  path: '/',
  component: () => import("@/views/layout/App.vue"),
  meta: {
    title: '首页',
    keepAlive: false,
  },
  children: [{
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
    path: '/machines',
    name: 'machines',
    meta: {
      title: '机器管理',
      keepAlive: true
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
  },
    //个人中心，可能有修改密码，头像修改等路由
    // Personal.index,
  ]
},
{
  path: '/login',
  name: 'Login',
  meta: {
    title: '后台登录',
    keepAlive: false
  },
  components: {
    blank: () => import('@/views/login/Login.vue')
  }
},

]


const createRouter = () => new Router({
  // mode: 'history',  // Disabled due to Github Pages doesn't support this, enable this if you need.
  scrollBehavior: (to, from, savedPosition) => {
    if (savedPosition) {
      return savedPosition
    } else {
      return { x: 0, y: 0 }
    }
  },
  routes: constantRoutes
})

const router = createRouter()

// Detail see: https://github.com/vuejs/vue-router/issues/1234#issuecomment-357941465
export function resetRouter() {
  const newRouter = createRouter();
  (router as any).matcher = (newRouter as any).matcher // reset router
}

export default router