import Vue from 'vue'
import store from './store'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import './assets/css/font-awesome.min.css'
import './assets/css/style.css'
import router from './router'
import Config from './common/config'
import Permission from './common/Permission'


import App from './App.vue'
Vue.prototype.$Permission = Permission
Vue.prototype.$Config = Config


Vue.use(ElementUI)

/**
 * 增加vue permission指令
 */
Vue.directive('permission', function (el, binding) {
  Permission.checkCodeAndSetDom(binding.value, el);
})

router.beforeEach((to, from, next) => {
  window.document.title = to.meta.title ? to.meta.title + '-' + Config.name.siteName : Config.name.siteName;

  if (!sessionStorage.getItem(Config.name.tokenKey) && to.path != '/login') {
    next({path: '/login'});
  } else {
    next();
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
