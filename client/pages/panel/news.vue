<template>
  <div class="panel-news panel-default-container">
    <h1>Новости</h1>
    <div class="button" @click="$router.push('/news/edit')">
      <img src="/img/ui/plus.svg" />
      <span>Новая статья</span>
    </div>
    <div class="panel-news-list">
      <PanelArticle
        v-for="(item, index) in articles"
        :id="item.id"
        :key="index"
        :title="item.title"
        :description="item.description"
        :published="item.published"
      />
    </div>
  </div>
</template>

<script>
  import PanelArticle from '~/components/panel/PanelArticle'

  export default {
    layout: 'panel',
    components: {
      PanelArticle,
    },
    data() {
      return {
        articles: [],
      }
    },
    mounted() {
      this.$axios.$get('/news/all').then((response) => {
        this.articles = response.reverse()
      })
    },
  }
</script>

<style lang="scss">
  .button {
    cursor: pointer;
    display: inline-flex;
    padding: 18px 36px;
    background-color: primary-color-opacity(15);
    border-radius: 100px;
    margin-top: 24px;
    margin-bottom: 32px;
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
</style>
