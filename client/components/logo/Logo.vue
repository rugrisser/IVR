<template>
  <div class="logo">
    <img :src="logoSrc" :style="[componentStyle]" />
    <span v-if="text">Совет лицеистов</span>
  </div>
</template>

<script lang="ts">
  import Vue from 'vue'
  import { LogoColor } from '~/assets/ts'

  export default Vue.extend({
    name: 'Logo',
    props: {
      height: {
        type: Number,
        required: false,
        default: 150,
      },
      fullWidth: {
        type: Boolean,
        required: false,
        default: false,
      },
      text: {
        type: Boolean,
        required: false,
        default: false,
      },
      color: {
        type: String,
        required: true,
      },
    },
    data() {
      return {
        logoSrcAlias: '/img/logo/',
      }
    },
    computed: {
      logoSrc(): string {
        let color: LogoColor

        switch (this.color) {
          case 'white':
            color = LogoColor.White
            break
          default:
            color = LogoColor.Black
            break
        }

        return this.logoSrcAlias + 'logo_transparent_' + color + '.png'
      },
      componentStyle(): Object {
        const height: string = this.fullWidth ? 'auto' : this.height + 'px'
        const result = {
          height,
          width: this.height + 'px',
        }

        if (height === 'auto') {
          result.width = '100%'
        }

        return result
      }
    },
  })
</script>

<style lang="scss">
  .logo {
    display: flex;
    span {
      color: white;
      margin: auto 0 auto 28px;
      font-size: 48px;
      font-family: 'Jost';
    }
  }
</style>
