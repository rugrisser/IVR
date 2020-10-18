<template>
  <div class="article-card">
    <h1>{{ title }}</h1>
    <p>{{ text }}</p>
    <div class="time d-flex flex-row">
      <img src="/img/ui/clock.svg" />
      <span>{{ date }}</span>
    </div>
  </div>
</template>

<script lang="ts">
  import Vue from 'vue'

  export default Vue.extend({
    name: 'ArticleCard',
    props: {
      title: {
        type: String,
        required: true,
      },
      text: {
        type: String,
        required: true,
      },
      date: {
        type: String,
        required: true,
      },
    },
  })
</script>

<style lang="scss">
  .article-card {
    display: flex;
    cursor: pointer;
    flex-direction: column;
    background-color: white;
    padding: 48px;
    border-radius: 32px;
    margin-bottom: 48px;
    box-shadow: 0px 0px 32px rgba(0, 0, 0, 0.15);
    transition: 250ms ease-out;
    h1 {
      font-size: 48px;
      font-weight: 500;
      font-family: 'Jost';
    }
    p {
      font-size: 36px;
      font-weight: 300;
      font-family: 'Jost';
    }
    &:hover {
      box-shadow: 0px 0px 32px rgba(0, 0, 0, 0.25);
    }
    .time {
      margin-top: 24px;
      img {
        height: 28px;
      }
      span {
        color: $primary-color-dark;
        margin: auto 0 auto 12px;
        font-size: 18px;
        font-weight: 500;
        font-family: 'Jost';
      }
    }
  }
</style>
