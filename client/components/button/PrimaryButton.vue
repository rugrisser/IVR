<template>
  <button class="primary-button">
    <slot></slot>
  </button>
</template>

<script>
  export default {
    name: 'PrimaryButton',
  }
</script>

<style lang="scss">
  .primary-button {
    cursor: pointer;
    color: white;
    background-color: $primary-color;
    border: 0;
    padding: 18px 42px;
    font-size: 24px;
    font-family: 'Jost';
    border-radius: 16px;
    box-shadow: 0 0 36px rgba($primary-color-dark, 0.4);
    transition: box-shadow 200ms ease-out;
    &:hover {
      box-shadow: 0 0 36px rgba($primary-color-dark, 0.6);
    }
  }
</style>
