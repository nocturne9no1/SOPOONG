<template>
  <div class="createjourney-wrap">
    <!-- 현재 스텝 표시 -->
    <div class="now-step-wrap">
      <div class="circles">
        <div class="step-wrap">
          <div class="circle step-1"></div>
          <div class="words">
              <p>step 1. upload photos</p>
          </div>
        </div>
        <div class="step-wrap">
          <div class="circle step-2"></div>
          <div class="words">
            <p>step 2. wirte journey</p>
          </div>
        </div>
        <div class="step-wrap">
          <div class="circle step-3"></div>
          <div class="words">
              <p>step 3. package journey</p>
          </div>
        </div>
      </div>
      <!-- <div class="words">
        
      </div> -->
      <!-- {{nowStep}} -->
    </div>
    <div>
      <UploadImage
        v-if="nowStep === 1"
        @next-step="nextStep"
        @upload-image="uploadImage"
      />
      <CreatePlace
        v-else-if="nowStep === 2"
        :images="images"
        :travel="travel"
        @next-step="nextStep"
      />
      <PackageJourney
        v-else-if="nowStep === 3"
        :images="images"
        :travel="travel"
      />
    </div>
  </div>
</template>
<script>
import UploadImage from './UploadImage.vue'
import CreatePlace from './CreatePlace.vue'
import PackageJourney from './PackageJourney.vue'
import '../../components/css/createJourney/createjourney.scss'


export default {
  name:'createJourney',
  components: {
    UploadImage, CreatePlace, PackageJourney,
  },
  data() {
    return {
      // 최종적으로 서버에 넘길 자료
      travel: {
        travelTitle: '',
        travelContent: '',
        travelIsVisible: 1,
        placeList: [],
        imageIdx: '',
        travelLat: '',
        travelLng: '',
        mainImage: '',
      },

      // 1. image upload 이후 2. markerDetail로 넘기기 전에 and 3. 마무리 에서 쓸 사진 자료 유통 창고
      images: [],

      // 현재 몇 단계인지 알려줄 변수
      nowStep: 1,
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
    // 새로운 이미지 업로드 될 시마다 marker 정보 생성
    images: function () {
      // 아마 업로드 시마다 시간 순 정렬을 해줘야 할 듯함
      // images 배열에서 각 image 에 대한 marker 정보 생성
      for (let image of this.images) {
        console.log('hi')
        console.log(image)
        const marker = new Object()

        marker.title = ''
        marker.comment = ''
        marker.category = {
          main: '',
          sub: '',
        }
        marker.rates = {
          rate1: 0,
          rate2: 0,
          rate3: 0,
        }
        marker.transport = ''
        marker.imageIdx = 0
        marker.visitDate = image.dateTime
        marker.position = {
          lat: image.position.lat,
          lng: image.position.lng
        }
        const eachImage = new Object
        eachImage.file = image.file
        eachImage.position = image.position
        eachImage.isPlaceLeader = true
        eachImage.isTravelLeader = false
        eachImage.preview = image.preview
        eachImage.dateTime = image.dateTime
        eachImage.imageWidth = image.imageData.ExifImageWidth
        eachImage.imageHeight = image.imageData.ExifImageHeight
        marker.imageList = [
          eachImage
        ]

        marker.imageNum = image.number

        this.travel.placeList.push(marker)

      }
    }

  },

  methods: {
    nextStep() {
      this.nowStep += 1
    },

    uploadImage(images) {
      this.images = images
    },


  }
}
</script>