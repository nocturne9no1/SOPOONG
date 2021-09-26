<template>
  <div style="display: flex;">
    <div class="journalCardList">
      <div class="wrapper">
        <div class="heading">
          <h1>{{ travel.travelTitle }}</h1>
          <!-- <button class="filterButton">Filter</button> -->
        </div>
        <div class="cards" v-if="images.length">
          <each-place-journal-card
            v-for="image in images"
            :key="image.id"
            :image="image"
            :userId="userId"
          />
        </div>
        <div class="cards-loading" v-else>
          Loading image....
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import EachPlaceJournalCard from "@/components/travel/EachPlaceJournalCard.vue";
// import router from '@/router'
// import axios from "axios";

export default {
  name: "",
  components: {
    EachPlaceJournalCard,
  },
  data() {
    return {
      sampleData: "",
      travel: null,
      images: [],
      userId: '',
    };
  },
  watch: {
    images: {
      deep: true,
      immediate: true,
      handler() {
        // 이미지들에 변동이 있을 경우(삭제 등), 다시 이미지 카드들 가져오기 위함.
        this.getTravelJournalDetail()
      }
    }
  },

  beforeCreate() {},
  async created() {
    this.getTravelJournalDetail();
  },
  beforeMount() {},
  mounted() {},
  beforeUpdate() {},
  updated() {},
  beforeUnmount() {},
  unmounted() {},
  methods: {
    getTravelJournalDetail() {
      // 뉴스피드에서 온거면
      // if ( router.history._startLocation === '/feed' ) {
      //   for (let x of this.$store.getters['getAllFeedsList']) {
      //     if (x.travelIdx === this.$store.getters['getPresentTravelIdx']) {
      //       this.travel = x
      //       console.log(this.travel)
      //     }
      //   }
      // } else if ( router.history._startLocation === '/feed/follow') {
      //   for (let x of this.$store.getters['getAllFeedsList']) {
      //     if (x.travelIdx === this.$store.getters['getPresentTravelIdx']) {
      //       this.travel = x
      //       console.log(this.travel)
      //     }
      //   }
      // } else {
      //   // 현재 여행정보 가져오기 위함.
      //   for (let x of this.$store.getters['getMyTravelJournal']) {
      //     if (x.travelIdx === this.$store.getters['getPresentTravelIdx']) {
      //       this.travel = x
      //     }
      //   }
      // }
      for (let x of this.$store.getters['getAllFeedsList']) {
        if (x.travelIdx === this.$store.getters['getPresentTravelIdx']) {
          this.travel = x
          console.log(this.travel)
          this.userId = this.travel.userId
        }
      }
      // 이미지 가져오기.
      this.images = this.$store.getters['getTravelDetail']
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
  /* text-align: center; */
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
  grid-template-columns: repeat(4, 300px);
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
