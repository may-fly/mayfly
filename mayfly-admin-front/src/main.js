import Vue from 'vue'
import store from './store'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import './assets/css/font-awesome.min.css'
import './assets/css/style.css'
import router from './router'
import Config from './common/config'
import Permission from './common/Permission'
import Utils from './common/Utils'
import sockets from './common/sockets.js'

import App from './App.vue'

Vue.prototype.$Permission = Permission
Vue.prototype.$Config = Config
Vue.prototype.$Utils = Utils



Vue.use(ElementUI)

/**
 * 定义vue permission指令
 */
Vue.directive('permission', function (el, binding) {
  Permission.checkCodeAndSetDom(binding.value, el);
})

router.beforeEach((to, from, next) => {
  window.document.title = to.meta.title ? to.meta.title + '-' + Config.name.siteName : Config.name.siteName;
  
  if (to.path == '/login') {
    // 如果是退出登录，则有系统通知socket，需要关闭
    if (Vue.prototype.$SysMsgSocket) {
      Vue.prototype.$SysMsgSocket.close();
      Vue.prototype.$SysMsgSocket = null;
    }
  }
  if (!Permission.getToken() && to.path != '/login') {
    next({path: '/login'});
  } else {
    next();
    // 如果不存在系统通知socket，则连接
    if (!Vue.prototype.$SysMsgSocket) {
      Vue.prototype.$SysMsgSocket = sockets.sysMsgSocket()
    }
  }
});
router.afterEach(transition => {
  
});


new Vue({
  el: '#app',
  router,
  store,
  render: h => h(App)
})
