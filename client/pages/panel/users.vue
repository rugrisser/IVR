<template>
  <div class="panel-users panel-default-container">
    <h1>Пользователи</h1>
    <div class="panel-users-list">
      <PanelUser
        v-for="(user, index) in users"
        :id="user.id"
        :key="index"
        :name="user.name"
        :grade="user.grade"
        :stream="user.stream"
        :role="user.role.name"
      />
    </div>
  </div>
</template>

<script>
  import { mapGetters, mapActions } from 'vuex'
  import PanelUser from '~/components/panel/PanelUser'

  export default {
    layout: 'panel',
    components: {
      PanelUser,
    },
    data() {
      return {
        users: [],
      }
    },
    computed: {
      ...mapGetters(['getToken', 'isLogged']),
    },
    mounted() {
      this.$store.dispatch('updateToken')
      this.$axios
        .$get('/user/all', {
          headers: {
            Authorization: 'Bearer ' + this.getToken,
          },
        })
        .then((response) => {
          this.users = response
        })
        .catch(() => this.$router.push('/panel'))
    },
    methods: {
      ...mapActions(['updateToken']),
    },
  }
</script>

<style lang="scss">
  .panel-users-list {
    margin-top: 32px;
  }
</style>
