<template>
  <div>
    <PanelMenu :items="panelMenuItems" />
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
              this.panelMenuItems = response
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
