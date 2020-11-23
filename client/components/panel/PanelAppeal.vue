<template>
  <div class="panel-appeal">
    <div class="info">
      <h1 class="title">{{ title }}</h1>
      <span class="author">Автор: {{ author.name }}</span>
      <div>
        <img :src="appealTypeIconLink" />
        <span>{{ appealTypeText }}</span>
      </div>
      <div>
        <img :src="appealStatusIconLink" />
        <span :class="appealStatusClassObject">
          {{ appealStatusText }}
        </span>
      </div>
    </div>
    <div class="actions">
      <SecondaryButton @click.native="$router.push(`/appeals/${id}`)">
        Открыть обращение
      </SecondaryButton>
      <div v-if="status.name === 'consideration'" class="d-flex flex-column">
        <PrimaryButton
          class="panel-appeal-button"
          @click.native="changeAppealStatus('reviewed')"
        >
          Принять положительное решение
        </PrimaryButton>
        <PrimaryButton
          class="panel-appeal-button"
          @click.native="changeAppealStatus('rejected')"
        >
          Принять отрицательное решение
        </PrimaryButton>
      </div>
      <PrimaryButton
        v-else-if="status.name === 'moderation'"
        class="panel-appeal-button"
        @click.native="changeAppealStatus('consideration')"
      >
        Одобрить обращение
      </PrimaryButton>
    </div>
  </div>
</template>

<script>
  import { mapGetters } from 'vuex'
  import PrimaryButton from '~/components/button/PrimaryButton'
  import SecondaryButton from '~/components/button/SecondaryButton'

  export default {
    name: 'PanelAppeal',
    components: {
      PrimaryButton,
      SecondaryButton,
    },
    props: {
      id: {
        type: Number,
        required: true,
      },
      title: {
        type: String,
        required: true,
      },
      author: {
        type: Object,
        required: true,
      },
      type: {
        type: Object,
        required: true,
      },
      status: {
        type: Object,
        required: true,
      },
    },
    data() {
      return {
        iconPathAlias: '/img/ui/',
      }
    },
    computed: {
      ...mapGetters(['getToken']),
      flagIconPathAlias() {
        return this.iconPathAlias + 'flag/'
      },
      appealTypeText() {
        let result = ''

        switch (this.type.name) {
          case 'proposal':
            result = 'Предложение'
            break
          case 'complaint':
            result = 'Жалоба'
            break
          default:
            result = 'Другое'
            break
        }

        return result
      },
      appealTypeIconLink() {
        let result = this.iconPathAlias

        switch (this.type.name) {
          case 'complaint':
            result += 'cross.svg'
            break
          default:
            result += 'hand.svg'
            break
        }

        return result
      },
      appealStatusIconLink() {
        let result = this.flagIconPathAlias

        switch (this.status.name) {
          case 'reviewed':
            result += 'green.svg'
            break
          case 'rejected':
            result += 'red.svg'
            break
          case 'consideration':
            result += 'orange.svg'
            break
          default:
            result += 'gray.svg'
            break
        }

        return result
      },
      appealStatusClassObject() {
        const result = {
          red: false,
          orange: false,
          green: false,
        }

        switch (this.status.name) {
          case 'reviewed':
            result.green = true
            break
          case 'rejected':
            result.red = true
            break
          case 'consideration':
            result.orange = true
            break
          default:
            break
        }

        return result
      },
      appealStatusText() {
        let result

        switch (this.status.name) {
          case 'moderation':
            result = 'На модерации'
            break
          case 'consideration':
            result = 'На рассмотрении'
            break
          case 'reviewed':
            result = 'Рассмотрено'
            break
          case 'rejected':
            result = 'Отклонено'
            break
          default:
            result = 'Неизвестно'
            break
        }

        return result
      },
    },
    methods: {
      changeAppealStatus(status) {
        this.$axios
          .$put(
            '/appeal/changeStatus',
            {
              id: this.id,
              status,
            },
            {
              headers: {
                Authorization: 'Bearer ' + this.getToken,
              },
            },
          )
          .then((response) => {
            this.status.name = status
          })
      },
    },
  }
</script>

<style lang="scss">
  $border-radius: 16px;

  .panel-appeal-button {
    margin-top: 16px;
  }

  .panel-appeal {
    display: flex;
    flex-direction: row;
    padding: 32px 48px;
    background-color: white;
    border: 2px solid rgba(0, 0, 0, 0.25);
    border-width: 2px 2px 0 2px;
    transition: 250ms ease-out;
    .info {
      margin: auto auto auto 0;
      .title {
        font-size: 42px;
        font-weight: 500;
        font-family: 'Jost';
      }
      .author {
        display: block;
        margin-top: 4px;
        font-size: 28px;
        font-weight: 400;
        font-family: 'Jost';
      }
      div {
        display: flex;
        flex-direction: row;
        margin-top: 16px;
        img {
          margin: auto 0;
          height: 32px;
        }
        span {
          color: $gray-1-color;
          margin: auto 0 auto 12px;
          font-size: 24px;
          font-weight: 300;
          font-family: 'Jost';
        }
        .red {
          color: $red-color;
        }
        .orange {
          color: $orange-color;
        }
        .green {
          color: $green-color;
        }
      }
    }
    .actions {
      display: flex;
      flex-direction: column;
      margin: auto 0 auto auto;
    }
    &:first-child {
      border-radius: $border-radius $border-radius 0 0;
    }
    &:last-child {
      border-radius: 0 0 $border-radius $border-radius;
      border-width: 2px;
    }
    &:only-child {
      border-radius: $border-radius;
      border-width: 2px;
    }
  }
</style>
