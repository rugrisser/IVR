<template>
  <div class="panel-user-card">
    <span class="name">{{ name }}</span>
    <span class="group">[{{ group }}]</span>
    <div class="role">
      <span class="role-description">Роль: </span>
      <span class="role-name" v-if="!roleChangable">{{ roleUIName }} </span>
      <select v-else v-model="selectRole" @change="updateRole">
        <option disabled value=""></option>
        <option value="user">Пользователь</option>
        <option value="deputy">Член совета</option>
      </select>
    </div>
  </div>
</template>

<script>
  import { mapGetters } from 'vuex'

  export default {
    name: 'PanelUser',
    props: {
      id: {
        type: Number,
        required: true,
      },
      name: {
        type: String,
        required: true,
      },
      grade: {
        type: Number,
        required: true,
      },
      stream: {
        type: String,
        required: true,
      },
      role: {
        type: String,
        required: true,
      },
    },
    data() {
      return {
        roleChangeAvailable: ['user', 'deputy'],
        selectRole: '',
      }
    },
    computed: {
      ...mapGetters(['getToken']),
      group() {
        let result = this.grade

        switch (this.stream) {
          case 'MATHINFO':
            result += 'И'
            break
          case 'MATHEC':
            result += 'Э'
            break
          case 'MATH':
            result += 'М'
            break
          case 'SOCEC':
            result += 'С'
            break
          case 'HUM':
            result += 'Г'
            break
          case 'PSYSOC':
            result += 'П'
            break
          case 'LAW':
            result += 'Ю'
            break
          case 'ORIENTAL':
            result += 'В'
            break
          case 'DESIGN':
            result += 'Д'
            break
          case 'SCIENCE':
            result += 'Е'
            break
          case 'FUTURITET':
            result += 'Ф'
            break
          default:
            break
        }

        return result
      },
      roleChangable() {
        return this.roleChangeAvailable.includes(this.role)
      },
      roleUIName() {
        let result = ''

        switch (this.role) {
          case 'admin':
            result = 'Администратор'
            break
          case 'chairman':
            result = 'Председатель совета'
            break
          case 'deputy':
            result = 'Член совета'
            break
          default:
            result = 'Пользователь'
            break
        }

        return result
      },
    },
    mounted() {
      this.selectRole = this.role
    },
    methods: {
      updateRole() {
        this.$axios.$patch(
          `/user/${this.id}/setRole`,
          {
            name: this.selectRole,
          },
          {
            headers: {
              Authorization: 'Bearer ' + this.getToken,
            },
          },
        )
      },
    },
  }
</script>

<style lang="scss">
  $border-radius: 16px;

  .panel-user-card {
    display: flex;
    padding: 32px 48px;
    background-color: white;
    border: 2px solid rgba(0, 0, 0, 0.25);
    border-width: 2px 2px 0 2px;
    transition: 250ms ease-out;
    .name {
      font-size: 28px;
      font-weight: 400;
      margin: 0;
      font-family: 'Jost';
    }
    .group {
      margin: auto 0 auto 8px;
      font-size: 28px;
      font-weight: 500;
      font-family: 'Jost';
    }
    .role {
      margin: auto 0 auto auto;
      margin-right: 0;
      .role-description {
        font-size: 24px;
        font-weight: 400;
        margin-right: 4px;
        font-family: 'Jost';
      }
      .role-name {
        font-size: 24px;
        font-weight: 400;
        font-family: 'Jost';
      }
      select {
        width: 200px;
        font-size: 18px;
        font-family: 'Roboto';
      }
    }
    &:first-child {
      border-radius: $border-radius $border-radius 0 0;
    }
    &:last-child {
      border-radius: 0 0 $border-radius $border-radius;
      border-width: 2px;
    }
    &:hover {
      cursor: pointer;
    }
  }
</style>
