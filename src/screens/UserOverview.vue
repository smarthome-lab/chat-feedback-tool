<template>
  <div id="app">
    <nav-bar></nav-bar>
    <UsersList msg="Welcome to Your Vue.js App"/>
  </div>
</template>

<script>
import UsersList from '../components/UsersList.vue'
export default {
  name: 'app',
  components: {
    UsersList
  }
}
</script>

<style scoped>

#app {
  font-family: "Avenir", Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}
</style>
