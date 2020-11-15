<template>
  <div class="logo">
    <img :src="logoSrc" :style="[componentStyle]" />
    <span v-if="text">Совет лицеистов</span>
  </div>
</template>

<script>
  export default {
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
      logoSrc() {
        let color

        switch (this.color) {
          case 'white':
            color = 'white'
            break
          default:
            color = 'black'
            break
        }

        return this.logoSrcAlias + 'logo_transparent_' + color + '.png'
      },
      componentStyle() {
        const height = this.fullWidth ? 'auto' : this.height + 'px'
        const result = {
          height,
          width: this.height + 'px',
        }

        if (height === 'auto') {
          result.width = '100%'
        }

        return result
      },
    },
  }
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
