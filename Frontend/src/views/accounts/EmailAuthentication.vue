<template>
<div class="background">
  <div class="EmailAuthDiv">
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
          @keyup.enter="confirmAuthKey(authKey)"
          id="authkey"
          placeholder="인증코드"
          type="text"
        />
        <button
          @click="confirmAuthKey(authKey)"
          @keyup.enter="confirmAuthKey(authKey)"
        >
          인증
        </button>
      </span>
      <p>
        <span href="" @click="resend">인증메일 다시 보내기</span>
      </p>
    </div>
  </div>
</div>
</template>

<script>
import { mapActions } from "vuex";
export default {
  name: "EmailAuthentication",
  components: {},
  data() {
    return {
      sampleData: "",
      authKey: "",
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
  methods: {
    ...mapActions(["confirmAuthKey"]),
    resend() {
      let id = this.$store.getters["getEmailAuthId"]
      this.$store.dispatch('requestEmailAuth', id)
      alert("인증 메일이 재전송되었습니다.");
    },
  },
};
</script>

<style scoped>
.background {
  background-image: url('../../assets/background.jpg');
  /* position: absolute; */
  background-size: cover;
  height: 100vh;
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
  margin-top: 0;

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
  color: rgba(237, 119, 102, 1);
  font-weight: bold;
  /* font-family: monospace; */
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
  /* font-family: monospace; */
  text-decoration: none;

  cursor: pointer;
}

span {
  display: flex;
  justify-content: space-around;
  align-items: center;

}

p {
  margin: 0px;
  cursor: pointer;
}

a {
  color: rgba(237, 119, 102, 1);
  text-decoration: none;
  font-weight: bold;
}
</style>
