<template>
  <button class="secondary-button">
    <slot></slot>
  </button>
</template>

<script>
  export default {
    name: 'SecondaryButton',
  }
</script>

<style lang="scss">
  .secondary-button {
    cursor: pointer;
    color: $primary-color;
    background-color: white;
    border: $primary-color 4px solid;
    padding: 18px 42px;
    font-size: 24px;
    font-family: 'Jost';
    border-radius: 16px;
    /*box-shadow: 0 0 36px rgba($primary-color-dark, 0.4);
    transition: box-shadow 200ms ease-out;
    &:hover {
      box-shadow: 0 0 36px rgba($primary-color-dark, 0.6);
    }*/
  }
</style>
