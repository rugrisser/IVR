<template>
  <div class="panel-ticket" :class="[{ closed: !isOpened }]">
    <h1 class="title">#{{ id }}</h1>
    <p class="text">{{ text }}</p>
    <div v-if="isOpened">
      <FormInput
        v-model="responseText"
        style="margin-top: 16px"
        placeholder="Ответ:"
        multiline
        :width="50"
      />
      <PrimaryButton style="width: 300px" @click.native="close">
        Закрыть тикет
      </PrimaryButton>
    </div>
    <div class="panel-ticket-closed" v-else>
      <span>Ответ:</span>
      <p>{{ response }}</p>
    </div>
  </div>
</template>

<script>
  import { mapGetters } from 'vuex'
  import FormInput from '~/components/form/FormInput'
  import PrimaryButton from '~/components/button/PrimaryButton'

  export default {
    name: 'PanelTicket',
    components: {
      FormInput,
      PrimaryButton,
    },
    props: {
      id: {
        type: Number,
        required: true,
      },
      text: {
        type: String,
        required: true,
      },
      opened: {
        type: Boolean,
        required: true,
      },
      response: {
        type: String,
        required: false,
        default: '',
      },
    },
    data() {
      return {
        isOpened: true,
        responseText: '',
        redHighlight: false,
      }
    },
    computed: {
      ...mapGetters(['getToken']),
    },
    mounted() {
      this.isOpened = this.opened
    },
    methods: {
      close() {
        if (this.responseText.length === 0) {
          this.redHighlight = true
          setTimeout(() => {
            this.redHighlight = false
          }, 5000)
        } else {
          this.$axios
            .$put(
              `/support/${this.id}`,
              { text: this.responseText },
              {
                headers: {
                  Authorization: 'Bearer ' + this.getToken,
                },
              },
            )
            .then(() => (this.isOpened = false))
        }
      },
    },
  }
</script>

<style lang="scss">
  $border-radius: 16px;

  .panel-ticket {
    display: flex;
    flex-direction: column;
    padding: 32px 48px;
    background-color: white;
    border: 2px solid rgba(0, 0, 0, 0.25);
    border-width: 2px 2px 0 2px;
    transition: 250ms ease-out;
    .title {
      font-size: 42px;
      font-weight: 500;
      font-family: 'Jost';
    }
    .text {
      margin-top: 4px;
      font-size: 28px;
      font-weight: 400;
      font-family: 'Jost';
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
    .panel-ticket-closed {
      margin-top: 16px;
      span {
        font-size: 28px;
        font-weight: 500;
        font-family: 'Jost';
      }
      p {
        font-size: 28px;
        font-weight: 300;
        font-family: 'Jost';
      }
    }
  }
  .closed {
    background-color: rgba($color: #000000, $alpha: 0.15);
  }
</style>
