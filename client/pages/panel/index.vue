<template>
  <div></div>
</template>

<script>
  import { mapActions, mapGetters } from 'vuex'
  import { getRole } from '~/assets/js'

  export default {
    computed: {
      ...mapGetters(['isLogged', 'getToken']),
    },
    mounted() {
      this.$store.dispatch('updateToken')

      if (this.isLogged) {
        getRole(this, this.getToken)
          .then((result) => {
            let route = '/news'
            switch (result) {
              case 'admin':
                route = '/panel/support'
                break
              case 'deputy':
              case 'chairman':
                route = '/panel/appeals'
                break
              default:
                break
            }
            this.$router.push(route)
          })
          .catch(() => this.$router.push('/news'))
      } else {
        this.$router.push('/news')
      }
    },
    methods: {
      ...mapActions(['updateToken']),
    },
  }
</script>
