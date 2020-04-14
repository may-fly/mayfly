import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    permissionMap: new Map()
  },
  getters: {
    permissionMap(state) {
      return state.permissionMap
    }
  },
  mutations: {
    setPermissions(state, permissions) {
      for (let p of permissions) {
        state.permissionMap.set(p.code, p.url)
      }
    }
  }
})


