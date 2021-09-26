<template>
  <div id="app">

  <header class="header">
    <nav class="header__nav">
      <ul class="header__navbar">
        <li class="header__item">
          <a href="#" class="header__link">
            <transition name="slide-fade">
              <!-- Header Navigation Menu Icons -->
              <button class="header--button" v-if="show" key="on" @click="show = false">
                <img src="../../assets/alarm-bell.png" alt="" class="alarm-bell">
              </button>
              <button class="header--button" v-else key="off" @click="show = true">
                <img src="../../assets/alarm-bell.png" alt="" class="alarm-bell">
              </button>
            </transition>
          </a>
          <!-- Dropdown Menu -->
          <transition name="dropdown">
            <div class="dropdown__menu" v-bind:class="{ active: show }" v-if="show">
              <ul class="dropdown__menu-nav">
                <div v-for="(alarm, idx) in alarmList" :key="idx" class="dropdown__menu-text">
                  <div v-if="alarm.alarmCheck === 0">
                    <li v-if="alarm.alarmCategory === 1" >
                      <p @click="clickAlarm(alarm.alarmIdx, idx)">
                        <i class="fas fa-heart"></i> {{ alarm.userNickname }} 님의 좋아요!
                      </p>
                    </li>
                    <li v-else-if="alarm.alarmCategory === 2" class="dropdown__menu-item">
                      <p @click="clickAlarm(alarm.alarmIdx, idx)">
                        <i class="fas fa-user-friends"></i> {{ alarm.userNickname }} 님의 팔로우!
                      </p>
                    </li>
                    <li v-else-if="alarm.alarmCategory === 3">
                      <p @click="clickAlarm(alarm.alarmIdx, idx)">
                        <i class="far fa-newspaper"></i> {{ alarm.userNickname }} 님의 새 여행!
                      </p>
                    </li>
                    <hr>
                  </div>
                </div>
                <!--
                <li class="dropdown__menu-item">
                  <a href="#" class="dropdown__menu-link" title="Send feedback">
                    <div class="dropdown__menu-svg">
                      <svg viewBox="0 0 512 512">
                        <path fill="currentColor" d="M448 0H64C28.7 0 0 28.7 0 64v288c0 35.3 28.7 64 64 64h96v84c0 7.1 5.8 12 12 12 2.4 0 4.9-.7 7.1-2.4L304 416h144c35.3 0 64-28.7 64-64V64c0-35.3-28.7-64-64-64zm32 352c0 17.6-14.4 32-32 32H293.3l-8.5 6.4L192 460v-76H64c-17.6 0-32-14.4-32-32V64c0-17.6 14.4-32 32-32h384c17.6 0 32 14.4 32 32v288zm-224-88c-13.3 0-24 10.7-24 24s10.7 24 24 24 24-10.7 24-24-10.7-24-24-24zm-8.5-24h17c4.2 0 7.7-3.3 8-7.5l7-112c.3-4.6-3.4-8.5-8-8.5h-31c-4.6 0-8.3 3.9-8 8.5l7 112c.3 4.2 3.8 7.5 8 7.5z" />
                      </svg>
                    </div>
                    <div class="dropdown__menu-text">Send feedback</div>
                  </a>
                </li> -->
              </ul>
            </div>
          </transition>
        </li>
      </ul>
    </nav>
  </header>

</div>

</template>
<script>
import '../../components/css/dropdown.scss'
import axios from 'axios'

export default {
  name:'',
  components: {},
  data() {
    return {
      show: false,
      alarmList: this.$store.state.alarm.alarmList
    };
  },
  created() {

  }, 
  beforeMount() {}, 
  mounted() {}, 
  beforeUpdate() {},
  updated() {},
  beforeUnmount() {}, 
  unmounted() {},
  methods: {
    clickAlarm(alarmIdx, idx) {
      axios({
        method: 'get',
        url: '/alarm',
        params: {
          alarmIdx: alarmIdx,
        },
        headers: { 'X-AUTH-TOKEN' : this.$store.state.accounts.authToken },
      })
        .then(res => {
          console.log(res)
          // this.alarmList.splice(idx, 1)
          this.$store.dispatch('delAlarm', idx)
        })
        .catch(err => console.log(err))
    }
  }
}
</script>