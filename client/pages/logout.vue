<template>
  <div class="logout"></div>
</template>

<script lang="ts">
  import Vue from 'vue'
  import { mapActions } from 'vuex'

  export default Vue.extend({
    methods: {
      ...mapActions(['logout']),
    },
    mounted() {
      this.$store.dispatch('logout')
      this.$router.push('/login')
    },
  })
</script>

<style lang="scss"></style>
