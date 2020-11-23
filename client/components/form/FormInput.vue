<template>
  <div class="form-input" :style="`width: ${width}%`">
    <span>{{ placeholder }}</span>
    <div class="form-input-tag" :class="{ focused: focused }">
      <input
        v-if="!multiline"
        v-model="value"
        :type="visible ? 'text' : 'password'"
        @focus="focused = true"
        @blur="focused = false"
        @input="$emit('input', $event.target.value)"
      />
      <textarea
        v-else
        v-model="value"
        :rows="multilineRows"
        @focus="focused = true"
        @blur="focused = false"
        @input="$emit('input', $event.target.value)"
      ></textarea>
      <img v-if="password" src="/img/ui/eye.svg" @click="visible = !visible" />
    </div>
  </div>
</template>

<script>
  export default {
    name: 'FormInput',
    props: {
      placeholder: {
        type: String,
        required: true,
      },
      value: {
        type: String,
        required: true,
      },
      multiline: {
        type: Boolean,
        required: false,
        default: false,
      },
      password: {
        type: Boolean,
        required: false,
        default: false,
      },
      multilineRows: {
        type: Number,
        required: false,
        default: 4,
      },
      width: {
        type: Number,
        required: false,
        default: 70,
      },
    },
    data() {
      return {
        visible: true,
        focused: false,
      }
    },
    mounted() {
      if (this.password) {
        this.visible = false
      }
    },
  }
</script>

<style lang="scss">
  .form-input {
    display: block;
    margin-bottom: 16px;
    span {
      display: block;
      margin-bottom: 8px;
      font-size: 24px;
      font-family: 'Jost';
    }
    .form-input-tag {
      display: flex;
      flex-direction: row;
      border: rgba(0, 0, 0, 0.15) 2px solid;
      border-radius: 16px;
      transition: box-shadow 250ms ease-out;
      input,
      textarea {
        outline: 0;
        margin: 0;
        width: 100%;
        font-size: 20px;
        font-family: 'Roboto';
        padding: 12px 20px;
        border-radius: 16px;
        background-color: transparent;
        border: 0;
      }
      textarea {
        resize: none;
      }
      img {
        cursor: pointer;
        margin: auto 16px;
        height: 24px;
      }
    }
    .focused {
      box-shadow: inset 0 0 4px rgba($color: #000000, $alpha: 0.15);
    }
  }
</style>
