<template>
  <div class="travelmap">
    <div class="header">
      <div class="travel-title">
        <h1>{{travel.travelTitle}}</h1>
      </div>
      <div>
        <button class="btn" @click="clickEdit" style="margin: 20px;">Edit</button>
      </div>
    </div>

    <GmapMap
        ref="mapRef"
        :center="path[0]"
        :zoom="14"
        :options="options"
        class="google-map"
    >
      <div v-for="(nowPlace, idx) in markers" :key="idx">
        <!-- 현재 카테고리에 따른 icon 랜더링 -->
        <div v-if="nowPlace.category.main === 'hotel'">
          <gmap-marker
            :position="nowPlace.position"
            :clickable="true"
            :icon="icons.hotel"
            @click="openWindow(nowPlace)"
          >
            <gmap-info-window 
                @closeclick="nowPlace.windowOpen=false" 
                @click="openModal(nowPlace)"
                :opened="nowPlace.windowOpen"
                :position="nowPlace.position"
                :options="{
                  pixelOffset: {
                  width: 0,
                  height: 0
                }
              }"
              >
                <div class="window-wrap" @click="openModal(nowPlace)">
                  <img :src="nowPlace.imgUrl" alt="window-image" class="window-image">
                  <div class="content">
                    <span>{{nowPlace.title}}</span>
                    <span><i class="fas fa-star" style="color: #ED7766"></i> {{nowPlace.placeRate}}</span>
                    <!-- <p>{{nowPlace.comment}}</p> -->
                  </div>
                </div>
            </gmap-info-window>
          </gmap-marker>
        </div>
        <div v-else-if="nowPlace.category.main === 'res'">
          <gmap-marker
            :position="nowPlace.position"
            :clickable="true"
            :icon="icons.res"
            @click="openWindow(nowPlace)"
          >
            <gmap-info-window 
                @closeclick="nowPlace.windowOpen=false" 
                @click="openModal(nowPlace)"
                :opened="nowPlace.windowOpen"
                :position="nowPlace.position"
                :options="{
                  pixelOffset: {
                  width: 0,
                  height: 0
                }
              }"
              >
                <div class="window-wrap" @click="openModal(nowPlace)">
                  <img :src="nowPlace.imgUrl" alt="window-image" class="window-image">
                  <div class="content">
                    <span>{{nowPlace.title}}</span>
                    <span><i class="fas fa-star" style="color: #ED7766"></i> {{nowPlace.placeRate}}</span>
                    <!-- <p>{{nowPlace.comment}}</p> -->
                  </div>
                </div>
            </gmap-info-window>
          </gmap-marker>
        </div>
        <div v-else-if="nowPlace.category.main === 'activity'">
          <gmap-marker
            :position="nowPlace.position"
            :clickable="true"
            :icon="icons.activity"
            @click="openWindow(nowPlace)"
          >
            <gmap-info-window 
                @closeclick="nowPlace.windowOpen=false" 
                :opened="nowPlace.windowOpen"
                :position="nowPlace.position"
                :options="{
                  pixelOffset: {
                  width: 0,
                  height: 0,
                  }
                }"
                class="info-window"
              >
                <div class="window-wrap" @click="openModal(nowPlace)">
                  <img :src="nowPlace.imgUrl" alt="window-image" class="window-image">
                  <div class="content">
                    <span>{{nowPlace.title}}</span>
                    <span><i class="fas fa-star" style="color: #ED7766"></i> {{nowPlace.placeRate}}</span>
                    <!-- <p>{{nowPlace.comment}}</p> -->
                  </div>
                </div>
            </gmap-info-window>
          </gmap-marker>
        </div>
        <!-- 아직 아무 카테고리도 선택 안했을 때 -->
        <div v-else>
          <gmap-marker
            :position="nowPlace.position"
            :clickable="true"
            :icon="icons.basic"
            @click="openModal(nowPlace)"
          >
          </gmap-marker>
        </div>
        

      </div>

      <!-- 
        모달여는거 -> gmap-marker 에 들어감
            @click="openModal(nowPlace)"
            
             -->
      <!-- <div v-for="(eachPath, idx) in path" :key="idx">
        <EachPolyline
          :eachPath="eachPath"
        />
      </div> -->
      <gmap-polyline v-bind:path.sync="path" v-bind:options="{ strokeColor:'#000000'}"/> 
    </GmapMap>
    <Modal
      :nowPlace="nowPlace"
      :isModalOpen="isModalOpen"
      @close-modal="closeModal"
      v-if="isModalOpen"
    />
    <div class="info-section">
      <div class="comment">
        {{ travel.travelContent }}
      </div>
      <div class="date">
        DATE : {{ travel.startDate.substr(0, 10)}} - {{ travel.endDate.substr(0, 10) }}
      </div>
    </div>
  </div>
</template>
<script>
import * as VueGoogleMaps from 'vue2-google-maps'
import Vue from 'vue'
import '../../components/css/travelmap.scss'
import Modal from '../../components/travel/Modal.vue'
import vClickOutside from 'v-click-outside'

Vue.use(VueGoogleMaps, {
  load: {
    key: "AIzaSyBwPv3o2CKXGTbC5qPIjTCdU5F1ToEExio",
    libraries: "places",
    map_id: '6d451fecd5c294f9',
  }
})

export default {
  name:'TravelMap',
  components: {
    Modal,
  },

  directives: {
    clickOutside: vClickOutside.directive
  },

  data() {
    return {
      travel: '',
      images: '',

      options : {
        mapId: "6d451fecd5c294f9"
      },

      icons:  {
        basic: {
          url: require("../../assets/pins/basicpin.png"),
          scaledSize: {width: 45, height: 45},
          labelOrigin: {x: 16, y: -10},
        },
        hotel: {
          url: require("../../assets/pins/hotel-pin.png"),
          scaledSize: {width: 45, height: 45},
          labelOrigin: {x: 16, y: -10},
        },
        res: {
          url: require("../../assets/pins/restaurant-pin.png"),
          scaledSize: {width: 45, height: 45},
          labelOrigin: {x: 16, y: -10},
        },
        activity: {
          url: require("../../assets/pins/activity-pin.png"),
          scaledSize: {width: 45, height: 45},
          labelOrigin: {x: 16, y: -10},
        },
      },

      path: [],

      center: '',

      markers: [],

      isModalOpen: false,

      // component 간 통신용
      nowPlace: '',
    };
  },
  beforeCreate() {},
  created() {
    for (let x of this.$store.getters['getMyTravelJournal']) {
      if (x.travelIdx === this.$store.getters['getPresentTravelIdx']) {
        this.travel = x
      }
    }
    this.images = this.$store.getters['getTravelDetail']
    
    // 경로 보여주기
    for (let image of this.images) {
      const position = new Object()
      position.lat = image.placeLat
      position.lng = image.placeLong
      this.path.push(position)

      const marker = new Object()
      marker.title = image.placeTitle
      marker.transport = image.placeTransport
      marker.visitDate = image.placeVisitDate
      marker.position = position
      marker.category = image.placeCategory
      marker.comment = image.placeComment
      marker.windowOpen = false,
      marker.imgUrl = 'https://i5a404.p.ssafy.io/api/image/'+ image.imageOriginTitle
      const avgRate = (image.placeRates.rate1 + image.placeRates.rate2 + image.placeRates.rate3) / 3
      marker.placeRate = avgRate.toFixed(1)
      marker.placeRates = image.placeRates
      marker.rates = image.placeRates
      marker.placeIdx = image.placeIdx
      this.markers.push(marker)
    }

    // 중심 좌표
    this.center = this.path[0]
  },
  beforeMount() {}, 
  mounted() {}, 
  beforeUpdate() {},
  updated() {},
  beforeUnmount() {}, 
  unmounted() {},
  methods: {
    openWindow(m) {
      m.windowOpen = true
    },

    openModal(nowPlace) {
      // console.log('asdf')
      this.nowPlace = nowPlace
      this.isModalOpen = true
    },

    closeModal() {
      this.isModalOpen = false
    },

    clickEdit() {
      this.$store.dispatch('nowTravel', this.travel)
      this.$router.push({name: 'TravelEdit'})
    },

  }
}
</script>