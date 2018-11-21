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
.pagination {
  margin-top: 20px !important;
  margin-bottom: 20px !important;
}

.content {
  margin-left: 30px;
  margin-right: 30px;
}
.number {
  width: 4000px !important;
  text-align: center !important;
}

.displayTable {
  background-color: gray !important;
}

#app {
  font-family: "Avenir", Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}
</style>
