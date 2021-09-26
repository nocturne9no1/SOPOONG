<template>
<div>
  <profile-box />
  <div style="display:flex; justify-content:center" v-if="test()">
    <TravelMap v-if="isMapSelected"/>
    <each-place-journal-card-list v-else/>
    <div class="toggle-btn">
      <div v-if="isMapSelected">
        <button @click="toggle">LIST</button>
      </div>
      <div v-else>
        <button @click="toggle">MAP</button>
      </div>
    </div>
  </div>
</div>
</template>

<script>
// import PersonalProfileBox from '../../components/personal/PersonalProfileBox.vue';
import ProfileBox from '../../views/accounts/ProfileBox.vue';
import EachPlaceJournalCardList from './EachPlaceJournalCardList.vue';
import TravelMap from './TravelMap.vue';
import router from '@/router';

export default {
  name:'',
  components: {
    ProfileBox,
    EachPlaceJournalCardList,
    TravelMap,
    // PersonalProfileBox,
  },
  data() {
    return {
      isMapSelected: true,
    };
  },
  beforeCreate() {},
  created() {
    console.log(router.history)
  },
  beforeMount() {},
  mounted() {},
  beforeUpdate() {},
  updated() {},
  beforeUnmount() {},
  unmounted() {},
  methods: {
    test() {
      // 없어짐
      // return "" 
      console.log(this.image)
      return "값이 있으니 생김."
    },

    toggle() {
      this.isMapSelected = !this.isMapSelected
    }

  }
}
</script>

<style lang="scss" scoped>
  .toggle-btn {
    position: absolute;
    top: 15%;
    right: 15%;
    button {
      width: 150px;
      height: 40px;
      padding: 12px 20px 5x;
      margin-bottom: 25px;
      
      border: none;
      border-radius: 15px;
      background-color: #b7dccc;
      
      color: #F4EEC8;
      font-size: 20px;
      font-weight: bold;
      font-family: monospace;
      text-decoration: none;
    
      cursor: pointer;
    }
  }
</style>>