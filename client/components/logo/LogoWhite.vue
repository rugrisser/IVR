<template>
  <div class="logo-white" :style="{ height: height + 'px' }">
    <img src="/img/logo/logo_transparent_white.png" style="height: 100%;">
    <span v-if="text">Совет лицеистов</span>
  </div>
</template>

<script lang="ts">
  import Vue from 'vue'

  export default Vue.extend({
    props: {
      height: {
        type: Number,
        required: false,
        default: 150,
      },
      text: {
        type: Boolean,
        required: false,
        default: false,
      }
    },
  })
</script>

<style lang="scss">
  .logo-white {
    display: flex;
    span {
      color: white;
      margin: auto 0 auto 28px;
      font-size: 48px;
      font-family: 'Jost';
    }
  }
</style>