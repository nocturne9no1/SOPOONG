<template>
  <div>
    <ProfileBox/>
    <div class="settings">
      <div class="container">

        <div class="logo" id="logo">
          <h1 class="logo-font">SO<br/>POONG</h1>
          <img class="logo-img" src="../../assets/logo.png" alt="logo img">
        </div>

        <div class="leftbox">
          <nav>
            <a @click="clickCategory('user')"
            id="user" :class="{'active':nowSelect.user}"><i class="fa fa-user"></i>
            </a>
            <a @click="clickCategory('passwordChange')"
            id="passwordChange" :class="{'active':nowSelect.passwordChange}"><i class="fas fa-key"></i>
            </a>
            <a @click="clickCategory('alarmSetting')"
            id="alarmSetting" :class="{'active':nowSelect.alarmSetting}"><i class="fas fa-bell"></i>
            </a>
            <a @click="clickCategory('accountRelease')"
            id="accountRelease" :class="{'active':nowSelect.accountRelease}"><i class="fas fa-unlock-alt"></i>
            </a>
            <a @click="clickCategory('deleteAccount')"
            id="deleteAccount" :class="{'active':nowSelect.deleteAccount}"><i class="fas fa-trash-alt"></i>
            </a>
          </nav>
        </div>
        <div class="rightbox">

          <div v-if="nowSelect.user" class="profile">
            <h1>Personal Info</h1>
            <h2>user</h2>
            <p><input type="text" v-model="user.nickname"></p>
            <h2>Email</h2>
            <p>{{user.email}}</p>
            <h2>Introduce</h2>
            <p><input type="textarea" v-model="user.introduce"></p>
            <h2>Profile Image</h2>
            <div class="profile-image-button-wrap">
              <div class="profile-change">
                <label for="file">프사바꿩</label>
                <input 
                  type="file"
                  id="file"
                  ref="file"
                  name="file"
                  accept="image/*"
                  @change="uploadProfileImage"
                >
                <div class="image-preview-box">
                  <img :src="imagePreview" alt="">
                </div>
              </div>
              <button @click="changeProfile" class="btn">Change</button>
            </div>
          </div>

          <div v-if="nowSelect.passwordChange" class="payment">
            <h1>Passwrod Change</h1>
            <h2>Now Password</h2>
            <p><input type="password"
            v-model="passwordChange.nowPassword"
            placeholder="Enter your password"></p>
            <h2>Change Password</h2>
            <p><input type="password" 
            v-model="passwordChange.changePassword"
            placeholder="Enter change password"></p>
            <h2>Confirm Password</h2>
            <p><input type="password" 
            v-model="passwordChange.passwordConfirm"
            placeholder="Enter password again"></p>
            <br>
            <button @click="changePW" class="btn">Update</button>
          </div>

          <div v-if="nowSelect.alarmSetting" class="subscription">
            <h1>Alarm Setting</h1>
            <h2>Someone Like My Journey</h2>
            <div
              class="toggle-btn"
              :class="{'active':alarmSetting.someoneLike}">
              <input type="checkbox"
                :checked="alarmSetting.someoneLike"
                class="cb-value"
                @click="[clickAlarmSetting('someoneLike'), changeAlarm()]"
              >
              <span class="round-btn"></span>
            </div>
            
            <h2>Someone Followed Me</h2>
            <div
              class="toggle-btn"
              :class="{'active':alarmSetting.someoneFollow}">
              <input type="checkbox"
                :checked="alarmSetting.someoneFollow"
                class="cb-value"
                @click="[clickAlarmSetting('someoneFollow'), changeAlarm()]"
              >
              <span class="round-btn"></span>
            </div>
            <h2>Follower Submit Journey</h2>
            <div
              class="toggle-btn"
              :class="{'active':alarmSetting.followerSubmit}">
              <input type="checkbox"
                :checked="alarmSetting.followerSubmit"
                class="cb-value"
                @click="[clickAlarmSetting('followerSubmit'), changeAlarm()]"
              >
              <span class="round-btn"></span>
            </div>
            <button class="btn">Update</button>
          </div>

          <div v-if="nowSelect.accountRelease" class="privacy">
            <h1>Account Release Setting</h1>
            <h2>Do you want set your account to public?</h2>
            <div
              class="toggle-btn"
              :class="{'active':accountRelease}">
              <input type="checkbox"
                :checked="accountRelease"
                class="cb-value"
                @click="clickAccountRelease()"
              >
              <span class="round-btn"></span>
            </div>
          </div>

          <div v-if="nowSelect.deleteAccount">
            <h1>Delete Account</h1>
            <h2>Do you want delete your account?</h2>
            <p><input type="password" placeholder="Enter your password"></p>
            <button @click="deleteAccount" class="btn">Delete</button>
          </div>

        </div>
      </div>

    </div>
  </div>
</template>


<script>
import '../../components/css/profilesetting.scss'
import ProfileBox from './ProfileBox.vue'
import axios from 'axios'
export default {
  name:'',
  components: {
    ProfileBox,
  },
  data() {
    return {
      // 아래 데이터 들은 향후 백엔드 완성 시에
      // 모두 서버의 대이터로 대체되어야 합니다.
      nowSelect: {
        user: true,
        passwordChange: false,
        alarmSetting: false,
        accountRelease: false,
        deleteAccount: false,
      },
      user: {
        nickname: '',
        email: '',
        introduce: '',
      },
      passwordChange: {
        nowPassword: '',
        changePassword: '',
        passwordConfirm: '',
      },
      alarmSetting: {
        someoneLike: true,
        someoneFollow: true,
        followerSubmit: true,
      },
      accountRelease: true,

      userProfile: '',

      profileImageFile: null,
      imagePreview: '',
    }
  },

  created() {
    this.user.nickname = this.$store.state.accounts.userProfile.userNickname
    this.user.email = this.$store.state.accounts.userProfile.userEmail
    this.user.introduce = this.$store.state.accounts.userProfile.userComment
    
    // 알람 세팅 정보 설정
    var alarmSetting = this.$store.state.accounts.userProfile.userAlarm.toString(2)
    if ( alarmSetting.length === 1 ) {
      alarmSetting = '0' + '0' + alarmSetting
    } else if ( alarmSetting.length === 2 ) {
      alarmSetting = '0' + alarmSetting
    }
    if ( alarmSetting[0] === 1 ) { this.alarmSetting.followerSubmit = true }
    if ( alarmSetting[1] === 1 ) { this.alarmSetting.someoneFollow = true }
    if ( alarmSetting[2] === 1 ) { this.alarmSetting.someoneLike = true }
    
    // 계정 공개 정보 설정
    var isVisible = this.$store.state.accounts.userProfile.userIsVisible
    if ( isVisible === 0 ) {
      this.accountRelease = false
    } else {
      this.accountRelease = true
    }
  },

  methods: {
    clickCategory(category) {
      this.nowSelect[category] = true;
      for (let key in this.nowSelect) {
        if (key != category) { this.nowSelect[key] = false }
      }
    },
    clickAlarmSetting(category) {
      this.alarmSetting[category] = !this.alarmSetting[category];
    },
    clickAccountRelease() {
      this.accountRelease = !this.accountRelease
      const userId = this.$store.state.accounts.userProfile.userId
      var setting = 0
      if ( this.accountRelease === true ) { setting = 1 }
      axios({
        method: 'patch',
        url: '/user/visible',
        data: {
          userId: userId,
          userAlarm: setting,
        },
        headers: {
          'X-AUTH-TOKEN' : this.$store.state.accounts.authToken
        },
      })
        .then(res => console.log(res))
        .catch(err => console.log(err.message))
    },
    // 프로필 사진 프론트에 업로드
    uploadProfileImage() {
      var file = this.$refs['file'].files[0]
      this.profileImageFile = file
      this.imagePreview = URL.createObjectURL(file)
    },

    // 프로필 정보 변경
    changeProfile() {
      console.log(this.$store.state.accounts.authToken)
      const userId = this.$store.state.accounts.userProfile.userId
      const data = new FormData()
      data.append('userId', userId)
      data.append('userNickname', this.user.nickname)
      data.append('userComment', this.user.introduce)
      if ( !!this.profileImageFile === true ) { data.append('image', this.profileImageFile) }
      // else { data.append('image', false) }
      console.log(this.profileImageFile)
      axios({
        method: 'patch',
        url: '/user/profile',
        data: data,
        headers: {
          'X-AUTH-TOKEN' : this.$store.state.accounts.authToken
        },
      })
        .then(res => {
          console.log(res)
          // 프로필 정보 갱신
          this.$store.dispatch('getProfile', userId)
          // this.$router.go();
        })
        .catch(err => console.log(err.message))
    },
    changeAlarm() {
      var someoneLike = '0'
      var someoneFollow = '0'
      var followerSubmit = '0'
      if ( this.alarmSetting.someoneLike === true ) { someoneLike = '1' } 
      if ( this.alarmSetting.someoneFollow === true ) { someoneFollow = '1' } 
      if ( this.alarmSetting.followerSubmit === true ) { followerSubmit = '1' }
      const alarmdata = parseInt(followerSubmit + someoneFollow + someoneLike, 2)
      console.log(alarmdata)

      axios({
        method: 'patch',
        url: '/user/alarm',
        data: {
          userId: this.$store.state.accounts.userProfile.userId,
          userAlarm: alarmdata,
        },
        headers: {
          'X-AUTH-TOKEN' : this.$store.state.accounts.authToken
        },
      })
        .then(res => console.log(res))
        .catch(err => console.log(err.message))
    },

    changePW() {
      axios({
        method: 'patch',
        url: '/user/password',
        data: {
          userId: this.$store.state.accounts.userProfile.userId,
          userEmail: this.user.email,
          userPassword: this.passwordChange.nowPassword,
          changedPassword: this.passwordChange.changePassword,
        },
        headers: {
          'X-AUTH-TOKEN' : this.$store.state.accounts.authToken
        },
      })
        .then(res => console.log(res))
        .catch(err => console.log(err.message))
    },

    deleteAccount() {
      // 비밀번호 확인 후 계정 삭제 진행
    },
  }
}
</script>
