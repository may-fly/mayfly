import Vue from 'vue'
import Router from 'vue-router'
import Personal from './personal'

Vue.use(Router)



let RouteList = [{
    path: '/',
    component: resolve => require(['~/views/Layout/App.vue'], resolve),
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
        component: resolve => require(['~/views/Home/Index.vue'], resolve),
      },
      {
        path: '/role_manage',
        name: 'RoleManage',
        meta: {
          title: '角色列表',
          keepAlive: true
        },
        component: resolve => require(['~/views/PermissionManage/role/role_list.vue'], resolve),
      },
      {
        path: '/permission_list',
        name: 'permission_list',
        meta: {
          title: '权限列表',
          keepAlive: true
        },
        component: resolve => require(['~/views/PermissionManage/permission/permission_list.vue'], resolve),
      },
      {
        path: '/permission_group_list',
        name: 'permission_group_list',
        meta: {
          title: '权限组管理',
          keepAlive: true
        },
        component: resolve => require(['~/views/PermissionManage/permission/permission_group_list.vue'], resolve),
      },
      {
        path: '/menu_manage',
        name: 'menu_manage',
        meta: {
          title: '菜单管理',
          keepAlive: true
        },
        component: resolve => require(['~/views/PermissionManage/menu/menu_list.vue'], resolve),
      },
      {
        path: '/account_list',
        name: 'account_list',
        meta: {
          title: '菜单管理',
          keepAlive: true
        },
        component: resolve => require(['~/views/PermissionManage/account/account_list.vue'], resolve),
      },
      {
        path: '/redis_manage',
        name: 'redis_manage',
        meta: {
          title: 'redis机器列表',
          keepAlive: true
        },
        component: resolve => require(['~/views/Sys/redis/index.vue'], resolve),
      },
      {
        path: '/redis_operation/:cluster/:id',
        name: 'redis_operation',
        meta: {
          title: 'redis管理',
          keepAlive: true
        },
        component: resolve => require(['~/views/Sys/redis/redis_operation.vue'], resolve),
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
      blank: resolve => require(['~/views/Login/Login.vue'], resolve),
    }
  },

]

// 
// RouteList[0].children.push({
// 	path: '/build_code',
// 	name: 'BuildCode',
// 	meta: {
// 		title: '构建代码',
// 		keepAlive: true
// 	},
// 	component: resolve => require(['~/views/DevelopmentTool/Build.vue'], resolve),
// });

export default new Router({
  routes: RouteList
})
