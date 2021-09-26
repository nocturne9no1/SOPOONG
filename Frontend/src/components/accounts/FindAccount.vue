<template>
  <div>
    <div v-show="!existingEmail" class="FindAccountDiv">
      <h1>계정 찾기</h1>
      <h3>
        가입시 입력하셨던 이메일을 입력해주세요.
      </h3>
      <br />
      <br />
      <span>
        <div>
          <input
            v-model="email"
            :class="{ invalidEmail: error.email }"
            id="email"
            pattern="/^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/"
            placeholder="Email"
            type="text"
          />
          <div class="error-text" v-show="error.email">
            {{ error.email }}
          </div>
        </div>
        <!-- <button @click="existed()" v-show="!error.email">
          확인
        </button> -->
        <!-- <button @click="existed()" id="confirmBtn"> -->
          <div>
            <button @click="existed(email)" id="confirmBtn" disabled>
              확인
            </button>
          </div>
      </span>
    </div>

    <div v-show="existingEmail" class="EmailAuthDiv">
      <div>
        <h1>이메일 인증</h1>
        <h3>
          인증 메일이 전송되었습니다.<br />메일을 확인하시고 인증코드를
          입력해주세요.
        </h3>
        <br />
        <span>
          <input
            v-model="authKey"
            @keyup.enter="findIdConfirmAuthKey({email, authKey})"
            id="authkey"
            placeholder="인증코드"
            type="text"
          />
          <button
            @click="findIdConfirmAuthKey({email, authKey})"
            @keyup.enter="findIdConfirmAuthKey({email, authKey})"
          >
            인증
          </button>
        </span>
        <span class="EmailAuthDivFooter">
          <p>
            <!-- a태그 이용한 새로고침 이용 뒤로 돌아가기 -->
            <a href="">메일 주소 다시 입력</a>
          </p>
          <p @click="resend(email)">
            인증메일 다시 보내기
          </p>
        </span>
      </div>
    </div>
  </div>
</template>

<script>
import { mapActions } from "vuex";
import axios from 'axios';

export default {
  name: "FindAccount",
  components: {},
  data() {
    return {
      sampleData: "",
      authKey: "",
      existingEmail: false,
      email: "",
      error: {
        email: "",
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
    email: {
      deep: true,
      handler() {
        this.checkEmail(this.email);
      },
    },
  },
  methods: {
    ...mapActions(["requestEmailAuth", "findIdConfirmAuthKey"]),

    // 이메일 검증
    checkEmail(email) {
      console.log("I am watching !");
      const target = document.getElementById('confirmBtn')
      // 이메일 중복검사 이용
      axios.get('auth/email', { params :{ email: email }})
        .then(res => {
        if ( res.data.data.errors && email.length >= 1 ) {
          // 버튼 활성화 비활성화때문에 else if 추가 + axios문 내에서 이 3개가 주요역할.
          this.error.email = false;
          target.disabled = false;
        } else if ( res.data.data.errors && email.length == 0 ) {
          this.error.email = false;
          target.disabled = true;
        } else {
          this.error.email = '존재하지 않는 이메일입니다.';

          target.disabled = true;
        } 
      })
    },

    // 존재했다 !
    existed(email) {
      this.existingEmail = true;
      this.$store.dispatch('findIdEmailAuth', email)
    },
    // 다시 보낸다 !
    resend(email) {
      alert('메일이 다시 전송되었습니다.')
      this.existingEmail = true;
      this.$store.dispatch('findIdEmailAuth', email)
    }

  },
};
</script>

<style scoped>
div {
  font-family: monospace;
}

.FindAccountDiv {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);

  width: 600px;
  height: 200px;
  padding: 40px 30px 40px 30px;

  text-align: center;

  border-radius: 50px;
  background-color: rgba(183, 220, 204, 0.6);
  box-shadow: rgba(0, 0, 0, 0.15) 2.4px 2.4px 3.2px;
}

.EmailAuthDiv {
  /* display: table; */
  /* inline-block 사용시 margin과 함께 사용. inline-block은 가로 중앙정렬만 가능 */
  /* display: inline-block; */

  /* 다른 중앙정렬 방식 position ~ transform */
  /* body에 overflow:auto 통해 뷰포트가 작아져도 웹사이트 구조는 유지하게 만들 수 있다.(여기선 index.html) */
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);

  width: 600px;
  height: 200px;
  /* 상우하좌 padding으로 내부컨텐츠 정렬 구현 */
  padding: 40px 30px 40px 30px;
  /* margin: 50px auto; */
  /* margin-block: 10%; */

  text-align: center;

  border-radius: 50px;
  /* background-color: rgba(0, 0, 0, .75); */
  background-color: rgba(183, 220, 204, 0.6);
  /* opacity: 0.5; */
  box-shadow: rgba(0, 0, 0, 0.15) 2.4px 2.4px 3.2px;
}

h1 {
  color: #707070;
}

input {
  width: 250px;
  height: 30px;
  /* 상 좌우 하 */
  padding: 12px 20px 5px;
  margin-bottom: 15px;

  border: none;
  border-radius: 20px;
  background-color: rgba(255, 255, 255, 0.6);

  color: black;
  font-size: 16px;

  caret-color: black;
}

input:focus {
  outline: none;
}

input::placeholder {
  /* text-align: center; */
  color: rgba(237, 119, 102, 1);
  font-weight: bold;
  font-family: monospace;
}

#email {
  width: 300px;
}

.invalidEmail {
  border: 3px solid red;
}

button {
  width: 100px;
  height: 40px;
  padding: 12px 20px 5x;
  margin-bottom: 12px;

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

#confirmBtn:disabled {
  background-color: rgba(255, 255, 255, 0.35);
  color: rgba(237, 119, 102, .3);
  cursor: not-allowed;
  /* cursor: default; */
}

span {
  display: flex;
  justify-content: space-around;
  align-items: center;
}

.EmailAuthDivFooter {
  justify-content:space-evenly;
}

p {
  margin: 0px;

  color: rgba(237, 119, 102, 1);
  text-decoration: none;
  font-weight: bold;

  cursor: pointer;
}

a {
  color: rgba(237, 119, 102, 1);
  text-decoration: none;
  font-weight: bold;
}
</style>
