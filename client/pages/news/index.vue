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
        <div class="news-feed-head">
          <div class="button" @click="$router.push('/news/edit')">
            <img src="/img/ui/plus.svg" />
            <span>Новая статья</span>
          </div>
        </div>
        <div class="news-feed-body">
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
    padding-left: 48px;
    .news-feed-head {
      display: flex;
      height: 204px;
      .button {
        cursor: pointer;
        display: inline-flex;
        padding: 18px 36px;
        background-color: primary-color-opacity(15);
        border-radius: 100px;
        margin: auto auto 24px 0;
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
</style>
