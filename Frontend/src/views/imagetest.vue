<template>
  <div id="test">
    <br>
    <br>
    <br>
    <br>
    <br>
    <file-pond
      name="test"
      ref="pond"
      allowImageExifOrientation = "true"
      label-idle="Drop files here..."
      allowprocess="false"
      v-bind:allow-multiple="true"
      accepted-file-types="image/jpeg, image/png"
      :server="myServer"
      instantUpload ="false"
      v-bind:files="myFiles"
      v-on:init="handleFilePondInit"
      imageTransformOutputStripImageHead = "false"
      allowImagePreview="true"
      imagePreviewHeight="300px"
      @initfile="oninitfile()"
      @updatefiles="FilePondUpdateFiles"
    />
    <br>
    <p style="text-align:center">hi</p>
    <div v-for="imageFile in imageFiles" :key="imageFile.name" style="text-align:center;">
      <img :src="imageFile.src" alt="" style="width: 200px">
    </div>
  </div>
</template>

<script>
// Import Vue FilePond
import vueFilePond, { setOptions } from "vue-filepond";

// Import FilePond styles
import "filepond/dist/filepond.min.css";

// Import FilePond plugins
// Please note that you need to install these plugins separately

// Import image preview plugin styles
import "filepond-plugin-image-preview/dist/filepond-plugin-image-preview.min.css";

// Import image preview and file type validation plugins
import FilePondPluginImageExifOrientation from 'filepond-plugin-image-exif-orientation';
import FilePondPluginFileValidateType from "filepond-plugin-file-validate-type";
import FilePondPluginImagePreview from "filepond-plugin-image-preview";
// 메타데이터 삽입
import FilePondPluginFileMetadata from 'filepond-plugin-file-metadata';

setOptions({

})

// Create component
const FilePond = vueFilePond(
  FilePondPluginFileValidateType,
  FilePondPluginImagePreview,
  FilePondPluginFileMetadata,
  FilePondPluginImageExifOrientation
);


export default {
  name: "test",
  data: function () {
    return { 
      myFiles: [],
      imageFiles: [],
      // myFiles: ["cat.jpeg"],
      myServer: {
        process: (fieldName, file, metadata, load, error, progress, abort) => {
          console.log("check what I can get", fieldName);
          const formdata = new FormData();
          formdata.append('file', file, file.name, error, progress, abort);
          for (let key of formdata.values()) {
            console.log(key);
          }
          load(Date.now())
        }
      } 
    };
  },
  watch: {
    // 객체로 된 데이터를 watch에서 사용할경우 deep watch 사용.
    imageFiles: {
      deep: true,
      handler() {
        this.processImageFiles();
      },
    },
  },
  
  methods: {
    // 이미지 파일 정보 필요한 것만 따오게 재 가공.
    processImageFiles() {
      for (let index = 0; index < this.imageFiles.length; index++) {
        // blob 관련 공부가 필요
        // let src = window.URL.createObjectURL(this.imageFiles[index].file)
        // 생각해보니 서버에 파일을 통으로 보내야 하지 않나? 이렇게 보내도 되나
        // this.imageFiles[index] = { 'file': this.imageFiles[index].file, 'name': this.imageFiles[index].file.name, 'date': this.imageFiles[index].file.lastModifiedDate, 'src':src }
        // console.log(src)

        // url  사용이 다 끝나면 삭제 해야함. 안그러면 브라우저에서 계속 들고있어 메모리 누수가 일어난다.
        // src.onload = () => { window.URL.revokeObjectURL(this.src) } //나중에 반드시 해제해주어야 메모리 누수가 안생김.
      }
      console.log(this.imageFiles)
    },

    handleFilePondInit: function () {
      console.log("FilePond has initialized");
      // FilePond instance methods are available on `this.$refs.pond`
      this.$refs.pond.getFiles();
      console.log(this.$refs.pond.getFiles())
    },

    // 파일 staging시
    oninitfile() {
      console.log("onInit!")
    },
    // 위에서 ()로 선언하면 안됐음 ㅁㅊ 하 시발 이거때무네 내 5시간 개시발
    FilePondUpdateFiles(files) {
      console.log("기본적인 이미지 검색 방식", this.$refs['pond'].file)
      console.log(files)
      console.log("onAdd!")
      // let files2 = this.$refs.pond.getFiles()
      // this.imageFiles.push({'name': `${files2[files2.length-1].file.name}`, 'date': `${files2[files2.length-1].file.lastModifiedDate}`})
      this.imageFiles = files
      // console.log(files2, "사진이다.")
      // console.log(files2[0].getMetadata())
      console.log("총 사진 데이터", this.imageFiles)
      
    }
  },
  components: {
    FilePond,
  },
};
</script>

<style>
  .filepond--item {
    width: calc(20% - 1em);
  }
  /* 업로드 버튼 숨기기 */
  /* .filepond--action-process-item{
    visibility:hidden;
  } */
</style>