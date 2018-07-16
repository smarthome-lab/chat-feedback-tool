<template>

</template>

<script>
  export default {
    name: "feedback-settings",
    props: {
      resultsInput: {
        required: true
      },
      results: {
        type: Number,
        required: false
      },
      columnsInput: {
        required: true
      },
      columns: {
        type: Number,
        required: false
      }
    },
  }
</script>

<style scoped>

</style>
