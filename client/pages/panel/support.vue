<template>
  <div class="panel-support panel-default-container">
    <h1>Тикеты</h1>
    <div class="panel-support-list">
      <PanelTicket
        v-for="(ticket, index) in tickets"
        :id="ticket.id"
        :key="index"
        :text="ticket.text"
        :opened="ticket.opened"
        :response="ticket.response"
      />
    </div>
  </div>
</template>

<script>
  import { mapGetters, mapActions } from 'vuex'
  import PanelTicket from '~/components/panel/PanelTicket'

  export default {
    layout: 'panel',
    components: {
      PanelTicket,
    },
    data() {
      return {
        tickets: [],
      }
    },
    computed: {
      ...mapGetters(['getToken']),
    },
    mounted() {
      this.$store.dispatch('updateToken')
      this.$axios
        .$get('/support/all', {
          headers: {
            Authorization: 'Bearer ' + this.getToken,
          },
        })
        .then((response) => {
          this.tickets = response.reverse()
        })
        .catch(() => this.$router.push('/panel'))
    },
    methods: {
      ...mapActions(['updateToken']),
    },
  }
</script>

<style lang="scss">
  .panel-support-list {
    margin-top: 32px;
  }
</style>
