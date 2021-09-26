import axios from 'axios'
import router from '@/router'
import cookies from 'vue-cookies'

const state = {
  authToken: cookies.get('X-AUTH-TOKEN'),
  userProfile: null,
  personalUserProfile: null,
  emailAuthId: null,
  passwordResetInfo: null,
}
const getters = {
  isSignedIn: state => !!state.authToken,
  // 로그아웃시 정보 삭제용
  config: state => ({
    headers: {
      "X-AUTH-TOKEN": `Token ${state.authToken}`
    }
  }),
  getToken: state => state.authToken,
  getUserProfile: state => state.userProfile,
  getPersonalUserProfile: state => state.personalUserProfile,
  getEmailAuthId: state => state.emailAuthId,
  getPasswordResetInfo: state => state.passwordResetInfo,

}
const mutations = {
  SET_TOKEN(state, token) {
    // console.log(token)
    state.authToken = token
  },
  SET_PROFILE(state, userData) {
    state.userProfile = userData
  },
  SET_PERSONAL_PROFILE(state, userData) {
    state.personalUserProfile = userData
  },
  SET_EMAIL_AUTH_ID(state, id) {
    state.emailAuthId = id
  },
  SET_PASSWORD_RESET_INFO(state, data) {
    state.passwordResetInfo = data
  },
  RESET_SET_PASSWORD_RESET_INFO(state) {
    state.passwordResetInfo = null
  },
}
const actions = {
  
  // 로그인
  signIn(context, signInData) {
    // 로그인시도
    axios.get('auth/login', { params :{ id: signInData.id, password: signInData.password } })
    .then(res => {
      context.commit('SET_TOKEN', res.data.data.success) // 보내주는 cookie key 저장? 키값이 이렇게 오는게 맞나?
      cookies.set('X-AUTH-TOKEN', res.data.data.success, "7d") // 키 , 값, 만료일
      // this.$cookies.set('auth-token', res.data.key, "7d")  // 글로벌 설정으로 쿠키 가져올때(main.js).
      
      // 프로필 정보 기억
      // console.log(signInData)
      context.dispatch('getProfile', signInData.id)
      // console.log('아 됐다고 ㅋㅋㅋ')
      // router.push('/main')
    })
    .catch(err => {console.log(err), alert('아이디와 비밀번호를 확인하세요.')})
  },
  
  // 로그인시 프로필 데이터 get
  getProfile(context, id) {
    // header에 X-HEADER-TOKEN 넣어 보낼 시, 403에러 해결 가능
    axios.get(`user`, { params: {id : id}, headers: { 'X-AUTH-TOKEN' : context.state.authToken, 'Access-Control-Allow-Origin': '*' } })
      .then(res => {
        context.commit("SET_PROFILE", res.data.data.success);
        if ( router.history.current.name === 'SignIn' ) {
          router.push('/main')
        }
      })
      .catch(err => console.error(err))
  },

  getPersonalUserProfile(context, id) {
    axios.get(`user`, { params: {id : id}, headers: { 'X-AUTH-TOKEN' : context.state.authToken, 'Access-Control-Allow-Origin': '*' } })
      .then(res => {
        context.commit("SET_PERSONAL_PROFILE", res.data.data.success);
    //     if ( router.history.current.name === 'SignIn' ) {
    //       router.push(`/travellist/${id}`)
    })
  },

  // 로그아웃
  signOut(context) {
    context.commit('SET_TOKEN', null)  // state에서
    context.commit('SET_PROFILE', null)
    context.commit('SET_FOLLOWING_LIST', null)
    context.commit('SET_MY_TRAVEL_JOURNAL', null)
    context.commit('SET_TRAVEL_DETAIL', [null, null, null])
    cookies.remove('X-AUTH-TOKEN')  // cookie에서
    context.commit('SET_ALARMLIST', null)  // 로그아웃 시 알람 리스트 초기화
    router.push('/intro')
  },
  
  // 회원가입
  signUp(context, signUpData) {
    // 이메일 인증용 ID 기억
    context.commit('SET_EMAIL_AUTH_ID', signUpData.id)
    // 회원가입 실행
    context.dispatch('postSignUpData', signUpData)
  },
  
  postSignUpData(context, signUpData) {
    axios.post('auth/register', signUpData)
    .then(res => {
      
      // 회원가입이 성공적으로 되었다 메세지?
      console.log(res)  // 회원가입시 받아오는 모든것들.
      console.log(context)
      router.push('/auth')
      // 인증 메일 발송. 순서상 문제로 바로 발송되지 않는 문제 발생
      context.dispatch('requestEmailAuth', signUpData.id)
    })
    .catch(err => console.error(err))
  },
  
  // 가입시 이메일 인증 요청
  requestEmailAuth(context, id) {
    const url = 'auth/email'
    axios.post(url, { id: id, headers: {
      "Content-Type": "application/json; charset=utf-8",
      "Accept": "application/json; charset=utf-8"
      }})
      .then(res => {
        res.data
      })
      .catch(err => {
        console.log(err)
      })
  },

  // 가입시 이메일 인증번호 확인
  confirmAuthKey(context, authKey) {
    axios.post('auth/confirm/account', { id: context.getters['getEmailAuthId'], authNumber : authKey})
      .then(res => {
        res,
        // alert('회원가입이 완료되었습니다.')
        router.push('/signin')
      })
      .catch(err => {err, alert('인증키를 확인해주세요.')})
  },

  // 아이디 찾기 이메일 인증 요청
  findIdEmailAuth(context, email) {
    axios.post('auth/find/id', { email : email })
      .then(res => {
        console.log(res.data)
      })
  },

  // 아이디 찾기 인증번호 인증
  findIdConfirmAuthKey(context, data) {
    axios.post('auth/confirm/id', { email : data.email, authNumber : data.authKey })
     .then(res => {
       console.log(data.authKey, data.email, res.data)
       router.push('/signin/identify/password/reset')
       context.commit('SET_PASSWORD_RESET_INFO', [res.data.data.success, data.email])
     })
  },
  // 패스워드 리셋 후 state 데이터 리셋.
  resetPassword(context, data) {
    axios.patch('auth/find/password', data)
      .then(res => {
        console.log(res.data, '성공!')
        // 로그인창 복귀
        router.push('/signin')
        // vue 데이터 초기화
        context.commit('RESET_SET_PASSWORD_RESET_INFO')
      })
      .catch(err => {err, alert('비밀번호 양식을 확인해주세요.')})    
  },
}

export default {
  state, getters, mutations, actions
}