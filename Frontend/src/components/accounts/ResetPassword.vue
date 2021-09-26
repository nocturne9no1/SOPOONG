<template>
  <!-- signIn과 다른점은 최상위 div를 하나 더 두었음. -->
  <div class="TableDiv">
    <div class="TableDivCell">
      <div class="SignUpDiv">
        <!-- <img src="@/assets/sopoong_korean_logo.png" alt=""> -->
        <div>
          <h1>
           비밀번호 재설정
          </h1>
          <p>
            비밀번호를 재설정해주세요.
          </p>
        </div>
        <div>
          <input
            id="id"
            pattern="^([a-z0-9_]){6,50}$"
            type="text"
            :value="resetData.id"
            disabled
          />
        </div>
        <div>
          <input
            v-model="resetData.email"
            :class="{ invalidEmail: error.email }"
            id="email"
            pattern="/^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/"
            placeholder="Email"
            type="email"
            disabled
          />
        </div>
        <div class="error-text" v-if="error.email">
          {{ error.email }}
        </div>

        <div>
          <input
            v-model="resetData.password"
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
            v-model="resetData.confirmPassword"
            id="confirm-password"
            pattern=""
            placeholder="Confirm Password"
            type="password"
          />
        </div>
        <div class="error-text" v-if="error.confirmPassword">
          {{ error.confirmPassword }}
        </div>

        <button @click="resetPassword(resetData)" @keyup.enter="resetPassword(resetData)">
          비밀번호 변경
        </button>
      </div>
      <!-- 추가할 것.
            정규표현식 이용한 패턴 관리.
            에러메세지 출력 구현? 이거 어째야될지 모르겠음...
            CSS(나중에)
      -->
    </div>
  </div>
</template>

<script>
import { mapActions } from "vuex";
// import axios from 'axios'

export default {
  name: "ResetPassword",
  components: {},
  data() {
    return {
      resetData: {
        id: "",
        email: "",
        password: "",
        confirmPassword: "",
      },
      error: {
        password: false,
        confirmPassword: false,
      },
    };
  },
  beforeCreate() {},
  created() {},
  beforeMount() {},
  mounted() {
    let resetData = this.$store.getters['getPasswordResetInfo']
    this.resetData.id = resetData[0]
    this.resetData.email = resetData[1]
  },
  beforeUpdate() {},
  updated() {},
  beforeUnmount() {},
  unmounted() {},
  watch: {
    // 객체로 된 데이터를 watch에서 사용할경우 deep watch 사용.
    resetData: {
      deep: true,
      handler() {
        this.checkForm();
        //this.$store.dispatch('duplicateCheck', this.resetData)
      },
    },
  },
  methods: {
    ...mapActions(["resetPassword"]),

    // 검증
    checkForm() {
      // 비밀번호 검증 (re.test(var) var이 re의 양식에 맞는 지 체크)
      if (
        !/^(?=.*[A-Za-z])(?=.*\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\d~!@#$%^&*()+|=]{8,16}$/.test(
          this.resetData.password
        ) &&
        this.resetData.password.length >= 1
      ) {
        this.error.password =
          "영문자, 숫자, 특수문자를 포함한 8~16자 이내여야 합니다.";
      } else this.error.password = false;
      // 비밀번호 확인 검증
      if (
        this.resetData.password != this.resetData.confirmPassword &&
        this.resetData.confirmPassword.length >= 1 &&
        this.error.password == false
      ) {
        this.error.confirmPassword = "비밀번호가 일치하지 않습니다.";
      } else this.error.confirmPassword = false;
    },
  },
};
</script>

<style scoped>

.TableDiv {
  display: table;
  margin: auto;
  height: 100vh;
  font-family: 'Do Hyeon', monospace;
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

input:disabled {
  color: rgba(57, 57, 57, .8);
  background-color: rgba(175, 170, 170, 0.6);
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
  font-family: 'Do Hyeon', monospace;
  font-size: 20px;
  font-weight: bold;
  text-decoration: none;

  cursor: pointer;
}

.error-text {
  color: red;
}
</style>
