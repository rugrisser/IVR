<template>
  <div class="logout"></div>
</template>

<script>
  import { mapActions } from 'vuex'
  export default {
    mounted() {
      this.$store.dispatch('logout')
      this.$router.push('/login')
    },
    methods: {
      ...mapActions(['logout']),
    },
  }
</script>

<style lang="scss"></style>
