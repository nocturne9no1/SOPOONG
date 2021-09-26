<template>
  <!-- base code -->
  <!-- <gmap-polyline v-bind:path.sync="path" v-bind:options="{ strokeColor:'#008000'}"/> -->
  <div>
    <!-- 아무것도 선택하지 않았을 떄 : 회색 -->
    <div v-if="eachPath.transport=''">
      <gmap-polyline v-bind:path.sync="eachPath.position" v-bind:options="{ strokeColor:'#656565'}"/>
    </div>
    <div v-else-if="eachPath.transport='plane'">
      <gmap-polyline v-bind:path.sync="eachPath.position" v-bind:options="{ strokeColor:'#4fb88a'}"/>
    </div>
    <div v-else-if="eachPath.transport='train'">
      <gmap-polyline v-bind:path.sync="eachPath.position" v-bind:options="{ strokeColor:'#4f7bb8'}"/>
    </div>
    <div v-else-if="eachPath.transport='bus'">
      <gmap-polyline v-bind:path.sync="eachPath.position" v-bind:options="{ strokeColor:'#a54fb8'}"/>
    </div>
    <div v-else-if="eachPath.transport='car'">
      <gmap-polyline v-bind:path.sync="eachPath.position" v-bind:options="{ strokeColor:'#b89e4f'}"/>
    </div>
    <div v-else-if="eachPath.transport='walking'">
      <gmap-polyline v-bind:path.sync="eachPath.position" v-bind:options="{ strokeColor:'#b8594f'}"/>
    </div>
  </div>
</template>
<script>
export default {
  name:'eachpolyline',
  components: {},
  props: {
    eachPath: Object,
  },
  data() {
    return {
      sampleData: ''
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
  // computed: {
  //   path() {
  //     return this.eachPath.position
  //   },

  //   transport() {
  //     return this.eachPath.transport
  //   },

  // },
  methods: {}
}
</script>
