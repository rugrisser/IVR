<template>
  <div class="form-input">
    <span>{{ placeholder }}</span>
    <input
      v-if="!multiline"
      v-model="value"
      @input="$emit('input', $event.target.value)"
    />
    <textarea
      v-else
      v-model="value"
      :rows="multilineRows"
      @input="$emit('input', $event.target.value)"
    ></textarea>
  </div>
</template>

<script lang="ts">
  import Vue from 'vue'

  export default Vue.extend({
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
      multilineRows: {
        type: Number,
        required: false,
        default: 4,
      },
    },
  })
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
    input,
    textarea {
      width: 70%;
      font-size: 20px;
      font-family: 'Roboto';
      padding: 12px 20px;
      border: rgba(0, 0, 0, 0.15) 2px solid;
      border-radius: 16px;
      transition: box-shadow 250ms ease-out;
      &:focus {
        box-shadow: inset 0 0 4px rgba($color: #000000, $alpha: 0.15);
      }
    }
    textarea {
      resize: none;
    }
  }
</style>
