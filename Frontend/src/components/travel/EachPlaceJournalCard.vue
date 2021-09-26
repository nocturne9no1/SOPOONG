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
    @click="travelDetail()"
  >
    <div class="image">
      <img :src="`https://i5a404.p.ssafy.io/api/image/${image.imageOriginTitle}`" class="image" :class="{ imgBlur : imgHover }" :style="{ width: `${imgSize()}%` }" @mouseover="imgHoverCheck()" @mouseout="mouseOutCheck()" />
    </div>
    <div class="patchDeleteDiv" v-if="(this.$store.getters['isSignedIn']) && (userId === this.$store.getters['getUserProfile'].userId)">
      <!-- v-bind는 false값도 true로 인식? -->
      <i class="fas fa-eraser" v-show="imgHover" style="margin-right: 10px"></i>
      <i class="far fa-trash-alt" v-show="imgHover" @click="deleteTravelDetail(image.placeIdx)"></i>
    </div>
    
    <div class="textDiv" v-show="imgHover">
      <h2>{{ image.placeTitle }}</h2>
      <p v-if="image.placeComment">
        {{ image.placeComment }}  
      </p>
      <p v-else>
        여기는 개별 일지 확인하는 여행 일지 디테일!
      </p>
      <!-- 별점 -->
      <span class="fa fa-star" :class="{ checked : checkRating(1) }"></span>
      <span class="fa fa-star" :class="{ checked : checkRating(2) }"></span>
      <span class="fa fa-star" :class="{ checked : checkRating(3) }"></span>
      <span class="fa fa-star" :class="{ checked : checkRating(4) }" ></span>
      <span class="fa fa-star" :class="{ checked : checkRating(5) }"></span>
    </div>
  </div>
</template>

<script>
// import { mapGetters } from 'vuex'

export default {
  props: {
    image: {
      type: Object,
      required: true,
    },
    userId: {
      type: String,
    }
  },

  data: () => ({
    gap: null,
    tH: null,
    imgHover: false,
    lt: 100,
  }),

  watch: {
    image: {
      deep: true,
      immediate: true,
      handler() {
        // 이미지 삭제 등 변동이 일어난 경우 이미지 사이즈 재측정 위함.
        this.getImageSize()
      }
    }
  },

  created() {
    this.getImageSize();
    console.log(this.image);
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
      let avg = Math.round((this.image.placeRates.rate1 + this.image.placeRates.rate2 + this.image.placeRates.rate3) / 3)
      if (x > avg) {
        return false
      }
      else return true
    },

    // 일지 디테일로 연결
    travelDetail() {
      // this.$router.push('/')
      console.log(this.image)
      // 멀티플 파라미터 쏘고 싶을 때
      // this.$store.dispatch('', [this.image])
    },

    deleteTravelDetail(placeIdx) {
      this.$store.dispatch('deleteTravelDetail', placeIdx)
    },

    getImageSize() {
      this.tH = Math.round(this.image.imageHeight / (this.image.imageWidth / 300));
      const gap = Math.round(this.tH / 10);
      this.gap = `span ${gap}`;
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
/* 프로필 */
.profileDiv {
  position: absolute;
  top: 1.2rem;
  left: 1rem;

  width: 100%;

  text-align: left;
}

/* 수정과 삭제 */
.patchDeleteDiv {
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

.fa-eraser:hover {
  color: pink;
  text-shadow: rgb(241, 157, 199) 1px 0 10px;
}


/* 이미지 위 출력 div */
.textDiv {
  position: absolute;
  bottom: 5%;

  width: 100%;

  text-align: center;

  color: white;
  font-weight: bold;

  cursor: pointer;
}

/* 글 div위에 mouse hover시에도 작동할 수 있도록 */
.imgBlur {
  filter: blur(4px);
  opacity: .7;
}

h2 {
  /* font-size: 3vh; */
  margin-bottom: 10px;
}
/* h2::after {
  content: " ★";
} */

p {
  text-align: center;

  /* 좌우패딩 */
  padding-inline: 20px;
  margin-top: 0;
  margin-bottom: 0;
  /* 한줄 */
  /* white-space: nowrap; */
  /* 한줄 마지막 줄임말 부분 처리 */
  /* text-overflow: ellipsis; */


  /* 여러줄 */
  white-space: normal;
  /* line-height 배수로 몇줄 지정 */
  height: 1em;
  line-height: 1;
  
  /* 여러줄 숨긴 부분 처리 */
  display: -webkit-box; 
  -webkit-line-clamp: 1; 
  -webkit-box-orient: vertical;

  /* 기준 넘는 글자 숨기기 */
  overflow: hidden; 
}

/* 별점 */
.fa-star {
  color: gray;
}
.checked {
  color: orange;
}


</style>