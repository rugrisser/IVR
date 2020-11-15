<template>
  <div class="appeal default-container">
    <div class="row">
      <div class="col-12 col-md-6 d-flex flex-column sidebar-fixed">
        <div style="margin: 0 auto 48px">
          <Logo color="black" />
        </div>
        <SidebarMenu :items="menuItems" />
      </div>
      <div class="col-12 col-md-6 sidebar"></div>
      <div class="col-12 col-md-6 news-edit-content">
        <div class="news-edit-head">
          <h1 v-if="isEdit">Редактирование</h1>
          <h1 v-else>Новая статья</h1>
        </div>
        <div class="news-edit-body">
          <FormInput v-model="article.title" placeholder="Заголовок" />
          <FormInput
            v-model="article.description"
            placeholder="Описание (выводится в ленте)"
            multiline
          />
          <FormInput
            v-model="article.text"
            placeholder="Текст (используйте '# ' для заголовков)"
            :multiline-rows="10"
            multiline
          />
          <div class="buttons">
            <PrimaryButton
              style="margin-right: 24px"
              @click.native="submit(true)"
            >
              Опубликовать
            </PrimaryButton>
            <SecondaryButton @click.native="submit(false)">
              Сохранить черновик
            </SecondaryButton>
          </div>
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
  import SecondaryButton from '~/components/button/SecondaryButton.vue'

  export default {
    components: {
      Logo,
      SidebarMenu,
      FormInput,
      PrimaryButton,
      SecondaryButton,
    },
    data() {
      return {
        id: 0,
        isEdit: false,
        article: {
          title: '',
          description: '',
          text: '',
        },
      }
    },
    computed: {
      ...mapGetters({
        menuItems: 'getSidebarMenuItems',
        logged: 'isLogged',
        token: 'getToken',
      }),
    },
    mounted() {
      this.$store.dispatch('updateToken')

      if (!this.logged) {
        this.$router.push('/news')
      }

      if (this.logged) {
        this.$axios
          .$get('/user/getRole', {
            headers: {
              Authorization: 'Bearer ' + this.token,
            },
          })
          .then((response) => {
            if (response.name === 'user') {
              this.$router.push('/news')
            }
          })
          .catch(() => this.$router.push('/news'))
      }

      const id = parseInt(this.$route.params.id)

      if (isNaN(id)) {
        this.isEdit = false
        this.id = 0
      } else {
        this.isEdit = true
        this.id = id
        this.$axios
          .$get('/news/' + id)
          .then((response) => {
            this.article = response
          })
          .catch((error) => {
            console.log(error)
            this.$router.push('/news')
          })
      }
    },
    methods: {
      ...mapActions(['updateToken']),
      submit(publish) {
        if (this.isEdit) {
          const body = {
            id: this.id,
            publish,
            title: this.article.title,
            description: this.article.description,
            text: this.article.text,
          }

          this.$axios
            .$put('/news/', body, {
              headers: {
                Authorization: 'Bearer ' + this.token,
              },
            })
            .then(() => {
              this.$router.push('/news/' + this.id)
            })
            .catch((error) => {
              console.log(error)
            })
        } else {
          const body = {
            publish,
            title: this.article.title,
            description: this.article.description,
            text: this.article.text,
          }

          this.$axios
            .$post('/news', body, {
              headers: {
                Authorization: 'Bearer ' + this.token,
              },
            })
            .then((response) => {
              this.$router.push('/news/' + response.id)
            })
            .catch((error) => {
              console.log(error)
            })
        }
      },
    },
  }
</script>

<style lang="scss">
  .news-edit-content {
    margin-left: 48px;
    .news-edit-head {
      display: flex;
      height: 204px;
      h1 {
        font-size: 64px;
        font-weight: 600;
        font-family: 'Jost';
        margin: auto 0 24px;
      }
    }
    .news-edit-body {
      .buttons {
        margin-top: 32px;
      }
    }
  }
</style>
