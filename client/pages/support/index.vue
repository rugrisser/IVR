<template>
  <div class="support default-container">
    <div class="row">
      <div class="col-12 col-md-6 d-flex flex-column sidebar-fixed">
        <div style="margin: 0 auto 48px">
          <Logo color="black" />
        </div>
        <SidebarMenu :items="menuItems" />
      </div>
      <div class="col-12 col-md-6 sidebar"></div>
      <div class="col-12 col-md-6 support-content">
        <div class="support-content-head">
          <div class="button" @click="$router.push('/support/create')">
            <img src="/img/ui/plus.svg" />
            <span>Новый тикет</span>
          </div>
        </div>
        <div class="support-content-body">
          <Ticket
            v-for="ticket in tickets"
            :key="ticket.id"
            :text="ticket.text"
            :solution="ticket.response"
          />
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
  import Vue from 'vue'
  import { mapGetters, mapActions } from 'vuex'
  import Logo from '~/components/logo/Logo.vue'
  import Ticket from '~/components/Ticket.vue'
  import SidebarMenu from '~/components/menu/sidebar/SidebarMenu.vue'

  export default Vue.extend({
    components: {
      Logo,
      SidebarMenu,
      Ticket,
    },
    data() {
      return {
        tickets: [],
      }
    },
    computed: {
      ...mapGetters({
        menuItems: 'getSidebarMenuItems',
        logged: 'isLogged',
        token: 'getToken',
      }),
    },
    methods: {
      ...mapActions(['updateToken']),
    },
    mounted() {
      this.$store.dispatch('updateToken')

      if (this.logged) {
        this.$axios
          .$get('/user/getRole', {
            headers: {
              Authorization: 'Bearer ' + this.token,
            },
          })
          .then((response) => {
            if (response.name === 'admin') {
              this.$router.push('/admin')
            }
          })
          .catch(() => this.$router.push('/login'))
      } else {
        this.$router.push('/login')
      }

      this.$axios
        .$get('/support', {
          headers: {
            Authorization: 'Bearer ' + this.token,
          },
        })
        .then((response) => {
          this.tickets = response.reverse()
        })
    },
  })
</script>

<style lang="scss">
  .support-content {
    padding-left: 48px;
    .support-content-head {
      display: flex;
      height: 204px;
      .button {
        cursor: pointer;
        display: inline-flex;
        padding: 18px 36px;
        background-color: primary-color-opacity(15);
        border-radius: 100px;
        margin: auto auto 24px 0;
        transition: 200ms ease-out;
        img {
          height: 32px;
          width: auto;
          margin: auto 12px auto 0;
        }
        span {
          color: $primary-color-dark;
          font-size: 24px;
          font-family: 'Jost';
        }
        &:hover {
          background-color: primary-color-opacity(25);
        }
        &:last-child {
          margin-right: 0;
        }
      }
    }
  }
</style>
