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
      <div class="col-12 col-md-6 ticket-create-content">
        <div class="ticket-create-content-head">
          <h1>Новый тикет</h1>
        </div>
        <div class="ticket-create-content-body">
          <FormInput
            v-model="text"
            multiline
            :multiline-rows="5"
            placeholder="С чем у вас возникли проблемы?"
          />
          <PrimaryButton style="margin-top: 24px" @click.native="submit">
            Отправить
          </PrimaryButton>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import { mapGetters, mapActions } from 'vuex'
  import Logo from '~/components/logo/Logo.vue'
  import SidebarMenu from '~/components/menu/sidebar/SidebarMenu.vue'
  import FormInput from '~/components/form/FormInput.vue'
  import PrimaryButton from '~/components/button/PrimaryButton.vue'
  import { generateMainMenu } from '~/assets/js'

  export default {
    components: {
      Logo,
      SidebarMenu,
      FormInput,
      PrimaryButton,
    },
    data() {
      return {
        text: '',
        menuItems: [],
      }
    },
    computed: {
      ...mapGetters({
        logged: 'isLogged',
        token: 'getToken',
      }),
    },
    mounted() {
      this.$store.dispatch('updateToken')
      generateMainMenu(this, this.token).then(
        (result) => (this.menuItems = result),
      )

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
    },
    methods: {
      ...mapActions(['updateToken']),
      submit() {
        this.$axios
          .$post(
            '/support',
            { text: this.text },
            {
              headers: {
                Authorization: 'Bearer ' + this.token,
              },
            },
          )
          .then(() => {
            this.$router.push('/support')
          })
          .catch((error) => {
            console.log(error)
          })
      },
    },
  }
</script>

<style lang="scss">
  .ticket-create-content {
    padding-left: 48px;
    .ticket-create-content-head {
      display: flex;
      height: 204px;
      h1 {
        font-size: 64px;
        font-weight: 600;
        font-family: 'Jost';
        margin: auto 0 24px;
      }
    }
  }
</style>
