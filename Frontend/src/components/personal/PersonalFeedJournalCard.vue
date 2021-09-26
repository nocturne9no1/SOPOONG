<template>
  <div
    class="card"
    :style="{
      height: tH + 'px',
      gridRowEnd: gap,
      backgroundColor: 'gray',
      borderRadius: '20px',
    }"
    @mouseover="imgHoverCheck()"
    @mouseout="mouseOutCheck()"
  >
    <div class="image" @click="journalDetail()">
      <img :src="imgSrc" class="image" :class="{ imgBlur : imgHover }" :style="{ width: `${imgSize()}%` }" @mouseover="imgHoverCheck()" @mouseout="mouseOutCheck()" />
    </div>
    <!-- <div class="deleteDiv">
      <i class="far fa-trash-alt" v-show="imgHover" @click="deleteTravel(travel.travelIdx)"></i>
    </div> -->
    <div class="textDiv" v-show="imgHover" @click="journalDetail()">
      <h1>{{ travel.travelTitle }}</h1>
      <p>
        {{ travel.travelComment }}
      </p>

    </div>
  </div>
</template>

<script>
// import { mapGetters } from 'vuex'
// import router from '@/router'

export default {
  props: {
    // image: {
    //   type: Object,
    //   required: true,
    // },
    travel: Object,
  },

  data: () => ({
    gap: null,
    tH: null,
    imgHover: false,
    lt: 100,
    liked: false,
  }),

  created() {
    console.log(this.travel)
    this.tH = Math.round(this.travel.imageHeight / (this.travel.imageWidth / 400));
    const gap = Math.round(this.tH / 10);
    this.gap = `span ${gap}`;
  },
  computed: {
    imgSrc() {
      return 'https://i5a404.p.ssafy.io/api/image/'+this.travel.imageOriginTitle
    }
  },
  methods: {
    imgHoverCheck() {
      this.imgHover = true;
    },
    mouseOutCheck() {
      this.imgHover = false;
    },
    imgSize() {
      this.lt = 100
      return this.lt
    },
    // 별점을 백에서 받아와 정보 비교할 예정.
    checkRating(x) {
      if (x >= this.$store.getters['getRating']) {
        return 1
      }
      else return 1
    },

    // 일지 디테일로 연결
    journalDetail() {
      // 멀티플 파라미터 쏘고 싶을 때
      this.$store.dispatch('travelDetail', this.travel)
    },

    // 여행일지 삭제
    deleteTravel(travelIdx) {
      this.$store.dispatch('deleteTravel', travelIdx)
    }
  }
};
</script>

<style scoped>

.card {
  position: relative;
}
img {
  border-radius: 20px;
}

img:hover {
  cursor: pointer;
  filter: blur(4px);
  opacity: .7;
}

/* 이미지 위 출력 div */
.textDiv {
  position: absolute;
  bottom: 5%;

  width: 100%;
  /* height: 100%; */

  text-align: center;

  color: white;
  font-weight: bold;

  cursor: pointer;
}

.deleteDiv {
  position: absolute;
  top: 1.5rem;
  right: 1rem;

  /* width: 100%; */
  /* height: 60px; */

  text-align: right;

  font-size: 2ex;
  color: white;

  z-index: 1000;

}

i {
  cursor: pointer;
}

.fa-trash-alt:hover {
  color: red;
  text-shadow: rgb(209, 48, 48) 1px 0 10px;
}

/* 글 div위에 mouse hover시에도 작동할 수 있도록 */
.imgBlur {
  filter: blur(4px);
  opacity: .7;
}
/* 
h1::after {
  content: "  ♥";
} */
</style>

