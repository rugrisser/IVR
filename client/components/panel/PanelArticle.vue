<template>
  <div
    class="panel-article"
    :class="[{ 'not-published': !published }]"
    @click="go"
  >
    <h1 class="title">{{ title }}</h1>
    <p class="description">{{ description }}</p>
    <span class="published">{{ published ? 'Опубликовано' : 'Черновик' }}</span>
  </div>
</template>

<script>
  export default {
    name: 'PanelArticle',
    props: {
      id: {
        type: Number,
        required: true,
      },
      title: {
        type: String,
        required: true,
      },
      description: {
        type: String,
        required: true,
      },
      published: {
        type: Boolean,
        required: true,
      },
    },
    methods: {
      go() {
        if (this.published) {
          this.$router.push(`/news/${this.id}`)
        } else {
          this.$router.push(`/news/edit/${this.id}`)
        }
      },
    },
  }
</script>

<style lang="scss">
  $border-radius: 16px;

  .panel-article {
    padding: 32px 48px;
    background-color: white;
    border: 2px solid rgba(0, 0, 0, 0.25);
    border-width: 2px 2px 0 2px;
    transition: 250ms ease-out;
    .title {
      font-size: 48px;
      font-weight: 500;
      font-family: 'Jost';
    }
    .description {
      font-size: 28px;
      font-weight: 400;
      font-family: 'Jost';
    }
    .published {
      display: block;
      margin-top: 8px;
      font-size: 26px;
      font-weight: 300;
      font-family: 'Jost';
    }
    &:first-child {
      border-radius: $border-radius $border-radius 0 0;
    }
    &:last-child {
      border-radius: 0 0 $border-radius $border-radius;
      border-width: 2px;
    }
    &:hover {
      cursor: pointer;
      background-color: rgba($color: $primary-color, $alpha: 0.2);
    }
  }
  .not-published {
    background-color: rgba($color: #000000, $alpha: 0.1);
  }
</style>
