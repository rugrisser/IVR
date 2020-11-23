<template>
  <div>
    <PanelMenu
      :items="panelMenuItems"
      @mouseenter.native="shutter = true"
      @mouseleave.native="shutter = false"
    />
    <div v-if="shutter" class="shutter"></div>
    <Nuxt />
  </div>
</template>

<script>
  import { mapGetters, mapActions } from 'vuex'
  import { generatePanelMenu } from '~/assets/js'
  import PanelMenu from '~/components/menu/panel/PanelMenu'

  export default {
    components: {
      PanelMenu,
    },
    data() {
      return {
        panelMenuItems: [],
        shutter: false,
      }
    },
    computed: {
      ...mapGetters(['isLogged', 'getToken']),
    },
    mounted() {
      this.$store.dispatch('updateToken')

      if (this.isLogged) {
        generatePanelMenu(this, this.getToken)
          .then((response) => {
            if (response.length > 1) {
              let result = false
              const path = this.$route.path
              this.panelMenuItems = response

              this.panelMenuItems.forEach((item) => {
                if (item.link === path) {
                  result = true
                }
              })

              if (!result) {
                this.$router.push('/panel')
              }
            } else {
              this.$router.push('/news')
            }
          })
          .catch(() => {
            this.$router.push('/news')
          })
      } else {
        this.$router.push('/news')
      }
    },
    methods: {
      ...mapActions(['updateToken']),
    },
  }
</script>

<style lang="scss">
  .shutter {
    position: absolute;
    width: 100vw;
    height: 100vh;
    background-color: rgba($color: #000000, $alpha: 0.25);
    z-index: 1000;
  }
</style>
