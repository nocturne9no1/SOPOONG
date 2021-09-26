<template>
  <div class="profile-box" v-if="this.$store.getters['isSignedIn']">
    <div class="setting-btn">
      <router-link to="/settings">
        <span class="setting-icon">
          <i class="fas fa-lg fa-cog"></i>
        </span>
      </router-link>
    </div>
    <div class="container">
      <div class="profile-photo-box">
        <img class="photo" :src="imgSrc" alt="profile-photo">
      </div>
      <div class="text-box">
        <div class="nickname">
          {{ profile.userNickname }}
        </div>
        <div class="email">
          {{profile.userEmail}}
        </div>
        <div class="introduce" v-if="profile.userComment !== 'null'">
          {{profile.userComment}}
        </div>
        <div class="follow">
          <div class="following">
            <div>
              Following
            </div>
            <div>
              {{profile.followings}}
            </div>
          </div>
          <div class="follower">
            <div>
              Followers
            </div>
            <div>
              {{profile.followers}}
            </div>
          </div>
        </div>
      </div>
      
    </div>
  </div>
</template>

<script>
import '../../components/css/profilebox.scss'

export default {
  name:'',
  components: {},
  data() {
    return {
      // profile: this.$store.state.accounts.userProfile
      profile: null,
    };
  },

  // 페이지 생성 전, 프로필 데이터 받아와야 함
  beforeCreate() {},
  created() {
    // if ( this.$store.getters['isSignedIn'] ) {
    //   this.profile = this.$store.getters['getUserProfile']
    // }
    if ( this.$store.getters['isSignedIn']) {
      this.$store.dispatch('getProfile', this.$store.getters['getUserProfile'].userId)
      this.profile = this.$store.getters['getUserProfile']
    }
  }, 
  beforeMount() {}, 
  mounted() {
  }, 
  beforeUpdate() {},
  updated() {
  },
  beforeUnmount() {},
  unmounted() {},

  computed: {
    imgSrc() {
        return 'https://i5a404.p.ssafy.io/api/image/'+this.$store.state.accounts.userProfile.imageOriginTitle
    }
  },

  methods: {
  }
}
</script>