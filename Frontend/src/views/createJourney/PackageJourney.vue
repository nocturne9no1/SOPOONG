<template>
  <div class="complete-form">
    <div class="form-box">
      <h1>PACKAGING</h1>
      <div class="title-wrap">
        <h2>Title</h2>
        <input v-model="travel.travelTitle" type="text" placeholder=" title">
      </div>
      <div class="comment-wrap">
        <h2>Comment</h2>
        <textarea v-model="travel.travelContent" class="text-box" name="" id="" rows="5" placeholder="content"></textarea>
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

      <!-- 대표사진 고르기 -->
      <div class="pick-wrap">
        <h2>대표사진 고르기</h2>
        <button class="pick-btn" @click="clickChoice">PICK</button>
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

      <button @click="onTrans" class="btn">Publish</button>
    </div>

    <div v-if="isChoicePushed" class="side-window" v-click-outside="closeSideWindow">
      <!-- 이부분에 이미지를 띄워준다. -->
      <div v-for="(image, idx) in images" :key="idx" class="image-box">
        <!-- 이미지를 클릭 시에 대표사진으로 설정할 수 있게 한다. -->
        <img @click="setMainImage(image)" :src="image.preview" alt="">
      </div>
    </div>
    <div v-if="mainImage">
      <img class="main-image" :src="mainImage.preview" alt="">
    </div>
  </div>
</template>

<script>
import '../../components/css/createJourney/packagejourney.scss'
import vClickOutside from 'v-click-outside'
import RangeDatePicker from 'vue-easy-range-date-picker';
import axios from 'axios'

export default {
  name:'completeForm',
  components: {
    RangeDatePicker,
  },

  props: {
    images: Array,
    travel: Object,
  },

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
    }
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

  onTrans() {
    const travel = new FormData()

    const leftPad = function(value) {
      if (value >= 10) {
        return value
      }

      return `0${value}`
    }
    const convertDate = function (source, delimiter = '-') {
      const year = source.getFullYear()
      const month = leftPad(source.getMonth() + 1)
      const day = leftPad(source.getDate())
      const hour = leftPad(source.getHours())
      const minutes = leftPad(source.getMinutes())
      const seconds = leftPad(source.getSeconds())

      return [year, month, day].join(delimiter) + ' ' + [hour, minutes, seconds].join(':')
    }

    travel.append('travelTitle', this.travel.travelTitle)
    travel.append('userId', this.$store.state.accounts.userProfile.userId)
    travel.append('travelContent', this.travel.travelContent)
    if ( this.travel.travelIsVisible === true ) { travel.append('travelIsVisible', 1) }
    else { travel.append('travelIsVisible', 0) }
    travel.append('travelLat', this.travel.travelLat)
    travel.append('travelLng', this.travel.travelLng)
    travel.append('startDate', this.startDate)
    travel.append('endDate', this.endDate)

    for ( let idx in this.travel.placeList ) {
      travel.append(`placeList[${idx}].title`, this.travel.placeList[idx].title)
      travel.append(`placeList[${idx}].comment`, this.travel.placeList[idx].comment)
      travel.append(`placeList[${idx}].category.main`, this.travel.placeList[idx].category.main)
      travel.append(`placeList[${idx}].category.sub`, this.travel.placeList[idx].category.sub)
      travel.append(`placeList[${idx}].rates.rate1`, this.travel.placeList[idx].rates.rate1)
      travel.append(`placeList[${idx}].rates.rate2`, this.travel.placeList[idx].rates.rate2)
      travel.append(`placeList[${idx}].rates.rate3`, this.travel.placeList[idx].rates.rate3)
      travel.append(`placeList[${idx}].transport`, this.travel.placeList[idx].transport)
      const date = new Date(this.travel.placeList[idx].visitDate)
      console.log(convertDate(date))
      travel.append(`placeList[${idx}].visitDate`, convertDate(date))
      travel.append(`placeList[${idx}].position.lat`, this.travel.placeList[idx].position.lat)
      travel.append(`placeList[${idx}].position.lng`, this.travel.placeList[idx].position.lng)

      for ( let i in this.travel.placeList[idx].imageList ) {
        travel.append(`placeList[${idx}].imageList[${i}].file`, this.travel.placeList[idx].imageList[i].file)
        const d = new Date(this.travel.placeList[idx].imageList[i].dateTime)
        console.log(convertDate(d))
        travel.append(`placeList[${idx}].imageList[${i}].dateTime`, convertDate(d))
        travel.append(`placeList[${idx}].imageList[${i}].position.lat`, this.travel.placeList[idx].imageList[i].position.lat)
        travel.append(`placeList[${idx}].imageList[${i}].position.lng`, this.travel.placeList[idx].imageList[i].position.lng)
        travel.append(`placeList[${idx}].imageList[${i}].imageWidth`, this.travel.placeList[idx].imageList[i].imageWidth)
        travel.append(`placeList[${idx}].imageList[${i}].imageHeight`, this.travel.placeList[idx].imageList[i].imageHeight)
        if(this.travel.placeList[idx].imageList[i].isPlaceLeader == true) {
          travel.append(`placeList[${idx}].imageList[${i}].isPlaceLeader`, 1)
        } else {
          travel.append(`placeList[${idx}].imageList[${i}].isPlaceLeader`, 0)
        }
        if(this.travel.placeList[idx].imageList[i].isTravelLeader == true) {
          travel.append(`placeList[${idx}].imageList[${i}].isTravelLeader`, 1)
        } else {
          travel.append(`placeList[${idx}].imageList[${i}].isTravelLeader`, 0)
        }
      }
    }

    console.log(travel)
    axios.post('/travel/create', travel, {
      headers: {
        'Content-Type' : 'multipart/form-data',
        'X-AUTH-TOKEN' : this.$store.state.accounts.authToken,
      }
    })
      .then(res => {
        console.log(res)
        this.$router.push('/main')
      })
      .catch(err => {
        console.log(err)
      })
    },
  }

}
</script>