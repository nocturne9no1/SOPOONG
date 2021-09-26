<template>
  <div>
    <form method="post" enctype="multipart/form-data">
      <div>
        <label for="chooseFile">
          Click
        </label>
      </div>
    <input ref="image" @change="uploadImg()" type="file" id="chooseFile" name="chooseFile" accept="image/*">
    </form>
    <img :src="image" alt="">
  </div>
</template>

<script>
import EXIF from 'exif-js'

export default {
  data() {
    return {
      image : ''
    }
  },
  methods: {
    uploadImg() {
      console.log('들어왔다')
      var image = this.$refs['image'].files[0]

      const url = URL.createObjectURL(image)
      this.image = url
      console.log(image)

      EXIF.getData(image, function() {
        var exifLong = EXIF.getTag(this, "GPSLongitude");
        var exifLat = EXIF.getTag(this, "GPSLatitude");
        var exifLongRef = EXIF.getTag(this, "GPSLongitudeRef");
        var exifLatRef = EXIF.getTag(this, "GPSLatitudeRef");
        var latitude
        var longitude
        if (exifLatRef == "S") {
            latitude = (exifLat[0]*-1) + (( (exifLat[1]*-60) + (exifLat[2]*-1) ) / 3600);                        
        } else {
            latitude = exifLat[0] + (( (exifLat[1]*60) + exifLat[2] ) / 3600);
        }

        if (exifLongRef == "W") {
            longitude = (exifLong[0]*-1) + (( (exifLong[1]*-60) + (exifLong[2]*-1) ) / 3600);                        
        } else {
            longitude = exifLong[0] + (( (exifLong[1]*60) + exifLong[2] ) / 3600);
        }

        console.log(latitude)
        console.log(longitude)
      })
    }
  }
}
</script>

<style>

</style>