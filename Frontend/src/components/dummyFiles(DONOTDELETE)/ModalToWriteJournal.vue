<template>
  <div>
    <!-- 들어올 시 모달이 뜬 것처럼 show로 오픈이 가능 but 스크롤처리 문제(body) -->
    <div class="modal show">
      <div class="modalContent">
        <div class="modalHeader">
          <div class="progressBarContainer">
            <ul class="progressbar">
              <li class="uploadProgressBar">Upload Photos</li>
              <li class="writeProgressBar">Write Journal</li>
              <li class="">Complete Journal</li>
            </ul>
          </div>
        </div>
        <hr />

        <div class="modalBody uploadModalBody" v-if="uploadModalBody">
          <p>a</p>
          <UploadImages @changed="handleImages" />
        </div>

        <div class="modalBody writeModalBody" v-if="writeModalBody">
          <p>I am Write Modal Body !</p>
        </div>

        <hr />

        <div class="modalFooter">
          <button class="nextButton" @click="nextModalBody()">Next</button>
          <button class="closeButton" @click="closeModal()">x</button>
        </div>
      </div>
    </div>
    <button class="openModalButton" @click="openModal">Modal 띄우기</button>
  </div>
</template>

<script>
import UploadImages from "vue-upload-drop-images"

export default {
  name: "",
  components: {
    UploadImages,
  },
  data() {
    return {
      sampleData: "",
      uploadModalBody: true,
      writeModalBody: false,
    };
  },
  beforeCreate() {},
  created() {},
  beforeMount() {},
  mounted() {
    // 이 화면 들어오는 주소로 접속시(mounted) 실행
    const body = document.querySelector("body");
    body.style.overflow = "hidden";
  },
  beforeUpdate() {},
  updated() {},
  beforeUnmount() {},
  unmounted() {},
  methods: {
    openModal() {
      const modal = document.querySelector(".modal");
      // 모달 띄울시 스크롤을 모달 내에서 사용하기 위함.
      const body = document.querySelector("body");
      // modal.style.display = "block";
      modal.classList.toggle("show");
      body.style.overflow = "hidden";
    },

    nextModalBody() {
      const modalBody = document.querySelector(".modalBody");
      const uploadProgBar = document.querySelector(".uploadProgressBar");
      // 사진 업로드 모달 바디였다면
      if (modalBody.classList.contains("uploadModalBody")) {
        // 모달 바디 바꾸기
        this.uploadModalBody = false;
        this.writeModalBody = true;
        // 프로그레스 바 진행
        uploadProgBar.classList.add("active");
        // 글 작성 모달 바디였다면
      } else if (modalBody.classList.contains("writeModalBody")) {
        console.log("I am Write Modal Body!");
      }
    },

    closeModal() {
      const body = document.querySelector("body");
      const modal = document.querySelector(".modal");
      const li = document.querySelector("li");
      // 스크롤바 살리고
      body.style.overflow = "auto";
      // 토글로 show 꺼서 display="block"이 사라진다.
      modal.classList.toggle("show");
      // 다시 업로드 모달 바디로 돌리고
      this.uploadModalBody = true;
      this.writeModalBody = false;
      // 프로그레스바도 초기화
      li.classList.remove("active");
    },

    handleImages(files) {
      console.log(files)
    }
  },
};
</script>

<style scoped>
.modal {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  display: none;
  background-color: rgba(0, 0, 0, 0.4);

  /* overflow-y: initial !important; */
}

.modal.show {
  display: block !important;
}

.modalContent {
  /* 
    relative: 상대적 위치, 내 위 엘리먼트로부터 T B L R 이라 생각하면 좋을듯
    fixed: 현재 보여지는 화면으로부터 T B L R (*모바일 브라우저에서 지원이 불안정)
    absolute: 부모 엘리먼트로부터 T B L R, 없으면 body
  */
  position: absolute;
  top: 50%;
  left: 50%;

  width: 600px;
  height: 800px;
  max-height: 90vh;

  padding: 40px;

  background-color: rgb(255, 255, 255);
  border-radius: 10px;
  box-shadow: 0 2px 3px 0 rgba(34, 36, 38, 0.15);
  transform: translateX(-50%) translateY(-50%);

  text-align: center;

  /* overflow-y: scroll; */
}
/* modalBody의 스크롤바 숨기기 */
.modalContent ::-webkit-scrollbar {
  display: none !important;
}

.modalHeader {
  height: 10%;
}

.modalBody {
  /* modalContent height의 80% */
  height: 80%;
  /* max-height: calc(500px - 200px); */
  overflow-y: scroll;
}

.modalFooter {
  height: 5%;
  margin-right: 5%;
  text-align: right;
}

/* progress bar */
.progressBarContainer {
  width: 100%;
  /* max-height: 100%; */
  height: 100px;
  margin: 10px auto;
  align-content: center;
}
.progressbar {
  /* add numbers in the circle */
  counter-reset: step;
}
.progressbar li {
  list-style-type: none;
  position: relative;
  float: left;
  width: 31%;
  /* color: #7d7d7d; */
  color: rgba(237, 119, 102, 1);
  font-size: 12px;
  text-transform: uppercase;
  text-align: center;
}

/* circle */
.progressbar li:before {
  display: block;
  width: 30px;
  height: 30px;
  margin: 0 auto 10px auto;

  /* border: 2px solid #7d7d7d; */
  border: 2px solid rgba(237, 119, 102, 1);
  border-radius: 50%;
  background-color: white;

  text-align: center;
  line-height: 30px;
  content: counter(step);
  counter-increment: step;
}
/* line between circle */
.progressbar li:after {
  position: absolute;

  top: 15px;
  left: -50%;
  z-index: -1;

  width: 100%;
  height: 2px;

  /* background-color: #7d7d7d; */
  background-color: rgba(237, 119, 102, 1);
  content: "";
}
.progressbar li:first-child:after {
  content: none;
}
.progressbar li.active {
  color: green;
}
.progressbar li.active:before {
  border-color: #55b776;
}
.progressbar li.active + li:after {
  background-color: #55b776;
}

/* 임시로 UploadImages 강제 컨트롤 */
.container {
  width: auto;
  height: auto;
}

.imgsPreview .imageHolder .plus {
  border: none;
}
</style>
