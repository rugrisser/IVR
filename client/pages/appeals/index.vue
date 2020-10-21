<template>
  <div class="appeals default-container">
    <div class="row">
      <div class="col-12 col-md-6 d-flex flex-column sidebar-fixed">
        <div style="margin: 0 auto 48px">
          <Logo color="black" />
        </div>
        <SidebarMenu :items="menuItems" />
      </div>
      <div class="col-12 col-md-6 sidebar"></div>
      <div class="col-12 col-md-6 appeals-feed">
        <div class="head">
          <div class="buttons">
            <div @click="$router.push('/appeals/edit')">
              <img src="/img/ui/plus.svg" />
              <span>Создать обращение</span>
            </div>
            <div>
              <span>Ваши обращения: 2</span>
            </div>
          </div>
        </div>
        <div class="row">
          <AppealCard
            :id="1"
            text="Закупить стаканчики для кулеров"
            type="proposal"
            date="Неделю назад"
            status="reviewed"
          />
          <AppealCard
            :id="2"
            text="Установить велопарковку на БХ"
            type="proposal"
            date="Две недели назад"
            status="consideration"
          />
          <AppealCard
            :id="3"
            text="Lorem Ipsum"
            type="proposal"
            date="Две недели назад"
            status="rejected"
          />
          <AppealCard
            :id="4"
            text="Lorem Ipsum"
            type="complaint"
            date="Месяц назад"
            status="modearation"
          />
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
  import Vue from 'vue'
  import { mapGetters } from 'vuex'
  import Logo from '~/components/logo/Logo.vue'
  import SidebarMenu from '~/components/menu/sidebar/SidebarMenu.vue'
  import AppealCard from '~/components/AppealCard.vue'

  export default Vue.extend({
    components: {
      Logo,
      SidebarMenu,
      AppealCard,
    },
    computed: {
      ...mapGetters({
        menuItems: 'getSidebarMenuItems',
      }),
    },
  })
</script>

<style lang="scss">
  .appeals-feed {
    .head {
      display: flex;
      height: 204px;
      .buttons {
        display: inline-flex;
        flex-direction: row;
        flex-wrap: wrap;
        margin: auto 0 20px;
        div {
          cursor: pointer;
          display: inline-flex;
          padding: 18px 36px;
          background-color: primary-color-opacity(15);
          border-radius: 100px;
          margin-right: 32px;
          margin-bottom: 16px;
          transition: 200ms ease-out;
          img {
            height: 32px;
            width: auto;
            margin: auto 12px auto 0;
          }
          span {
            color: $primary-color-dark;
            font-size: 24px;
            font-family: 'Jost';
          }
          &:hover {
            background-color: primary-color-opacity(25);
          }
          &:last-child {
            margin-right: 0;
          }
        }
      }
    }
  }
</style>
