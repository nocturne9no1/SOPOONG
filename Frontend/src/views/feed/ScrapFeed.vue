<template>
<div>

  <profile-box />
  <div style="display: flex; justify-content:center;">
    <div class="journalCardList">
      <div class="wrapper">
        <div class="heading">
          <h1>ScrapFeed</h1>
          <!-- <button class="filterButton">Filter</button> -->
        </div>
        <div class="cards" v-if="images.length">
          <following-people-journal-card
            v-for="image in images"
            :key="image.travelIdx"
            :image="image"
            @onFollow="onFollow"
            @onLike="onLike"
            @onScrap="onScrap"
          />
        </div>
        <div class="cards-loading" v-else>
          이미지가 없습니다.
        </div>
      </div>
    </div>
    <!-- <FeedFilter /> -->
  </div>
</div>
</template>

<script>
import FollowingPeopleJournalCard from "@/components/feed/FollowingPeopleJournalCard.vue";
import axios from "axios";
import ProfileBox from '@/views/accounts/ProfileBox.vue';
// import FeedFilter from '@/components/FeedFilter.vue';

export default {
  name: "",
  components: {
    FollowingPeopleJournalCard,
    ProfileBox,
    // FeedFilter,
  },
  data() {
    return {
      sampleData: "",
      images: [],
      like: false,
      likeData: null,
      follow: false,
      followData: null,
      scrap: false,
      scrapData: null,
    };
  },

  watch: {
    follow: {
      deep: true,
      immediate: true,
      async handler() {
        if ( this.followData !==null ) {
          await axios.post('follow', { relationFollowing: this.followData[0], relationFollowed: this.followData[1] }, { headers: { 'X-AUTH-TOKEN' : this.$store.getters['getToken'] }})
              .then(res => {
              console.log(res.data)
          })
          await axios.get('scrap', { params : { page: 0, size: 30, id: this.$store.getters['getUserProfile'].userId }, headers: { 'X-AUTH-TOKEN' : this.$store.getters['getToken'] }})
            .then(res => {
              this.images = res.data.data.success
              this.$store.commit('SET_SCRAP_FEEDS_LIST', res.data.data.success)
            })
          await axios.get('feed/follow', { params: {page: 0, size: 30, userId: this.$store.getters['getUserProfile'].userId}, headers: { 'X-AUTH-TOKEN' : this.$store.getters['getToken'] }})
            .then(res => {
              this.$store.commit('SET_FOLLOWING_PEOPLE_FEEDS_LIST', res.data.data.success)
          })
          // 다른 페이지도 입히기
          await axios.get('feed/all', { params: { page:0, size:30, userId: this.$store.getters['getUserProfile'].userId} })
            .then(res => {
                this.$store.commit('SET_ALL_FEEDS_LIST', res.data.data.success)
          })
        }
      }
    },

    like: {
      deep: true,
      immediate: true,
      async handler() {
        if (this.likeData !== null) {
          await axios.post('good', { userId: this.likeData[0], travelIdx: this.likeData[1] }, { headers: { 'X-AUTH-TOKEN' : this.$store.getters['getToken'] }})
              .then(res => {
                console.log(res.data)
          })
          await axios.get('scrap', { params : { page: 0, size: 30, id: this.$store.getters['getUserProfile'].userId }, headers: { 'X-AUTH-TOKEN' : this.$store.getters['getToken'] }})
            .then(res => {
              this.images = res.data.data.success
              this.$store.commit('SET_SCRAP_FEEDS_LIST', res.data.data.success)
            })
          await axios.get('feed/follow', { params: {page: 0, size: 30, userId: this.$store.getters['getUserProfile'].userId}, headers: { 'X-AUTH-TOKEN' : this.$store.getters['getToken'] }})
              .then(res => {
                this.$store.commit('SET_FOLLOWING_PEOPLE_FEEDS_LIST', res.data.data.success)
            })
            // 다른 페이지도 입히기
          await axios.get('feed/all', { params: { page:0, size:30, userId: this.$store.getters['getUserProfile'].userId} })
            .then(res => {
                this.$store.commit('SET_ALL_FEEDS_LIST', res.data.data.success)
          })
        }
      }
    },

    scrap: {
      deep: true,
      immediate: true,
      async handler() {
        if (this.scrapData !== null) {
          await axios.post('scrap', { userId: this.scrapData[0], travelIdx: this.scrapData[1] }, { headers: { 'X-AUTH-TOKEN' : this.$store.getters['getToken'] }})
            .then(res => {
              console.log(res.data)
            })
            // 스크랩 게시글 가져오기
          await axios.get('scrap', { params : { page: 0, size: 30, id: this.$store.getters['getUserProfile'].userId }, headers: { 'X-AUTH-TOKEN' : this.$store.getters['getToken'] }})
            .then(res => {
              this.images = res.data.data.success
              this.$store.commit('SET_SCRAP_FEEDS_LIST', res.data.data.success)
            })
          await axios.get('feed/follow', { params: {page: 0, size: 30, userId: this.$store.getters['getUserProfile'].userId}, headers: { 'X-AUTH-TOKEN' : this.$store.getters['getToken'] }})
              .then(res => {
                this.$store.commit('SET_FOLLOWING_PEOPLE_FEEDS_LIST', res.data.data.success)
            })
            // 다른 페이지도 입히기
          await axios.get('feed/all', { params: { page:0, size:30, userId: this.$store.getters['getUserProfile'].userId} })
            .then(res => {
                this.$store.commit('SET_ALL_FEEDS_LIST', res.data.data.success)
          })
        }
      }
    }
  },

  beforeCreate() {},
  created() {
    this.getScrapFeedsList();
  },
  beforeMount() {},
  mounted() {
    this.$store.dispatch('scrapFeedsList', this.$store.getters['getUserProfile'].userId)
  },
  beforeUpdate() {},
  updated() {},
  beforeUnmount() {},
  unmounted() {},
  methods: {
    async getScrapFeedsList() {
      this.$store.dispatch('scrapFeedsList', this.$store.getters['getUserProfile'].userId)
      this.images = this.$store.getters['getScrapFeedsList']
      console.log(this.images)
    },

    onFollow(data) {
      this.followData = data
      this.follow = !this.follow
    },

    onLike(data) {
      this.likeData = data
      this.like = !this.like
    },

    onScrap(data) {
      this.scrapData = data
      this.scrap = !this.scrap
    }
    
  },
};
</script>

<style scoped>
/* .journalCardList {
  display: block;
  position: inherit;
} */

.wrapper {
  margin-left: 10rem;
  margin-top: 7rem;
  margin-right: 3rem;
}
.heading h1 {
  text-align: center;
  padding: 0 1rem;
}
.wrapper > div {
  margin: 3rem auto;
}
.wrapper .cards {
  display: grid;
  column-gap: 10px;
  row-gap: 7px;
  grid-template-columns: repeat(3, 400px);
  justify-content: center;
  max-width: var(--card-container-width);
}
.cards-loading {
  font-size: 1.25rem;
  text-align: center;
}

.heading {
  position: relative;
}

/* .filterButton {
  position: absolute;
  right: 2rem;
} */
</style>
