<template>
  <div class="complete-form">
    <div class="form-box">
      <h1>PACKAGING</h1>
      <h2>Title</h2>
      <input type="text" placeholder=" title">
      <h2>Comment</h2>
      <textarea class="text-box" name="" id="" rows="5" placeholder="content"></textarea>
      <p></p>
      <h2>대표사진 고르기</h2>
      <button @click="clickChoice">사진고르기</button>
      <p></p>
      <div class="public-setting">
        <h2>공개 여부</h2>

        <div
          class="toggle-btn"
          :class="{'active':publicSetting}">
          <input type="checkbox"
            :checked="publicSetting"
            class="cb-value"
            @click="toggle()"
          >
          <span class="round-btn"></span>
        </div>
      </div>
      <p></p>

      <button class="btn">Publish</button>
    </div>

    <div v-if="isChoicePushed" class="side-window" v-click-outside="closeSideWindow">
      <!-- 이부분에 이미지를 띄워준다. -->
      <!-- <div v-for=""> -->
        <!-- 이미지를 클릭 시에 대표사진으로 설정할 수 있게 한다. -->
      <!-- </div> -->
    </div>
  </div>
</template>

<script>
import '../components/css/completeform.scss'
import vClickOutside from 'v-click-outside'


export default {
  name:'completeForm',
  components: {},

  directives: {
    clickOutside: vClickOutside.directive
  },

  data() {
    return {
      sampleData: '',
      publicSetting: false,
      isChoicePushed: false,
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
    toggle() {
      this.publicSetting = !this.publicSetting
    },
    clickChoice() {
      this.isChoicePushed = !this.isChoicePushed
    },
    closeSideWindow() {
      this.isChoicePushed = false
    },
  }
}
</script>