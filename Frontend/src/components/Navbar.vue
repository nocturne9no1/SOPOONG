<template>
  <div class="nav-wrap">
    <nav>
      <ul class="nav-container">
        <div class="nav-left">
          <li class="nav-item nav-logo">
            <router-link to="/main">
              <img class="logo" src="../assets/logo.png" alt="Sopoong logo">
            </router-link></li>
            <router-link to="/main" class="text-logo">
              SOPOONG
            </router-link>
          <li class="nav-item nav-newsfeed"><router-link to="/feed">NewsFeed</router-link></li>
          <li v-if="isSignedIn" class="nav-item nav-followfeed" @click="toFollowFeed()">FollowFeed</li>
          <li v-if="isSignedIn" class="nav-item nav-scrapfeed" @click="toScrapFeed()">ScrapFeed</li>
        </div>
        <div class="nav-right">
          <div class="search-bar" id="search-bar">
            <input type="search" v-model="searchWord" @keydown.enter="onEnter">
          </div>
          <div v-if="!isSignedIn" class="nav-right">
            <li class="nav-item nav-login"><router-link to="/signin">Login</router-link></li>
            <li class="nav-item nav-signup"><router-link to="/signup">Signup</router-link></li>
          </div>
          <div v-else class="nav-right">
            <li class="nav-item nav-login" @click="signOut">Logout</li>
            <li class="alarm-wrap">
              
              <DropDown class="dropdown"/>
              <div class="if-there-alarm"/>
            </li>
            <li class="">
              <div class="profile-image-box">
                <img :src="imgSrc" alt="profile-image" class="profile-image">
              </div>
            </li>
          </div>
        </div>
      </ul>
    </nav>
  </div>
</template>

<script>
import "./css/navbar.scss"
import { mapActions, mapGetters } from 'vuex'
import router from '@/router'
import DropDown from './Navbar/DropDown.vue'

export default {

  name: 'NavBar',

  components: {
    DropDown,
  },
  
  data() {
    return {
      // 향후 vuex에서 로그인 여부를 가져오는 것으로 바꿔야함
      // test 용
      isSearchClick: false,
      searchWord: '',
    }
  },

  computed: {
    ...mapGetters(["isSignedIn", "isAlarm"]),
    
    imgSrc() {
      return 'https://i5a404.p.ssafy.io/api/image/'+this.$store.state.accounts.userProfile.imageOriginTitle
    }
  },
  methods: {
    ...mapActions(["signOut", "removeAlarmList"]),

    onEnter() {
      // 향후 검색 페이지로 이동
    console.log(this.searchWord)
    },

    toFollowFeed() {
      if (this.$route.name != 'FollowingPeopleFeed') {
        router.push({name: 'FollowingPeopleFeed'})
      }
    },

    toScrapFeed() {
      if (this.$route.name != 'ScrapFeed') {
        router.push({name: 'ScrapFeed'})
      }
    }
  },
}
</script>

<style>
</style>
