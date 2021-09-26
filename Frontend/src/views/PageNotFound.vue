<template>
  <div class="pageNotFoundDiv">
    <div>
      <img src="https://media.giphy.com/media/UoeaPqYrimha6rdTFV/giphy.gif" alt="" style="width: 30%">
    </div>
    <div style="padding:3rem">
      <p>찾을 수 없는 페이지입니다.<br>요청하신 페이지가 사라졌거나, 잘못된 경로를 이용하셨습니다. </p>
      <button type="button" class="btn btn-warning btn-lg" @click="goBack">Go back</button>
    </div>
  </div>
</template>

<script>

export default {
  name:'PageNotFound',
  components: {
  },
  data() {
    return {
      sampleData: ''
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
    goBack() {
      this.$router.go(-1)
    }
  }
}
</script>

<style scoped>
  .pageNotFoundDiv {
    padding: 100px;
    
    text-align: center;
    font-family: 'Do Hyeon';

  }

  .pageNotFoundDiv > div > button {
    background-color: #B7DCCC;

    padding: .5rem 1rem;

    border: 1px solid transparent;
    border-radius: .3rem;

    color: #F7F8F4;
    font-family: 'Do Hyeon';
    font-size: 1.25rem;
    font-weight: 700;

    cursor: pointer;
  }

  .pageNotFoundDiv > div > button:hover {
    color:black;
  }

  .pageNotFoundDiv > div > p {
    font-size: 1.5em;
  }
</style>