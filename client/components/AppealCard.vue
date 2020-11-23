<template>
  <div class="appeal-card col-12 col-xl-5" @click="go">
    <h1>{{ text }}</h1>
    <div>
      <img :src="appealTypeIconLink" />
      <span>{{ appealTypeText }}</span>
    </div>
    <div>
      <img src="/img/ui/calendar.svg" />
      <span>{{ date }}</span>
    </div>
    <div>
      <img :src="appealStatusIconLink" />
      <span :class="appealStatusClassObject">
        {{ appealStatusText }}
      </span>
    </div>
  </div>
</template>

<script>
  export default {
    name: 'AppealCard',
    props: {
      id: {
        type: Number,
        required: true,
      },
      text: {
        type: String,
        required: true,
      },
      type: {
        type: String,
        required: true,
      },
      date: {
        type: String,
        required: true,
      },
      status: {
        type: String,
        required: true,
      },
    },
    data() {
      return {
        appealAlias: '/appeals/',
        iconPathAlias: '/img/ui/',
      }
    },
    computed: {
      link() {
        return this.appealAlias + this.id
      },
      flagIconPathAlias() {
        return this.iconPathAlias + 'flag/'
      },
      appealTypeText() {
        let result = ''

        switch (this.type) {
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

        switch (this.type) {
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

        switch (this.status) {
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

        switch (this.status) {
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

        switch (this.status) {
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
      go() {
        this.$router.push(this.link)
      },
    },
  }
</script>

<style lang="scss">
  .appeal-card {
    display: inline-flex;
    cursor: pointer;
    flex-direction: column;
    border-radius: 32px;
    box-shadow: 0px 0px 32px rgba(0, 0, 0, 0.15);
    padding: 48px !important;
    transition: 250ms ease-out;
    margin: 0 24px 48px;
    h1 {
      font-size: 28px;
      font-weight: 400;
      font-family: 'Jost';
    }
    div {
      display: flex;
      flex-direction: row;
      margin-top: 18px;
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
    &:hover {
      box-shadow: 0px 0px 32px rgba(0, 0, 0, 0.25);
    }
  }
</style>
