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
        <div class="head"></div>
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
  import AppealCard from '~/components/AppealCard.vue'

  export default Vue.extend({
    components: {
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
    }
  }
</style>
