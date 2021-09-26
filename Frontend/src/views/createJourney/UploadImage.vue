<template>
  <div class="main-container">
    <div class="image-box">
      <!-- <div class="image-profile">
            <img :src="profileImage" />
          </div>-->
      <label for="file">Upload File</label>
      <input
        ref="files"
        @change="imageUpload"
        type="file"
        id="file"
        name="file"
        accept="image/*"
        multiple
      />
    </div>
    <div class="room-deal-information-container">
      <div class="room-file-upload-wrapper">
        <div v-if="!files.length" class="room-file-upload-example-container">
          <div class="room-file-upload-example">
            <!-- <div class="room-file-image-example-wrapper">이미지</div>
            <div class="room-file-notice-item">
              실사진 최소 3장 이상 등록하셔야 하며, 가로사진을 권장합니다.
            </div>
            <div class="room-file-notice-item room-file-notice-item-red">
              로고를 제외한 불필요한 정보(워터마크,상호,전화번호 등)가 있는
              매물은 비공개처리됩니다
            </div> -->
            <div class="plus-sign">
              <i class="fas fa-plus"></i>
            </div>
            <div class="room-file-notice-item room-file-upload-button">
              
            </div>
          </div>
        </div>
        <!-- :key="index"로 했을 떄, 체크박스에 체크하고 x버튼으로 앞에거 지우면 index때문에
              체크박스가 뒤로 밀리는 현상 발생. key값은 진짜 혼자 갖고있는 고유값을 줘야한다. -->
        <div v-else class="file-preview-content-container">
          <div class="file-preview-container">
            <div
              v-for="file in files"
              :key="file.number"
              class="file-preview-wrapper"
            >
              <div
                class="file-close-button"
                @click="fileDeleteButton"
                :name="file.number"
              >
                <i class="fas fa-times"></i>
              </div>
              <!-- checkImage()로 사용시, 기본 파라미터 event 사용이 불가능 -->
              <!-- <input
                class="file-check-button"
                type="checkbox"
                @change="checkImage"
                :name="file.number"
              /> -->
              <img :src="file.preview"/>
            </div>
            <!-- <div class="file-preview-wrapper-upload">
              <div class="image-box">
                <label for="file">추가 사진 등록</label>
                <input
                  type="file"
                  id="file"
                  ref="files"
                  name="file"
                  accept="image/*"
                  @change="imageAddUpload"
                  multiple
                />
              </div> -->
              <!-- <div class="file-close-button" @click="fileDeleteButton" :name="file.number">x</div> -->
            <!-- </div> -->
          </div>
        </div>
      </div>
    </div>

    <!-- <div v-for="image in files" :key="image.file.name">
      <img :src="image.preview" alt="" style="width:200px;" />
    </div> -->
    <div>
      <button class="next-btn" @click="onClickNextBtn">Next Step</button>
    </div>
  </div>
</template>

<script>
import exifr from "exifr";
import '../../components/css/createJourney/uploadimage.scss'

export default {
  name: "UploadImage",

  data() {
    return {
      image: "",
      files: [], //업로드용 파일 + 정보가공 추가
      filesPreview: [],
      uploadImageIndex: 0, // 이미지 업로드를 위한 변수,
      checkedData: [], // 체크한 이미지 데이터, 나중에 개별 일지에 넘겨주려고
    };
  },
  methods: {
    imageUpload() {
      // 내가 선언한 refs 자료 참조 == 막 업로드한 자료
      console.log(this.$refs["files"].files[0]);

      // this.files = [...this.files, this.$refs.files.files];
      //하나의 배열로 넣기
      let num = -1;

      for (let i = 0; i < this.$refs.files.files.length; i++) {
        this.files = [
          ...this.files, // 기존파일들?
          //이미지 업로드
          {
            //실제 파일
            file: this.$refs.files.files[i],
            //이미지 프리뷰
            preview: URL.createObjectURL(this.$refs.files.files[i]),
            //삭제및 관리를 위한 number
            number: i,
          },
        ];
        num = i;
        //이미지 업로드용 프리뷰
        // this.filesPreview = [
        //   ...this.filesPreview,
        //   { file: URL.createObjectURL(this.$refs.files.files[i]), number: i }
        // ];

        // EXIF
        var image = this.$refs.files.files[i];
        var position = new Array();

        // 스크린샷은 undefined로, exif 없는 경우 조회가 안되는 문제
        exifr.parse(image).then((output) => {
          if (output) {
            console.log("I am in !", output.latitude);
            this.files[i].dateTime = output.CreateDate
            this.files[i].imageData = output;
            if (output.latitude !== undefined) {
              position = {
                lat: output.latitude,
                lng: output.longitude,
              };
              this.files[i].position = position;
              position = [];
            } else {
              position = {
                lat: 37.512126019029,
                lng: 127.04489721131,
              };
              this.files[i].position = position;
              position = [];
            }
          } else {
            position = { lat: 37.512126019029, lng: 127.04489721131 };
            this.files[i].position = position;
            position = [];
          }
        });
        // 이미지 미리보기
        const url = URL.createObjectURL(image);
        this.image = url;
      }
      this.uploadImageIndex = num + 1; //이미지 index의 마지막 값 + 1 저장
      console.log(this.files);
    },

    imageAddUpload() {
      // 기존 파일 + 새로 추가한 파일
      // this.files = [...this.files, this.$refs.files.files];
      //하나의 배열로 넣기c
      let num = -1;
      for (let i = 0; i < this.$refs.files.files.length; i++) {
        this.files = [
          ...this.files,
          //이미지 업로드
          {
            //실제 파일
            file: this.$refs.files.files[i],
            //이미지 프리뷰
            preview: URL.createObjectURL(this.$refs.files.files[i]),
            //삭제및 관리를 위한 number
            number: i + this.uploadImageIndex,
          },
        ];
        num = i;

        // EXIF
        var image = this.$refs.files.files[i];
        var position = new Array();

        exifr.parse(image).then((output) => {
          console.log('들어옴')
          console.log(output)
          if (output) {
            console.log("I am in !", output.latitude);
            this.files[this.files.length - this.$refs.files.files.length + i].dateTime = output.CreateDate
            if (output.latitude !== undefined) {
              position = {
                lat: output.latitude,
                lng: output.longitude,
              };
              // this.files.length-1 을 바꿔줘야함. 안그러면 계속 최고값 나옴
              this.files[ this.files.length - this.$refs.files.files.length + i].position = position;
              position = [];
            } else {
              position = {
                lat: 37.512126019029,
                lng: 127.04489721131,
              };
              this.files[ this.files.length - this.$refs.files.files.length + i].position = position;
              position = [];
            }
          } else {
            position = { lat: 37.512126019029, lng: 127.04489721131 };
            this.files[ this.files.length - this.$refs.files.files.length + i].position = position;
            position = [];
          }
        });
        // 이미지 미리보기
        const url = URL.createObjectURL(image);
        this.image = url;
      }

      this.uploadImageIndex = this.uploadImageIndex + num + 1;

      console.log(this.files);
      // console.log(this.filesPreview);
    },

    fileDeleteButton(e) {
      // console.log(e.target)
      const name = e.target.getAttribute("name");
      // 특정값에 부합하는것만으로 새로 만드는 filter
      this.files = this.files.filter((data) => data.number !== Number(name)); // files 내용 재정의
      this.uploadImageIndex -= 1;
    },

    checkImage(e) {
      if (e.target.checked) {
        // 임의의 v-bind:name값을 줘서, 그것을 unique value로 사용
        console.log("체크됨!");
        // 배열내 배열이 싫어서 [0]번으로 빼왔고(어차피 하나니까) + Number 안쓰면 동일검사 통과 불가
        this.checkedData.push(
          this.files.filter(
            (data) => data.number === Number(e.target.getAttribute("name"))
          )[0]
        );
        console.log(this.checkedData);
      } else {
        this.checkedData = this.checkedData.filter(
          (elements) =>
            elements.number !== Number(e.target.getAttribute("name"))
        );
        console.log("체크 안된경우 새로운 checkedData 배열!", this.checkedData);
      }
    },

    onClickNextBtn() {
      if ( this.files.length > 0 ) {
        this.$emit('next-step')
        this.$emit('upload-image', this.files)
      } else {
        alert('한 장 이상의 사진을 올려주세용')
      }
    },
  },
};
</script>

<style scoped>
/*
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
.main-container {
  width: 1200px;
  height: 400px;
  margin: 0 auto;
}

.room-deal-information-container {
  margin-top: 50px;
  color: #222222;
  border: 1px solid #dddddd;
}

.room-deal-information-title {
  text-align: center;
  font-size: 18px;
  line-height: 60px;
  border-bottom: 1px solid #dddddd;
}

.room-deal-information-content-wrapper {
  min-height: 50px;
  display: flex;
}
 .room-deal-informtaion-content-title {
  font-size: 15px;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 150px;
  background-color: #f9f9f9;
}
.room-deal-information-content {
  width: 100%;
  font-size: 14px;
}

.room-deal-option-selector {
  display: flex;
  align-items: center;
  padding: 15px;
}

.room-deal-option-item {
  width: 100px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  border: 1px solid #cccccc;
  border-radius: 5px;
  cursor: pointer;
}

.room-deal-option-item:last-child {
  margin-left: 10px;
}

.room-deal-option-notice {
  margin-left: auto;
  font-size: 14px;
  color: #888888;
}

.room-deal-option-item-deposit {
  margin-left: 10px;
}

.room-deal-information-wrapper {
  display: flex;
  flex-direction: column;
}

.room-deal-information-option {
  padding: 10px;
  display: flex;
  align-items: center;
}

.room-deal-information-option:last-child {
  border-bottom: 1px solid #dddddd;
}

.room-deal-information-item-type {
  font-size: 13px;
  color: #fff;
  background-color: #61b6e5;
  min-width: 50px;
  height: 26px;
  display: flex;
  justify-content: center;
  align-items: center;
  border-radius: 3px;
}

.room-deal-information-item-wrapper {
  display: flex;
  align-items: center;
  margin-left: 10px;
  height: 46px;
  width: 100%;
}

.room-deal-information-item-wrapper > input {
  border: 1px solid #dddddd;
  width: 140px;
  height: 100%;
  padding: 0 15px;
  font-size: 15px;
}

.room-deal-inforamtion-won {
  margin: 0 10px;
}

.room-deal-information-example {
  color: #888888;
}

.room-deal-information-option:not(:first-child) {
  margin-top: 10px;
}

.room-deal-inforamtion-divide {
  font-size: 22px;
  margin: 0 8px;
  color: #222222;
  font-weight: 100;
}

.room-deal-close-button-wrapper {
  margin-left: auto;
  cursor: pointer;
}

.room-deal-close-button {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 22px;
  height: 22px;
  background-color: #666666;
  color: rgb(220, 220, 220);
}

.room-deal-cliked {
  background-color: rgb(235, 235, 235);
  color: rgb(170, 170, 170);
}

.room-file-upload-example {
  height: 100%;
}

.room-write-content-container {
  border-top: 1px solid #dddddd;
  min-height: 260px;
}

.file-preview-content-container {
  height: 100%;
}

.room-file-upload-wrapper {
  margin: 20px;
  border: 1px solid #dddddd;
  border-radius: 10px;
  background-color: #f4f4f4;
  min-height: 350px;
  font-size: 15px;
  color: #888888;
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100%;
}

.room-file-upload-example-container {
  display: flex;
  align-items: center;
  justify-content: center;
  /* height: 100%;
  width: 100%; 
}

.room-file-image-example-wrapper {
  text-align: center;
}

.room-file-notice-item {
  margin-top: 5px;
  text-align: center;
}

.room-file-notice-item-red {
  color: #ef4351;
}

.image-box {
  margin-top: 30px;
  padding-bottom: 20px;
  text-align: center;
}

.image-box input[type="file"] {
  position: absolute;
  width: 0;
  height: 0;
  padding: 0;
  overflow: hidden;
  border: 0;
}

.image-box label {
  display: inline-block;
  padding: 10px 20px;
  background-color: #232d4a;
  color: #fff;
  vertical-align: middle;
  font-size: 15px;
  cursor: pointer;
  border-radius: 5px;
}

.file-preview-wrapper {
  padding: 10px;
  position: relative;
}

.file-preview-wrapper > img {
  position: relative;
  width: 190px;
  height: 130px;
  z-index: 10;
}

.file-close-button {
  position: absolute;
  /* align-items: center; 
  line-height: 18px;
  z-index: 99;
  font-size: 18px;
  right: 5px;
  top: 10px;
  color: #fff;
  font-weight: bold;
  background-color: #666666;
  width: 20px;
  height: 20px;
  text-align: center;
  cursor: pointer;
}

.file-preview-container {
  height: 100%;
  display: flex;
  flex-wrap: wrap;
}

.file-preview-wrapper-upload {
  margin: 10px;
  padding-top: 20px;
  background-color: #888888;
  width: 190px;
  height: 130px;
}

.room-write-button-wrapper {
  margin-top: 20px;
  display: flex;
  justify-content: center;
  align-items: center;
  color: #222222;
}

.room-write-button-wrapper > div {
  width: 160px;
  height: 50px;
  border: 1px solid #dddddd;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 15px;
  cursor: pointer;
}

.room-write-button {
  margin-left: 15px;
  color: #fff;
  background-color: #1564f9;
}

.room-write-button:hover {
  opacity: 0.8;
}

.file-check-button {
  position: absolute;
  z-index: 100;
} */
</style>
