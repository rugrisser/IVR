<template>
  <div class="main-menu">
    <MainMenuItem
      v-for="(item, index) in items"
      :key="index"
      :text="item.text"
      :icon="item.icon"
      :link="item.link"
    />
  </div>
</template>

<script>
  import MainMenuItem from '@/components/menu/main/MainMenuItem'
  export default {
    name: 'MainMenu',
    components: {
      MainMenuItem,
    },
    props: {
      items: {
        type: Array,
        required: true,
      },
    },
  }
</script>

<style lang="scss" scoped></style>
