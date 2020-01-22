import Vue from 'vue'
import Router from 'vue-router'
import Personal from './personal'

Vue.use(Router)


let RouteList = [{
    path: '/',
    component: resolve => require(['~/views/layout/app.vue'], resolve),
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
        component: resolve => require(['~/views/home'], resolve),
      },
      {
        path: '/role_manage',
        name: 'RoleManage',
        meta: {
          title: '角色列表',
          keepAlive: true
        },
        component: resolve => require(['~/views/sys-manage/role'], resolve),
      },
      {
        path: '/menu_manage',
        name: 'menu_manage',
        meta: {
          title: '菜单管理',
          keepAlive: true
        },
        component: resolve => require(['~/views/sys-manage/resource'], resolve),
      },
      {
        path: '/account_list',
        name: 'account_list',
        meta: {
          title: '账号管理',
          keepAlive: true
        },
        component: resolve => require(['~/views/sys-manage/account'], resolve),
      },
      {
        path: '/machines',
        name: 'machines',
        meta: {
          title: '机器管理',
          keepAlive: true
        },
        component: resolve => require(['~/views/devops/machine'], resolve),
      },
      {
        path: '/redis_manage',
        name: 'redis_manage',
        meta: {
          title: 'redis机器列表',
          keepAlive: true
        },
        component: resolve => require(['~/views/devops/redis'], resolve),
      },
      {
        path: '/redis_operation/:cluster/:id',
        name: 'redis_operation',
        meta: {
          title: 'redis管理',
          keepAlive: true
        },
        component: resolve => require(['~/views/devops/redis/redis-operation.vue'], resolve),
      },
      //个人中心，可能有修改密码，头像修改等路由
      Personal.index,
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
      blank: resolve => require(['~/views/login/login.vue'], resolve),
    }
  },

]

export default new Router({
  routes: RouteList
})
