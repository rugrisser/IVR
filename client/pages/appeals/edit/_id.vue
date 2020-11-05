<template>
  <div class="edit-appeal default-container">
    <div class="row">
      <div class="col-12 col-md-6 d-flex flex-column sidebar-fixed">
        <div style="margin: 0 auto 48px">
          <Logo color="black" />
        </div>
        <SidebarMenu :items="menuItems" />
      </div>
      <div class="col-12 col-md-6 sidebar"></div>
      <div class="col-12 col-md-6 create-appeal-content">
        <div class="create-appeal-head">
          <h1 v-if="isEdit">Редактирование</h1>
          <h1 v-else>Новое обращение</h1>
        </div>
        <div class="create-appeal-body">
          <div>
            <span class="step-name">Тип обращения</span>
            <div class="appeal-type">
              <div
                class="appeal-type-card"
                :class="{ checked: type === AppealType.Proposal }"
                @click="checkType(AppealType.Proposal)"
              >
                <img
                  v-if="type === AppealType.Proposal"
                  src="/img/ui/hand_primary.svg"
                />
                <img v-else src="/img/ui/hand.svg" />
                <span>Предложение</span>
              </div>
              <div
                class="appeal-type-card"
                :class="{ checked: type === AppealType.Complaint }"
                @click="checkType(AppealType.Complaint)"
              >
                <img
                  v-if="type === AppealType.Complaint"
                  src="/img/ui/cross_primary.svg"
                />
                <img v-else src="/img/ui/cross.svg" />
                <span>Жалоба</span>
              </div>
            </div>
          </div>
          <FormInput v-model="form.title" placeholder="Заголовок" />
          <FormInput
            v-model="form.text"
            placeholder="Текст обращения"
            multiline
          />
          <span class="step-name">Обратная связь</span>
          <div class="feedback">
            <FormCheckbox id="email" val="email" v-model="form.feedback">
              E-Mail
            </FormCheckbox>
            <FormCheckbox id="vk" val="vk" v-model="form.feedback"
              >ВК</FormCheckbox
            >
          </div>
          <div>
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

<script lang="ts">
  import Vue from 'vue'
  import { mapGetters, mapActions } from 'vuex'
  import Logo from '~/components/logo/Logo.vue'
  import SidebarMenu from '~/components/menu/sidebar/SidebarMenu.vue'
  import FormInput from '~/components/form/FormInput.vue'
  import FormCheckbox from '~/components/form/FormCheckbox.vue'
  import PrimaryButton from '~/components/button/PrimaryButton.vue'
  import SecondaryButton from '~/components/button/SecondaryButton.vue'
  import { AppealType } from '~/assets/ts/appeal'

  export default Vue.extend({
    components: {
      Logo,
      SidebarMenu,
      FormInput,
      FormCheckbox,
      PrimaryButton,
      SecondaryButton,
    },
    data() {
      return {
        isEdit: false,
        id: 0,
        form: {
          title: '',
          text: '',
          feedback: [],
        },
        type: AppealType.Proposal,
        AppealType,
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
      const id = parseInt(this.$route.params.id)

      if (this.logged) {
        this.$axios
          .$get('/user/getRole', {
            headers: {
              'Authorization': 'Bearer ' + this.token,
            },
          })
          .then((response) => {
            if (response.name !== 'user') {
              this.$router.push('/appeals')
            }
          })
          .catch(() => this.$router.push('/login'))
      } else {
        this.$router.push('/login')
      }

      if (isNaN(id)) {
        this.isEdit = false
        this.id = 0
      } else {
        this.isEdit = true
        this.id = id
        this.$axios
          .$get('/appeal/' + id)
          .then((response) => {
            // console.log(response)

            this.form.title = response.title
            this.form.text = response.text

            if (response.type.name === 'complaint') {
              this.type = AppealType.Complaint
            } else {
              this.type = AppealType.Proposal
            }

            if (response.feedback >= 2) {
              this.form.feedback.push('vk')
              response.feedback -= 2
            }
            if (response.feedback === 1) {
              this.form.feedback.push('email')
            }
          })
          .catch((error) => {
            console.log(error)
            this.$router.push('/appeals')
          })
      }
    },
    methods: {
      checkType(type: AppealType): void {
        this.type = type
      },
      submit(publish: Boolean) {
        let feedback = 0
        const type: string =
          this.type === AppealType.Proposal ? 'proposal' : 'complaint'

        if (this.form.feedback.includes('email')) {
          feedback += 1
        }
        if (this.form.feedback.includes('vk')) {
          feedback += 2
        }

        const body = {
          title: this.form.title,
          text: this.form.text,
          feedback,
          type,
          published: publish,
        }

        if (this.isEdit) {
          const body = {
            id: this.id,
            title: this.form.title,
            text: this.form.text,
            feedback,
            type,
            published: publish,
          }

          this.$axios
            .$put('/appeal', body, {
              headers: {
                'Authorization': 'Bearer ' + this.token,
              },
            })
            .then((response) => {
              this.$router.push('/appeals/' + this.id)
            })
            .catch((error) => {
              console.log(error)
            })
        } else {
          const body = {
            title: this.form.title,
            text: this.form.text,
            feedback,
            type,
            published: publish,
          }

          this.$axios
            .$post('/appeal', body, {
              headers: {
                'Authorization': 'Bearer ' + this.token,
              },
            })
            .then((response) => {
              const id = response.id
              this.$router.push('/appeals/' + id)
            })
            .catch((error) => {
              console.log(error)
            })
        }
      },
    },
  })
</script>

<style lang="scss">
  .create-appeal-content {
    margin-left: 48px;
    .create-appeal-head {
      display: flex;
      height: 204px;
      h1 {
        font-size: 64px;
        font-weight: 600;
        font-family: 'Jost';
        margin: auto 0 24px;
      }
    }
    .step-name {
      display: block;
      margin-bottom: 8px;
      font-size: 24px;
      font-family: 'Jost';
    }
  }
  .appeal-type {
    display: flex;
    flex-direction: row;
    flex-wrap: wrap;
    margin-bottom: 16px;
    .appeal-type-card {
      cursor: pointer;
      display: flex;
      flex-direction: column;
      background-color: rgba(#000000, 0.05);
      border: rgba(#000000, 0.15) 2px solid;
      padding: 24px;
      border-radius: 16px;
      margin-right: 24px;
      img {
        height: 64px;
        margin: 0 auto;
      }
      span {
        margin: 12px auto 0;
        font-size: 24px;
        font-weight: 400;
        font-family: 'Jost';
      }
      &:last-child {
        margin-right: 0;
      }
    }
    .checked {
      background-color: primary-color-opacity(10);
      border: primary-color-opacity(35) 2px solid;
      span {
        color: $primary-color-dark;
      }
    }
  }
  .feedback {
    margin-bottom: 32px;
    div {
      margin-right: 32px;
      &:last-child {
        margin-right: 0;
      }
    }
  }
</style>
