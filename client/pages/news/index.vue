<template>
  <div class="news default-container">
    <div class="row">
      <div class="col-12 col-md-6 d-flex flex-column sidebar-fixed">
        <div style="margin: 0 auto 48px">
          <Logo color="black" />
        </div>
        <SidebarMenu :items="menuItems" />
      </div>
      <div class="col-12 col-md-6 sidebar"></div>
      <div class="col-12 col-md-6 news-feed">
        <ArticleCard
          title="Lorem Ipsum"
          text="Lorem ipsum dolor sit amet, consectetur adipiscing elit. Suspendisse eleifend lectus nascetur massa cras in. Arcu turpis dolor lacus, quis eget turpis tellus dui."
          date="5 минут назад"
        />
        <ArticleCard
          title="Lorem Ipsum"
          text="Lorem ipsum dolor sit amet, consectetur adipiscing elit. Suspendisse eleifend lectus nascetur massa cras in. Arcu turpis dolor lacus, quis eget turpis tellus dui."
          date="5 минут назад"
        />
      </div>
    </div>
  </div>
</template>

<script lang="ts">
  import Vue from 'vue'
  import Logo from '~/components/logo/Logo.vue'
  import SidebarMenu from '~/components/menu/sidebar/SidebarMenu.vue'
  import ArticleCard from '~/components/ArcticleCard.vue'
  import { MenuItem } from '~/assets/ts'

  export default Vue.extend({
    components: {
      Logo,
      SidebarMenu,
      ArticleCard,
    },
    data() {
      return {
        menuItems: [
          new MenuItem('Новости', 'news.svg', '/news'),
          new MenuItem('Обращения', 'speaker.svg', '/appeals'),
          new MenuItem('Техподдержка', 'siren.svg', '/support'),
          new MenuItem('Вход', 'key.svg', '/login'),
        ],
      }
    },
  })
</script>

<style lang="scss">
  body {
    background-color: white !important;
  }
  .sidebar {
    max-width: $max-sidebar-menu-width;
  }
  .sidebar-fixed {
    position: fixed;
    max-width: $max-sidebar-menu-width;
  }
  .news-feed {
    padding-top: 204px;
    padding-left: 48px;
  }
</style>
