<template>
  <div class="pintest">
    <GmapMap
        ref="mapRef"
        :center="travel.placeList[0].position"
        :zoom="14"
        :options="options"
        class="google-map"
    >
      <div v-for="(nowPlace, idx) in travel.placeList" :key="idx">
        <!-- 현재 카테고리에 따른 icon 랜더링 -->
        <div v-if="nowPlace.category.main === 'hotel'">
          <gmap-marker
            :position="nowPlace.position"
            :clickable="true"
            :icon="icons.hotel"
            @click="openModal(nowPlace)"
          />
        </div>
        <div v-else-if="nowPlace.category.main === 'res'">
          <gmap-marker
            :position="nowPlace.position"
            :clickable="true"
            :icon="icons.res"
            @click="openModal(nowPlace)"
          />
        </div>
        <div v-else-if="nowPlace.category.main === 'activity'">
          <gmap-marker
            :position="nowPlace.position"
            :clickable="true"
            :icon="icons.activity"
            @click="openModal(nowPlace)"
          />
        </div>
        <!-- 아직 아무 카테고리도 선택 안했을 때 -->
        <div v-else-if="nowPlace.category.main === ''">
          <gmap-marker
            :position="nowPlace.position"
            :clickable="true"
            :icon="icons.basic"
            @click="openModal(nowPlace)"
          />
        </div>
      </div>

      <!-- 모달 열리기 실험용 -->
      <!-- <gmap-marker
        :position="marker.position"
        :clickalbe="true"
        :icon="icon"
        @click="clickBasicPin"
      /> -->

      <!-- polyline component -->
      <div v-for="(eachPath, idx) in paths" :key="idx">
        <EachPolyline
          :eachPath="eachPath"
        />
      </div>
      <gmap-polyline v-bind:path.sync="path" v-bind:options="{ strokeColor:'#000000'}"/> 
    </GmapMap>
        <!-- Button to go to next step -->
    <div style="position: absolute; top: 10%;">
      <button class="next-btn" @click="onClickNextBtn">Next Step</button>
    </div>


    <!-- Modal part -->
    <div
      v-if="isModalOpen"
      class="modal"
      >
      <Modal
        :nowPlace="nowPlace"
        :isModalOpen="isModalOpen"
        @close-modal="closeModal"
      />
    
    </div>



  </div>
</template>

<script>
import * as VueGoogleMaps from 'vue2-google-maps'
import Vue from 'vue'
import '../../components/css/createJourney/createplace.scss'
import vClickOutside from 'v-click-outside'
import Modal from './Modal.vue'
import EachPolyline from '../../components/createJourney/Polyline.vue'

Vue.use(VueGoogleMaps, {
  load: {
    key: "AIzaSyBwPv3o2CKXGTbC5qPIjTCdU5F1ToEExio",
    libraries: "places",
    map_id: '6d451fecd5c294f9',
  }
})

export default {

  name: 'createPlace',

  props: {
    images: Array,
    travel: Object,
  },

  components: {
    Modal, EachPolyline,
  },

  directives: {
    clickOutside: vClickOutside.directive
  },

  data() {
    return {
      center: {
        lat: 37.5,
        lng: 127,
      },
      options : {
        mapId: "6d451fecd5c294f9"
      },
      // icon 모음
      icons:  {
        basic: {
          url: require("../../assets/pins/basicpin.png"),
          scaledSize: {width: 28, height: 28},
          labelOrigin: {x: 16, y: -10},
        },
        hotel: {
          url: require("../../assets/pins/hotel-pin.png"),
          scaledSize: {width: 28, height: 28},
          labelOrigin: {x: 16, y: -10},
        },
        res: {
          url: require("../../assets/pins/restaurant-pin.png"),
          scaledSize: {width: 28, height: 28},
          labelOrigin: {x: 16, y: -10},
        },
        activity: {
          url: require("../../assets/pins/activity-pin.png"),
          scaledSize: {width: 28, height: 28},
          labelOrigin: {x: 16, y: -10},
        },
      },
      // for marker test
      marker: {
        position: {
          lat: 37.5,
          lng: 127,
        },
        modal: false,
      },
      // 데이터 입력 확인용 임시
      markerData: {
        title: '',
        category: {
          main: '',
          sub: '',
        },
        rates: {
          rate1: 0,
          rate2: 0,
          rate3: 0,
        },
        comment: '',
        images: [
          {
            url: 'https://www.penguintravel.com/uploads/articleThumb/Th5file_8233.jpg',
          }
        ],
      },
      // 어떤 카테고리 골랐는지
      whichCategory: '',
      // polyline test
      paths: [
            // {lat: 55.9352001, lng: -4.7766924 },
            // {lat: 55.9358131, lng: -4.7770143 },
            // {lat: 55.9361256, lng: -4.7767353 },
            // {lat: 37.9366784, lng: -4.7739458 }
      ],
      path: [],
      category: {
        main: '',
        sub: '',
      },
      categoryBind: {
        hotel: false,
        res: false,
        activity: false,
      },
      // 내려보낼 데이터
      nowPlace: {},
      // 모달 열고 닫기용
      isModalOpen: false,
    }
  },

  // DOM 에 표시 전에 polyline 을 위한 path 데이터 뽑아내야함
  created() {
    // 필요에 따라 (아마도 해야할듯...?) 시간 순으로 images 정렬해야함
    for (let image of this.images) {
      const position = new Object()
      position.lat = image.position.lat
      position.lng = image.position.lng
      this.path.push(position)
    }

  },

  // mounted() {
  //   for (let i = 0; i < this.travel.placeList.length - 1; i++ ) {
  //     const forPath = new Object()
  //     const position = new Array()
  //     position.push(this.travel.placeList[i].position)
  //     position.push(this.travel.placeList[i + 1].position)
  //     forPath.position = position
  //     forPath.transport = ''
  //     console.log(forPath)
  //     this.paths.push(forPath)
  //     console.log(this.paths)
  //   }
  // },
  // watch: {
  //   travel: function() {
  //     for (let i=0; i<travel.placeList.length() - 1; i++) {
        
  //     }
  //   },
  // },

  methods: {
    openModal(nowPlace) {
      console.log('asdf')
      this.nowPlace = nowPlace
      this.isModalOpen = true
    },

    clickBasicPin() {
      this.marker.modal = true
    },

    closeModal() {
      this.isModalOpen = false
    },

    clickBtn(category) {
      this.whichCategory = category
      this.category.main = category
      this.categoryBind[category] = true
      for (let key in this.categoryBind) {
        if (key != category) { this.categoryBind[key] = false }
      }
    },

    onClickNextBtn() {
      console.log('asdf')
      this.$emit('next-step')
    },
  }
}
</script>