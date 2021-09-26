// import axios from 'axios'
import Vue from 'vue'
import Vuex from 'vuex'

// 새로고침해도 (뒤로가기등) store에 변화가 없이 저장되어 있도록 하는 vuex-persistedstate
import createPersistedState from "vuex-persistedstate";

import accounts from './modules/accounts'
import feed from './modules/feed'
import alarm from './modules/alarm.js'
import travel from './modules/travel'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
  },
  mutations: {
  },
  actions: {
  },
  modules: {
    accounts,
    feed,
    travel,
    alarm,
    
  },
  plugins: [createPersistedState()] // 미사용시 null값시 데이터 못 불러오는 문제
})
