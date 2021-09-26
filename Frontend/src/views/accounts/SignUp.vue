<template>
  <!-- signIn과 다른점은 최상위 div를 하나 더 두었음. -->
  <div class="background">
    <div class="TableDiv">
      <div class="TableDivCell">
        <div class="SignUpDiv">
          <img src="@/assets/sopoong_korean_logo.png" alt="">
          <div>
            <input
              v-model="signUpData.id"
              :class="{ invalidId: error.id }"
              id="id"
              pattern="^([a-z0-9_]){6,50}$"
              placeholder="ID"
              type="text"
            />
          </div>
          <div class="error-text" v-if="error.id">
            {{ error.id }}
          </div>

          <div>
            <input
              v-model="signUpData.nickname"
              :class="{ invalidNickname: error.nickname }"
              id="nickname"
              placeholder="Nickname"
              type="text"
            />
          </div>
          <div class="error-text" v-if="error.nickname">
            {{ error.nickname }}
          </div>

          <div>
            <input
              v-model="signUpData.email"
              :class="{ invalidEmail: error.email }"
              id="email"
              pattern="/^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/"
              placeholder="Email"
              type="email"
            />
          </div>
          <div class="error-text" v-if="error.email">
            {{ error.email }}
          </div>

          <div>
            <input
              v-model="signUpData.password"
              :class="{ invalidPassword: error.password }"
              id="password"
              pattern="/^(?=.*[A-Za-z])(?=.*\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\d~!@#$%^&*()+|=]{8,16}$/"
              placeholder="Password"
              type="password"
            />
          </div>
          <div class="error-text" v-if="error.password">
            {{ error.password }}
          </div>

          <div>
            <input
              v-model="signUpData.confirmPassword"
              id="confirm-password"
              pattern=""
              placeholder="Confirm Password"
              type="password"
            />
          </div>
          <div class="error-text" v-if="error.confirmPassword">
            {{ error.confirmPassword }}
          </div>

          <button @click="signUp(signUpData)" @keyup.enter="signUp(signUpData)">
            회원가입
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { mapActions } from "vuex";
import axios from 'axios'

export default {
  name: "SignUp",
  components: {},
  data() {
    return {
      signUpData: {
        id: "",
        nickname: "",
        email: "",
        password: "",
        confirmPassword: "",
      },
      error: {
        id: false,
        email: false,
        nickname: false,
        password: false,
        confirmPassword: false,
      },
    };
  },
  beforeCreate() {},
  created() {},
  beforeMount() {},
  mounted() {},
  beforeUpdate() {},
  updated() {},
  beforeUnmount() {},
  unmounted() {},
  watch: {
    // 객체로 된 데이터를 watch에서 사용할경우 deep watch 사용.
    signUpData: {
      deep: true,
      handler() {
        this.checkForm();
        //this.$store.dispatch('duplicateCheck', this.signUpData)
        this.existId();
        this.existNickname();
        this.existEmail();
      },
    },
  },
  methods: {
    ...mapActions(["signUp", "requestEmailAuth"]),

    // 검증
    checkForm() {
      // 아이디 검증
      if (
        !/^([a-z0-9_]){1,50}$/.test(this.signUpData.id) &&
        this.signUpData.id.length >= 1
      ) {
        this.error.id = "id는 영문 소문자, 숫자, _ 기호만 사용 가능합니다.";
      } else if (
        this.signUpData.id.length < 6 &&
        this.signUpData.id.length > 0
      ) {
        this.error.id = "아이디는 6자 이상이어야 합니다.";
      } else this.error.id = false;

      // 이메일 검증
      if (
        !this.validateEmail(this.signUpData.email) &&
        this.signUpData.email.length >= 1
      ) {
        this.error.email = "이메일 형식을 확인해주세요.";
      } else this.error.email = false;

      // 비밀번호 검증 (re.test(var) var이 re의 양식에 맞는 지 체크)
      if (
        !/^(?=.*[A-Za-z])(?=.*\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\d~!@#$%^&*()+|=]{8,16}$/.test(
          this.signUpData.password
        ) &&
        this.signUpData.password.length >= 1
      ) {
        this.error.password =
          "영문자, 숫자, 특수문자를 포함한 8~16자 이내여야 합니다.";
      } else this.error.password = false;
      // 비밀번호 확인 검증
      if (
        this.signUpData.password != this.signUpData.confirmPassword &&
        this.signUpData.confirmPassword.length >= 1 &&
        this.error.password == false
      ) {
        this.error.confirmPassword = "비밀번호가 일치하지 않습니다.";
      } else this.error.confirmPassword = false;
    },

    validateEmail: function(email) {
      var regExp = /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/;
      return regExp.test(email);
    },

    existId() {
      axios.get('auth/id', { params :{ id: this.signUpData.id }})
        .then(res => {
          if ( res.data.data.errors && this.signUpData.id.length >= 6 ) {
            this.error.id = "아이디가 중복됩니다."
          }
        })
    },

    existNickname() {
      axios.get('auth/nickname', { params :{ nickName: this.signUpData.nickname }})
        .then(res => {
          if ( res.data.data.errors && this.signUpData.nickname.length >=1 ) {
            this.error.nickname = "닉네임이 중복됩니다."
          } else this.error.nickname = false;
        })
    },

    existEmail() {
      axios.get('auth/email', { params :{ email: this.signUpData.email }})
        .then(res => {
          if ( res.data.data.errors && this.signUpData.email.length >= 6 ) {
            this.error.email = "이메일이 중복됩니다."
          }
        })
        // .catch(err => console.error(err))
    }
  },
};
</script>

<style scoped>

.background {
}

.background::before {
  content: "";
  position: absolute;
  top: 0; left: 0;
  background-image: url('../../assets/background/DSC04007.jpg');
  background-size: cover;
  width: 100%; height: 100%;
  /* filter: grayscale(100%); */
  filter: blur(5px);
  z-index: -1;
}

div {
  font-family: monospace;
}

.TableDiv {
  display: table;
  margin: auto;
  height: 100vh;
}

/* display: table-cell은 Table의 높이를 그대로 상속받아 height 설정이 불가능. */
.TableDivCell {
  display: table-cell;
  vertical-align: middle;
}

/* table-cell 내 table-cell로 가로 세로 크기 조절 가능 */
.SignUpDiv {
  display: table-cell;
  /* inline-block 사용시 margin과 함께 사용. inline-block은 가로 중앙정렬만 가능 */
  /* display: inline-block; */

  /* 다른 중앙정렬 방식 position ~ transform */
  /* position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%); */
  background-image: url('../../assets/background/Wonhyeok1.jpg');
  background-size: cover;

  width: 300px;
  height: 500px;
  /* 상우하좌 padding으로 내부컨텐츠 정렬 구현 */
  padding: 40px 45px 40px 45px;
  margin: auto;
  vertical-align: middle;
  /* margin-block: 10%; */

  text-align: center;

  border-radius: 50px;
  /* background-color: rgba(0, 0, 0, .75); */
  background-color: rgba(183, 220, 204, 0.5);
  /* opacity: 0.5; */
  box-shadow: rgba(0, 0, 0, 0.15) 2.4px 2.4px 3.2px;
}

img {
  width: 100px;
  height: 100px;

}

input {
  width: 250px;
  height: 30px;
  /* 상 좌우 하 */
  padding: 12px 20px 5px;
  margin: 12px;

  border: none;
  border-radius: 20px;
  background-color: rgba(255, 255, 255, 0.6);

  color: black;
  font-size: 16px;
}

input:focus {
  outline: none;
}

input::placeholder {
  color: rgba(237, 119, 102, 1);
  font-weight: bold;
  font-family: monospace;
}

/* input:valid {
  background-color: greenyellow;
  } */

/* 패턴을 줘야 효율적으로 사용이 가능. */
/* input:invalid {
  border: 3px solid red;
} */

.invalidId {
  border: 3px solid red;
}

.invalidNickname {
  border: 3px solid red;
}

.invalidEmail {
  border: 3px solid red;
}

.invalidPassword {
  border: 3px solid red;
}

button {
  width: 150px;
  height: 40px;
  padding: 12px 20px 5x;
  margin-top: 30px;

  border: none;
  border-radius: 15px;
  background-color: rgba(255, 255, 255, 0.6);

  color: rgba(237, 119, 102, 1);
  font-size: 20px;
  font-weight: bold;
  font-family: monospace;
  text-decoration: none;

  cursor: pointer;
}

.error-text {
  color: red;
}
</style>
