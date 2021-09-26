<template>
  <div class="pintest">
    <GmapMap
        ref="mapRef"
        :center="center"
        :zoom="2"
        class="google-map"
    >
      <gmap-marker
        :position="marker.position"
        :clickalbe="true"
        :icon="icon"
        @click="clickBasicPin"
      />
      <gmap-polyline v-bind:path.sync="path" v-bind:options="{ strokeColor:'#008000'}"/>
    </GmapMap>
    <div
      v-if="marker.modal"
      class="modal"
      >
      <div class="modal-body" v-click-outside="closeModal">

        <!-- 사진 들어가는 부분 -->
        <div class="image">
          <img src="https://picsum.photos/300/500" alt="" class="pic">
          <!-- <img :src="markerData.images[0].url" alt=""> -->
        </div>


        <!-- 글 작성 부분 -->
        <div class="text">
          <!-- 제목 -->
          <input type="text" placeholder="Title">
          <!-- 대분류 -->
          <fieldset class="category">
            <legend>Category</legend>
            <div class="category-btns">
              <button @click="clickBtn('hotel')" class="hotel"><img src="../assets/icon/hotel-icon.png" alt="hotel-icon"></button>
              <button @click="clickBtn('res')" class="res"><img src="../assets/icon/res-icon.png" alt="res-icon"></button>
              <button  @click="clickBtn('activity')" class="activity"><img src="../assets/icon/activity-icon.png" alt="activity-icon"></button>
            </div>

            <!-- 소분류 -->
            <div class="sub-category">
              <div v-if="whichCategory === 'hotel'" class="select">
                <label for="hotel"></label>
                <select name="hotel-category" id="hotel">
                  <option selected="" hidden="" value="">Where</option>
                  <option value="">Hotel</option>
                  <option value="">Guest House</option>
                  <option value="">Pension</option>
                  <option value="">AirBnB</option>
                  <option value="">Motel</option>
                  <option value="">Etc.</option>
                </select>
              </div>
              <div v-else-if="whichCategory === 'res'" class="select">
                <label for="res"></label>
                <select name="res-category" id="res">
                  <option selected="" hidden="" value="">어떤 식당?</option>
                  <option value="">Food</option>
                  <option value="">Cafe</option>
                  <option value="">Bar</option>
                  <option value="">Etc.</option>
                </select>
              </div>
              <div v-else-if="whichCategory === 'activity'" class="select">
                <label for="activity"></label>
                <select name="activity-category" id="activity">
                  <option selected="" hidden="">어떤 활동?</option>
                  <option value="">Visit</option>
                  <option value="">Shopping</option>
                  <option value="">Leisure</option>
                  <option value="">Etc.</option>
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
          <fieldset class="transport">
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
                    <input type="radio" name='rate1' id="rate1-star5" />
                    <label for="rate1-star5"></label>

                    <input type="radio" name='rate1' id="rate1-star4" />
                    <label for="rate1-star4"></label>

                    <input type="radio" name='rate1' id="rate1-star3" />
                    <label for="rate1-star3"></label>
                  
                    <input type="radio" name='rate1' id="rate1-star2" />
                    <label for="rate1-star2"></label>

                    <input type="radio" name='rate1' id="rate1-star1" />
                    <label for="rate1-star1"></label>

                    <div class="text"></div>
                  </div>
                </div>

                <div class="rating-system">
                  <class class="rate-name">
                    <h3>서비스</h3>
                  </class>
                  <div class="bars" id="rate2">
                    <input type="radio" name='rate2' id="rate2-star5" />
                    <label for="rate2-star5"></label>

                    <input type="radio" name='rate2' id="rate2-star4" />
                    <label for="rate2-star4"></label>

                    <input type="radio" name='rate2' id="rate2-star3" />
                    <label for="rate2-star3"></label>
                  
                    <input type="radio" name='rate2' id="rate2-star2" />
                    <label for="rate2-star2"></label>

                    <input type="radio" name='rate2' id="rate2-star1" />
                    <label for="rate2-star1"></label>

                    <div class="text"></div>
                  </div>
                </div>
              
                <div class="rating-system">
                  <div class="rate-name">
                    <h3>위치</h3>
                  </div>
                  <div class="bars">
                    <input type="radio" name='rate3' id="rate3-star5" />
                    <label for="rate3-star5"></label>

                    <input type="radio" name='rate3' id="rate3-star4" />
                    <label for="rate3-star4"></label>

                    <input type="radio" name='rate3' id="rate3-star3" />
                    <label for="rate3-star3"></label>
                  
                    <input type="radio" name='rate3' id="rate3-star2" />
                    <label for="rate3-star2"></label>

                    <input type="radio" name='rate3' id="rate3-star1" />
                    <label for="rate3-star1"></label>

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
                    <input type="radio" name='rate1' id="rate1-star5" />
                    <label for="rate1-star5"></label>

                    <input type="radio" name='rate1' id="rate1-star4" />
                    <label for="rate1-star4"></label>

                    <input type="radio" name='rate1' id="rate1-star3" />
                    <label for="rate1-star3"></label>
                  
                    <input type="radio" name='rate1' id="rate1-star2" />
                    <label for="rate1-star2"></label>

                    <input type="radio" name='rate1' id="rate1-star1" />
                    <label for="rate1-star1"></label>

                    <div class="text"></div>
                  </div>
                </div>

                <div class="rating-system">
                  <class class="rate-name">
                    <h3>서비스</h3>
                  </class>
                  <div class="bars" id="rate2">
                    <input type="radio" name='rate2' id="rate2-star5" />
                    <label for="rate2-star5"></label>

                    <input type="radio" name='rate2' id="rate2-star4" />
                    <label for="rate2-star4"></label>

                    <input type="radio" name='rate2' id="rate2-star3" />
                    <label for="rate2-star3"></label>
                  
                    <input type="radio" name='rate2' id="rate2-star2" />
                    <label for="rate2-star2"></label>

                    <input type="radio" name='rate2' id="rate2-star1" />
                    <label for="rate2-star1"></label>

                    <div class="text"></div>
                  </div>
                </div>
              
                <div class="rating-system">
                  <div class="rate-name">
                    <h3>맛</h3>
                  </div>
                  <div class="bars">
                    <input type="radio" name='rate3' id="rate3-star5" />
                    <label for="rate3-star5"></label>

                    <input type="radio" name='rate3' id="rate3-star4" />
                    <label for="rate3-star4"></label>

                    <input type="radio" name='rate3' id="rate3-star3" />
                    <label for="rate3-star3"></label>
                  
                    <input type="radio" name='rate3' id="rate3-star2" />
                    <label for="rate3-star2"></label>

                    <input type="radio" name='rate3' id="rate3-star1" />
                    <label for="rate3-star1"></label>

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
                    <input type="radio" name='rate1' id="rate1-star5" />
                    <label for="rate1-star5"></label>

                    <input type="radio" name='rate1' id="rate1-star4" />
                    <label for="rate1-star4"></label>

                    <input type="radio" name='rate1' id="rate1-star3" />
                    <label for="rate1-star3"></label>
                  
                    <input type="radio" name='rate1' id="rate1-star2" />
                    <label for="rate1-star2"></label>

                    <input type="radio" name='rate1' id="rate1-star1" />
                    <label for="rate1-star1"></label>

                    <div class="text"></div>
                  </div>
                  </div>

                <div class="rating-system">
                  <class class="rate-name">
                    <h3>만족도</h3>
                  </class>
                  <div class="bars" id="rate2">
                    <input type="radio" name='rate2' id="rate2-star5" />
                    <label for="rate2-star5"></label>

                    <input type="radio" name='rate2' id="rate2-star4" />
                    <label for="rate2-star4"></label>

                    <input type="radio" name='rate2' id="rate2-star3" />
                    <label for="rate2-star3"></label>
                  
                    <input type="radio" name='rate2' id="rate2-star2" />
                    <label for="rate2-star2"></label>

                    <input type="radio" name='rate2' id="rate2-star1" />
                    <label for="rate2-star1"></label>

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
            ></textarea>
          </div>

        </div>
      </div>
    </div>
  </div>
</template>

<script>
import * as VueGoogleMaps from 'vue2-google-maps'
import Vue from 'vue'
import '../components/css/pintest.scss'
import vClickOutside from 'v-click-outside'

Vue.use(VueGoogleMaps, {
  load: {
    key: "AIzaSyBwPv3o2CKXGTbC5qPIjTCdU5F1ToEExio",
    libraries: "places",
  }
})

export default {

  name: 'eachPin',

  directives: {
    clickOutside: vClickOutside.directive
  },

  data() {
    return {
      center: {
        lat: 37.5,
        lng: 127,
      },
      icon:  {
        url: require("../assets/pins/basicpin.png"),
        scaledSize: {width: 28, height: 28},
        labelOrigin: {x: 16, y: -10},
      },
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
      path: [
            {lat: 55.9352001, lng: -4.7766924 },
            {lat: 55.9358131, lng: -4.7770143 },
            {lat: 55.9361256, lng: -4.7767353 },
            {lat: 37.9366784, lng: -4.7739458 }
      ],
      category: {
        main: '',
        sub: '',
      },
      categoryBind: {
        hotel: false,
        res: false,
        activity: false,
      }
    }
  },
  methods: {
    clickBasicPin() {
      this.marker.modal = true
    },
    closeModal() {
      this.marker.modal = false
    },
    clickBtn(category) {
      this.whichCategory = category
      this.category.main = category
      this.categoryBind[category] = true
      for (let key in this.categoryBind) {
        if (key != category) { this.categoryBind[key] = false }
      }
    }
  }
}
</script>