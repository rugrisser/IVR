<template>
  <div class="news-article default-container">
    <div class="row">
      <div class="col-12 col-md-6 d-flex flex-column sidebar-fixed">
        <div style="margin: 0 auto 48px">
          <Logo color="black" />
        </div>
        <SidebarMenu :items="menuItems" />
      </div>
      <div class="col-12 col-md-6 sidebar"></div>
      <div class="col-12 col-md-6 news-content">
        <div class="heading">
          <h1>{{ article.title }}</h1>
        </div>
        <div class="main-content">
          <div
            v-for="(paragraph, index) in text"
            :key="index"
            class="paragraph"
          >
            <h2 v-if="paragraph.type == 'h2'">{{ paragraph.content }}</h2>
            <p v-else>{{ paragraph.content }}</p>
          </div>
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

  export default Vue.extend({
    components: {
      Logo,
      SidebarMenu,
    },
    data() {
      return {
        articleID: 0,
        article: {
          title: '',
          text: '',
        },
      }
    },
    computed: {
      ...mapGetters({
        menuItems: 'getSidebarMenuItems',
      }),
      text() {
        const paragraphs = this.article.text.split('\n')
        const result = []

        for (const paragraph of paragraphs) {
          if (paragraph.substring(0, 1) === '#') {
            result.push({
              type: 'h2',
              content: paragraph.substring(2),
            })
          } else {
            result.push({
              type: 'p',
              content: paragraph,
            })
          }
        }

        return result
      },
    },
    mounted() {
      const parsed = parseInt(this.$route.params.id)
      if (isNaN(parsed)) {
        this.$router.push('/news')
      } else {
        this.articleID = parsed
        this.$axios
          .$get('/news/' + parsed)
          .then((response) => {
            this.article = response
          })
          .catch(() => {
            this.$router.push('/news')
          })
      }
    },
  })
</script>

<style lang="scss">
  .news-content {
    margin-left: 48px;
    .heading {
      display: flex;
      height: 204px;
      h1 {
        font-size: 64px;
        font-weight: 600;
        font-family: 'Jost';
        margin: auto 0 24px;
      }
    }
    .main-content {
      p {
        font-size: 32px;
        font-weight: 300;
        font-family: 'Jost';
      }
      h2 {
        font-size: 48px;
        font-weight: 500;
        font-family: 'Jost';
        margin: 36px 0;
      }
    }
  }
</style>
