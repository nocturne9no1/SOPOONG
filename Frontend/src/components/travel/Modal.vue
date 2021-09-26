<template>
  <div class="modal">
  <div class="modal-body" v-click-outside="closeModal">
    <div class="image">
      <img :src="nowImage" alt="" class="pic">
    </div>
    <!-- <div class="image-2">
      <img :src="nowImage" alt="" class="pic">
    </div> -->

    <!-- 글 작성 부분 -->
    <div class="text">
      <!-- 제목 -->
      <input v-model="nowPlace.title" type="text" placeholder="Title">
      <!-- 대분류 -->
      <fieldset class="category">
        <legend>Category</legend>
        <div class="category-btns">
          <button @click="clickBtn('hotel')" class="hotel"><img src="../../assets/icon/hotel-icon.png" alt="hotel-icon"></button>
          <button @click="clickBtn('res')" class="res"><img src="../../assets/icon/res-icon.png" alt="res-icon"></button>
          <button @click="clickBtn('activity')" class="activity"><img src="../../assets/icon/activity-icon.png" alt="activity-icon"></button>
        </div>

        <!-- 소분류 -->
        <div class="sub-category">
          <div v-if="whichCategory === 'hotel'" class="select">
            <label for="hotel"></label>
            <select v-model="subCategory" name="hotel-category" id="hotel" @change="onSubCategoryChange($event)">
              <option selected="" hidden="" value="">Where</option>
              <option value="Hotel">Hotel</option>
              <option value="Guest House">Guest House</option>
              <option value="Pension">Pension</option>
              <option value="AirBnB">AirBnB</option>
              <option value="Motel">Motel</option>
              <option value="Etc.">Etc.</option>
            </select>
          </div>
          <div v-else-if="whichCategory === 'res'" class="select" @change="onSubCategoryChange($event)">
            <label for="res"></label>
            <select v-model="subCategory" name="res-category" id="res">
              <option selected="" hidden="" value="">어떤 식당?</option>
              <option value="Food">Food</option>
              <option value="Cafe">Cafe</option>
              <option value="Bar">Bar</option>
              <option value="Etc.">Etc.</option>
            </select>
          </div>
          <div v-else-if="whichCategory === 'activity'" class="select" @change="onSubCategoryChange($event)">
            <label for="activity"></label>
            <select v-model="subCategory" name="activity-category" id="activity">
              <option selected="" hidden="">어떤 활동?</option>
              <option value="Visit">Visit</option>
              <option value="Shopping">Shopping</option>
              <option value="Leisure">Leisure</option>
              <option value="Etc.">Etc.</option>
            </select>
          </div>
          <!-- 아무것도 선택하지 않았을 때 -->
          <div v-else>
            <label for="before"></label>
            <select name="before" id="before" disabled>
              <option value="">우선 어떤 곳인지 정해주세요</option>
            </select>
          </div>
        </div>
      </fieldset>
      

      <!-- 이동수단 -->
      <fieldset class="transport" v-if="categoryBind['hotel']">
        <legend>transport</legend>
        <div class="nothing" :class="{'hotel': categoryBind['hotel'], 'res' : categoryBind['res'], 'activity' : categoryBind['activity']}">
          <div class="transport-icons">
            <span :class="{'active': transport.plane }" @click="selectTransport('plane')"><i class="fas fa-2x fa-plane"></i></span>
            <span :class="{'active': transport.train }" @click="selectTransport('train')"><i class="fas fa-2x fa-subway"></i></span>
            <span :class="{'active': transport.bus }" @click="selectTransport('bus')"><i class="fas fa-2x fa-bus"></i></span>
            <span :class="{'active': transport.car }" @click="selectTransport('car')"><i class="fas fa-2x fa-car"></i></span>
            <span :class="{'active': transport.walking }" @click="selectTransport('walking')"><i class="fas fa-2x fa-walking"></i></span>
          </div>
        </div>
      </fieldset>

      
      <fieldset class="transport" v-else-if="categoryBind['res']">
        <legend>transport</legend>
        <div class="nothing" :class="{'hotel': categoryBind['hotel'], 'res' : categoryBind['res'], 'activity' : categoryBind['activity']}">
          <div class="transport-icons">
            <span :class="{'active': transport.plane }" @click="selectTransport('plane')"><i class="fas fa-2x fa-plane"></i></span>
            <span :class="{'active': transport.train }" @click="selectTransport('train')"><i class="fas fa-2x fa-subway"></i></span>
            <span :class="{'active': transport.bus }" @click="selectTransport('bus')"><i class="fas fa-2x fa-bus"></i></span>
            <span :class="{'active': transport.car }" @click="selectTransport('car')"><i class="fas fa-2x fa-car"></i></span>
            <span :class="{'active': transport.walking }" @click="selectTransport('walking')"><i class="fas fa-2x fa-walking"></i></span>
          </div>
        </div>
      </fieldset>

      
      <fieldset class="transport" v-else-if="categoryBind['activity']">
        <legend>transport</legend>
        <div class="nothing" :class="{'hotel': categoryBind['hotel'], 'res' : categoryBind['res'], 'activity' : categoryBind['activity']}">
          <div class="transport-icons">
            <span :class="{'active': transport.plane }" @click="selectTransport('plane')"><i class="fas fa-2x fa-plane"></i></span>
            <span :class="{'active': transport.train }" @click="selectTransport('train')"><i class="fas fa-2x fa-subway"></i></span>
            <span :class="{'active': transport.bus }" @click="selectTransport('bus')"><i class="fas fa-2x fa-bus"></i></span>
            <span :class="{'active': transport.car }" @click="selectTransport('car')"><i class="fas fa-2x fa-car"></i></span>
            <span :class="{'active': transport.walking }" @click="selectTransport('walking')"><i class="fas fa-2x fa-walking"></i></span>
          </div>
        </div>
      </fieldset>

      
      <fieldset class="transport" v-else>
        <legend>transport</legend>
        <div class="nothing" :class="{'hotel': categoryBind['hotel'], 'res' : categoryBind['res'], 'activity' : categoryBind['activity']}">
          <div class="transport-icons">
            <span><i class="fas fa-2x fa-plane"></i></span>
            <span><i class="fas fa-2x fa-subway"></i></span>
            <span><i class="fas fa-2x fa-bus"></i></span>
            <span><i class="fas fa-2x fa-car"></i></span>
            <span><i class="fas fa-2x fa-walking"></i></span>
          </div>
        </div>
      </fieldset>


      <!-- 별점 -->
      <div class="rating">
        <div v-if="categoryBind['hotel']" class="rates">
          <div class="hotel">
            <div class="rating-system">
              <div class="rate-name">
                <h3>청결</h3>
              </div>
              <div class="bars">
                <input v-model="rate1" value="5" type="radio" name='rate1' :id="rate1star5" />
                <label @click="rate('rate1', 5)" :for="rate1star5"></label>

                <input v-model="rate1" value="4" type="radio" name='rate1' :id="rate1star4" />
                <label @click="rate('rate1', 4)" :for="rate1star4"></label>

                <input v-model="rate1" value="3" type="radio" name='rate1' :id="rate1star3" />
                <label @click="rate('rate1', 3)" :for="rate1star3"></label>
              
                <input v-model="rate1" value="2" type="radio" name='rate1' :id="rate1star2" />
                <label @click="rate('rate1', 2)" :for="rate1star2"></label>

                <input v-model="rate1" value="1" type="radio" name='rate1' :id="rate1star1" />
                <label @click="rate('rate1', 1)" :for="rate1star1"></label>

                <div class="text"></div>
              </div>
            </div>

            <div class="rating-system">
              <div class="rate-name">
                <h3>서비스</h3>
              </div>
              <div class="bars" id="rate2">
                <input v-model="rate2" value="5" type="radio" name='rate2' :id="rate2star5" />
                <label @click="rate('rate2', 5)" :for="rate2star5"></label>

                <input v-model="rate2" value="4" type="radio" name='rate2' :id="rate2star4" />
                <label @click="rate('rate2', 4)" :for="rate2star4"></label>

                <input v-model="rate2" value="3" type="radio" name='rate2' :id="rate2star3" />
                <label @click="rate('rate2', 3)" :for="rate2star3"></label>
              
                <input v-model="rate2" value="2" type="radio" name='rate2' :id="rate2star2" />
                <label @click="rate('rate2', 2)" :for="rate2star2"></label>

                <input v-model="rate2" value="1" type="radio" name='rate2' :id="rate2star1" />
                <label @click="rate('rate2', 1)" :for="rate2star1"></label>

                <div class="text"></div>
              </div>
            </div>
          
            <div class="rating-system">
              <div class="rate-name">
                <h3>위치</h3>
              </div>
              <div class="bars">
                <input v-model="rate3" value="5" type="radio" name='rate3' id="rate3star5" />
                <label @click="rate('rate3', 5)" for="rate3star5"></label>

                <input v-model="rate3" value="4" type="radio" name='rate3' id="rate3star4" />
                <label @click="rate('rate3', 4)" for="rate3star4"></label>

                <input v-model="rate3" value="3" type="radio" name='rate3' id="rate3star3" />
                <label @click="rate('rate3', 3)" for="rate3star3"></label>
              
                <input v-model="rate3" value="2" type="radio" name='rate3' id="rate3star2" />
                <label @click="rate('rate3', 2)" for="rate3star2"></label>

                <input v-model="rate3" value="1" type="radio" name='rate3' id="rate3star1" />
                <label @click="rate('rate3', 1)" for="rate3star1"></label>

                <div class="text"></div>
              </div>
            </div>
          </div>
        </div>
        
        <div v-if="categoryBind['res']" class="rates">
          <div class="res">
            <div class="rating-system">
              <div class="rate-name">
                <h3>청결</h3>
              </div>
              <div class="bars">
                <input v-model="rate1" value="5" type="radio" name='rate1' :id="rate1star5" />
                <label @click="rate('rate1', 5)" :for="rate1star5"></label>

                <input v-model="rate1" value="4" type="radio" name='rate1' :id="rate1star4" />
                <label @click="rate('rate1', 4)" :for="rate1star4"></label>

                <input v-model="rate1" value="3" type="radio" name='rate1' :id="rate1star3" />
                <label @click="rate('rate1', 3)" :for="rate1star3"></label>
              
                <input v-model="rate1" value="2" type="radio" name='rate1' :id="rate1star2" />
                <label @click="rate('rate1', 2)" :for="rate1star2"></label>

                <input v-model="rate1" value="1" type="radio" name='rate1' :id="rate1star1" />
                <label @click="rate('rate1', 1)" :for="rate1star1"></label>

                <div class="text"></div>
              </div>
            </div>

            <div class="rating-system">
              <div class="rate-name">
                <h3>서비스</h3>
              </div>
              <div class="bars" id="rate2">
                <input v-model="rate2" value="5" type="radio" name='rate2' :id="rate2star5" />
                <label @click="rate('rate2', 5)" :for="rate2star5"></label>

                <input v-model="rate2" value="4" type="radio" name='rate2' :id="rate2star4" />
                <label @click="rate('rate2', 4)" :for="rate2star4"></label>

                <input v-model="rate2" value="3" type="radio" name='rate2' :id="rate2star3" />
                <label @click="rate('rate2', 3)" :for="rate2star3"></label>
              
                <input v-model="rate2" value="2" type="radio" name='rate2' :id="rate2star2" />
                <label @click="rate('rate2', 2)" :for="rate2star2"></label>

                <input v-model="rate2" value="1" type="radio" name='rate2' :id="rate2star1" />
                <label @click="rate('rate2', 1)" :for="rate2star1"></label>

                <div class="text"></div>
              </div>
            </div>
          
            <div class="rating-system">
              <div class="rate-name">
                <h3>맛</h3>
              </div>
              <div class="bars">
                <input v-model="rate3" value="5" type="radio" name='rate3' id="rate3star5" />
                <label @click="rate('rate3', 5)" for="rate3star5"></label>

                <input v-model="rate3" value="4" type="radio" name='rate3' id="rate3star4" />
                <label @click="rate('rate3', 4)" for="rate3star4"></label>

                <input v-model="rate3" value="3" type="radio" name='rate3' id="rate3star3" />
                <label @click="rate('rate3', 3)" for="rate3star3"></label>
              
                <input v-model="rate3" value="2" type="radio" name='rate3' id="rate3star2" />
                <label @click="rate('rate3', 2)" for="rate3star2"></label>

                <input v-model="rate3" value="1" type="radio" name='rate3' id="rate3star1" />
                <label @click="rate('rate3', 1)" for="rate3star1"></label>

                <div class="text"></div>
              </div>
            </div>
          </div>
        </div>

        <div v-if="categoryBind['activity']" class="rates">
          <div class="activity">
            <div class="rating-system">
              <div class="rate-name">
                <h3>위치</h3>
              </div>
              <div class="bars">
                <input v-model="rate1" value="5" type="radio" name='rate1' :id="rate1star5" />
                <label @click="rate('rate1', 5)" :for="rate1star5"></label>

                <input v-model="rate1" value="4" type="radio" name='rate1' :id="rate1star4" />
                <label @click="rate('rate1', 4)" :for="rate1star4"></label>

                <input v-model="rate1" value="3" type="radio" name='rate1' :id="rate1star3" />
                <label @click="rate('rate1', 3)" :for="rate1star3"></label>
              
                <input v-model="rate1" value="2" type="radio" name='rate1' :id="rate1star2" />
                <label @click="rate('rate1', 2)" :for="rate1star2"></label>

                <input v-model="rate1" value="1" type="radio" name='rate1' :id="rate1star1" />
                <label @click="rate('rate1', 1)" :for="rate1star1"></label>

                <div class="text"></div>
              </div>
              </div>

            <div class="rating-system">
              <div class="rate-name">
                <h3>만족도</h3>
              </div>
              <div class="bars" id="rate2">
                <input v-model="rate2" value="5" type="radio" name='rate2' :id="rate2star5" />
                <label @click="rate('rate2', 5)" :for="rate2star5"></label>

                <input v-model="rate2" value="4" type="radio" name='rate2' :id="rate2star4" />
                <label @click="rate('rate2', 4)" :for="rate2star4"></label>

                <input v-model="rate2" value="3" type="radio" name='rate2' :id="rate2star3" />
                <label @click="rate('rate2', 3)" :for="rate2star3"></label>
              
                <input v-model="rate2" value="2" type="radio" name='rate2' :id="rate2star2" />
                <label @click="rate('rate2', 2)" :for="rate2star2"></label>

                <input v-model="rate2" value="1" type="radio" name='rate2' :id="rate2star1" />
                <label @click="rate('rate2', 1)" :for="rate2star1"></label>

                <div class="text"></div>
              </div>
            </div>
          </div>

        </div>
      </div>
      <!-- 느낀점 -->
      <div class="content">
        <p>Content</p>
        <textarea
          name="content" id="content"
          class="content"
          placeholder="느낀점"
          v-model="nowPlace.comment"
        >
        </textarea>
      </div>

      <div class="update-btn">
        <button @click="updatePlace">UPDATE</button>
      </div>
    </div>
  </div>
  </div>
</template>


<script>
import vClickOutside from 'v-click-outside'
import '../../components/css/modal.scss'
import axios from 'axios'

export default {
  name:'Modal',

  components: {},

  props: {
    nowPlace: Object,
  },

  directives: {
    clickOutside: vClickOutside.directive
  },

  data() {
    return {
      // 현재 선택한 카테고리
      categoryBind: {
        hotel: false,
        res: false,
        activity: false,
      },
      // 모달을 껏다 켜도 별점 보이게 하는 변수들
      rate1: '',
      rate2: '',
      rate3: '',

      transport: {
        plane: false,
        train: false,
        bus: false,
        car: false,
        walking: false,
      },

      subCategory: '',

    }
  },

  beforeCreate() {},
  created() {
    // 이전에 선택한 카테고리가 있으면 표현해줘야 함
    if ( this.nowPlace.category.main != '' ) {
      this.categoryBind[this.nowPlace.category.main] = true
      for (let key in this.categoryBind) {
        if (key != this.nowPlace.category.main) { this.categoryBind[key] = false }
      }
    }

    // 매겼던 점수 있으면 표시해줘야함
    this.rate1 = String(this.nowPlace.rates.rate1)
    this.rate2 = String(this.nowPlace.rates.rate2)
    this.rate3 = String(this.nowPlace.rates.rate3)

    // 이전에 고른 transport 있으면 표시
    for (let each in this.transport) {
      if ( each === this.nowPlace.transport ) { this.transport[each] = true }
    }

    // 이전에 고른 sub category
    this.subCategory = this.nowPlace.category.sub
  }, 
  beforeMount() {}, 
  mounted() {}, 
  beforeUpdate() {},
  updated() {},
  beforeUnmount() {}, 
  unmounted() {},

  computed: {
    // 이전에 선택한 카테고리가 있으면 표현해줘야 함
    whichCategory() {
      return this.nowPlace.category.main
    },
    // 각각의 고유값을 갖기 위함... 꼭 할필요 있을까??
    rate1star5() {
      return this.nowPlace.file + '1-5'
    },
    rate1star4() {
      return this.nowPlace.file + '1-4'
    },
    rate1star3() {
      return this.nowPlace.file + '1-3'
    },
    rate1star2() {
      return this.nowPlace.file + '1-2'
    },
    rate1star1() {
      return this.nowPlace.file + '1-1'
    },
    
    rate2star5() {
      return this.nowPlace.file + '2-5'
    },
    rate2star4() {
      return this.nowPlace.file + '2-4'
    },
    rate2star3() {
      return this.nowPlace.file + '2-3'
    },
    rate2star2() {
      return this.nowPlace.file + '2-2'
    },
    rate2star1() {
      return this.nowPlace.file + '2-1'
    },

    rate3star5() {
      return this.nowPlace.file + '3-5'
    },
    rate3star4() {
      return this.nowPlace.file + '3-4'
    },
    rate3star3() {
      return this.nowPlace.file + '3-3'
    },
    rate3star2() {
      return this.nowPlace.file + '3-2'
    },
    rate3star1() {
      return this.nowPlace.file + '3-1'
    },

    nowImage() {
      
      return this.nowPlace.imgUrl
    },

  },

  methods: {
    // main category 선택
    clickBtn(category) {
      // this.whichCategory = category
      this.nowPlace.category.main = category
      this.categoryBind[category] = true
      for (let key in this.categoryBind) {
        if (key != category) { this.categoryBind[key] = false }
      }

      // 매겼던 점수들 초기화 시켜줘야함
      this.nowPlace.rates.rate1 = 0
      this.nowPlace.rates.rate2 = 0
      this.nowPlace.rates.rate3 = 0

      // data에도 초기화
      this.rate1 = ''
      this.rate2 = ''
      this.rate3 = ''
    },

    // sub category 선택
    onSubCategoryChange(event) {
      this.nowPlace.category.sub = event.target.value
    },

    // transport 선택
    selectTransport(transport) {
      this.nowPlace.transport = transport
      this.transport[transport] = true
      for (let each in this.transport) {
        if (each != transport) { this.transport[each] = false }
      }
    },
    
    // 별점 매기기
    rate(which, rate) {
      this.nowPlace.rates[which] = rate
      this[which] = String(rate)
    },

    // modal 닫기
    closeModal() {
      this.$emit('close-modal')
    },

    updatePlace() {
      axios({
        method: 'patch',
        url: '/place/update',
        data: {
          placeIdx: this.nowPlace.placeIdx,
          userId: this.$store.state.accounts.userProfile.userId,
          title: this.nowPlace.title,
          comment: this.nowPlace.comment,
          category: this.nowPlace.category,
          rates: this.nowPlace.rates,
          transport: this.nowPlace.transport,
        },
        headers: { 'X-AUTH-TOKEN' : this.$store.state.accounts.authToken },
      })
        .then(res => {
          console.log(res)
          this.$emit('close-modal')
        })
        .catch(err => console.log(err))
    },

  }
}
</script>