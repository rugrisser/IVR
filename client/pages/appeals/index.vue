<template>
  <div class="appeals default-container">
    <div class="row">
      <div class="col-12 col-md-6 d-flex flex-column sidebar-fixed">
        <div style="margin: 0 auto 48px">
          <Logo color="black" />
        </div>
        <SidebarMenu :items="menuItems" />
      </div>
      <div class="col-12 col-md-6 sidebar"></div>
      <div class="col-12 col-md-6 appeals-feed">
        <div class="head">
          <div v-if="user" class="buttons">
            <div @click="$router.push('/appeals/edit')">
              <img src="/img/ui/plus.svg" />
              <span>Создать обращение</span>
            </div>
            <div
              v-if="ownAppealsLen >= 0"
              @click="ownView = !ownView"
              :class="{ selected: ownView }"
            >
              <span>Ваши обращения: {{ ownAppealsLen }}</span>
            </div>
          </div>
        </div>
        <div class="row">
          <AppealCard
            v-for="appeal in appeals"
            :key="appeal.id"
            :id="appeal.id"
            :text="appeal.title"
            :type="appeal.type.name"
            date="Сегодня"
            :status="appeal.status.name"
          />
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  /* eslint-disable vue/no-side-effects-in-computed-properties */
  import { mapGetters, mapActions } from 'vuex'
  import Logo from '~/components/logo/Logo.vue'
  import SidebarMenu from '~/components/menu/sidebar/SidebarMenu.vue'
  import AppealCard from '~/components/AppealCard.vue'

  export default {
    components: {
      Logo,
      SidebarMenu,
      AppealCard,
    },
    data() {
      return {
        allAppeals: [],
        ownAppeals: [],
        ownView: false,
        user: false,
      }
    },
    computed: {
      ...mapGetters({
        menuItems: 'getSidebarMenuItems',
        logged: 'isLogged',
        token: 'getToken',
      }),
      appeals() {
        return this.ownView
          ? this.ownAppeals.reverse()
          : this.allAppeals.reverse()
      },
      ownAppealsLen() {
        return this.ownAppeals.length
      },
    },
    mounted() {
      this.$store.dispatch('updateToken')
      this.$axios.$get('/appeal').then((response) => {
        this.allAppeals = response
      })
      this.$axios
        .$get('/appeal/own', {
          headers: {
            Authorization: 'Bearer ' + this.token,
          },
        })
        .then((response) => {
          this.ownAppeals = response
        })
      if (this.logged) {
        this.$axios
          .$get('/user/getRole', {
            headers: {
              Authorization: 'Bearer ' + this.token,
            },
          })
          .then((response) => {
            if (response.name === 'user') {
              this.user = true
            } else {
              this.user = false
            }
          })
          .catch(() => (this.user = false))
      } else {
        this.user = false
      }
    },
    methods: {
      ...mapActions(['updateToken']),
    },
  }
</script>

<style lang="scss">
  .appeals-feed {
    .head {
      display: flex;
      height: 204px;
      .buttons {
        display: inline-flex;
        flex-direction: row;
        flex-wrap: wrap;
        margin: auto 0 20px;
        div {
          cursor: pointer;
          display: inline-flex;
          padding: 18px 36px;
          background-color: primary-color-opacity(15);
          border-radius: 100px;
          margin-right: 32px;
          margin-bottom: 16px;
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
        .selected {
          background-color: $primary-color;
          span {
            color: white;
          }
          &:hover {
            background-color: $primary-color-dark;
          }
        }
      }
    }
  }
</style>
