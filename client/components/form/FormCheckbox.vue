<template>
  <div class="form-checkbox">
    <input :id="id" v-model="model" type="checkbox" :value="val" />
    <label :for="id">
      <slot></slot>
    </label>
  </div>
</template>

<script lang="ts">
  import Vue from 'vue'
  export default Vue.extend({
    name: 'FormCheckbox',
    props: {
      id: {
        type: String,
        required: true,
      },
      val: {
        type: String,
        required: true,
      },
      value: {
        type: Array,
        required: true,
      },
    },
    computed: {
      model: {
        get(): Array {
          return this.value
        },
        set(val): void {
          this.$emit('input', val)
        },
      },
    },
  })
</script>

<style lang="scss" scoped>
  .form-checkbox {
    display: inline-block;
    position: relative;
    padding-left: 25px;
    label {
      font-size: 22px;
      font-weight: 500;
      font-family: 'Jost';
      margin-left: 25px;
    }
    input[type='checkbox'] {
      display: none;
    }
    label:after {
      content: '';
      display: block;
      height: 14px;
      width: 14px;
      outline: 2px solid gray;
      position: absolute;
      top: 0;
      left: 0;
      width: 35px;
      height: 35px;
    }
    input[type='checkbox']:checked + label:after {
      outline: 2px solid $primary-color-dark;
      //border: 2px solid #fff;
      background-color: $primary-color-dark;
    }
  }
</style>
