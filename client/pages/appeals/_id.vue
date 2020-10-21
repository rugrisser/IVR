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
      <div class="col-12 col-md-6 appeal-content">
        <div class="appeal-head">
          <h1>{{ appeal.title }}</h1>
          <img
            v-if="appealStatus == AppealStatus.Moderation"
            src="/img/ui/pen.svg"
            @click="$router.push('/appeals/edit/' + id)"
          />
        </div>
        <div class="appeal-body">
          <div class="type">
            <img :src="appealTypeIconLink" />
            <span>{{ appealTypeText }}</span>
          </div>
          <div class="status">
            <img :src="appealStatusIconLink" />
            <span :class="appealStatusClassObject">
              {{ appealStatusText }}
            </span>
          </div>
          <p>{{ appeal.text }}</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
  import Vue from 'vue'
  import { mapGetters } from 'vuex'
  import Logo from '~/components/logo/Logo.vue'
  import SidebarMenu from '~/components/menu/sidebar/SidebarMenu.vue'
  import * as Appeal from '~/assets/ts/appeal'

  export default Vue.extend({
    components: {
      Logo,
      SidebarMenu,
    },
    data() {
      return {
        id: null,
        appeal: {
          title: '',
          text: '',
          status: {
            name: '',
          },
          type: {
            name: '',
          },
        },
        appealAlias: '/appeals/',
        iconPathAlias: '/img/ui/',
        AppealStatus: Appeal.AppealStatus,
      }
    },
    computed: {
      ...mapGetters({
        menuItems: 'getSidebarMenuItems',
      }),
      flagIconPathAlias(): string {
        return this.iconPathAlias + 'flag/'
      },
      appealType(): Appeal.AppealType {
        let result: Appeal.AppealType

        switch (this.appeal.type.name) {
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

        switch (this.appeal.status.name) {
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
    mounted() {
      const id = parseInt(this.$route.params.id)

      if (isNaN(id)) {
        this.$router.push('/appeals')
      } else {
        this.id = id
        this.$axios
          .$get('/appeal/' + id)
          .then((response) => {
            console.log(response)
            this.appeal = response
          })
          .catch(() => {
            this.$router.push('/appeals')
          })
      }
    },
  })
</script>

<style lang="scss">
  .appeal-content {
    margin-left: 48px;
    .appeal-head {
      display: flex;
      height: 204px;
      h1 {
        font-size: 64px;
        font-weight: 600;
        font-family: 'Jost';
        line-height: 64px;
        margin: auto 0 24px;
      }
      img {
        cursor: pointer;
        margin: auto 0 24px 24px;
        height: 48px;
        opacity: 0.5;
        transition: 200ms ease-out;
        &:hover {
          opacity: 1;
        }
      }
    }
    .appeal-body {
      .status,
      .type {
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
      p {
        margin-top: 36px;
        font-size: 32px;
        font-weight: 300;
        font-family: 'Jost';
      }
    }
  }
</style>
