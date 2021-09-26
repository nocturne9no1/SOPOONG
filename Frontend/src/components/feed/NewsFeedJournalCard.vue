<template>
  <!-- 클릭 div가 아닌 다른곳으로 옮겨줘야할듯. -->

  <div
    class="card"
    :style="{
      height: tH + 'px',
      gridRowEnd: gap,
      borderRadius: '20px',
    }"
    @mouseover="imgHoverCheck()"
    @mouseout="mouseOutCheck()"

  >
    <div class="image" @click="journalDetail()">
      <img :src="`https://i5a404.p.ssafy.io/api/image/${image.imageOriginTitle}`" class="image" :class="{ imgBlur : imgHover }" :style="{ width: `${imgSize()}%` }" @mouseover="imgHoverCheck()" @mouseout="mouseOutCheck()" />
    </div>
    <div class="profilePhotoDiv" @click="personalPage()">
          <img class="photo" :src="`https://i5a404.p.ssafy.io/api/image/${image.profileOriginTitle}`" alt="profile-photo">
    </div>
    <div class="nickFollowDiv">
      <p>{{ image.userNickname }}</p>
      <!-- 들고있는 자료 쏴주는법 참고! -->
      <div v-if="(this.$store.getters['isSignedIn'] ) && (image.userId !== this.$store.getters['getUserProfile'].userId)">
        <button v-if="!image.isFollow" @click="toFollow(image.userId)" class="followButton">follow</button>
        <button v-if="image.isFollow" @click="toFollow(image.userId) " class="unfollowButton">following</button>
      </div>
    </div>
    <div class="likeBookMarkDiv" v-if="(this.$store.getters['isSignedIn']) && (image.userId !== this.$store.getters['getUserProfile'].userId)">
      <!-- v-bind는 false값도 true로 인식? -->
      <i class="far fa-bookmark" :class="{ 'fas': image.isScrap, 'fas-bookmark': image.isScrap }" @click="scrapedJournal(image.travelIdx)" v-show="imgHover" style="margin-right: 10px"></i>
      <i class="far fa-heart" :class="{ 'fas': image.isLike, 'fas-heart': image.isLike }" @click="likedJournal(image.travelIdx)" v-show="imgHover"></i>
    </div>
    <!-- <div class="deleteDiv" v-if="(this.$store.getters['isSignedIn']) && (image.userId === this.$store.getters['getUserProfile'].userId)">
      <i class="far fa-trash-alt" v-show="imgHover" @click="deleteTravel(travel.travelIdx)"></i>
    </div> -->
    <div class="textDiv" v-show="imgHover" @click="journalDetail()">
      <h1>{{ image.travelTitle }}</h1>
      <p class="travelReview">{{ image.travelContent }}</p>
    </div>
    
  </div>


  
</template>

<script>
// import { mapActions } from 'vuex';
// import { mapGetters } from 'vuex';
import axios from 'axios';

export default {
  props: {
    image: {
      type: Object,
      required: true,
    },
  },

  data: () => ({
    gap: null,
    tH: null,
    imgHover: false,
    lt: 100,

    scraped: false,
  }),

  created() {
    this.tH = Math.round(this.image.imageHeight / (this.image.imageWidth / 400));
    const gap = Math.round(this.tH / 10);
    this.gap = `span ${gap}`;

  },

  mounted() {
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
    // 좋아요 버튼 토글 구현. 게시글 내에 좋아요 저장 구현해야 함.
    likedJournal(travelIdx) {
      // this.liked = !this.liked
      // if (this.image.isLike === 1) {
      //   this.image.isLike = 0
      // } else {
      //   this.image.isLike = 1
      // }
      // 데이터 보내서 수정해야 함.
      // this.$store.dispatch('like', [this.$store.getters['getUserProfile'].userId, travelIdx]);
      this.$emit('onLike', [this.$store.getters['getUserProfile'].userId, travelIdx])
    },

    // 일단 없는 기능
    scrapedJournal(travelIdx) {
      // this.scraped = !this.scraped
      this.$emit('onScrap', [this.$store.getters['getUserProfile'].userId, travelIdx])
    },

    // Okay !
    journalDetail() {
      console.log(this.image)
      this.$store.dispatch('travelDetail', this.image)
    },

    // 팔로우
    toFollow(userId) {
      this.isFollowing = !this.isFollowing;
      // 팔로우하기
      // this.$store.dispatch('follow',[this.$store.getters['getUserProfile'].userId, userId]);
      // console.log(this.$store)
      this.$emit('onFollow', [this.$store.getters['getUserProfile'].userId, userId])
    },

    // 내 일지는 삭제 가능하도록
    deleteTravel(travelIdx) {
      this.$store.dispatch('deleteTravel', travelIdx)
    },

    async personalPage() {
      await axios.get('travel/travelList', { params: {userId : this.image.userId}, headers: { 'X-AUTH-TOKEN' : this.$store.getters['getToken'], 'Access-Control-Allow-Origin': '*' } })
      .then(res => {
        this.$store.commit("SET_PERSONAL_TRAVEL_JOURNAL", res.data.data)
        axios.get('user', { params: {id: this.image.userId}, headers: { 'X-AUTH-TOKEN' : this.$store.getters['getToken'], 'Access-Control-Allow-Origin': '*' } })
          .then(res => {
            this.$store.commit("SET_PERSONAL_PROFILE", res.data.data.success);
            if ( this.image.userId === this.$store.getters['getUserProfile'].userId ) {
              this.$router.push('/main')
            } else {
              this.$router.push(`/travellist/${this.image.userId}`)
            }
          })
      })
    },

    welcome() {
      alert('회원가입 하시고 소풍의 컨텐츠를 즐겨보세요 ! ')
      this.$router.push('/signup')
    }

  }
};
</script>

<style scoped>
.card {
  position: relative;
  
  background-color: gray;
  box-shadow: rgba(0, 0, 0, 0.15) 1.95px 1.95px 2.6px;

}
.image > img {
  border-radius: 20px;
}

.image > img:hover {
  cursor: pointer;
  filter: blur(4px);
  opacity: .7;
}

/* 프로필 이미지 */
.profilePhotoDiv {
  position: absolute;
  top: 1rem;
  left: 1rem;

  /* width: 100%; */
  cursor: pointer;
  text-align: left;
  z-index: 5;
}

.profilePhotoDiv > .photo {
  width: 50px;
  height: 60px;
  border-radius: 70%;
  overflow: hidden;
  position: relative;
  /* display: flex; */

  top : 100%;

  /* width: 100%;
  height: 100%;
  object-fit: cover; */
}

/* 닉네임 + 팔로우 Div */
.nickFollowDiv {
  position: absolute;
  top: 0.7rem;
  left: calc(80px);
  /* backdrop-filter: blur(10px); */
}

.nickFollowDiv > p {
  max-width: 200px;
  margin-block: 10px 5px;

  backdrop-filter: blur(2px);
  /* background-color: rgba(80, 110, 97, 0.1); */

  font-size: 18px;
  font-family: 'Do Hyeon';
  color: #F7F8F4;
  /* text-shadow: 3px 2px 2px #F2F1F6; */
  
  /* color: rgba(237, 119, 102, 1); */
}

/* 팔로우 버튼 */
.followButton {
  width: 75px;
  height: 20px;
  
  border: none;
  border-radius: 10px;

  color: #F7F8F4;
  font-family: 'Do Hyeon';
  background-color: rgba(183, 220, 204, 1);

  cursor: pointer;
}

.unfollowButton {
  width: 75px;
  height: 20px;
  
  border: none;
  border-radius: 10px;

  color: #F7F8F4;
  font-family: 'Do Hyeon';
  background-color: rgba(237, 119, 102, 1);

  cursor: pointer;
}

/* 좋아요, 북마크 */
.likeBookMarkDiv {
  position: absolute;
  top: 1.5rem;
  right: 1rem;

  /* width: 100%; */
  /* height: 60px; */

  text-align: right;

  font-size: 2ex;
  color: white;

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

.fas-heart {
  color: rgba(237, 119, 102, 1);
  text-shadow: rgb(209, 48, 48) 1px 0 10px;
}

.fas-bookmark {
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

  /* transform: translateY(calc(70px + 1em));
  transition: all 1s; */
}

/* .textDiv:hover {
  transform: translateY(0);
  transition: all 1s;
} */


/* 글 div위에 mouse hover시에도 작동할 수 있도록 */
.imgBlur {
  filter: blur(4px);
  opacity: .7;
}

/* h1::after {
  content: "  ♥";
} */

/* 여행글 */
.travelReview {
  text-align: center;

  /* 좌우패딩 */
  padding-inline: 20px;

  /* 한줄 */
  /* white-space: nowrap; */
  /* 한줄 마지막 줄임말 부분 처리 */
  /* text-overflow: ellipsis; */


  /* 여러줄 */
  white-space: normal;
  /* line-height의 배수 = height로 몇줄 지정 */
  height: 2em;
  line-height: 1;
  
  /* 여러줄 숨긴 부분 처리 + clamp가 height와 같아야 ...이 제대로 출력*/
  display: -webkit-box; 
  -webkit-line-clamp: 2; 
  -webkit-box-orient: vertical;

  /* 기준 넘는 글자 숨기기 */
  overflow: hidden; 
}
</style>