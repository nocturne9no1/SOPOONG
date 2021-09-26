<template>
  <div id="app">
    <Navbar/>
    <router-view/>
  </div>
</template>

<script>
import Navbar from './components/Navbar.vue'
import axios from 'axios'
import './components/css/style.scss'
import { mapGetters } from 'vuex'

export default ({
  name: 'App',
  components: {
    Navbar,
  },
  computed: {
    ...mapGetters(["isSignedIn"])
  },
  watch: {
    // 주소 변경시마다 알람 있는지 요청 보내서 확인
    '$route' () {
      console.log('주소 변경')
      // 로그인 했을 때만 불러옴
      if ( this.$store.getters['isSignedIn'] === true ) {
        console.log('들어옴 알람')
        axios({
          method: 'get',
          url: '/alarm/list',
          params: {
            page: 0,
            size: 100,
            userId: this.$store.getters.getUserProfile.userId,
          },
          headers: {
            'X-AUTH-TOKEN' : this.$store.state.accounts.authToken
          },
        })
          .then(res => {
            console.log(res)
            this.$store.dispatch('getAlarm', res.data.data.success)
            // this.$store.dispatch('getAlarm', temp)

          })
          .catch(err => console.log(err))
      }
    }
  }
})
</script>


<style lang="scss">
</style>