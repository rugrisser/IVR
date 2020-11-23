<template>
  <div class="panel-appeals panel-default-container">
    <h1>Обращения</h1>
    <div class="panel-appeals-list">
      <PanelAppeal
        v-for="(appeal, index) in appeals"
        :id="appeal.id"
        :key="index"
        :title="appeal.title"
        :author="appeal.author"
        :type="appeal.type"
        :status="appeal.status"
      />
    </div>
  </div>
</template>

<script>
  import PanelAppeal from '~/components/panel/PanelAppeal'

  export default {
    layout: 'panel',
    components: {
      PanelAppeal,
    },
    data() {
      return {
        appeals: [],
      }
    },
    mounted() {
      this.$axios.$get('/appeal/all').then((response) => {
        this.appeals = response
        this.response.filter((item) => item.published === true)
      })
    },
  }
</script>

<style lang="scss">
  .panel-appeals-list {
    margin-top: 32px;
  }
</style>
