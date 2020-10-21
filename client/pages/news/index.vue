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
          v-for="article in articles"
          :id="article.id"
          :key="article.id"
          :title="article.title"
          :text="article.description"
          date="Сегодня"
        />
      </div>
    </div>
  </div>
</template>

<script lang="ts">
  import Vue from 'vue'
  import { mapGetters } from 'vuex'
  import Logo from '~/components/logo/Logo.vue'
  import SidebarMenu from '~/components/menu/sidebar/SidebarMenu.vue'
  import ArticleCard from '~/components/ArcticleCard.vue'

  export default Vue.extend({
    components: {
      Logo,
      SidebarMenu,
      ArticleCard,
    },
    data() {
      return {
        articles: [],
      }
    },
    computed: {
      ...mapGetters({
        menuItems: 'getSidebarMenuItems',
      }),
    },
    mounted() {
      this.$axios.$get('/news').then((response) => {
        this.articles = response.reverse()
      })
    },
  })
</script>

<style lang="scss">
  body {
    background-color: white !important;
  }
  .news-feed {
    padding-top: 204px;
    padding-left: 48px;
  }
</style>
