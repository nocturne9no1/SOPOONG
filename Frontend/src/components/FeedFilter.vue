<template>
  <div class="feedFilterDiv">
    <span>
      <i
        class="fas fa-filter feedFilterIcon"
        @click="onFilter()"
        style="color:#B7DCCC;"
      ></i>
    </span>
    <div v-show="filter">
      <div class="feedFilterDateDiv">
        <h4>
          날짜
        </h4>
      </div>
      <br />
      <div class="feedFilterCategoryDiv">
        <h4>
          카테고리
        </h4>
        <div class="feedFilterCheckboxDiv">
          <label 
            >
            <input v-model="feedFilter.categories" type="checkbox" checked="checked" value="숙소"/>
            숙소
          </label>
          <label
            >
            <input v-model="feedFilter.categories" type="checkbox" value="식당"/>
            식당
          </label>

          <label
            >
            <input v-model="feedFilter.categories" type="checkbox" value="활동"/>
            활동
          </label>
        </div>
      </div>
      <div class="feedFilterRatingDiv">
        <h4>별점</h4>
        <div style="margin:5px">
          <input v-model="feedFilter.rating" type="radio" style="" name="ratingRadio" value="">
          <i class="fas fa-star" style="color:orange;"></i>
          상관없음
        </div>
        <div style="margin:5px">
          <input v-model="feedFilter.rating" type="radio" style="" name="ratingRadio" value=1>
          <i class="fas fa-star" style="color:orange;"></i>
          1점 이상
        </div>
        <div style="margin:5px">
          <input v-model="feedFilter.rating" type="radio" style="" name="ratingRadio" value=2>
          <i class="fas fa-star" style="color:orange;"></i>
          2점 이상
        </div>
        <div style="margin:5px">
          <input v-model="feedFilter.rating" type="radio" style="" name="ratingRadio" value=3>
          <i class="fas fa-star" style="color:orange;"></i>
          3점 이상
        </div>
        <div style="margin:5px">
          <input v-model="feedFilter.rating" type="radio" style="" name="ratingRadio" value=4>
          <i class="fas fa-star" style="color:orange;"></i>
          4점 이상
        </div>
        <div style="margin:5px">
          <input v-model="feedFilter.rating" type="radio" style="" name="ratingRadio" value=5>
          <i class="fas fa-star" style="color:orange;"></i>
          5점 이상
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "FeedFilter",
  components: {},
  data() {
    return {
      sampleData: "",
      feedFilter: {
        categories: [],
        rating: Number,
      },
      filter: false,
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
  methods: {
    onFilter() {
      this.filter = !this.filter;
    },
  },
};
</script>

<!-- scoped로 사용시 newsfeed 내에서 스타일 안먹음.-->
<style>
.feedFilterDiv {
  display: block;
  margin-top: 15rem;
  margin-right: 5%;
  width: 280px;
  height: 430px;
  padding: 20px;
  border-radius: 12px;
  box-shadow: 3px 3px 10px rgba(gray, 0.5);

  font-family: helvetica;
}

.feedFilterIcon {
  cursor: pointer;
  font-size: 3ex;
}

.feedFilterRatingDiv {
  font-weight: 600;
}
</style>
