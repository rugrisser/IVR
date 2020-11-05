<template>
  <div class="login d-flex">
    <div class="login-form">
      <h1>Авторизация</h1>
      <p>
        Введите свои данные от профиля
        <a target="_blank" href="https://hselyceum.eljur.ru">ЭлЖур</a>
      </p>
      <div v-if="error" class="login-form-error">Неправильный логин или пароль</div>
      <div class="login-form-input">
        <FormInput v-model="login" style="width: 100%" placeholder="Логин" />
        <FormInput
          v-model="password"
          style="width: 100%"
          placeholder="Пароль"
          password
        />
        <PrimaryButton
          style="width: 100%; margin-top: 24px"
          @click.native="submit"
        >
          Войти
        </PrimaryButton>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
  import Vue from 'vue'
  import { mapActions } from 'vuex'
  import FormInput from '~/components/form/FormInput.vue'
  import PrimaryButton from '~/components/button/PrimaryButton.vue'

  export default Vue.extend({
    components: {
      FormInput,
      PrimaryButton,
    },
    data() {
      return {
        login: '',
        password: '',
        error: false,
      }
    },
    methods: {
      ...mapActions(['login']),
      submit() {
        this.error = true
        this.$axios
          .$get('/user/auth?login=' + this.login + '&password=' + this.password)
          .then((response) => {
            this.$store.dispatch('login', response.token)
            this.$router.push('/news')
          })
          .catch((error) => {
            if (error.response.status === 403) {
              this.error = true
            }
          })
      },
    },
  })
</script>

<style lang="scss">
  .login {
    width: 100vw;
    height: 100vh;
    .login-form {
      width: 400px;
      margin: auto auto;
      h1 {
        text-align: center;
        font-size: 42px;
        font-weight: 500;
        letter-spacing: 1px;
        font-family: 'Jost';
        margin-bottom: 18px;
      }
      p {
        text-align: center;
        font-size: 28px;
        font-weight: 300;
        font-family: 'Jost';
        a {
          font-weight: 400;
          color: $primary-color-dark;
        }
      }
      .login-form-input {
        margin-top: 24px;
      }
      .login-form-error {
        color: $red-color;
        padding: 24px;
        margin-top: 24px;
        background-color: rgba($color: $red-color, $alpha: 0.15);
        font-size: 20px;
        font-family: 'Jost';
        border: 2px solid $red-color;
        border-radius: 16px;
      }
    }
  }
</style>
