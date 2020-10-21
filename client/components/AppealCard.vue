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

<script lang="ts">
  import Vue from 'vue'
  import * as Appeal from '~/assets/ts/appeal'

  export default Vue.extend({
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
        AppealStatus: Appeal.AppealStatus,
      }
    },
    computed: {
      link(): string {
        return this.appealAlias + this.id
      },
      flagIconPathAlias(): string {
        return this.iconPathAlias + 'flag/'
      },
      appealType(): Appeal.AppealType {
        let result: Appeal.AppealType

        switch (this.type) {
          case 'proposal':
            result = Appeal.AppealType.Proposal
            break
          case 'complaint':
            result = Appeal.AppealType.Complaint
            break
          default:
            result = Appeal.AppealType.Other
            break
        }

        return result
      },
      appealTypeText(): string {
        let result = ''

        switch (this.appealType) {
          case Appeal.AppealType.Proposal:
            result = 'Предложение'
            break
          case Appeal.AppealType.Complaint:
            result = 'Жалоба'
            break
          default:
            result = 'Другое'
            break
        }

        return result
      },
      appealTypeIconLink(): string {
        let result = this.iconPathAlias

        switch (this.appealType) {
          case Appeal.AppealType.Complaint:
            result += 'cross.svg'
            break
          default:
            result += 'hand.svg'
            break
        }

        return result
      },
      appealStatus(): Appeal.AppealStatus {
        let result: Appeal.AppealStatus

        switch (this.status) {
          case 'moderation':
            result = Appeal.AppealStatus.Moderation
            break
          case 'consideration':
            result = Appeal.AppealStatus.Consideration
            break
          case 'reviewed':
            result = Appeal.AppealStatus.Reviewed
            break
          case 'rejected':
            result = Appeal.AppealStatus.Rejected
            break
          default:
            result = Appeal.AppealStatus.Unknown
            break
        }

        return result
      },
      appealStatusIconLink(): string {
        let result = this.flagIconPathAlias

        switch (this.appealStatus) {
          case Appeal.AppealStatus.Reviewed:
            result += 'green.svg'
            break
          case Appeal.AppealStatus.Rejected:
            result += 'red.svg'
            break
          case Appeal.AppealStatus.Consideration:
            result += 'orange.svg'
            break
          default:
            result += 'gray.svg'
            break
        }

        return result
      },
      appealStatusClassObject(): Object {
        const result = {
          red: false,
          orange: false,
          green: false,
        }

        switch (this.appealStatus) {
          case Appeal.AppealStatus.Reviewed:
            result.green = true
            break
          case Appeal.AppealStatus.Rejected:
            result.red = true
            break
          case Appeal.AppealStatus.Consideration:
            result.orange = true
            break
          default:
            break
        }

        return result
      },
      appealStatusText(): string {
        let result: string

        switch (this.appealStatus) {
          case Appeal.AppealStatus.Moderation:
            result = 'На модерации'
            break
          case Appeal.AppealStatus.Consideration:
            result = 'На рассмотрении'
            break
          case Appeal.AppealStatus.Reviewed:
            result = 'Рассмотрено'
            break
          case Appeal.AppealStatus.Rejected:
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
      go(): void {
        this.$router.push(this.link)
      },
    },
  })
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
