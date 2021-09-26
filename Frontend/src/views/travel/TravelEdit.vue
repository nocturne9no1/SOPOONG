<template>
  <div class="edit-travel">
    <div class="form-box">
      <h1>Edit Travel</h1>
      <div class="title-wrap">
        <h2>Title</h2>
        <input v-model="getNowTravel.travelTitle" type="text" placeholder=" title">
      </div>
      <div class="comment-wrap">
        <h2>Comment</h2>
        <textarea v-model="getNowTravel.travelContent" class="text-box" name="" id="" rows="5" placeholder="content"></textarea>
      </div>
      <p></p>

      <!-- 날짜 고르기 -->
      <div class="range-date-picker-container">
        <transition name="panelIn">
          <range-date-picker
            class="range-date-picker"
            v-model="dates"
            language="en"
            v-show="show"
            @closePicker="closePicker"
          ></range-date-picker>
        </transition>
        <input
          class="range-date-input"
          @click="showPicker"
          placeholder="click to use vue-easy-range-datepicker"
          :value="rangeDates"
        />
      </div>


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

      <button @click="updateTravel" class="btn">Publish</button>
    </div>

    
    <div>
      <img class="main-image" :src="imgSrc" alt="">
    </div>
  </div>
</template>

<script>
import '../../components/css/edittravel.scss'
import vClickOutside from 'v-click-outside'
import RangeDatePicker from 'vue-easy-range-date-picker';
import axios from 'axios'
import { mapGetters } from 'vuex'

export default {
  name:'EditTravel',
  components: {
    RangeDatePicker,
  },

  props: {},

  directives: {
    clickOutside: vClickOutside.directive
  },

  data() {
    return {
      sampleData: '',
      publicSetting: true,
      isChoicePushed: false,
      imageList: new Array(),
      mainImage: '',
      dates: { 
        start: 1573052400000, // example
        end: 1574434800000    // example
      },
      show: false,
    };
  },
  beforeCreate() {},
  created() {
    
  }, 
  beforeMount() {}, 
  mounted() {

  }, 
  beforeUpdate() {},
  updated() {},
  beforeUnmount() {}, 
  unmounted() {},

  computed: {
    ...mapGetters(['getNowTravel']),
    renderImage() {
      return (image) => { URL.createObjectURL(image.file) }
    },
    
    rangeDates() {
      if (Object.keys(this.dates).length === 2) {
        return `${this.parseTime(
          this.dates.start,
          "y-m-d"
        )}  -  ${this.parseTime(this.dates.end, "y-m-d")}`;
      } else {
        return "";
      }
    },
    startDate() {
      return this.parseTime( this.dates.start, "y-m-d") + ' ' + '00:00:00'
    },
    endDate() {
      return this.parseTime( this.dates.end, "y-m-d") + ' ' + '00:00:00'
    },

    imgSrc() {
      return 'https://i5a404.p.ssafy.io/api/image/'+this.getNowTravel.imageOriginTitle
    },


  },

methods: {
  parseTime(time, format = "y-m-d h:i:s") {
  if (!time && time !== 0) {
    return "0000-00-00 00:00:00";
  }

  const date = time instanceof Date ? time : new Date(parseInt(time, 10));

  const formatObj = {
    y: date.getFullYear(),
    m: date.getMonth() + 1,
    d: date.getDate(),
    h: date.getHours(),
    i: date.getMinutes(),
    s: date.getSeconds(),
    a: date.getDay()
  };

    return format.replace(/(y|m|d|h|i|s|a)+/gi, (match, p1) => {
      p1 = p1.toLowerCase();
      let value = formatObj[p1];

      if (match.length > 0 && value < 10) {
        value = `0${value}`;
      }

      return value || (p1 === "y" ? "0000" : "00");
    });
  },

    toggle() {
      this.publicSetting = !this.publicSetting
      this.travel.travelIsVisible = !this.travel.travelIsVisible
    },

    clickChoice() {
      this.isChoicePushed = !this.isChoicePushed
    },

    closeSideWindow() {
      this.isChoicePushed = false
    },

    setMainImage(image) {
      this.travel.mainImage = image
      this.mainImage = image
      this.travel.travelLat = image.position.lat
      this.travel.travelLng = image.position.lng
      for ( let idx in this.travel.placeList ) {
        for ( let each in this.travel.placeList[idx].imageList ) {
          console.log(this.travel.placeList[idx].imageList[each].file)
          console.log(image)
          if ( this.travel.placeList[idx].imageList[each].file == image.file ) {
            console.log('여기까진 ㅇㅋ')
            this.travel.placeList[idx].imageList[each].isTravelLeader = true
          }
        }
      }
    },
    closePicker() {
      this.show = false;
    },
    showPicker() {
      this.show = true;
    },
    updateTravel() {
      axios({
        method: 'patch',
        url: 'travel/update',
        data: {
          travelIdx: this.getNowTravel.travelIdx,
          travelTitle: this.getNowTravel.travelTitle,
          travelContent: this.getNowTravel.travelContent,
          travelIsVisible: this.getNowTravel.Isvisible,
          startDate: this.getNowTravel.startDate,
          endDate: this.getNowTravel.endDate,
        },
        headers: { 'X-AUTH-TOKEN' : this.$store.state.accounts.authToken },
        })
          .then(res => {
            console.log(res)
            this.$emit('close-modal')
            this.$router.go(-1)
          })
          .catch(err => console.log(err))
    },
  }

}
</script>