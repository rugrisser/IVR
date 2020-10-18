<template>
  <div class="main-menu-item" @click="go">
    <img :src="iconLink" />
    <span>{{ text }}</span>
  </div>
</template>

<script lang="ts">
  import Vue from 'vue'
  export default Vue.extend({
    name: 'MainMenuItem',
    props: {
      text: {
        type: String,
        required: true,
      },
      icon: {
        type: String,
        required: true,
      },
      link: {
        type: String,
        required: true,
      },
    },
    data() {
      return {
        iconLinkAlias: '/img/ui/menu/icon/white/',
      }
    },
    computed: {
      iconLink(): string {
        return this.iconLinkAlias + this.icon
      },
    },
    methods: {
      go() {
        this.$router.push(this.link)
      },
    },
  })
</script>

<style lang="scss" scoped>
  .main-menu-item {
    cursor: pointer;
    margin-top: 24px;
    display: flex;
    flex-direction: row;
    img {
      width: 32px;
      height: 32px;
      margin: auto 0;
    }
    span {
      color: white;
      margin: auto 0 auto 32px;
      font-size: 36px;
      font-family: 'Jost';
    }
    &:first-child {
      margin-top: 48px;
    }
  }
</style>
