<template>
  <div class="intro">
    <section id="section-1" class="section-1">
      <div class="words">
        <h1>저희가 기억할게요.</h1>
        <br>
        <h1>보고싶을 때 찾아와요.</h1>
      </div>
      <div class="logo-img">
        <img src="../assets/logo.png" alt="">
      </div>
      <!-- <a href="#section-2">아래로</a> -->
    </section>

    <section id="section-2" class="section-2">
      <div class="words">
        <h1>나 하늘로 돌아가리라</h1>
        <h1>아름다운 이 세상 소풍 끝내는 날</h1>
        <h1>가서, 아름다웠더라고 말하리라.</h1>
      </div>
    </section>
    <section id="section-3" class="section-3">
      <div class="markers">
        <img src="../assets/pins/hotel-pin.png" alt="">
        <img src="../assets/pins/restaurant-pin.png" alt="">
        <img src="../assets/pins/activity-pin.png" alt="">
      </div>
      <div class="words">
        <h1>당신의 추억을</h1>
        <h1>좀 더</h1>
        <h1>추억하기 편하게</h1>
        <!-- 클릭이나 호버 이벤트로 멘트 바꾸기 -->
        <!-- <h1>그래서 소풍은</h1>
        <h1>당신의 소풍으로</h1>
        <h1>당신의 소풍을</h1> -->
      </div>
    </section>
    <section id="section-4" class="section-4">
      <div class="box">
        <div class="words">
          <h1>저희와 함께.</h1>
          <h1>소풍 가시지 않을래요</h1>
        </div>
        <div class="SignInDiv">
          <div class="SignInDivBody" @keyup.enter="signIn(signInData)">
            <!-- <h1>Sign In</h1> -->
            <!-- <img src="https://cdn.discordapp.com/attachments/869113013892964412/870324360420929586/logo_png.png" alt=""> -->
            <img src="@/assets/sopoong_image_logo.png" alt="">
            <div>
              <!-- <label for="id">ID </label> -->
              <input
                v-model="signInData.id"
                id="id"
                pattern="^([a-z0-9_]){6,50}$"
                placeholder="ID"
                type="text"
              />
            </div>

            <div>
              <!-- <label for="password">Password : </label> -->
              <input
                v-model="signInData.password"
                id="password"
                pattern=""
                placeholder="Password"
                type="password"
              />
            </div>

            <button @click="signIn(signInData)">Login</button>
          </div>
          <!-- <span>
            첫 소풍이신가요? 지금
            <a @click="signUp">회원가입</a>
            하세요!
          </span> -->
          <div class="SignInDivFooter">
            <span>
              <a @click="identify()">Forgot ID/PW?</a>
            </span>
            <span>
              <a @click="signUp()">Create Account</a>
            </span>
          </div>  
        </div>
      </div>
    </section>
  </div>
</template>

<script>
import '../components/css/intro.scss'
import { mapGetters } from 'vuex'

export default {

  name:'',
  components: {},
  data() {
    return {
      scrolled: false,
      scrollPosition: 0,
      signInData: {
        id: "",
        password: "",
      },
      error: {
        id: false,
        email: false,
        password: false,
      },
      where: {
        '1': false,
        '2': false,
        '3': false,
        '4': false,
      }
    };
  },
  created() {
    window.addEventListener('scroll', this.handleScroll);
    console.log(this.isSignedIn)
    if ( this.$store.getters.isSignedIn === true ) { this.$router.push('/main') }
  },
  destroyed() {
    window.removeEventListener('scroll', this.handleScroll);
  },
  computed: {
    ...mapGetters['isSignedIn']
  },
  methods: {
    handleScroll() {
      this.scrollPosition = window.scrollY
      console.log(this.scrollPosition)
    },

    signIn(signInData) {
      this.$store.dispatch('signIn', signInData)
    },

    signUp() {
      this.$router.push('/signup')
    },

    identify() {
      console.log(this.$route.path.length)
      this.$router.push('signin/identify')
    },


  },
}
</script>
