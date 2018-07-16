<template>
  <div v-if="number>0" class="badge" @click=onclick()>
      <a class="ui teal tag label">
        {{number}} new Item<span v-if="number!==1" >s</span>
      </a>
  </div>
</template>

<script>

  export default {
    name: 'Badge',
    props: {
      number: {
        type: Number,
        default: 0
      },
      onclick: {
        type: Function,
      },
    },
    data() {
      return {
        loading: true
      }
    },
    methods: {}
  }
</script>

<style>
  .badge{
    position: fixed;
    bottom: 20px;
    right: 50px;
    background-color: white;
  }
</style>
