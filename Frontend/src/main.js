import Vue from 'vue'
import App from './App.vue'
import axios from 'axios'

import store from './store'
import router from './router'

import VueCookies from 'vue-cookies';

Vue.config.productionTip = false
// 쿠키 사용
Vue.use(VueCookies)

axios.defaults.baseURL = 'https://i5a404.p.ssafy.io/api/'
// 모든 요청에 추가할 헤더 설정
// token 보내면서 로그인하면 로그인 불가 
// if ( store.state.accounts.authToken ) {
//   axios.defaults.headers.common['X-HEADER-TOKEN'] = store.state.accounts.authToken
// }
// GET 요청에 추가할 헤더 설정
// axios.defaults.headers.get['Accepts'] = 'application/json'

new Vue({
  store,
  router,
  render: h => h(App)
}).$mount('#app')
