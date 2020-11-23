<template>
  <div class="panel-menu">
    <div class="panel-menu-content">
      <div class="panel-menu-head">
        <Logo :height="64" color="white" />
      </div>
      <NuxtLink
        v-for="(item, index) in items"
        :key="index"
        class="panel-menu-item"
        :to="item.link"
        tag="div"
      >
        <img :src="iconLinkAlias + item.icon" />
        <span>{{ item.text }}</span>
      </NuxtLink>
    </div>
  </div>
</template>

<script>
  import Logo from '~/components/logo/Logo'

  export default {
    name: 'PanelMenu',
    components: {
      Logo,
    },
    props: {
      items: {
        type: Array,
        required: true,
      },
    },
    data() {
      return {
        iconLinkAlias: '/img/ui/menu/icon/white/',
      }
    },
  }
</script>

<style lang="scss">
  $full-width: 400px;

  .panel-menu {
    position: fixed;
    display: flex;
    flex-direction: column;
    width: 96px;
    height: 100vh;
    background-color: black;
    overflow: hidden;
    transition: ease-out 300ms;
    overflow: hidden;
    .panel-menu-content {
      width: $full-width;
      .panel-menu-head {
        padding: 16px;
        background-color: $primary-color-dark;
      }
      .panel-menu-item {
        cursor: pointer;
        display: flex;
        flex-direction: row;
        transition: ease-out 300ms;
        &:hover {
          background-color: rgba($color: #ffffff, $alpha: 0.15);
        }
        img {
          width: 36px;
          margin: 30px;
        }
        span {
          color: white;
          font-size: 28px;
          font-family: 'Jost';
          margin: auto 0;
        }
      }
    }
    &:hover {
      width: $full-width;
      box-shadow: 0 0 32px rgba($color: #000000, $alpha: 0.25);
    }
  }
</style>
