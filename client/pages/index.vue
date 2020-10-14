<template>
  <div>
    <div class="menu" :class="{ closed: !menuOpened }" ref="menu">
      <div class="left-border"></div>
      <div class="content">
        <h1>Меню</h1>
        <MainMenu :items="menuItems" />
      </div>
    </div>
    <div class="home" @click="closeMenu">
      <div class="row">
        <div class="col-12 col-md-6">
          <LogoWhite text />
        </div>
        <div class="col-12 col-md-6 d-flex">
          <img
            ref="menuButton"
            src="/img/ui/menu/menu_white.svg"
            class="menu-button"
            @click="openMenu"
          />
        </div>
      </div>
      <div class="row">
        <div class="col-12 col-md-6 info-block">
          <h1>Добро пожаловать!</h1>
          <p>
            Вы попали на платформу для учеников лицея НИУ ВШЭ, которая
            направлена на наиболее удобное и простое взаимодействие с советом.
            Здесь вы сможете оставить обращение в совет, которое будет
            рассмотрено в кратчайшие сроки; выражать своё мнение о текущих
            вопросах; а также получать самые свежие новости о работе совета
          </p>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
  import Vue from 'vue'
  import LogoWhite from '~/components/logo/LogoWhite.vue'
  import MainMenu from '~/components/menu/main/MainMenu.vue'
  import { MenuItem } from '~/assets/ts'

  export default Vue.extend({
    components: {
      LogoWhite,
      MainMenu,
    },
    methods: {
      openMenu() {
        this.menuOpened = true
      },
      closeMenu(event) {
        if (event.target !== this.$refs.menuButton) {
          this.menuOpened = false
        }
      },
    },
    data() {
      return {
        menuItems: [
          new MenuItem('Новости', 'news.svg', '/news'),
          new MenuItem('Обращения', 'speaker.svg', '/appeals'),
          new MenuItem('Техподдержка', 'siren.svg', '/support'),
          new MenuItem('Вход', 'key.svg', '/login'),
        ],
        menuOpened: false,
      }
    },
  })
</script>

<style lang="scss">
  body {
    overflow: hidden;
    background-color: $primary-color;
  }
  .home {
    margin: 72px 72px 0;
  }
  .info-block {
    margin-top: 48px;
    margin-left: 72px;
    h1 {
      color: white;
      font-size: 48px;
      font-weight: 500;
      font-family: 'Jost';
    }
    p {
      color: rgba($color: white, $alpha: 0.7);
      margin-top: 24px;
      font-size: 36px;
      font-weight: 400;
      font-family: 'Jost';
    }
  }
  .menu-button {
    cursor: pointer;
    height: 36px;
    margin: auto 72px auto auto;
  }
  .menu {
    top: 0;
    display: flex;
    left: calc(100vw - 550px);
    width: 550px;
    position: absolute;
    height: 100vh;
    background-color: $primary-color-dark;
    transition: left 350ms ease-out;
    box-shadow: inset 0 0 36px rgba($color: #000000, $alpha: 0.25);
    z-index: 100;
    .left-border {
      position: absolute;
      background-color: $primary-color;
      height: 100vh;
      width: 64px;
      border-radius: 0 64px 64px 0;
    }
    .content {
      display: flex;
      flex-direction: column;
      margin: auto auto auto 128px;
      h1 {
        margin-left: 64px;
        color: white;
        font-size: 48px;
        font-weight: 600;
        // font-stretch: expanded;
        font-family: 'Jost';
      }
    }
  }
  .closed {
    left: 100vw !important;
  }
</style>
