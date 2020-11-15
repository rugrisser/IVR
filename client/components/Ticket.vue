<template>
  <div class="ticket">
    <div>
      <h1>Проблема:</h1>
      <p>{{ text }}</p>
    </div>
    <div v-if="solution != ''" style="margin-top: 12px">
      <h1>Решение:</h1>
      <p>{{ solution }}</p>
    </div>
  </div>
</template>

<script>
  export default {
    name: 'Ticket',
    props: {
      text: {
        type: String,
        required: true,
      },
      solution: {
        type: String,
        required: false,
        default: '',
      },
    },
  }
</script>

<style lang="scss">
  .ticket {
    color: black;
    background-color: white;
    padding: 48px;
    border-radius: 32px;
    margin-bottom: 48px;
    box-shadow: 0px 0px 32px rgba(0, 0, 0, 0.15);
    transition: 250ms ease-out;
    h1 {
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
</style>
